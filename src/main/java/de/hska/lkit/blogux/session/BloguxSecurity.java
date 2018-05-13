package de.hska.lkit.blogux.session;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import de.hska.lkit.blogux.model.User;
import org.springframework.util.ObjectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

public class BloguxSecurity {

  public static void createCookie(String name, HttpServletResponse res) {

  }

  public static void removeCookie(String name){

  }

  public static void refreshCookie(){

  }

  public static User getUserByCookie(HttpServletRequest req, StringRedisTemplate template){
    System.out.println("Get user by cookie");
    Cookie[] cookies = req.getCookies();
    User currentUser = null;
    if (!ObjectUtils.isEmpty(cookies))
      for (Cookie cookie : cookies)
        if (cookie.getName().equals("auth")) {
          String auth = cookie.getValue();
          System.out.println("Auth:"+auth);
          if (auth != null) {
            String uid = template.opsForValue().get("auth:" + auth + ":uid");
            System.out.println("UID:"+uid);
            if (uid != null) {
              String name = template.opsForValue().get("uid:" + uid + ":name");
              //TODO change it with normal user
              currentUser = new User();
              currentUser.setId(uid);
              currentUser.setUsername(name);
              currentUser.setSessionToken(auth);
              
              return currentUser;
            }
          }
        }
        return currentUser;
  }

}
