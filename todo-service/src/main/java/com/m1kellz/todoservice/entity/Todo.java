package com.m1kellz.todoservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotEmpty(message = "enter the title")
    @Column(name = "title")
    private String title;

    /*@NotEmpty(message = "user id is a mandatory field")*/
    @NotNull (message = "user id is a mandatory field")
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "todo_details_id")
    private TodoDetails todoDetails ;

}
