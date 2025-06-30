
package com.iguroo.task.dto;

import com.iguroo.task.entity.TaskStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long userId;
    private String task;
    private String status;
}
