package de.hska.lkit.blogux.repo;

import java.util.concurrent.TimeUnit;

public interface SessionRepository {
  public String addAuthToken(String name, long duration, TimeUnit timeunit );
  public boolean checkAuth(String name, String pwd);
  public boolean deleteAuthToken(String token);
}
