
package com.spboot.todo.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    
    @NotBlank(message = "Full name cannot be empty")
    @Size(min = 3, max = 50)
    private String fullname;
    
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8)
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;
}
