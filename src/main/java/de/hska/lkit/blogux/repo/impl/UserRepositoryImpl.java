package de.hska.lkit.blogux.repo.impl;

import java.util.Map;
import java.util.Set;

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
	private static final String KEY_SET_ALL_USERNAMES 	= "all:usernames";

	private static final String KEY_ZSET_ALL_USERNAMES 	= "all:usernames:sorted";

	private static final String KEY_HASH_ALL_USERS 		= "all:user";

	private static final String KEY_PREFIX_USER 	= "user:";

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
	 * hash operations for redisTemplate
	 */
	@Resource(name="redisTemplate")
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
		}

		@Override
		public void createUser(User user) {
			// generate a unique id
			String id = String.valueOf(userid.incrementAndGet());
			user.setId(id);

			String key = KEY_PREFIX_USER + user.getUsername();
			srt_hashOps.put(key, "id", id);
			srt_hashOps.put(key, "username", user.getUsername());
			srt_hashOps.put(key, "password", user.getPassword());
			// the key for a new user is added to the set for all usernames
			srt_setOps.add(KEY_SET_ALL_USERNAMES, user.getUsername());
			// the key for a new user is added to the sorted set for all usernames
			srt_zSetOps.add(KEY_ZSET_ALL_USERNAMES, user.getUsername(), 0);
			// to show how objects can be saved
			rt_hashOps.put(KEY_HASH_ALL_USERS, key, user);
		}


		@Override
		public void saveUser(User user) {
			// TODO: check if user exists
/*
			user.setId(id);

			// to show how objects can be saved
			// be careful, if username already exists it's not added another time
			String key = KEY_PREFIX_USER + user.getUsername();
			srt_hashOps.put(key, "id", id);
			srt_hashOps.put(key, "firstname", user.getFirstname());
			srt_hashOps.put(key, "lastname", user.getLastname());
			srt_hashOps.put(key, "username", user.getUsername());
			srt_hashOps.put(key, "password", user.getPassword());

			// the key for a new user is added to the set for all usernames
			srt_setOps.add(KEY_SET_ALL_USERNAMES, user.getUsername());

			// the key for a new user is added to the sorted set for all usernames
			srt_zSetOps.add(KEY_ZSET_ALL_USERNAMES, user.getUsername(), 0);

			// to show how objects can be saved
			rt_hashOps.put(KEY_HASH_ALL_USERS, key, user);
			*/

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
				user.setFirstname(srt_hashOps.get(key, "firstName"));
				user.setLastname(srt_hashOps.get(key, "lastName"));
				user.setUsername(srt_hashOps.get(key, "username"));
				user.setPassword(srt_hashOps.get(key, "password"));
			} else
				user = null;
			return user;
		}
	}
