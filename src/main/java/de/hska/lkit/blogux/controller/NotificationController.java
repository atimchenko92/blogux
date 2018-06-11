package de.hska.lkit.blogux.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import de.hska.lkit.blogux.model.NewPostMsg;
import de.hska.lkit.blogux.model.NewPostNotification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {
  private SimpMessagingTemplate template;

   @Autowired
   public NotificationController(SimpMessagingTemplate template) {
     this.template = template;
   }

   @MessageMapping("/newpost/{username}")
   public void notify(@DestinationVariable String username, NewPostMsg message) throws Exception {
     NewPostNotification notification = new NewPostNotification("Hello, " + message.getName() + "!");
     this.template.convertAndSend("/topic/"+username, notification);
   }

}
