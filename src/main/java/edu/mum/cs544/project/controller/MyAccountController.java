package edu.mum.cs544.project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import edu.mum.cs544.project.config.SessionListener;
import edu.mum.cs544.project.model.Role;
import edu.mum.cs544.project.model.User;
import edu.mum.cs544.project.service.RoleService;
import edu.mum.cs544.project.service.UserService;

@Controller
@RequestMapping("/me/")
public class MyAccountController {


  @Autowired
  private UserService userService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private SessionListener sessionListener;

  @Autowired
  private PasswordEncoder encoder;

  // Update Account
  @GetMapping({"/account/update"})
  public String account(Model model) {
    User user = userService.findByEmail(sessionListener.getUser().getEmail());
    model.addAttribute("user", user);
    return "myaccount/account";
  }

  @PostMapping("/account/update")
  public String updateAccount(Model model, @ModelAttribute("user") User user) {
    userService.save(user);
    return "redirect:/";
  }

  // Sign up
  @GetMapping({"/account/signup"})
  public String signUp(Model model, @ModelAttribute("user") User user) {
    List<Role> roles = roleService.getAll();
    // set the default role for a new user
    user.addRole(roles.get(1));
    
    return "myaccount/signup";
  }

  @PostMapping("/account/signup")
  public String createNewAccount(Model model, @ModelAttribute("user") User user) {
    String view = "myaccount/signup";
    User existingUser = userService.findByEmail(user.getEmail());
    if (existingUser != null) {
      model.addAttribute("errorMsg", "This email already exists. Please use another email.");
      return view;
    }
    userService.save(user);
    model.addAttribute("infoMsg",
        "Your new account has been created sucessfully. Click here to login");
    return view;
  }

}
