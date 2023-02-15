package com.secuirityTutorial.user.entity;

import com.secuirityTutorial.payment.entity.PaymentInformation;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class MeetingConnectInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User connectedBy;

    @ManyToOne
    private User connectedTo;

    @OneToOne
    private PaymentInformation paymentInformation;


    private LocalDateTime connectedAt;


    public MeetingConnectInformation( PaymentInformation paymentInformation){
        this.connectedBy=paymentInformation.getPaymentBy();
        this.connectedTo=paymentInformation.getPaymentFor();
        this.paymentInformation=paymentInformation;
        this.connectedAt=LocalDateTime.now();
    }

}
