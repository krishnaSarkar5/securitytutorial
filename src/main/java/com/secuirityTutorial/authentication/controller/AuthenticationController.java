package com.secuirityTutorial.authentication.controller;

import com.secuirityTutorial.authentication.dto.LoginDto;
import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> loginwithPassword(@RequestBody LoginDto loginDto){
        ResponseDto responseDto = authenticationService.loginWithPassword(loginDto);
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestHeader("Authorization") String Authorization){
        ResponseDto responseDto = authenticationService.logout(Authorization);
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
    }
}
