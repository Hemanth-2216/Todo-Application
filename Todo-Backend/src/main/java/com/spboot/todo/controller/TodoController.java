
package com.spboot.todo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spboot.todo.Dto.TodoDto;
import com.spboot.todo.service.TodoService;

@RestController
@RequestMapping("/api/todo")
@CrossOrigin("*")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping("/addtask/{userId}")
    public ResponseEntity<TodoDto> addTask(@PathVariable Long userId, @RequestBody TodoDto todoDto) {
        TodoDto task = todoService.addTask(userId, todoDto);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/updatetask/{userId}/{taskId}")
    public ResponseEntity<TodoDto> updateTask(
            @PathVariable Long userId,
            @PathVariable Long taskId,
            @RequestBody TodoDto todoUpdate) {
        TodoDto task = todoService.updateTask(userId, taskId, todoUpdate);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/deletetask/{userId}/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long userId, @PathVariable Long taskId) {
        todoService.deleteTask(userId, taskId);
        return ResponseEntity.ok("Task deleted successfully.");
    }

    @GetMapping("/getalltasks/{userId}")
    public ResponseEntity<List<TodoDto>> getAllTasks(@PathVariable Long userId) {
        List<TodoDto> tasks = todoService.getAllTasks(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/gettaskbyId/{userId}/{taskId}")
    public ResponseEntity<TodoDto> getTaskById(@PathVariable Long userId, @PathVariable Long taskId) {
        TodoDto task = todoService.getTaskById(userId, taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/updatestatus/{userId}/{taskId}")
    public ResponseEntity<TodoDto> updateTaskStatus(
            @PathVariable Long userId,
            @PathVariable Long taskId,
            @RequestBody TodoDto statusUpdate) {
        TodoDto updatedTask = todoService.updateTaskStatus(userId, taskId, statusUpdate.isCompleted());
        return ResponseEntity.ok(updatedTask);
    }
}
