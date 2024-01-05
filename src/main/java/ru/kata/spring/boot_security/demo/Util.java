package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Component
public class Util implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserService userService;

    @Autowired
    public Util(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.createRolesIfNotExist();
        userService.add(new User("user", "user@mail.ru","8911111111", "user", List.of(roleRepository.getById(1L))));
        userService.add(new User("admin", "admin@mail.ru","8911511111", "admin", List.of(roleRepository.getById(2L))));

    }
}
