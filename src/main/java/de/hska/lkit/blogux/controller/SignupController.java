package de.hska.lkit.blogux.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hska.lkit.blogux.model.Signup;

@Controller
public class SignupController {

	@RequestMapping(value = "/signup")
	public String signupSubmit(@ModelAttribute Signup signup, Model model) {
		model.addAttribute("signup", signup != null ? signup : new Signup());
		return "signup";
	}

}
