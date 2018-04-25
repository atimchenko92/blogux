package de.hska.lkit.blogux.controller;

import org.springframework.ui.Model;
import de.hska.lkit.blogux.places.Home;
import de.hska.lkit.blogux.model.User;
import de.hska.lkit.blogux.places.Signup;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import de.hska.lkit.blogux.repo.UserRepository;

@Controller
public class HomeController {

  @RequestMapping(value = "/")
	public String showHome(@ModelAttribute Home home, Model model) {
		model.addAttribute("home", home != null ? home : new Home());
		return "main_template";
	}

}
