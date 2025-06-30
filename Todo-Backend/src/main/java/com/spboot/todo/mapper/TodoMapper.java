package com.spboot.todo.mapper;

import com.spboot.todo.Dto.TodoDto;
import com.spboot.todo.entity.Todo;
import com.spboot.todo.entity.TodoId;
import com.spboot.todo.entity.User;

public class TodoMapper {

    // Mapping from Todo entity to TodoDto
    public static TodoDto maptoTodoDto(Todo todo) {
        if (todo == null || todo.getId() == null) {
            throw new IllegalArgumentException("Todo or TodoId cannot be null");
        }

        return new TodoDto(
            todo.getId().getTaskId(),  // Extracting taskId from composite key
            todo.getId().getUserId(),  // Extracting userId from composite key
            todo.getTitle(),
            todo.getDescription(),
            todo.isCompleted()
        );
    }

    // Mapping from TodoDto to Todo entity (for POST or PUT requests)
    public static Todo maptoTodoEntity(Long userId, Long taskId, TodoDto todoDto, User user) {
        if (todoDto == null || user == null) {
            throw new IllegalArgumentException("TodoDto or User cannot be null");
        }

        return new Todo(
            new TodoId(userId, taskId),  // Creating composite key with userId and taskId
            todoDto.getTitle(),
            todoDto.getDescription(),
            todoDto.isCompleted(),
            user  // Associating the task with the user
        );
    }
}
