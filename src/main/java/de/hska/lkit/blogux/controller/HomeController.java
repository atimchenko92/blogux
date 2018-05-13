package de.hska.lkit.blogux.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
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
  private final UserRepository userRepository;
  private StringRedisTemplate template;

  @Autowired
  public HomeController(SessionRepository sessionRepository, UserRepository userRepository, StringRedisTemplate template) {
    super();
    this.sessionRepository = sessionRepository;
    this.userRepository = userRepository;
    this.template = template;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
	public String showHome( @ModelAttribute Home home, Model model, HttpServletRequest req) {
    User user = BloguxSecurity.getUserByCookie(req, template);
		model.addAttribute("user", user != null ? user : new User());
    model.addAttribute("home", home != null ? home : new Home());
    System.out.println("Home-get:Username "+user.getUsername());
		return "main_template";
	}

  @RequestMapping(value = "/", method = RequestMethod.GET, params="action=toSettings")
  public String showSettings(@ModelAttribute Home home, Model model, HttpServletRequest req) {
    User user = BloguxSecurity.getUserByCookie(req, template);
    model.addAttribute("user", user != null ? user : new User());
    model.addAttribute("home", home != null ? home : new Home());
    home.setActivetab("settings");
    System.out.println("Home-to settings");
    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.POST, params="action=saveSettings")
  public String saveSettings(@ModelAttribute User user, @ModelAttribute Home home, Model model, HttpServletRequest req) {
    model.addAttribute("user", user != null ? user : new User());
    model.addAttribute("home", home != null ? home : new Home());
    home.setActivetab("timeline-my");
    User currentUser = BloguxSecurity.getUserByCookie(req, template);
    currentUser.setFirstname(user.getFirstname());
    currentUser.setLastname(user.getLastname());
    System.out.println("Home-save settings: "+currentUser.getFirstname()+currentUser.getLastname());
    userRepository.saveUser(currentUser);
    return "redirect:/";
  }

}
