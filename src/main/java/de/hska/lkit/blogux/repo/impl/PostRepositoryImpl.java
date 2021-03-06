package de.hska.lkit.blogux.repo.impl;

import org.springframework.data.redis.core.ValueOperations;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

import de.hska.lkit.blogux.model.Post;
import de.hska.lkit.blogux.repo.PostRepository;

/**
 * @author knad0001
 *
 */
@Repository
public class PostRepositoryImpl implements PostRepository {

	/**
	 *
	 */
	private static final String KEY_SET_ALL_POSTS = "all:posts";

	private static final String KEY_ZSET_ALL_POSTS = "all:posts:sorted";

	private static final String KEY_HASH_ALL_POSTS = "all:posts";

	private static final String KEY_PREFIX_POST = "post:";

	private static final String KEY_GLOBAL_POST = "post:global";

	/**
	 * to generate unique ids for user
	 */
	private RedisAtomicLong postid;

	/**
	 * to save data in String format
	 */
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * hash operations for stringRedisTemplate
	 */
	private HashOperations<String, String, String> srt_hashOps;

	/**
	 * set operations for stringRedisTemplate
	 */
	private SetOperations<String, String> srt_setOps;

	/**
	 * zset operations for stringRedisTemplate
	 */
	private ZSetOperations<String, String> srt_zSetOps;

	/**
	* list operations for stringRedisTemplate
	*/
	private ListOperations<String, String> srt_listOps;

	/**
	* Value operations
	**/
	private ValueOperations<String, String> val_Opts;

	/**
	 * hash operations for redisTemplate
	 */
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Post> rt_hashOps;

	@Autowired
	public PostRepositoryImpl(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
		this.postid = new RedisAtomicLong("postid", stringRedisTemplate.getConnectionFactory());
	}

	@PostConstruct
	private void init() {
		srt_hashOps = stringRedisTemplate.opsForHash();
		srt_setOps = stringRedisTemplate.opsForSet();
		srt_zSetOps = stringRedisTemplate.opsForZSet();
		srt_listOps = stringRedisTemplate.opsForList();
		val_Opts = stringRedisTemplate.opsForValue();
	}

	@Override
	public void savePost(Post post) {
		// generate a unique id
		String id = String.valueOf(postid.incrementAndGet());

		post.setId(id);

		// to show how objects can be saved
		// be careful, if username already exists it's not added another time
		String key = KEY_PREFIX_POST + id;
		srt_hashOps.put(key, "id", id);
		srt_hashOps.put(key, "author", post.getAuthor());
		srt_hashOps.put(key, "datetime", post.getDatetime());
		srt_hashOps.put(key, "text", post.getText());

		// user own post persistence under user id
		String keyUserPosts = "user:" + post.getAuthor() + ":posts";
		srt_listOps.leftPush(keyUserPosts, post.getId());

		// global List
		srt_listOps.leftPush(KEY_GLOBAL_POST, post.getId());

		// the key for a new user is added to the set for all usernames
		srt_setOps.add(KEY_SET_ALL_POSTS, post.getId());

		// the key for a new user is added to the sorted set for all usernames
		srt_zSetOps.add(KEY_ZSET_ALL_POSTS, post.getId(), 0);

		// to show how objects can be saved
		rt_hashOps.put(KEY_HASH_ALL_POSTS, key, post);
	}

	@Override
	public Post getPost(String id) {

		// if username is in set for all usernames,
		if (srt_setOps.isMember(KEY_SET_ALL_POSTS, id)) {
			Post post = new Post();

			// get the user data out of the hash object with key "'user:' + username"
			String key = KEY_PREFIX_POST + id;
			post.setId(id);
			post.setAuthor(srt_hashOps.get(key, "author"));
			post.setDatetime(srt_hashOps.get(key, "datetime"));
			post.setText(srt_hashOps.get(key, "text"));
			post.setProfilePicture(val_Opts.get("user:"+post.getAuthor()+":profilePicture"));

			return post;
		}
		return null;

	}

	@Override
	public List<Post> getUserPostsInRange(String username, long start, long end) {
		String key = "user:" + username + ":posts";
		List<Post> postList = new ArrayList<>();
		List<String> postIdList = srt_listOps.range(key, start, end);
		for (String postId : postIdList) {
			postList.add(getPost(postId));
		}
		return postList;
	}

	@Override
	public List<Post> getGlobalPostsInRange(long start, long end) {
		List<Post> postList = new ArrayList<>();
		List<String> postIdList = srt_listOps.range(KEY_GLOBAL_POST, start, end);
		for (String postId : postIdList) {
			postList.add(getPost(postId));
		}
		return postList;
	}

	@Override
	public Map<String, Post> getAllPosts() {
		return rt_hashOps.entries(KEY_HASH_ALL_POSTS);
	}

	@Override
	public List<Post> prepareFollowingPosts(String username) {
		Set<String> userList = srt_setOps.members("user:" + username + ":follows");
		return performResolutionForUsers(username, userList);
	}

	@Override
	public List<Post> preparePersonalAndFollowingPosts(String username) {
		Set<String> userList = srt_setOps.members("user:" + username + ":follows");
		userList.add(username);
		return performResolutionForUsers(username, userList);
	}

	private List<Post> performResolutionForUsers(String username, Set<String> userList){
		List<Post> resList = new ArrayList<>();
		HashMap<String, Post> postSet = new HashMap<>();

		for (String followUser : userList) {
			for (Post curPost : getUserPostsInRange(followUser, 0, -1)) {
				postSet.put(curPost.getId(), curPost);
			}
		}

		List<Post> glPosts = getGlobalPostsInRange(0, -1);
		for (Post glPost : glPosts) {
			if (postSet.containsKey(glPost.getId())) {
				resList.add(glPost);
			}
		}
		return resList;
	}

}
