package de.hska.lkit.blogux.repo.impl;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Repository;
import de.hska.lkit.blogux.repo.SessionRepository;

@Repository
public class SessionRepositoryImpl implements SessionRepository{
  @Autowired
  private StringRedisTemplate template;

  @Override
  public String addAuthToken(String name, long duration, TimeUnit timeunit ){
    //TODO: implement
    return "todo";
  }
  @Override
  public boolean checkAuth(String name, String pwd){
    //TODO: implement
    //String uid = template.opsForValue().get("name:" + name + ":uid");
    //BoundHashOperations<String, String, String> userOps = template.boundHashOps("uid:" + uid + ":user");
    //return userOps.get("pwd").equals(pwd);
    return true;
  }

  @Override
  public boolean deleteAuthToken(String token){
    //TODO: implement
    return true;
  }
}
