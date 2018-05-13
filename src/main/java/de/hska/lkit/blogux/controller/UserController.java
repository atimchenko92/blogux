package de.hska.lkit.blogux.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import de.hska.lkit.blogux.session.BloguxSecurity;
import de.hska.lkit.blogux.places.Home;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import de.hska.lkit.blogux.model.User;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import de.hska.lkit.blogux.repo.UserRepository;

/**
 * @author atimchenko
 *
 */
@Controller
public class UserController {

	private final UserRepository userRepository;
  private StringRedisTemplate template;

	@Autowired
	public UserController(UserRepository userRepository, StringRedisTemplate template) {
		super();
		this.userRepository = userRepository;
    this.template = template;
	}

  @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
  public String showUserMain(@ModelAttribute User user, @ModelAttribute Home home, @PathVariable String username, Model model, HttpServletRequest req) {
    System.out.println("In users main");

    User inspectedUser = userRepository.getUser(username);
//		inspectedUser.setFollows(userRepository.getUserFollows(inspectedUser));
//		inspectedUser.setFollowers(userRepository.getUserFollowers(inspectedUser));

    model.addAttribute("user", inspectedUser != null ? inspectedUser : new User());
    model.addAttribute("home", home != null ? home : new Home());
    home.setIsself(false);
    System.out.println("show user main "+inspectedUser.getUsername());
    return "main_template";
  }

  @RequestMapping(value = "/user/{username}", method = RequestMethod.GET, params="action=followUnfollow")
  public String followUnfollow(@ModelAttribute User user, @ModelAttribute Home home,
    @PathVariable String username, Model model, HttpServletRequest req) {
    System.out.println("In users follow unfollow");
    User currentUser = BloguxSecurity.getUserByCookie(req, template);

    model.addAttribute("user", user != null ? user : new User());
    model.addAttribute("home", home != null ? home : new Home());
    home.setIsself(false);
    System.out.println("inspectedUser "+user.getUsername());
    System.out.println("currentUser "+currentUser.getUsername());

		userRepository.followUnfollow(currentUser, user);

    return "redirect:/user/"+username;
  }




}
