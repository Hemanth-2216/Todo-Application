
package com.iguroo.task.service.impl;

import com.iguroo.task.dto.TaskDto;
import com.iguroo.task.entity.Task;
import com.iguroo.task.entity.TaskStatus;
import com.iguroo.task.entity.User;
import com.iguroo.task.exception.TodoApiException;
import com.iguroo.task.repo.TaskRepository;
import com.iguroo.task.repo.UserRepository;
import com.iguroo.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task assignTask(TaskDto taskDto) {
        User user = userRepository.findById(taskDto.getUserId())
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "User not found"));

        Task task = new Task();
        task.setTask(taskDto.getTask());

        // Convert String status to Enum before setting it
        try {
            task.setStatus(TaskStatus.valueOf(taskDto.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Invalid status. Allowed: PENDING, COMPLETED, ON_PROGRESS");
        }

        task.setUser(user);

        return taskRepository.save(task);
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public Task updateTask(Long taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "Task not found"));

        task.setTask(taskDto.getTask());

        // Convert String status to Enum
        try {
            task.setStatus(TaskStatus.valueOf(taskDto.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Invalid status. Allowed: PENDING, COMPLETED, ON_PROGRESS");
        }

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "Task not found"));

        taskRepository.delete(task);
    }

    @Override
    public Task updateTaskStatus(Long taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "Task not found"));

        // Convert string to enum
        try {
            task.setStatus(TaskStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Invalid status. Allowed: PENDING, COMPLETED, ON_PROGRESS");
        }

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
