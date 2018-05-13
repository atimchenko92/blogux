package de.hska.lkit.blogux.session;

import de.hska.lkit.blogux.model.User;
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
  //
    System.out.println("Cookie Interceptor: INSIDE PREHANDLE");

    User currentUser = BloguxSecurity.getUserByCookie(req, template);
    if(currentUser != null){
      //TODO if needed
      BloguxSecurity.refreshCookie();
    }
    else{
      System.out.println("Sending redirect...");
      res.sendRedirect("/login");
      return false;
    }

    return true;

    }
//TODO: clean up SimpleSession State in the end (skipped here)



}
