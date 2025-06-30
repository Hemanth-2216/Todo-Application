
package com.spboot.todo.service;

import com.spboot.todo.Dto.UserDto;
import com.spboot.todo.Dto.LoginDto;

public interface AuthService {
    String register(UserDto userDto);
    String login(LoginDto loginDto);
}
