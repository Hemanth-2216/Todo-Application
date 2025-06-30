
package com.iguroo.task.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iguroo.task.config.JwtUtil;
import com.iguroo.task.dto.AuthResponseDto;
import com.iguroo.task.dto.LoginDto;
import com.iguroo.task.dto.UserDto;
import com.iguroo.task.entity.Role;
import com.iguroo.task.entity.User;
import com.iguroo.task.exception.TodoApiException;
import com.iguroo.task.mapper.UserMapper;
import com.iguroo.task.repo.RoleRepository;
import com.iguroo.task.repo.UserRepository;
import com.iguroo.task.service.AuthService;

import jakarta.validation.constraints.NotNull;

@Service
public class AuthImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String register(@NotNull UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // Assign USER role by default
        Role userRole = roleRepository.findByRolename("USER")
            .orElseThrow(() -> new RuntimeException("USER role not found!"));

        // Map user data and assign default USER role
        User user = UserMapper.mapToEntity(userDto, null);
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // ✅ Encode Password
        user.setRoles(Set.of(userRole)); // Assign USER role

        userRepository.save(user);
        return "✅ User registered successfully!";
    }

    @Override
    public AuthResponseDto login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new TodoApiException(HttpStatus.UNAUTHORIZED, "Invalid username or password!");
        }

        // Generate Access & Refresh Tokens
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRoles().iterator().next().getRolename());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        // Return structured response with user details
        return new AuthResponseDto(
                accessToken,
                refreshToken,
                user.getId(),
                user.getUsername(),
                user.getRoles().iterator().next().getRolename()
        );
    }


    @Override
    public String registerAdmin(UserDto userDto, Long adminId) {
        // Verify that the requesting user (adminId) is an ADMIN
        User adminUser = userRepository.findById(adminId)
            .orElseThrow(() -> new TodoApiException(HttpStatus.FORBIDDEN, "Admin not found!"));

        boolean isAdmin = adminUser.getRoles().stream()
            .anyMatch(role -> role.getRolename().equals("ADMIN"));

        if (!isAdmin) {
            throw new TodoApiException(HttpStatus.FORBIDDEN, "Only an ADMIN can create another ADMIN!");
        }

        // Check if the new admin's username and email already exist
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // Assign ADMIN role
        Role adminRole = roleRepository.findByRolename("ADMIN")
            .orElseThrow(() -> new RuntimeException("ADMIN role not found!"));

        // Map user data and assign ADMIN role
        User newAdmin = UserMapper.mapToEntity(userDto, null);
        newAdmin.setPassword(passwordEncoder.encode(userDto.getPassword())); // ✅ Encode Password
        newAdmin.setRoles(Set.of(adminRole)); // Assign ADMIN role

        userRepository.save(newAdmin);
        return "✅ Admin registered successfully!";
    }
}
