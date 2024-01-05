package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RoleRepository roleRepository;
    private final UserServiceImpl userService;

    public AdminController(RoleRepository roleRepository, UserServiceImpl userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping
    public String getUsersList(Model model) {
        List<User> list = userService.usersList();
        model.addAttribute("usersList", list);
        return "users";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("newUser", new User());
        return "new_user";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        User user = userService.get(id);
        model.addAttribute("user", user);
        return "update_user";
    }


    @PostMapping("/edit")
    public String edit(@RequestParam("id") Long id, @ModelAttribute("user") User user) {
        userService.update(user, (List<Role>) user.getRoles());
        return "redirect:/admin";
    }


    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user) {
        userService.add(user);
        return "redirect:/admin";
    }

    @PostMapping("/remove")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
