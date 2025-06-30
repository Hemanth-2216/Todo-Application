
package com.iguroo.task.service;

import com.iguroo.task.entity.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    User getUserById(Long userId);
    void deleteUser(Long userId);
}
