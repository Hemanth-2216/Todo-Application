
package com.spboot.todo.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import com.spboot.todo.Dto.TodoDto;
import com.spboot.todo.entity.Todo;
import com.spboot.todo.entity.TodoId;
import com.spboot.todo.entity.User;
import com.spboot.todo.exception.TodoApiException;
import com.spboot.todo.mapper.TodoMapper;
import com.spboot.todo.repository.TodoRepository;
import com.spboot.todo.repository.UserRepository;
import com.spboot.todo.service.TodoService;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public TodoDto addTask(Long userId, TodoDto todoDto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));

        Long nextTaskId = todoRepo.getNextTaskIdForUser(userId); // Ensures unique taskId per user

        Todo todo = TodoMapper.maptoTodoEntity(userId, nextTaskId, todoDto, user);

        Todo savedTask = todoRepo.save(todo);
        return TodoMapper.maptoTodoDto(savedTask);
    }

    @Override
    public TodoDto updateTask(Long userId, Long taskId, TodoDto todoUpdate) {
        TodoId todoId = new TodoId(userId, taskId);
        Todo existingTask = todoRepo.findById(todoId)
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "Task not found for user"));

        existingTask.setTitle(todoUpdate.getTitle());
        existingTask.setDescription(todoUpdate.getDescription());
        existingTask.setCompleted(todoUpdate.isCompleted());

        Todo updatedTask = todoRepo.save(existingTask);
        return TodoMapper.maptoTodoDto(updatedTask);
    }

    @Override
    public void deleteTask(Long userId, Long taskId) {
        TodoId todoId = new TodoId(userId, taskId);
        if (!todoRepo.existsById(todoId)) {
            throw new TodoApiException(HttpStatus.NOT_FOUND, "Task not found for user");
        }
        todoRepo.deleteById(todoId);
    }

    @Override
    public List<TodoDto> getAllTasks(Long userId) {
        List<Todo> tasks = todoRepo.findByUserId(userId);
        return tasks.stream()
                .map(TodoMapper::maptoTodoDto)
                .collect(Collectors.toList());
    }

    @Override
    public TodoDto getTaskById(Long userId, Long taskId) {
        TodoId todoId = new TodoId(userId, taskId);
        Todo task = todoRepo.findById(todoId)
                .orElseThrow(() -> new TodoApiException(HttpStatus.NOT_FOUND, "Task not found for user"));
        
        return TodoMapper.maptoTodoDto(task);
    }

    @Override
    public TodoDto updateTaskStatus(Long userId, Long taskId, boolean completed) {
        TodoDto todo = getTaskById(userId, taskId);
        todo.setCompleted(completed);
        return updateTask(userId, taskId, todo);
    }
}
