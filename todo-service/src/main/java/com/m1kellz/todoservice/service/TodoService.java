package com.m1kellz.todoservice.service;

import com.m1kellz.todoservice.entity.Todo;
import com.m1kellz.todoservice.model.TodoRequest;
import com.m1kellz.todoservice.model.TodoResponse;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    List<Todo> getAllTodos();
    List<Todo> getAllTodosByUserId(Long userId);
    List<TodoResponse> getAllTodosWithDetailsByUserId(Long userId);
    Optional<Todo> getTodoById(Long id);
    void saveTodo(TodoRequest todoDto);
    void updateTodo(long id ,TodoRequest todoDto);
    void deleteTodo(Long id);
    List<TodoResponse> mapToTodoResponses(List<Todo> todos);
    TodoResponse mapToTodoResponse(Todo todo);
}
