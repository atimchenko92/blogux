package de.hska.lkit.blogux.session;

import de.hska.lkit.blogux.repo.UserRepository;
import de.hska.lkit.blogux.model.User;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class BloguxCookieInterceptor extends HandlerInterceptorAdapter {
  @Autowired
  private UserRepository userRepository;

  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    System.out.println("Cookie Interceptor: INSIDE PREHANDLE");
    User currentUser = userRepository.getUserByCookie(req);
    if (currentUser != null) {
      //TODO if needed -> cookie refresh
    } else {
      System.out.println("Sending redirect...");
      res.sendRedirect("/login");
      return false;
    }

    req.setAttribute("currentUser", currentUser);
    return super.preHandle(req, res, handler);
  }

}
