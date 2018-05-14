package de.hska.lkit.blogux.repo.impl;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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

	/**
	 * to generate unique ids for user
	 */
	private RedisAtomicLong postid;

	/**
	 * to save data in String format
	 */
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * to save user data as object
	 */
	private RedisTemplate<String, Object> redisTemplate;

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
	 * hash operations for redisTemplate
	 */
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Post> rt_hashOps;

	@Autowired
	public PostRepositoryImpl(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
		this.redisTemplate = redisTemplate;
		this.stringRedisTemplate = stringRedisTemplate;
		this.postid = new RedisAtomicLong("postid", stringRedisTemplate.getConnectionFactory());
	}

	@PostConstruct
	private void init() {
		srt_hashOps = stringRedisTemplate.opsForHash();
		srt_setOps = stringRedisTemplate.opsForSet();
		srt_zSetOps = stringRedisTemplate.opsForZSet();
		srt_listOps = stringRedisTemplate.opsForList();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * hska.iwi.vslab.repo.UserRepository#saveUser(hska.iwi.vslab.model.User)
	 */
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
		String keyUserPosts = "user:" + post.getAuthor() + ":myposts";
		srt_listOps.leftPush(keyUserPosts, post.getId());

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

			return post;
		}
		return null;

	}


	@Override
	public Map<String, Post> getAllPosts() {
		return rt_hashOps.entries(KEY_HASH_ALL_POSTS);
	}

}
