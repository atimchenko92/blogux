package de.hska.lkit.blogux.session;

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

  private static final Logger logger = Logger.getLogger("coockie-interceptor");

  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    Cookie[] cookies = req.getCookies();
    logger.info("INSIDE PREHANDLE");

    if (!ObjectUtils.isEmpty(cookies))
      for (Cookie cookie : cookies)
        if (cookie.getName().equals("auth")) {
          String auth = cookie.getValue();
          if (auth != null) {
            String uid = template.opsForValue().get("auth:" + auth + ":uid");
            if (uid != null) {
              String name = (String) template.boundHashOps("uid:" + uid + ":user").get("name");
              BloguxSecurity.setUser(name, uid);
            }
          }
        }
        return true;
    }
//TODO: clean up SimpleSession State in the end (skipped here)



}
