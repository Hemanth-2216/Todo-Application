
package com.spboot.todo.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoId implements Serializable {
    private Long userId;
    private Long taskId;
}
