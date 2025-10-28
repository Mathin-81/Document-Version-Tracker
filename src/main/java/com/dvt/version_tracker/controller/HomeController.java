package com.dvt.version_tracker.controller;

import com.dvt.version_tracker.model.User;
import com.dvt.version_tracker.repository.UserRepository;
import com.dvt.version_tracker.web.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;  // ✅ Add this line!

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionUser sessionUser;

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) String error) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("error", error);
        model.addAttribute("activeUser", sessionUser.getUser());
        return "index";
    }

    @PostMapping("/create-user")
    public String createUser(@RequestParam String username, RedirectAttributes ra) {
        if (username == null || username.trim().isEmpty()) {
            ra.addFlashAttribute("error", "Username cannot be empty!");
            return "redirect:/";
        }

        if (userRepository.findByUsername(username).isPresent()) {
            ra.addFlashAttribute("error", "Username already exists!");
            return "redirect:/";
        }

        User user = new User();
        user.setUsername(username.trim());
        userRepository.save(user);

        sessionUser.setUser(user);
        return "redirect:/documents";
    }

    @PostMapping("/user/select")
    public String selectUser(@RequestParam Long userId, RedirectAttributes ra) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            ra.addFlashAttribute("error", "User not found.");
            return "redirect:/";
        }

        sessionUser.setUser(user);
        return "redirect:/documents";
    }

    @PostMapping("/logout")
    public String logout() {
        sessionUser.clear();
        return "redirect:/";
    }
}
