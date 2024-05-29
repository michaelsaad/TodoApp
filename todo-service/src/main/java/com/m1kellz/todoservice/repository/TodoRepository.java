package com.m1kellz.todoservice.repository;

import com.m1kellz.todoservice.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository <Todo,Integer> {
    List<Todo> findAllByUserId(int userId);
}
