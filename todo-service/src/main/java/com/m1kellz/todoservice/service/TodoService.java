package com.m1kellz.todoservice.service;

import com.m1kellz.todoservice.entity.Todo;
import com.m1kellz.todoservice.model.TodoRequestForService;
import com.m1kellz.todoservice.model.TodoResponse;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    List<Todo> getAllTodos();
    List<Todo> getAllTodosByUserId(int userId);
    List<TodoResponse> getAllTodosWithDetailsByUserId(int userId);
    Optional<Todo> getTodoById(int id);
    void saveTodo(TodoRequestForService todoDto);
    void updateTodo(int id , TodoRequestForService todoDto);
    void deleteTodo(int id);

}
