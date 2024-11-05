package com.ad.dena_paona.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column( unique = true)
    private String userName;

    @Column( unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    @Column
    private String contactNumber;

    @Column(nullable = false)
    private LocalDateTime  createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

}
