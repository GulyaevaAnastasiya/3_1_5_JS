package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface UserService {
    void add(User user);

    void createRolesIfNotExist();

    List<User> usersList();

    void update(User user, Collection<Role> roles);

    void delete(Long id);

    User getUser(Long id);

    User findByUsername(String username);
}
