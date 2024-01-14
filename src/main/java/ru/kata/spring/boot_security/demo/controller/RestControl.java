package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class RestControl {

    @Autowired
    UserService userService;

    @PostMapping("/save")
    public String save(@RequestBody User user) {
        userService.add(user);
        return "redirect:/admin";
    }

    @GetMapping("/get")
    public User get(long id) {
        return userService.getUser(id);
    }

    @GetMapping("/all")
    public List<User> getAll() {
        return userService.usersList();
    }

    @GetMapping("/delete")
    public String delete(long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}