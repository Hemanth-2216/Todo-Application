
package com.spboot.todo;

import com.spboot.todo.entity.Role;
import com.spboot.todo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootTodoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTodoAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadRoles(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("ADMIN").isEmpty()) {
				Role adminRole = new Role();
				adminRole.setName("ADMIN");
				roleRepository.save(adminRole);
			}
			if (roleRepository.findByName("USER").isEmpty()) {
				Role userRole = new Role();
				userRole.setName("USER");
				roleRepository.save(userRole);
			}
		};
	}
}
