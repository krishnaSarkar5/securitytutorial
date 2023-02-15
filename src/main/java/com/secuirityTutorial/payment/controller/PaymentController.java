package com.secuirityTutorial.payment.controller;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.user.dto.request.DoPaymentRequestDto;
import com.secuirityTutorial.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/do-payment")
    public ResponseDto doPayment(@RequestHeader("Authorization") String Authorization, @RequestBody DoPaymentRequestDto request){
        return paymentService.doPayment(request);
    }
}
