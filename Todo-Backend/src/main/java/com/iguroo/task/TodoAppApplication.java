
package com.iguroo.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.iguroo.task.entity.Role;
import com.iguroo.task.entity.User;
import com.iguroo.task.repo.RoleRepository;
import com.iguroo.task.repo.UserRepository;

import java.util.Set;

@SpringBootApplication
public class TodoAppApplication {
// application main entry point//
    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
    }

    @Bean
    @Transactional // Ensures database consistency
    public CommandLineRunner loadRolesAndAdmin(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            // ✅ Create roles if they don't exist
            Role adminRole = roleRepository.findByRolename("ADMIN").orElseGet(() -> {
                Role role = new Role();
                role.setRolename("ADMIN");
                return roleRepository.save(role);
            });

            Role userRole = roleRepository.findByRolename("USER").orElseGet(() -> {
                Role role = new Role();
                role.setRolename("USER");
                return roleRepository.save(role);
            });

            // ✅ Prevent duplicate admin creation by checking email
            if (!userRepository.existsByEmail("Hemanth@email.com")) { 
                User admin = new User();
                admin.setFullname("Hemanth");
                admin.setUsername("Hemanth22");
                admin.setPassword("$2a$12$ZJQhk7uBRqM5kA5hlbcHE.01cXFWb1Iw8QksZMpz7FZ/yhc0yvTvS"); // ⚠ No encoding as per your request
                admin.setEmail("Hemanth@email.com");

                // Assign ADMIN role
                admin.setRoles(Set.of(adminRole));

                userRepository.save(admin);
                System.out.println("✅ First ADMIN created successfully!");
            } else {
                System.out.println("⚠ Admin with email 'Hemanth@email.com' already exists. Skipping creation.");
            }
        };
    }
}
