package com.secuirityTutorial.user.service;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.user.dto.request.RegistrationRequestDto;

public interface RegistrationService {

    ResponseDto registerUser(RegistrationRequestDto request);
}
