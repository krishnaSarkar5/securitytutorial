package com.secuirityTutorial.payment.entity;

import com.secuirityTutorial.common.enums.Status;
import com.secuirityTutorial.user.dto.request.DoPaymentRequestDto;
import com.secuirityTutorial.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String transactionId;

    @ManyToOne
    private User paymentBy;

    @ManyToOne
    private User paymentFor;

    private Integer amount;

    private LocalDateTime paymentAt;

    private String status;

    private String paymentMode;

    private boolean meetingConnect;

    @ManyToOne
    private CardDetails cardDetails;

    public PaymentInformation(DoPaymentRequestDto request, User userToBeConnect, User paymentBy,CardDetails cardDetails){
        this.transactionId = UUID.randomUUID().toString();
        this.paymentBy=paymentBy;
        this.paymentFor=userToBeConnect;
        this.amount=userToBeConnect.getCallCharge();
        this.paymentAt=LocalDateTime.now();
        this.meetingConnect=false;
        this.status= Status.SUCCESS.toString();
        this.paymentMode=request.getPaymentMode();
        this.cardDetails=cardDetails;
    }

}
