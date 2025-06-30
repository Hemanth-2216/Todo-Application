
package com.spboot.todo.serviceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spboot.todo.Dto.LoginDto;
import com.spboot.todo.Dto.UserDto;
import com.spboot.todo.config.JwtUtil;
import com.spboot.todo.entity.Role;
import com.spboot.todo.enums.AccountStatus;
import com.spboot.todo.exception.TodoApiException;
import com.spboot.todo.repository.RoleRepository;
import com.spboot.todo.repository.UserRepository;
import com.spboot.todo.service.AuthService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, 
                           PasswordEncoder passwordEncoder, JwtUtil jwtUtil, 
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String register(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        com.spboot.todo.entity.User user = new com.spboot.todo.entity.User();
        user.setFullname(userDto.getFullname());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        Set<Role> roles = new HashSet<>();
        if (userDto.getUsername().toLowerCase().contains("admin")) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
            roles.add(adminRole);
        } else {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("USER role not found"));
            roles.add(userRole);
        }

        user.setRoles(roles);
        user.setStatus(AccountStatus.ACTIVE);

        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public String login(LoginDto loginDto) {
        com.spboot.todo.entity.User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new TodoApiException(HttpStatus.UNAUTHORIZED, "Invalid username or password!"));

        if (user.getStatus() == AccountStatus.BLOCKED) {
            throw new TodoApiException(HttpStatus.UNAUTHORIZED, "Your account is blocked by admin.");
        }
        if (user.getStatus() == AccountStatus.DELETED) {
            throw new TodoApiException(HttpStatus.UNAUTHORIZED, "Your account has been deleted.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        UserDetails userDetails = User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                                .collect(Collectors.toList())
                )
                .build();

        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return accessToken + "," + refreshToken + "," + user.getId() + "," + user.getUsername() + "," + user.getRoles().iterator().next().getName();
    }
}
