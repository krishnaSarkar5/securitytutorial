package com.secuirityTutorial.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.secuirityTutorial.common.enums.PaymentMode;
import com.secuirityTutorial.common.exception.ServiceException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class DoPaymentRequestDto {

    private Long userId;

    private String paymentMode;

    private String number;

    @JsonIgnore
    public void validate(){
        Map<String ,String > errorMap = new HashMap<>();

        if (Objects.isNull(userId) || userId==0l){
            errorMap.put("User Id","Invalid User Id");
        }

        if(Objects.isNull(paymentMode) || paymentMode.equalsIgnoreCase("")){
            errorMap.put("Payment Mode","Invalid Payment mode");
        }else if(Objects.isNull(PaymentMode.valueOf(paymentMode.toUpperCase()))){
            errorMap.put(paymentMode,"Invalid Payment mode");
        }

        validateCardNumber(errorMap);


        if(Objects.isNull(number) || number.equalsIgnoreCase("")){
            errorMap.put(number,"Invalid number");
        }

        if(errorMap.size()>0){
            throw new ServiceException("Invalid Request",errorMap);
        }
    }

    @JsonIgnore
    private void validateCardNumber(Map<String, String> errorMap) {
        PaymentMode paymentMode1 = PaymentMode.valueOf(paymentMode.toUpperCase());
        if(Objects.isNull(paymentMode1)  && (paymentMode1.equals(PaymentMode.CREDITCARD)) || paymentMode1.equals(PaymentMode.DEBITCARD)){

            if (number.length()!=16){
                errorMap.put(number,"Invalid card number");
            }
        }
    }
}
