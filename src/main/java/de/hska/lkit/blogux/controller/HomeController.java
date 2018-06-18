package de.hska.lkit.blogux.controller;

import de.hska.lkit.blogux.util.BloguxUtils;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import java.util.List;
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

/**
 * @author atimchenko
 *
 */
@Controller
public class HomeController {

  private final PostRepository postRepository;

  @Autowired
  public HomeController(PostRepository postRepository) {
    super();
    this.postRepository = postRepository;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String showHome(@ModelAttribute Home home, @ModelAttribute Post post, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("post", post != null ? post : new Post());

    model.addAttribute("plist", BloguxUtils.getTimelinePostsByPage(0, currentUser.getPersAndFolPosts()));
    home.setCurrentUser(currentUser);
    home.setPagesAmount(BloguxUtils.getTimelinePagesNumber(currentUser.getPersAndFolPosts()));

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = { "page" })
  public String showHomePaged(@ModelAttribute Home home, @ModelAttribute Post post,
      @RequestParam(value = "page") int page, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    if (page == 0)
      return "redirect:/";

    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("post", post != null ? post : new Post());

    model.addAttribute("plist", BloguxUtils.getTimelinePostsByPage(page - 1, currentUser.getPersAndFolPosts()));
    home.setPagesAmount(BloguxUtils.getTimelinePagesNumber(currentUser.getPersAndFolPosts()));
    home.setCurrentUser(currentUser);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.POST, params = "action=sendPost")
  public String sendPost(@Valid @ModelAttribute Post post, BindingResult bindingResult, @ModelAttribute User user,
      @ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");

    model.addAttribute("user", currentUser);
    model.addAttribute("post", post != null ? post : new Post());
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("plist", currentUser.getPersAndFolPosts());
    home.setCurrentUser(currentUser);
    home.setIsself(true);
    home.setActivetab("timeline-my");

    if (bindingResult.hasErrors())
      return "main_template";

    postRepository.savePost(new Post(currentUser.getUsername(), post.getText()));
    return "redirect:/";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = "action=showFollows")
  public String showFollows(@ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");

    model.addAttribute("ulist", BloguxUtils.getUserListByPage(0, currentUser.getFollows()));
    home.setCurrentUser(currentUser);
    home.setActivetab("follows");
    home.setPagesAmount(BloguxUtils.getUserlistPagesNumber(currentUser.getFollows()));
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = { "action=showFollows", "page" })
  public String showFollowsPaged(@ModelAttribute Home home, @RequestParam(value = "page") int page, Model model,
      HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    if (page == 0)
      return "redirect:/?action=showFollows";

    model.addAttribute("ulist", BloguxUtils.getUserListByPage(page - 1, currentUser.getFollows()));
    home.setCurrentUser(currentUser);
    home.setActivetab("follows");
    home.setPagesAmount(BloguxUtils.getUserlistPagesNumber(currentUser.getFollows()));
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = "action=showFollowers")
  public String showFollowers(@ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");

    model.addAttribute("ulist", BloguxUtils.getUserListByPage(0, currentUser.getFollowers()));
    home.setCurrentUser(currentUser);
    home.setActivetab("followers");
    home.setPagesAmount(BloguxUtils.getUserlistPagesNumber(currentUser.getFollowers()));
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = { "action=showFollowers", "page" })
  public String showFollowersPaged(@ModelAttribute Home home, @RequestParam(value = "page") int page, Model model,
      HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");

    if (page == 0)
      return "redirect:/?action=showFollowers";

    model.addAttribute("ulist", BloguxUtils.getUserListByPage(page - 1, currentUser.getFollowers()));
    home.setCurrentUser(currentUser);
    home.setActivetab("followers");
    home.setPagesAmount(BloguxUtils.getUserlistPagesNumber(currentUser.getFollowers()));
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = "action=showGlobal")
  public String showMyGlobal(@ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    //TODO:Can be optimized
    List<Post> plist = postRepository.getGlobalPostsInRange(0, -1);
    model.addAttribute("plist", BloguxUtils.getTimelinePostsByPage(0, plist));
    home.setCurrentUser(currentUser);
    home.setActivetab("timeline-gl");
    home.setPagesAmount(BloguxUtils.getTimelinePagesNumber(plist));
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = { "action=showGlobal", "page" })
  public String showMyGlobalPaged(@ModelAttribute Home home, @RequestParam(value = "page") int page, Model model,
      HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    if (page == 0)
      return "redirect:/?action=showGlobal";

    //TODO:Can be optimized
    List<Post> plist = postRepository.getGlobalPostsInRange(0, -1);
    model.addAttribute("plist", BloguxUtils.getTimelinePostsByPage(page - 1, plist));
    home.setCurrentUser(currentUser);
    home.setActivetab("timeline-gl");
    home.setPagesAmount(BloguxUtils.getTimelinePagesNumber(plist));
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = "action=timeline-myposts")
  public String showMyPosts(@ModelAttribute Home home, @ModelAttribute Post post, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    model.addAttribute("plist", BloguxUtils.getTimelinePostsByPage(0, currentUser.getPersonalPosts()));
    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("post", post != null ? post : new Post());

    home.setCurrentUser(currentUser);
    home.setActivetab("timeline-myposts");
    home.setPagesAmount(BloguxUtils.getTimelinePagesNumber(currentUser.getPersonalPosts()));
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = { "action=timeline-myposts", "page" })
  public String showMyPostsPaged(@ModelAttribute Home home, @ModelAttribute Post post,
      @RequestParam(value = "page") int page, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    if (page == 0)
      return "redirect:/?action=timeline-myposts";

    model.addAttribute("plist", BloguxUtils.getTimelinePostsByPage(page - 1, currentUser.getPersonalPosts()));
    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("post", post != null ? post : new Post());

    home.setCurrentUser(currentUser);
    home.setActivetab("timeline-myposts");
    home.setPagesAmount(BloguxUtils.getTimelinePagesNumber(currentUser.getPersonalPosts()));
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = "action=timeline-myfollows")
  public String showMyFollowsPosts(@ModelAttribute Home home, @ModelAttribute Post post, Model model,
      HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    model.addAttribute("plist", BloguxUtils.getTimelinePostsByPage(0, currentUser.getFollowingPosts()));
    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("post", post != null ? post : new Post());

    home.setCurrentUser(currentUser);
    home.setActivetab("timeline-myfollows");
    home.setPagesAmount(BloguxUtils.getTimelinePagesNumber(currentUser.getFollowingPosts()));
    home.setIsself(true);

    return "main_template";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, params = { "action=timeline-myfollows", "page" })
  public String showMyFollowsPostsPaged(@ModelAttribute Home home, @ModelAttribute Post post,
      @RequestParam(value = "page") int page, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    if (page == 0)
      return "redirect:/?action=timeline-myfollows";

    model.addAttribute("plist", BloguxUtils.getTimelinePostsByPage(page - 1, currentUser.getFollowingPosts()));
    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("post", post != null ? post : new Post());

    home.setCurrentUser(currentUser);
    home.setActivetab("timeline-myfollows");
    home.setPagesAmount(BloguxUtils.getTimelinePagesNumber(currentUser.getFollowingPosts()));
    home.setIsself(true);

    return "main_template";
  }
}
