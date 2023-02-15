package com.secuirityTutorial.user.service;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.user.dto.request.ConnectUserRequestDto;
import com.secuirityTutorial.user.dto.request.IdDto;
import com.secuirityTutorial.user.dto.request.SearchUserRequestDto;

public interface UserService {

    ResponseDto getAContactDetails(IdDto request);

    ResponseDto searchUsers(SearchUserRequestDto request);


    ResponseDto connectToUser(ConnectUserRequestDto request);
}
