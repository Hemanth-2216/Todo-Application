
package com.iguroo.task.config;

import com.iguroo.task.entity.User;
import com.iguroo.task.repo.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(" Checking username in DB: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("User not found: " + username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        System.out.println("User found: " + user.getUsername() + " | Roles: " + user.getRoles());

        //  Convert roles to Spring Security authorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRolename()))
                .collect(Collectors.toSet());

        System.out.println(" Assigned Authorities: " + authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities  //  Fixed issue: Roles converted to GrantedAuthority
        );
    }
}
