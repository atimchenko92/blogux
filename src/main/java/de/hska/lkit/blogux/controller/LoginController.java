package de.hska.lkit.blogux.controller;

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

	@Autowired
	public LoginController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
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
	* Navigate to login formular
	**/
	@RequestMapping(value = "/login",  method = RequestMethod.POST, params="action=toLogin")
	public String toLogin(@ModelAttribute Login login, Model model) {
		model.addAttribute("login", login != null ? login : new Login());
		login.setIslogin(true);
		return "login_template";
	}

	/**
	* Navigate to sign up formular
	**/
	@RequestMapping(value = "/login",  method = RequestMethod.POST, params="action=toSignup")
	public String toSignup(@ModelAttribute Login login, Model model) {
		model.addAttribute("login", login != null ? login : new Login());
		login.setIslogin(false);
		return "login_template";
	}

	/**
	* Perform sign up
	**/
	@RequestMapping(value = "/login",  method = RequestMethod.POST, params="action=signup")
	public String signUp(@ModelAttribute Login login, Model model) {
		model.addAttribute("login", login != null ? login : new Login());
		login.setIslogin(true);
		//TODO: Validation checks (pwd == pwdConfirm)
		userRepository.createUser(new User(login.getName(), login.getPwd()));
		return "login_template";
	}
	/**
	* Perform login
	**/
	@RequestMapping(value = "/login",  method = RequestMethod.POST, params="action=login")
	public String logIn(@ModelAttribute Login login, Model model) {
		model.addAttribute("login", login != null ? login : new Login());
	  //TODO: Get Cookie, register session
		login.setIslogin(false);
		return "redirect:/";
	}
}
