package de.hska.lkit.blogux.repo.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Repository;
import de.hska.lkit.blogux.repo.SessionRepository;

@Repository
public class SessionRepositoryImpl implements SessionRepository {
  @Autowired
  private StringRedisTemplate template;

  @Override
  public boolean checkAuth(String name, String pwd) {
    try {
      BoundHashOperations<String, String, String> userOps = template.boundHashOps("user:" + name);
      return userOps.get("password").equals(pwd);
    } catch (NullPointerException e) {
      //In case user does not even exist
      return false;
    }
  }

  @Override
  public String addAuthTokens(String name, long duration, TimeUnit timeunit) {
    String uid = (String) template.opsForHash().get("user:" + name, "id");
    String auth = UUID.randomUUID().toString();
    //Create Auth Hash
    template.boundHashOps("uid:" + uid + ":auth").put("auth", auth);
    template.expire("uid:" + uid + ":auth", duration, timeunit);
    //Create reverse user key
    template.opsForValue().set("auth:" + auth + ":uid", uid, duration, timeunit);
    return auth;
  }

  @Override
  public void deleteAuthTokens(String uid, String auth) {
    String authKey = "uid:" + uid + ":auth";
    String authUID = "auth:" + auth + ":uid";
    List<String> keysToDelete = Arrays.asList(authKey, authUID);
    template.delete(keysToDelete);
  }

  @Override
  public String getUIDbyToken(String token) {
    return template.opsForValue().get("auth:" + token + ":uid");
  }
}
