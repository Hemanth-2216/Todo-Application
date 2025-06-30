
package com.iguroo.task.service;

import com.iguroo.task.dto.AuthResponseDto;
import com.iguroo.task.dto.LoginDto;
import com.iguroo.task.dto.UserDto;

public interface AuthService {
    String register(UserDto userDto);
    AuthResponseDto login(LoginDto loginDto);  // 🔥 FIX: Return AuthResponseDto instead of String
    String registerAdmin(UserDto userDto, Long adminId);
}
