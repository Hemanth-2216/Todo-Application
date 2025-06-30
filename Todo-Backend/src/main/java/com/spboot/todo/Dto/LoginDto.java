
package com.spboot.todo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class LoginDto {
	@NotBlank(message = "Username cannot be empty")
	@Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;
	@NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    

   }
