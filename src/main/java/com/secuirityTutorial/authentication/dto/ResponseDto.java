package com.secuirityTutorial.authentication.dto;

import lombok.Data;

@Data
public class ResponseDto {

	private boolean status;
	
	private String message;
	
	private Object data;
}
