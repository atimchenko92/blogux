package de.hska.lkit.blogux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import de.hska.lkit.blogux.socket.pojos.NewPostMsg;
import de.hska.lkit.blogux.socket.pojos.NewPostNotification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

  @Autowired
  private SimpMessagingTemplate template;

   @MessageMapping("/newpost")
   public void notify(NewPostMsg message) throws Exception {
     template.convertAndSend("/topic/" + message.getName(),
      new NewPostNotification(message.getName(), message.getMsg()));
   }

}
