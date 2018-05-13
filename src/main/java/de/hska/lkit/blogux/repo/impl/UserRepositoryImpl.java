package de.hska.lkit.blogux.repo.impl;

import java.util.Set;
import org.springframework.data.redis.core.ValueOperations;
import de.hska.lkit.blogux.places.Login;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

import de.hska.lkit.blogux.model.User;
import de.hska.lkit.blogux.repo.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {
	private static final String KEY_SET_ALL_USERNAMES = "all:usernames";

	private static final String KEY_ZSET_ALL_USERNAMES = "all:usernames:sorted";

	private static final String KEY_HASH_ALL_USERS = "all:user";

	private static final String KEY_PREFIX_USER = "user:";

	private RedisAtomicLong userid;

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
	* Value operations
	**/
	private ValueOperations<String, String> val_Opts;

	/**
	 * hash operations for redisTemplate
	 */
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, User> rt_hashOps;

	@Autowired
	public UserRepositoryImpl(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
		this.redisTemplate = redisTemplate;
		this.stringRedisTemplate = stringRedisTemplate;
		this.userid = new RedisAtomicLong("userid", stringRedisTemplate.getConnectionFactory());
	}

	@PostConstruct
	private void init() {
		srt_hashOps = stringRedisTemplate.opsForHash();
		srt_setOps = stringRedisTemplate.opsForSet();
		srt_zSetOps = stringRedisTemplate.opsForZSet();
		val_Opts = stringRedisTemplate.opsForValue();
	}

	@Override
	public void createUser(Login formular) {
		// generate a unique id
		String id = String.valueOf(userid.incrementAndGet());
		String key = KEY_PREFIX_USER + formular.getName();

		srt_hashOps.put(key, "id", id);
		srt_hashOps.put(key, "username", formular.getName());
		srt_hashOps.put(key, "password", formular.getPwd());
		// the key for a new user is added to the set for all usernames
		srt_setOps.add(KEY_SET_ALL_USERNAMES, formular.getName());
		// the key for a new user is added to the sorted set for all usernames
		srt_zSetOps.add(KEY_ZSET_ALL_USERNAMES, formular.getName(), 0);

		val_Opts.set("uid:" + id + ":name", formular.getName());

		//Creating new user
		User newUser = new User(formular.getName(), formular.getPwd());
		newUser.setId(id);
		rt_hashOps.put(KEY_HASH_ALL_USERS, key, newUser);
	}

	@Override
	public void saveUser(User user) {
		String key = KEY_PREFIX_USER + user.getUsername();
		User oldUser = null;
		System.out.println("saving user:" + user.getUsername());
		oldUser = getUser(user.getUsername());
		System.out.println("old user:" + oldUser.getFirstname());
		//TODO If pwd change
		oldUser.setFirstname(user.getFirstname());
		oldUser.setLastname(user.getLastname());
		//srt_hashOps.put(key, "password", formular.getPwd());
		srt_hashOps.put(key, "firstname", oldUser.getFirstname());
		srt_hashOps.put(key, "lastname", oldUser.getLastname());
		rt_hashOps.put(KEY_HASH_ALL_USERS, key, oldUser);
	}

	@Override
	public Map<String, User> getAllUsers() {
		return rt_hashOps.entries(KEY_HASH_ALL_USERS);
	}

	@Override
	public User getUser(String username) {
		User user = new User();

		// if username is in set for all usernames,
		if (srt_setOps.isMember(KEY_SET_ALL_USERNAMES, username)) {

			// get the user data out of the hash object with key "'user:' + username"
			String key = "user:" + username;
			user.setId(srt_hashOps.get(key, "id"));
			user.setFirstname(srt_hashOps.get(key, "firstname"));
			user.setLastname(srt_hashOps.get(key, "lastname"));
			user.setUsername(srt_hashOps.get(key, "username"));
			user.setPassword(srt_hashOps.get(key, "password"));
			System.out.println("before get follows and followers");
			user.setFollows(getFollows(username));
			user.setFollowers(getFollowers(username));
		} else
			user = null;
		return user;
	}

	@Override
	public void followUnfollow(User currentUser, User inspectedUser) {
		String currentFolows = KEY_PREFIX_USER + currentUser.getUsername() + ":follows";
		String inspectedFolowers = KEY_PREFIX_USER + inspectedUser.getUsername() + ":followers";

		if (checkIfFollows(currentUser, inspectedUser)) {
			srt_setOps.remove(currentFolows, inspectedUser.getUsername());
			srt_setOps.remove(inspectedFolowers, currentUser.getUsername());
		} else {
			srt_setOps.add(currentFolows, inspectedUser.getUsername());
			srt_setOps.add(inspectedFolowers, currentUser.getUsername());
		}
		//TODO: rebuild timeline
	}

	@Override
	public boolean checkIfFollows(User currentUser, User inspectedUser) {
		String currentFolows = KEY_PREFIX_USER + currentUser.getUsername() + ":follows";
		return srt_setOps.isMember(currentFolows, inspectedUser.getUsername());
	}

	@Override
	public Set<String> getUserFollowers(User currentUser) {
		String currentFolowers = KEY_PREFIX_USER + currentUser.getUsername() + ":followers";
		return srt_setOps.members(currentFolowers);
	}

	@Override
	public Set<String> getUserFollows(User currentUser) {
		String currentFolows = KEY_PREFIX_USER + currentUser.getUsername() + ":follows";
		return srt_setOps.members(currentFolows);
	}

	@Override
	public Set<String> getFollowers(String username) {
		String currentFolowers = KEY_PREFIX_USER + username + ":followers";
		return srt_setOps.members(currentFolowers);
	}

	@Override
	public Set<String> getFollows(String username) {
		String currentFolows = KEY_PREFIX_USER + username + ":follows";
		return srt_setOps.members(currentFolows);
	}

}
