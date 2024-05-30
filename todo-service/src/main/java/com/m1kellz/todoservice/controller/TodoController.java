package com.m1kellz.todoservice.controller;

import com.m1kellz.todoservice.entity.Todo;
import com.m1kellz.todoservice.model.TodoRequest;
import com.m1kellz.todoservice.model.TodoRequestForService;
import com.m1kellz.todoservice.model.TodoResponse;
import com.m1kellz.todoservice.service.TodoService;
import com.m1kellz.todoservice.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;
    private final JwtUtils jwtUtils;

    @Autowired
    public TodoController(TodoService todoService, JwtUtils jwtUtils) {
        this.todoService = todoService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/test")
    public ResponseEntity<Void> saveTodoTest(@RequestBody TodoRequest todoDto
                                         ) {
        int extractedUserId = 20;

        TodoRequestForService todoRequestForService = new TodoRequestForService(todoDto.title(),
                extractedUserId, todoDto.description(), todoDto.priority(), todoDto.status());

                todoService.saveTodo(todoRequestForService);
                return ResponseEntity.ok().build();
            }
    @GetMapping("/test")
    public ResponseEntity<List<Todo>> getAllTodosTest() {


            List<Todo> todos = todoService.getAllTodos();
            return ResponseEntity.ok(todos);


    }

    @GetMapping("/test/{userId}")
    public ResponseEntity<List<TodoResponse>> getAllTodosByUserIdTest(@PathVariable int userId) {
        List<TodoResponse> todos = todoService.getAllTodosWithDetailsByUserId(userId);
        return ResponseEntity.ok(todos);
    }

    @PutMapping("/test/{id}")
    public ResponseEntity<Void> updateTodoTest(@PathVariable int id,
                                           @RequestBody TodoRequest todoDto) {

            todoService.updateTodo(id, todoDto);
            return ResponseEntity.ok().build();

    }
    @PostMapping
    public ResponseEntity<Void> saveTodo(@RequestBody TodoRequest todoDto,
                                         @RequestHeader("Authorization") String token) {
        if (jwtUtils.isTokenValid(token)) {
            Integer extractedUserId = jwtUtils.extractUserIdFromToken(token);
            if (extractedUserId != null ) {
                TodoRequestForService todoRequestForService = new TodoRequestForService(
                        todoDto.title(),
                        extractedUserId,
                        todoDto.description(),
                        todoDto.priority(),
                        todoDto.status());

                todoService.saveTodo(todoRequestForService);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(@RequestHeader("Authorization") String token) {

        if (jwtUtils.isTokenValid(token)) {
            List<Todo> todos = todoService.getAllTodos();
            return ResponseEntity.ok(todos);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Todo>> getAllTodosForUserId(
            @RequestHeader("Authorization") String token) {
        if (jwtUtils.isTokenValid(token)) {
            Integer extractedUserId = jwtUtils.extractUserIdFromToken(token);
            if (extractedUserId != null ) {
                List<Todo> todos = todoService.getAllTodosByUserId(extractedUserId);
                return ResponseEntity.ok(todos);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/user/details/")
    public ResponseEntity<List<TodoResponse>> getAllTodosWithDetailsForUser(
            @RequestHeader("Authorization") String token) {

        if (jwtUtils.isTokenValid(token)) {
            Integer extractedUserId = jwtUtils.extractUserIdFromToken(token);
            if (extractedUserId != null ) {
                List<TodoResponse> todoResponses = (todoService
                        .getAllTodosWithDetailsByUserId(extractedUserId));
                return ResponseEntity.ok(todoResponses);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Todo>> getAllTodosByUserId(@PathVariable int userId,
                                                          @RequestHeader("Authorization") String token) {
        if (jwtUtils.isTokenValid(token)) {

                List<Todo> todos = todoService.getAllTodosByUserId(userId);
                return ResponseEntity.ok(todos);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/user/details/{userId}/")
    public ResponseEntity<List<TodoResponse>> getAllTodosWithDetailsByUserId(
            @PathVariable int userId ,@RequestHeader("Authorization") String token) {

        if (jwtUtils.isTokenValid(token)) {

            List<TodoResponse> todoResponses = (todoService
                    .getAllTodosWithDetailsByUserId(userId));
            return ResponseEntity.ok(todoResponses);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable int id,
                                            @RequestHeader("Authorization") String token) {
        if (jwtUtils.isTokenValid(token)) {

            Optional<Todo> todo = todoService.getTodoById(id);
            return todo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTodo(@PathVariable int id,
                                           @RequestBody TodoRequest todoDto,
                                           @RequestHeader("Authorization") String token) {
        if (jwtUtils.isTokenValid(token)) {

            todoService.updateTodo(id, todoDto);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable int id,
                                           @RequestHeader("Authorization") String token) {
        if (jwtUtils.isTokenValid(token)) {

            todoService.deleteTodo(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
