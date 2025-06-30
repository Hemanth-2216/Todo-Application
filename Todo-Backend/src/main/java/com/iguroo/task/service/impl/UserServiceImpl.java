
package com.iguroo.task.service.impl;

import com.iguroo.task.entity.User;
import com.iguroo.task.exception.TodoApiException;
import com.iguroo.task.repo.UserRepository;
import com.iguroo.task.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public User updateUserProfile(Long userId, User updatedUser) {
        User existingUser = getUserById(userId);
        existingUser.setFullname(updatedUser.getFullname());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        return userRepository.save(existingUser);
    }
}
