package de.hska.lkit.blogux.controller;


import de.hska.lkit.blogux.model.User;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hska.lkit.blogux.repo.UserRepository;
import de.hska.lkit.blogux.places.*;

/**
 * @author atimchenko
 *
 */
@Controller
public class UserController {
/*
	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/signup",  method = RequestMethod.GET)
	public String showSignup(@ModelAttribute User newUser) {
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String saveUser(@ModelAttribute User newUser) {
		userRepository.saveUser(newUser);
//		model.addAttribute("message"	, "User successfully added");
		return "redirect:/";
	}

	@RequestMapping(value = "/")
	public String loadHomePage(@ModelAttribute Home home, Model model) {
		model.addAttribute("home", home != null ? home : new Home());
		return "main_template";
	}
*/
}
