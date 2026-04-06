package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("roles");
    }

    @GetMapping
    public String adminPage(Model model, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.findByUsername(username);

        model.addAttribute("currentUser",currentUser);
        model.addAttribute("findAll", userService.findAll());
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleService.findAllRoles());
        return "admin";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("user") User user,
                             @RequestParam(value = "roles", required = false) List<String> roles,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("findAll", userService.findAll());
            model.addAttribute("roleList", roleService.findAllRoles());
            return "admin";
        }
        Set<Role> userRoles = roles == null ? Set.of() :
                roles.stream()
                        .map(roleService::findByRoleName)
                        .collect(Collectors.toSet());

        user.setRoles(userRoles);
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editUser(@Valid @ModelAttribute("user") User user,
                           @RequestParam(value = "roles", required = false) List<String> roles,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("findAll", userService.findAll());
            model.addAttribute("roleList", roleService.findAllRoles());
            return "admin";
        }
        Set<Role> userRoles = roles == null ? Set.of() :
                roles.stream()
                        .map(roleService::findByRoleName)
                        .collect(Collectors.toSet());
        user.setRoles(userRoles);
        userService.update(user.getId(), user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

}
