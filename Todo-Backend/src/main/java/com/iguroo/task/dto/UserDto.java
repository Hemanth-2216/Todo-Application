
package com.iguroo.task.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	private Long id;
    
	@NotBlank(message = "Full name cannot be empty")
    @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
    private String fullname;
	
	@NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;
	
	 @NotBlank(message = "Password cannot be empty")
	    @Size(min = 8, message = "Password must be at least 8 characters long")
	    @Pattern(
	        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
	        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
	    )
    private String password;
	 

	    @NotBlank(message = "Email cannot be empty")
	    @Email(message = "Invalid email format")
    private String email;


	  
}
