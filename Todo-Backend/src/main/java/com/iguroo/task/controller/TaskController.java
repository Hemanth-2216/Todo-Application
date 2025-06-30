
package com.iguroo.task.controller;

import com.iguroo.task.dto.TaskDto;
import com.iguroo.task.entity.Task;
import com.iguroo.task.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/admin/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping("/assign")
    public ResponseEntity<Task> assignTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.assignTask(taskDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksByUserId(userId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDto));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
