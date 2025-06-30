
package com.spboot.todo.service;

import com.spboot.todo.entity.User;
import com.spboot.todo.enums.AccountStatus;
import com.spboot.todo.exception.UserNotFoundException;
import com.spboot.todo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Block a user by ID
    @Transactional
    public User blockUser(Long id) {
        User user = findByIdOrThrow(id);
        user.setStatus(AccountStatus.BLOCKED);
        return userRepository.save(user);
    }

    // ✅ Unblock a user by ID
    @Transactional
    public User unblockUser(Long id) {
        User user = findByIdOrThrow(id);
        user.setStatus(AccountStatus.ACTIVE);
        return userRepository.save(user);
    }

    // ✅ Soft delete user
    @Transactional
    public User deleteUser(Long id) {
        User user = findByIdOrThrow(id);
        user.setStatus(AccountStatus.DELETED);
        return userRepository.save(user);
    }

    // ✅ Helper method to find user by ID
    private User findByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }
}
