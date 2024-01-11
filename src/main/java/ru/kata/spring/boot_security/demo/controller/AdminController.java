package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
        model.addAttribute("allRoles", roleService.findAll());
        return "new_user";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") long id, Model model) {
        User user = userService.getUser(id);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("allRoles", roleService.findAll());
            return "update_user";
        } else {
            return "redirect:/admin/users";
        }

    }


    @PostMapping("/edit")
    public String edit(@ModelAttribute("user") @Valid User user,
                       BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("editUserError", true);
            return "users";
        }
        userService.update(user, user.getRoles());
        return "redirect:/admin";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") @Valid User user) {
        userService.add(user);
        return "redirect:/admin";
    }

    @PostMapping("/remove")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
