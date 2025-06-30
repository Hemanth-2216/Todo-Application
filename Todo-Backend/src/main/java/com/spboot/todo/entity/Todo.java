package com.spboot.todo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "todos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    @EmbeddedId
    private TodoId id;

    private String title;
    private String description;
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"todos", "password", "roles", "status", "email"}) 
    // ✅ Prevent infinite loop and hide sensitive data
    private User user;
}
