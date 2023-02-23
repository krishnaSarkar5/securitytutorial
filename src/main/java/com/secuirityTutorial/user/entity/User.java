package com.secuirityTutorial.user.entity;

import com.secuirityTutorial.common.enums.AuthProvider;
import com.secuirityTutorial.common.enums.Role;
import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.user.dto.request.RegistrationRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;

    private String lastName;
    @Column(unique = true)
    private String email;

    private String password;

    private String status;


    private LocalDate dob;

    private String address;

//    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    private String imageUrl;

    private Integer callCharge;

    @OneToOne(cascade = CascadeType.ALL)
    private PhoneNumber phoneNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String role;

    public User(RegistrationRequestDto request){
        this.firstName=request.getFirstName();
        this.lastName=request.getLastName();
        this.password=request.getPassword();
        this.role="ROLE_"+ Role.USER.toString();
        this.email=request.getEmail();
        this.dob=LocalDate.parse(request.getDob(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.callCharge=request.getCallCharge();
        this.phoneNumber=PhoneNumber.builder().phoneNumber(request.getPhoneNumber()).imei(request.getImei()).deviceId(request.getDeviceId()).build();
        this.address=request.getAddress();

        this.status= Status.ACTIVE.toString();
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }

}
