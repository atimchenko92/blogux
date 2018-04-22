package de.hska.lkit.blogux.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hska.lkit.blogux.model.*;


@Controller
public class BloguxController {

	@RequestMapping(value = "/signup")
	public String signupSubmit(@ModelAttribute Signup signup, Model model) {
		model.addAttribute("signup", signup != null ? signup : new Signup());
		return "signup";
	}

	@RequestMapping(value = "/")
	public String loadHomePage(@ModelAttribute Home home, Model model) {
		model.addAttribute("home", home != null ? home : new Home());
		return "main_template";
	}

}
