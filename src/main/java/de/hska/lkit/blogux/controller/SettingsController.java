package de.hska.lkit.blogux.controller;

import org.apache.tomcat.util.codec.binary.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import de.hska.lkit.blogux.places.Home;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import de.hska.lkit.blogux.places.Settings;
import de.hska.lkit.blogux.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import de.hska.lkit.blogux.repo.UserRepository;
import org.springframework.stereotype.Controller;

/**
 * @author atimchenko
 *
 */
@Controller
public class SettingsController {

  private final UserRepository userRepository;

  @Autowired
  public SettingsController(UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  @RequestMapping(value = "/settings", method = RequestMethod.GET)
  public String showSettings(@ModelAttribute Home home, Model model, HttpServletRequest req) {
    User currentUser = (User) req.getAttribute("currentUser");
    Settings settings = new Settings(currentUser);

    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("settings", settings);

    home.setActivetab("settings");
    home.setCurrentUser(currentUser);

    return "main_template";
  }

  @RequestMapping(value = "/settings", method = RequestMethod.POST, params="action=saveSettings")
  public String saveSettings(@Valid @ModelAttribute Settings settings,
    BindingResult bindingResult,
    @ModelAttribute Home home,
    Model model,
    HttpServletRequest req) {

    User currentUser = (User) req.getAttribute("currentUser");

    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("settings", settings);

    home.setCurrentUser(currentUser);
    if (!bindingResult.hasErrors()){
      currentUser.setFirstname(settings.getFirstName());
      currentUser.setLastname(settings.getLastName());
      currentUser.setMail(settings.getMail());
      currentUser.setBio(settings.getBio());
      home.setCurrentUser(currentUser);

      userRepository.saveUser(currentUser);
    }

    home.setActivetab("settings");

    return "main_template";
  }

  @RequestMapping(value = "/settings", method = RequestMethod.POST, params="action=uploadImg")
  public String uploadImg(@RequestParam("file") MultipartFile file,
  @ModelAttribute Home home,
  @ModelAttribute Settings settings,
  Model model,
  HttpServletRequest req){

    User currentUser = (User) req.getAttribute("currentUser");

    model.addAttribute("user", currentUser);
    model.addAttribute("home", home != null ? home : new Home());
    model.addAttribute("settings", settings);

    home.setCurrentUser(currentUser);
    home.setActivetab("settings");

    if (file.isEmpty()) {
      return "redirect:/settings";
    }
    try {
        // Get the file and save it somewhere
        byte[] imageByteArray = file.getBytes();
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageByteArray, false)));
        currentUser.setProfilePicture(sb.toString());
        home.setCurrentUser(currentUser);

        userRepository.saveProfilePic(currentUser);

    } catch (Exception e) {
      e.printStackTrace();
      return "redirect:/settings";
    }

    return "main_template";
  }

}
