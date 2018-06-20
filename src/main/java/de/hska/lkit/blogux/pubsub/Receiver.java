package de.hska.lkit.blogux.pubsub;

import de.hska.lkit.blogux.socket.pojos.NewPostNotification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class Receiver {

  @Autowired
  private SimpMessagingTemplate messagingStopmTemplate;

  @Autowired
  public Receiver(){}

  // 2 means analyze max. 2 words
  // tokensVal = name | message
  public void receiveMessage(String message) {
    String[] tokensVal = message.split("\\|", 2);
    messagingStopmTemplate.convertAndSend("/topic/" + tokensVal[0],
     new NewPostNotification(tokensVal[0], tokensVal[1]));
  }
}
