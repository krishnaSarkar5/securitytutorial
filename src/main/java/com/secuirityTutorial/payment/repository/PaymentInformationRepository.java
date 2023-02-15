package com.secuirityTutorial.payment.repository;

import com.secuirityTutorial.payment.entity.PaymentInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentInformationRepository extends JpaRepository<PaymentInformation,Long> {

    public Optional<PaymentInformation> findByTransactionIdAndPaymentByIdAndPaymentForId(String transactionId,Long userId,Long connectUserId);
}
