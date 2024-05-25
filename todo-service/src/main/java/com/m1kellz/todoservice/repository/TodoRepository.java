package com.m1kellz.todoservice.repository;

import com.m1kellz.todoservice.entity.Todo;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository <Todo,Long> {
    List<Todo> findAllByUserId(Long userId);
}
