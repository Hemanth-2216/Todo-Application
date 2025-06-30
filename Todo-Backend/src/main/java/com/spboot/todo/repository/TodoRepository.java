package com.spboot.todo.repository;

import com.spboot.todo.entity.Todo;
import com.spboot.todo.entity.TodoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, TodoId> {

    List<Todo> findByUserId(Long userId);

    @Query("SELECT COALESCE(MAX(t.id.taskId), 0) + 1 FROM Todo t WHERE t.id.userId = :userId")
    Long getNextTaskIdForUser(@Param("userId") Long userId);
}
