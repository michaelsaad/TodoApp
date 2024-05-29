package com.m1kellz.todoservice.repository;

import com.m1kellz.todoservice.entity.TodoDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoDetailsRepository extends JpaRepository<TodoDetails,Integer> {
}
