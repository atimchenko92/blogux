package de.hska.lkit.blogux.repo;

import java.util.concurrent.TimeUnit;

public interface SessionRepository {
  public String addAuthTokens(String name, long duration, TimeUnit timeunit );
  public boolean checkAuth(String name, String pwd);
  public void deleteAuthTokens(String uid, String auth);
  public String getUIDbyToken(String token);
}
