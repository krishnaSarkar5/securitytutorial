package com.secuirityTutorial.authentication.service;


import com.secuirityTutorial.authentication.dto.LoginDto;
import com.secuirityTutorial.authentication.dto.ResponseDto;

public interface AuthenticationService {

    public ResponseDto loginWithPassword(LoginDto loginDto);

    public ResponseDto logout(String token);
}
