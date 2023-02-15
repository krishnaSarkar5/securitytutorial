package com.secuirityTutorial.common.dto;

import com.secuirityTutorial.authentication.dto.ResponseDto;
import com.secuirityTutorial.common.enums.ResponseStatus;

public class Response {

    public ResponseDto getSuccessResponseDto(Object data){

        ResponseDto responseDto = new ResponseDto();

        responseDto.setMessage(ResponseStatus.SUCCESS.toString());
        responseDto.setStatus(true);
        responseDto.setData(data);
        return responseDto;
    }

    public ResponseDto getSuccessResponseDto(Object data,String message){

        ResponseDto responseDto = new ResponseDto();

        responseDto.setMessage(message);
        responseDto.setStatus(true);
        responseDto.setData(data);
        return responseDto;
    }

    public ResponseDto getFailureResponseDto(Object data){

        ResponseDto responseDto = new ResponseDto();

        responseDto.setMessage(ResponseStatus.FAILURE.toString());
        responseDto.setStatus(false);
        responseDto.setData(data);
        return responseDto;
    }
}
