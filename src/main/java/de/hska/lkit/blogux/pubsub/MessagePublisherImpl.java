package de.hska.lkit.blogux.pubsub;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class MessagePublisherImpl implements MessagePublisher {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private ChannelTopic topic;

  public MessagePublisherImpl() {
  }

  public MessagePublisherImpl(
    RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
    this.redisTemplate = redisTemplate;
    this.topic = topic;
  }

  public void publish(String message) {
    redisTemplate.convertAndSend(topic.getTopic(), message);
  }
}
