package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user: " + username);
        }
        return user.get();
    }

    @Override
    public void add(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            System.out.println("User already exists");
        } else {
            createRolesIfNotExist();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }


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
    public void update(User user, List<Role> roles) {
        User userInDB = userRepository.findByUsername(user.getUsername()).get();
        if (userInDB != null) {
            userInDB.setName(user.getName());
            userInDB.setPassword(passwordEncoder.encode(user.getPassword()));
            userInDB.setEmail(user.getEmail());
            userInDB.setPhoneNumber(user.getPhoneNumber());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException
                ("User with id " + id + " not found"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }


}
