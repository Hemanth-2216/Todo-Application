
package com.iguroo.task.service;

import com.iguroo.task.entity.User;

public interface UserService {
    User getUserById(Long userId);
    User updateUserProfile(Long userId, User updatedUser);
}
