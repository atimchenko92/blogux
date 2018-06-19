package de.hska.lkit.blogux.controller;

import org.springframework.messaging.handler.annotation.Payload;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import de.hska.lkit.blogux.socket.pojos.NewPostMsg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.data.redis.core.StringRedisTemplate;

@Controller
public class NotificationController {

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

   @MessageMapping("/newpost")
   public void notify(@Valid @Payload NewPostMsg message) throws Exception {

     // send directly throug STOMP at same Application
     // -> here occurs Problem with double Messaging if Client go throug same App-Instanse
     // messagingStopmTemplate.convertAndSend("/topic/" + message.getName(), new NewPostNotification(message.getName(), message.getMsg()));


     // send directly in RedisQueue
     // it goes first in redisQueue only after in STOPMquee. Inefficient
     String redisQueue = patterToSendRedisQueue(message.getName(), message.getMsg());
     stringRedisTemplate.convertAndSend("redisChannel", redisQueue);
   }

   private static String patterToSendRedisQueue(String name, String msg) {
     return (name + "|" + msg);
   }
}
