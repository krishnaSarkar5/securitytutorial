package com.secuirityTutorial.user.dto.request;

import com.secuirityTutorial.common.enums.Role;
import com.secuirityTutorial.common.exception.ServiceException;
import com.secuirityTutorial.common.utility.CommonUtils;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class RegistrationRequestDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String role;

    private String  dob;


    private Integer callCharge;

    private String phoneNumber;

    private String imei;

    private String deviceId;

    private String address;


    public void validate(){

        Map<String ,String > errorMap = new HashMap<>();

        if(Objects.isNull(firstName) || firstName.trim().equalsIgnoreCase("")){
            errorMap.put("firstName","firstName can not be empty");
        }

        if(Objects.isNull(lastName) || lastName.trim().equalsIgnoreCase("")){
            errorMap.put("lastName","lastName can not be empty");
        }
        if(Objects.isNull(email) || email.trim().equalsIgnoreCase("")){
            errorMap.put("email","email can not be empty");
        } else if (!CommonUtils.isEmailValid(email)) {
            errorMap.put("email","enter a valid email id");
        }
        if(Objects.isNull(password) || password.trim().equalsIgnoreCase("")){
            errorMap.put("password","password can not be empty");
        }else if (!CommonUtils.isValidPassword(password)) {
            errorMap.put("password","enter a valid password");
        }

        if(Objects.isNull(role) || role.trim().equalsIgnoreCase("")){
            errorMap.put("role","role can not be empty");
        }else if (Objects.isNull(Role.valueOf(role.toUpperCase()))) {
            errorMap.put("role","enter a valid role");
        }



        if(Objects.isNull(Role.valueOf(role.toUpperCase())) && Role.valueOf(role.toUpperCase()).toString().equalsIgnoreCase("USER")){
            userSpecificValidation(errorMap);
        }

        if(errorMap.size()>0){
            throw new ServiceException("Invalid Request",errorMap);
        }
    }

    private void userSpecificValidation(Map<String, String> errorMap) {
        if(Objects.isNull(dob) || dob.trim().equalsIgnoreCase("")){
            errorMap.put("Date of birth","Date of birth can not be empty");
        } else if (!CommonUtils.isValidDob(dob)) {
            errorMap.put("Date of birth","Invalid");
        }
        if (Objects.isNull(callCharge)){
            errorMap.put("Call Charge","Call charge Can no te be null");
        }

        if(Objects.isNull(phoneNumber) || phoneNumber.trim().equalsIgnoreCase("")){
            errorMap.put("Phone Number","phoneNumber can no te be empty.");
        }else if (!CommonUtils.isPhoneNumberValid(phoneNumber)) {
            errorMap.put("Phone Number","phoneNumber is invalid.");
        }

        if(Objects.isNull(imei) || imei.trim().equalsIgnoreCase("")){
            errorMap.put("IMEI","IMEI can not be empty");
        }

        if(Objects.isNull(deviceId) || deviceId.trim().equalsIgnoreCase("")){
            errorMap.put("Device Id","Device Id can not be empty");
        }

        if(Objects.isNull(address) || address.trim().equalsIgnoreCase("")){
            errorMap.put("address","address can not be empty");
        }
    }

}
