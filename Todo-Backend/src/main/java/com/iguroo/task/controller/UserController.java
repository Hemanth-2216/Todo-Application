
package com.iguroo.task.controller;

import com.iguroo.task.dto.TaskDto;
import com.iguroo.task.entity.Task;
import com.iguroo.task.entity.User;
import com.iguroo.task.service.TaskService;
import com.iguroo.task.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/user")
public class UserController {

    private final TaskService taskService;
    private final UserService userService;

    public UserController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // Get all tasks for a user
    @GetMapping("/tasks/{userId}")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable Long userId) {
        List<Task> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    //  Update task status (Users can only update status)
    @PutMapping("/tasks/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody TaskDto taskDto) {
        Task updatedTask = taskService.updateTaskStatus(taskId, taskDto.getStatus());
        return ResponseEntity.ok(updatedTask);
    }

    //  Get User Profile
    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    //  Update User Profile
    @PutMapping("/profile/{userId}")
    public ResponseEntity<User> updateUserProfile(
            @PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userService.updateUserProfile(userId, updatedUser);
        return ResponseEntity.ok(user);
    }
}
