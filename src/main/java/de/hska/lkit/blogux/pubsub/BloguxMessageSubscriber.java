package de.hska.lkit.blogux.pubsub;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Service
public class BloguxMessageSubscriber implements MessageListener{

  public static List<String> messageList = new ArrayList<String>();

  @Override
  public void onMessage(Message message, byte[] pattern) {
    messageList.add(message.toString());
    System.out.println("Message Received on channel " + new String(message.getChannel()) + " message :: " + message.toString());
  }
}
