
package com.iguroo.task.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String task;

    @Enumerated(EnumType.STRING)
    private TaskStatus status; 

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
