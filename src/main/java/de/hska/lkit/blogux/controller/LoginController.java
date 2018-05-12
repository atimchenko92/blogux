package de.hska.lkit.blogux.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.view.RedirectView;
import java.util.Map;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import de.hska.lkit.blogux.session.BloguxSecurity;
import java.util.concurrent.TimeUnit;
import de.hska.lkit.blogux.repo.SessionRepository;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.time.Duration;
import org.springframework.ui.Model;
import de.hska.lkit.blogux.places.Login;
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
public class LoginController {
	private final UserRepository userRepository;

	private final SessionRepository sessionRepository;

	private StringRedisTemplate template;

	private static final Duration TIMEOUT = Duration.ofMinutes(15);

	@Autowired
	public LoginController(UserRepository userRepository, SessionRepository sessionRepository, StringRedisTemplate template) {
		super();
		this.userRepository = userRepository;
		this.sessionRepository = sessionRepository;
		this.template = template;
	}

	/**
	* Show signup template
	**/
	@RequestMapping(value = "/login",  method = RequestMethod.GET, params="action=toSignup")
	public String navigateToSignup(@ModelAttribute Login login, Model model) {
		model.addAttribute("login", login != null ? login : new Login());
		login.setIslogin(false);
		return "login_template";
	}

	/**
	* Show login template
	**/
	@RequestMapping(value = "/login",  method = RequestMethod.GET, params="action=toLogin")
	public String navigateToLogin(@ModelAttribute Login login, Model model) {
		model.addAttribute("login", login != null ? login : new Login());
		login.setIslogin(true);
		return "login_template";
	}

	/**
	* Show login template
	**/
	@RequestMapping(value = "/login",  method = RequestMethod.GET)
	public String showLogin(@ModelAttribute Login login, Model model) {
		model.addAttribute("login", login != null ? login : new Login());
		return "login_template";
	}

	/**
	* Perform sign up
	**/
	@RequestMapping(value = "/login",  method = RequestMethod.POST, params="action=signup")
	public String signUp(@ModelAttribute Login login, Model model) {
		model.addAttribute("login", login != null ? login : new Login());
		login.setIslogin(true);
		//TODO: Validation checks (if user already exists)
		userRepository.createUser(login);
		return "login_template";
	}
	/**
	* Perform login
	**/
	@RequestMapping(value = "/login",  method = RequestMethod.POST, params="action=login")
	public String logIn(@ModelAttribute Login login, HttpServletResponse response, Model model, RedirectAttributes rAttr) {
		if(sessionRepository.checkAuth(login.getName(), login.getPwd())){
			String token = sessionRepository.addAuthTokens(login.getName(), TIMEOUT.getSeconds(), TimeUnit.MINUTES);
			Cookie cookie = new Cookie("auth", token);
			response.addCookie(cookie);
			//TODO: get user information
			rAttr.addFlashAttribute("user", new User(login.getName(), login.getPwd()));
			return "redirect:/";
		}
		model.addAttribute("login", new Login());
		return "redirect:/login";
	}

	/**
	*	Perform logout
	**/
	@RequestMapping(value = "/logout",  method = RequestMethod.GET)
	public String logOut(HttpServletRequest req){
	  System.out.println("in logout");
		User user = BloguxSecurity.getUserByCookie(req, template);
		System.out.println("LOGOUT: "+user.getUsername());
		sessionRepository.deleteAuthTokens(user.getId(), user.getSessionToken());
		return "redirect:/login";
	}



}
