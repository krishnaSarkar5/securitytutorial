package com.secuirityTutorial.payment.entity;

import com.secuirityTutorial.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;

    private String type;

    private LocalDateTime createdAt;

    private  LocalDateTime updatedAt;

    private Integer balance;

    @ManyToOne
    private User user;
}
