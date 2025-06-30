package com.spboot.todo.repository;

import com.spboot.todo.entity.User;
import com.spboot.todo.enums.AccountStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findByStatus(AccountStatus status);
    List<User> findByStatusNot(AccountStatus deleted);
}
