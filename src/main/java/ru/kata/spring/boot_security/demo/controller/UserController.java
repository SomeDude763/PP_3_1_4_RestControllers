package ru.kata.spring.boot_security.demo.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String allUser(Model model) {
        List<User> users = userService.allUsers();
        model.addAttribute("usersList", users);
        return "users";
    }

    @GetMapping(value = "/edit")
    public String editPage(@RequestParam(value = "id") int id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "editPage";
    }

    @PostMapping(value = "/edit")
    public String editUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "editPage";
        }
        userService.edit(user);
        return "redirect:/users";
    }

    @GetMapping(value = "/add")
    public String addPage(Model model) {
        model.addAttribute("user",new User());
        return "editPage";
    }

    @PostMapping(value = "/add")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "editPage";
        }
        userService.add(user);
        return "redirect:/users";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@RequestParam(value = "id") int id) {
        User user = userService.getById(id);
        userService.delete(user);
        return "redirect:/users";
    }
}
