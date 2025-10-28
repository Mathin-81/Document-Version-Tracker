package com.dvt.version_tracker.controller;

import com.dvt.version_tracker.model.User;
import com.dvt.version_tracker.repository.UserRepository;
import com.dvt.version_tracker.web.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionUser sessionUser;

    @PostMapping("/select")
    public String selectUser(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/";
        }
        sessionUser.setUser(user);
        return "redirect:/documents";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam String username, RedirectAttributes redirectAttributes) {
        if (username == null || username.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Username cannot be blank.");
            return "redirect:/";
        }
        User user = new User();
        user.setUsername(username);
        userRepository.save(user);
        sessionUser.setUser(user);
        return "redirect:/documents";
    }
}
