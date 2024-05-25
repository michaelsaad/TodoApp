package com.m1kellz.todoservice.controller;

import com.m1kellz.todoservice.entity.Todo;
import com.m1kellz.todoservice.model.TodoRequest;
import com.m1kellz.todoservice.model.TodoResponse;
import com.m1kellz.todoservice.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/todos")

public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }


    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Todo>> getAllTodosByUserId(@PathVariable Long userId) {
        List<Todo> todos = todoService.getAllTodosByUserId(userId);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/user/{userId}/details")
    public ResponseEntity<List<TodoResponse>> getAllTodosWithDetailsByUserId(@PathVariable Long userId) {
        List<TodoResponse> todoResponses = (todoService.getAllTodosWithDetailsByUserId(userId));
        return ResponseEntity.ok(todoResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        return todo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> saveTodo(@RequestBody TodoRequest todoDto) {
        todoService.saveTodo(todoDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTodo(@PathVariable long id, @RequestBody TodoRequest todoDto) {
        todoService.updateTodo(id, todoDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok().build();
    }
}
