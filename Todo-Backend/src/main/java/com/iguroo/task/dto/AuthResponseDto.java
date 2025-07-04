
package com.iguroo.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String username;
    private String role;
}
