package com.secuirityTutorial.admin.entity;

import com.secuirityTutorial.common.enums.Role;
import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.user.dto.request.RegistrationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String role;

    public Admin(RegistrationRequestDto request){
        this.firstName=request.getFirstName();
        this.lastName=request.getLastName();
        this.password=request.getPassword();
        this.role="ROLE_"+ Role.ADMIN.toString();
        this.email=request.getEmail();
        this.status= Status.ACTIVE.toString();
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }
}
