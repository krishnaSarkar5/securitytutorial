package com.secuirityTutorial.common.secuirity;

import lombok.Data;

@Data
public class JwtAuthRequest {

	private String email;
	
	private String password;
}
