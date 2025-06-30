
package com.iguroo.task.service.impl;

import com.iguroo.task.entity.User;
import com.iguroo.task.exception.TodoApiException;
import com.iguroo.task.repo.UserRepository;
import com.iguroo.task.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(user);
    }
}
