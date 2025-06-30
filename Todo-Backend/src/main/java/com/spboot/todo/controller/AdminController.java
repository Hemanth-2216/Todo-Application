
package com.spboot.todo.controller;

import com.spboot.todo.entity.User;
import com.spboot.todo.enums.AccountStatus;
import com.spboot.todo.repository.UserRepository;
import com.spboot.todo.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Get all users (excluding deleted users)
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findByStatusNot(AccountStatus.DELETED);
        return ResponseEntity.ok(users);
    }

    // ✅ Block a user by ID
    @PutMapping("/block/{id}")
    public ResponseEntity<User> blockUser(@PathVariable Long id) {
        User user = findByIdOrThrow(id);
        user.setStatus(AccountStatus.BLOCKED);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // ✅ Unblock a user by ID
    @PutMapping("/unblock/{id}")
    public ResponseEntity<User> unblockUser(@PathVariable Long id) {
        User user = findByIdOrThrow(id);
        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // ✅ Soft delete user
    @PutMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User user = findByIdOrThrow(id);
        user.setStatus(AccountStatus.DELETED);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // ✅ Helper method to find a user by ID or return 404
    private User findByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }
}
