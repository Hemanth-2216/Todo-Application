
package com.spboot.todo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {
    private Long taskId;
    private Long userId;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 500)
    private String description;

    private boolean completed;
}
