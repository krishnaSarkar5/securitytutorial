package com.secuirityTutorial.user.controller;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.user.dto.request.RegistrationRequestDto;
import com.secuirityTutorial.user.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody RegistrationRequestDto request){
        ResponseDto responseDto = registrationService.registerUser(request);
        return  responseDto;
    }
}
