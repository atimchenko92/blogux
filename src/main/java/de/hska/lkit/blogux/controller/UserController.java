package de.hska.lkit.blogux.controller;

import de.hska.lkit.blogux.repo.PostRepository;
import de.hska.lkit.blogux.model.Post;
import java.util.List;
import java.util.Set;
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
  private final PostRepository postRepository;

	@Autowired
	public UserController(UserRepository userRepository, PostRepository postRepository) {
		super();
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

  @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
  public String showUserMain(@ModelAttribute User user, @ModelAttribute Home home, @PathVariable String username, Model model, HttpServletRequest req) {
		User currentUser = (User)req.getAttribute("currentUser");

		//Redirection to home, prohibit
		if(currentUser.getUsername().equals(username))
			return "redirect:/";

		User inspectedUser = userRepository.getUser(username);

    model.addAttribute("user", inspectedUser != null ? inspectedUser : new User());
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("plist", inspectedUser.getPersonalPosts());
    home.setIsself(false);
    home.setCurrentUser(currentUser);

    return "main_template";
  }

  @RequestMapping(value = "/user/{username}", method = RequestMethod.GET, params="action=followUnfollow")
  public String followUnfollow(@ModelAttribute User user, @ModelAttribute Home home,
    @PathVariable String username, Model model, HttpServletRequest req) {
		User currentUser = (User)req.getAttribute("currentUser");

		//Redirection to home, prohibit
		if(currentUser.getUsername().equals(username))
			return "redirect:/";

		userRepository.followUnfollow(currentUser, user);

    model.addAttribute("user", user != null ? user : new User());
    model.addAttribute("home", home != null ? home : new Home());
    home.setIsself(false);
		home.setCurrentUser(currentUser);

    return "redirect:/user/"+username;
  }

	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET, params = "action=showFollows")
	public String showUserFollows(@ModelAttribute User user, @ModelAttribute Home home, @PathVariable String username, Model model, HttpServletRequest req) {
		User currentUser = (User)req.getAttribute("currentUser");

		//Redirection to home
		if(currentUser.getUsername().equals(username))
			return "redirect:/?action=showFollows";

		User inspectedUser = userRepository.getUser(username);
		Set<String> ulist = inspectedUser.getFollows();

    model.addAttribute("user", inspectedUser);
		model.addAttribute("ulist", ulist);

		home.setActivetab("follows");
		home.setCurrentUser(currentUser);
	  home.setIsself(false);

		return "main_template";
	}

	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET, params = "action=showFollowers")
	public String showUserFollowers(@ModelAttribute Home home, @PathVariable String username, Model model, HttpServletRequest req) {
		User currentUser = (User)req.getAttribute("currentUser");

		//Redirection to home
		if(currentUser.getUsername().equals(username))
			return "redirect:/?action=showFollowers";

		User inspectedUser = userRepository.getUser(username);
		Set<String> ulist = inspectedUser.getFollowers();

		model.addAttribute("ulist", ulist);
    model.addAttribute("user", inspectedUser);

		home.setCurrentUser(currentUser);
		home.setActivetab("followers");
	  home.setIsself(false);

		return "main_template";
	}

	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET, params = "action=showGlobal")
	public String showUserGlobal(@ModelAttribute Home home,@PathVariable String username, Model model, HttpServletRequest req) {
		User currentUser = (User) req.getAttribute("currentUser");

		//Redirection to home
		if(currentUser.getUsername().equals(username))
			return "redirect:/?action=showGlobal";

		User inspectedUser = userRepository.getUser(username);
		List<Post> plist = postRepository.getGlobalPostsInRange(0, -1);

		model.addAttribute("plist", plist);
    model.addAttribute("user", inspectedUser);

		home.setCurrentUser(currentUser);
		home.setActivetab("timeline-gl");
		home.setIsself(false);

		return "main_template";
	}
}
