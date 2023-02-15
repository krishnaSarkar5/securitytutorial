package com.secuirityTutorial.admin.service;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.user.dto.request.MapUserRequestDto;
import com.secuirityTutorial.user.dto.request.SearchUserRequestDto;

public interface AdminService {

    ResponseDto mapUsers(MapUserRequestDto request);

    ResponseDto searchUsers(SearchUserRequestDto request);
}
