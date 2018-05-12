package de.hska.lkit.blogux.session;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.BoundHashOperations;
import java.util.logging.Logger;
import org.springframework.util.ObjectUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class BloguxCookieInterceptor extends HandlerInterceptorAdapter{

  @Autowired
  private StringRedisTemplate template;


  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    Cookie[] cookies = req.getCookies();
    System.out.println("INSIDE PREHANDLE");

    if (!ObjectUtils.isEmpty(cookies))
      for (Cookie cookie : cookies)
        if (cookie.getName().equals("auth")) {
          String auth = cookie.getValue();
          System.out.println("AUTH:"+auth);
          if (auth != null) {
            String uid = template.opsForValue().get("auth:" + auth + ":uid");
            if (uid != null) {
              String name = template.opsForValue().get("uid:" + uid + ":name");
              BloguxSecurity.setUser(name, uid);
            }
          }
        }
        return true;
    }
//TODO: clean up SimpleSession State in the end (skipped here)



}
