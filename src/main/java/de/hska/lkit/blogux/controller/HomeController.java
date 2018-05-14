package de.hska.lkit.blogux.controller;

import java.util.Set;
import de.hska.lkit.blogux.repo.PostRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import de.hska.lkit.blogux.places.Home;
import de.hska.lkit.blogux.model.User;
import de.hska.lkit.blogux.model.Post;
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

  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Autowired
  public HomeController(UserRepository userRepository, PostRepository postRepository) {
    super();
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String showHome(@ModelAttribute Home home, @ModelAttribute Post post, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("post", post != null ? post : new Post());
    home.setCurrentUser(currentUser);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = "action=toSettings")
  public String showSettings(@ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    home.setActivetab("settings");
    home.setCurrentUser(currentUser);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.POST, params = "action=saveSettings")
  public String saveSettings(@ModelAttribute User user, @ModelAttribute Home home, Model model,
      HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");

    currentUser.setFirstname(user.getFirstname());
    currentUser.setLastname(user.getLastname());
    userRepository.saveUser(currentUser);

    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    home.setActivetab("timeline-my");

    return "redirect:/";
  }

  @RequestMapping(value = "/", method = RequestMethod.POST, params = "action=sendPost")
  public String sendPost(@ModelAttribute Post post, @ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    model.addAttribute("post", post != null ? post : new Post());
    postRepository.savePost(new Post(currentUser.getUsername(), post.getText()));
    home.setCurrentUser(currentUser);

    return "redirect:/";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = "action=showFollows")
  public String showFollows(@ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    Set<String> ulist = currentUser.getFollows();
    model.addAttribute("ulist", ulist);
    home.setCurrentUser(currentUser);
    home.setActivetab("follows");
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = "action=showFollowers")
  public String showFollowers(@ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    Set<String> ulist = currentUser.getFollowers();
    model.addAttribute("ulist", ulist);
    home.setCurrentUser(currentUser);
    home.setActivetab("followers");
    home.setIsself(true);

    return "main_template";
  }

}
