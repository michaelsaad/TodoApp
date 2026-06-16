package com.m1kellz.todoservice.testcontroller;

import com.m1kellz.todoservice.controller.TodoController;
import com.m1kellz.todoservice.entity.Priority;
import com.m1kellz.todoservice.entity.Status;
import com.m1kellz.todoservice.entity.Todo;
import com.m1kellz.todoservice.model.TodoRequest;
import com.m1kellz.todoservice.service.TodoService;
import com.m1kellz.todoservice.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoControllerTest {
    @InjectMocks
    private TodoController todoController;

    @Mock
    private TodoService todoService;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        todoController = new TodoController(todoService, jwtUtils);
    }

    @Test
    void saveTodo_ValidToken_ReturnsOk() {
        when(jwtUtils.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtils.extractUserIdFromToken(anyString())).thenReturn(1);
        doNothing().when(todoService).saveTodo(any());

        ResponseEntity<Void> response = todoController.saveTodo(
                new TodoRequest("finish the task", "complete user service", Priority.CRITICAL, Status.IN_PROGRESS),
                "valid-token");

        verify(todoService).saveTodo(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllTodos_ValidToken_ReturnsTodos() {
        when(jwtUtils.isTokenValid(anyString())).thenReturn(true);

        List<Todo> mockTodos = Arrays.asList(new Todo(), new Todo());
        when(todoService.getAllTodos()).thenReturn(mockTodos);

        ResponseEntity<List<Todo>> response = todoController.getAllTodos("valid-token");

        verify(todoService).getAllTodos();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTodos, response.getBody());
    }

    @Test
    void saveTodo_InvalidToken_ReturnsUnauthorized() {
        when(jwtUtils.isTokenValid(anyString())).thenReturn(false);

        ResponseEntity<Void> response = todoController.saveTodo(
                new TodoRequest("finish the task", "complete user service", Priority.CRITICAL, Status.IN_PROGRESS),
                "invalid-token");

        verify(todoService, never()).saveTodo(any());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
