package com.m1kellz.todoservice.service;

import com.m1kellz.todoservice.entity.Todo;
import com.m1kellz.todoservice.entity.TodoDetails;
import com.m1kellz.todoservice.messaging.TodoCreatedEvent;
import com.m1kellz.todoservice.messaging.TodoEventProducer;
import com.m1kellz.todoservice.model.TodoRequest;
import com.m1kellz.todoservice.model.TodoRequestForService;
import com.m1kellz.todoservice.model.TodoResponse;
import com.m1kellz.todoservice.repository.TodoDetailsRepository;
import com.m1kellz.todoservice.repository.TodoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;


@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final TodoDetailsRepository todoDetailsRepository;
    private final TodoEventProducer todoEventProducer;

    public TodoServiceImpl(TodoRepository todoRepository,
                           TodoDetailsRepository todoDetailsRepository,
                           @org.springframework.beans.factory.annotation.Autowired(required = false)
                           TodoEventProducer todoEventProducer) {
        this.todoRepository = todoRepository;
        this.todoDetailsRepository = todoDetailsRepository;
        this.todoEventProducer = todoEventProducer;
    }

    public TodoResponse mapToTodoResponse(Todo todo) {
        TodoDetails todoDetails = todo.getTodoDetails();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getUserId(),
                todoDetails.getDescription(),
                todoDetails.getPriority(),
                todoDetails.getStatus(),
                todoDetails.getCreatedAt(),
                todoDetails.getLastUpdated());
    }

    public List<TodoResponse> mapToTodoResponses(List<Todo> todos) {
        return todos.stream()
                .map(this::mapToTodoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    @Cacheable(value = "userTodos", key = "#userId")
    public List<Todo> getAllTodosByUserId(int userId) {
        return todoRepository.findAllByUserId(userId);
    }

    @Override
    @Cacheable(value = "userTodoDetails", key = "#userId")
    public List<TodoResponse> getAllTodosWithDetailsByUserId(int userId) {
        List<Todo> todos = todoRepository.findAllByUserId(userId);
        return mapToTodoResponses(todos);
    }

    @Override
    public Optional<Todo> getTodoById(int id) {
        return todoRepository.findById(id);
    }

    @Override
    @CacheEvict(value = {"userTodos", "userTodoDetails"}, allEntries = true)
    public void saveTodo(TodoRequestForService todoDto) {
        Todo todo = new Todo();
        todo.setTitle(todoDto.title());
        todo.setUserId(todoDto.userId());

        TodoDetails todoDetails = new TodoDetails();
        todoDetails.setDescription(todoDto.description());
        todoDetails.setPriority(todoDto.priority());
        todoDetails.setStatus(todoDto.status());

        todo.setTodoDetails(todoDetails);
        Todo savedTodo = todoRepository.save(todo);
        if (todoEventProducer != null) {
            todoEventProducer.publishTodoCreated(
                    new TodoCreatedEvent(savedTodo.getId(), savedTodo.getUserId(), savedTodo.getTitle()));
        }
    }

    @Override
    @CacheEvict(value = {"userTodos", "userTodoDetails"}, allEntries = true)
    public void updateTodo(int id, TodoRequest todoDto) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isEmpty()) {
            return;
        }

        Todo todo = todoOptional.get();
        todo.setTitle(todoDto.title());

        TodoDetails todoDetails = todo.getTodoDetails();
        if (todoDetails != null) {
            todoDetails.setDescription(todoDto.description());
            todoDetails.setPriority(todoDto.priority());
            todoDetails.setStatus(todoDto.status());
        }

        todoRepository.save(todo);
    }

    @Override
    @CacheEvict(value = {"userTodos", "userTodoDetails"}, allEntries = true)
    public void deleteTodo(int id) {
        todoRepository.deleteById(id);
    }
}
