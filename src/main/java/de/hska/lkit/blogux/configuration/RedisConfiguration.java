package de.hska.lkit.blogux.configuration;

import de.hska.lkit.blogux.pubsub.MessagePublisherImpl;
import de.hska.lkit.blogux.pubsub.MessagePublisher;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import de.hska.lkit.blogux.pubsub.BloguxMessageSubscriber;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
	@Bean
	public JedisConnectionFactory getConnectionFactory() {
		// falls andere als die Default Werte gesetzt werden sollen
	//	JedisConnectionFactory jRedisConnectionFactory = new JedisConnectionFactory(new JedisPoolConfig());
	//	jRedisConnectionFactory.setHostName("localhost");
	//  jRedisConnectionFactory.setPort(6379);
	//	jRedisConnectionFactory.setPassword("");
	//	return jRedisConnectionFactory;
		return new JedisConnectionFactory();
	}

	@Bean(name = "stringRedisTemplate")
	public StringRedisTemplate getStringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(getConnectionFactory());
		stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
		stringRedisTemplate.setHashValueSerializer(new StringRedisSerializer());
		stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
		return stringRedisTemplate;
	}

	@Bean(name = "redisTemplate")
	public RedisTemplate<String, Object> getRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(getConnectionFactory());
		return redisTemplate;
	}

	@Bean
	MessageListenerAdapter messageListener() {
		return new MessageListenerAdapter(new BloguxMessageSubscriber());
	}

	@Bean
	RedisMessageListenerContainer container() {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(getConnectionFactory());
		container.addMessageListener(messageListener(), topic());
		return container;
	}

	@Bean
	ChannelTopic topic() {
		return new ChannelTopic("messageQueue");
	}

	@Bean
	MessagePublisher redisPublisher() {
    return new MessagePublisherImpl(getRedisTemplate(), topic());
	}

}
