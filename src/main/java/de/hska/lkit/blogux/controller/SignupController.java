package de.hska.lkit.blogux.controller;

import de.hska.lkit.blogux.model.User;
import de.hska.lkit.blogux.places.Signup;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import de.hska.lkit.blogux.repo.UserRepository;

@Controller
public class SignupController {

	private final UserRepository userRepository;

	@Autowired
	public SignupController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/signup",  method = RequestMethod.GET)
	public String showSignup(@ModelAttribute Signup signup) {
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String submitUser(@ModelAttribute Signup signup) {
		userRepository.createUser(new User(signup.getName(), signup.getPwd()));
//		model.addAttribute("message"	, "User successfully added");
		return "redirect:/";
	}

}
