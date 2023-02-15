package com.secuirityTutorial.authentication.dto;


import com.secuirityTutorial.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
	

	private String username;
	

	private String password;
	



	public void validate(){

		Map<String ,String> errorMap = new HashMap<>();

		if(Objects.isNull(username) || username.trim().equalsIgnoreCase("")){
			errorMap.put("username","username can not be empty");
		}

		if(Objects.isNull(password) || password.trim().equalsIgnoreCase("")){
			errorMap.put("password","password can not be empty");
		}

		if (errorMap.size()>0){
			throw  new ServiceException("Invalid Data",errorMap);
		}
	}

}
