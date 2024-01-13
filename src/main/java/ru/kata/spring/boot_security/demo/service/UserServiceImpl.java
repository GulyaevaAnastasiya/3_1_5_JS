package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository,
                           @Lazy PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void add(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            System.out.println("User already exists");
        } else {
            createRolesIfNotExist();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    @Transactional
    public void createRolesIfNotExist() {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role(1L, "ROLE_USER"));
        }
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(new Role(2L, "ROLE_ADMIN"));
        }
    }

    @Override
    public List<User> usersList() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void update(User user, Collection<Role> roles) {
//        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
//            throw new EntityNotFoundException("User with name " + user.getName() + " is already exist");
//        } else {
            userRepository.save(user);
//        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException
                ("User with id " + id + " not found"));
    }

    @Override
    public User findByUsername(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new EntityNotFoundException("User not found");
        } else {
            return userRepository.findByUsername(username).get();
        }
    }
}
