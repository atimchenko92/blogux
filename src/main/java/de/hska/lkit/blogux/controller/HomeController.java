package de.hska.lkit.blogux.controller;

import javax.servlet.http.HttpServletRequest;
import de.hska.lkit.blogux.repo.SessionRepository;
import de.hska.lkit.blogux.session.BloguxSecurity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import de.hska.lkit.blogux.places.Home;
import de.hska.lkit.blogux.model.User;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import de.hska.lkit.blogux.repo.UserRepository;

/**
 * @author atimchenko
 *
 */
@Controller
public class HomeController {

  private final SessionRepository sessionRepository;

  @Autowired
  public HomeController(SessionRepository sessionRepository) {
    super();
    this.sessionRepository = sessionRepository;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
	public String showHome(@ModelAttribute User user, Model model) {
		model.addAttribute("user", user != null ? user : new User());
    System.out.println("Home-get:Username "+user.getUsername());
		return "main_template";
	}

}
