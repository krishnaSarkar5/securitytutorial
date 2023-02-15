package com.secuirityTutorial.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="admin_login_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="token",columnDefinition = "text", nullable = true)
    private String token;

    @Column(name="login_time",nullable = false)
    private LocalDateTime loginTime;

    @Column(name="logout_time",nullable = true)
    private LocalDateTime logoutTime;


    private String status;


    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at",nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="admin_id")
    private Admin admin;
}
