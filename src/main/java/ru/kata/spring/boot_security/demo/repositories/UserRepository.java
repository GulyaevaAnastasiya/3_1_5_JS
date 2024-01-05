package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    @Query("select u from User u left join fetch u.roles where u.name=:username")
    Optional<User> findByUsername (String username);
}
