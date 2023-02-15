package com.secuirityTutorial.payment.repository;

import com.secuirityTutorial.payment.entity.CardDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardDetailsRepository extends JpaRepository<CardDetails,Long> {

    Optional<CardDetails> findByCardNumberAndUserId(String cardNumber,Long userId);
}
