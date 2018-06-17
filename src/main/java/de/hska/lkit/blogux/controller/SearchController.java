package de.hska.lkit.blogux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import de.hska.lkit.blogux.repo.UserRepository;
import de.hska.lkit.blogux.places.Home;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import de.hska.lkit.blogux.util.BloguxUtils;
import java.util.Set;
import de.hska.lkit.blogux.model.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * @author atimchenko
 *
 */
@Controller
public class SearchController {
  private final UserRepository userRepository;

  @Autowired
  public SearchController(UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }


  @RequestMapping(value = "/search", method = RequestMethod.POST)
  public String searchPost(@ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    System.out.println("in post"+home.getSrcPattern());

    model.addAttribute("home", home);

    home.setCurrentUser(currentUser);
    home.setActivetab("search");
    home.setIsself(true);

    if(home.getSrcPattern().isEmpty())
      return "redirect:/search";
    else
      return "redirect:/search/"+home.getSrcPattern();
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public String searchAll(@ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    System.out.println("search all");
    Set<String> userList = userRepository.getSearchResults("");

    model.addAttribute("ulist", BloguxUtils.getUserListByPage(0, userList));
    model.addAttribute("home", home);

    home.setCurrentUser(currentUser);
    home.setActivetab("search");
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET, params = { "page" })
  public String searchAllPaged(@ModelAttribute Home home, Model model, @RequestParam(value = "page") int page,
      HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    if (page == 0)
      return "redirect:/search";

    System.out.println("search all");
    Set<String> userList = userRepository.getSearchResults("");

    model.addAttribute("ulist", BloguxUtils.getUserListByPage(page - 1, userList));
    model.addAttribute("home", home);

    home.setCurrentUser(currentUser);
    home.setActivetab("search");
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/search/{pattern}", method = RequestMethod.GET)
  public String searchUsers(@ModelAttribute Home home, @PathVariable String pattern, Model model,
      HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    System.out.println("search pattern " + pattern);
    Set<String> userList = userRepository.getSearchResults(pattern);

    model.addAttribute("ulist", BloguxUtils.getUserListByPage(0, userList));
    model.addAttribute("home", home);

    home.setCurrentUser(currentUser);
    home.setActivetab("search");
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/search/{pattern}", method = RequestMethod.GET, params = { "page" })
  public String searchUsersPaged(@ModelAttribute Home home, @PathVariable String pattern,
      @RequestParam(value = "page") int page, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    if (page == 0)
      return "redirect:/search/" + pattern;

    Set<String> userList = userRepository.getSearchResults(pattern);

    model.addAttribute("ulist", BloguxUtils.getUserListByPage(page - 1, userList));
    model.addAttribute("home", home);

    home.setCurrentUser(currentUser);
    home.setActivetab("search");
    home.setIsself(true);

    return "main_template";
  }
}
