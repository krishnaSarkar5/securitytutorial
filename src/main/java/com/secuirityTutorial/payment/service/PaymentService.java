package com.secuirityTutorial.payment.service;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.user.dto.request.DoPaymentRequestDto;

public interface PaymentService {

    ResponseDto doPayment(DoPaymentRequestDto request);
}
