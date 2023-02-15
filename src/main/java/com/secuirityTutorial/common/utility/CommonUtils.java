package com.secuirityTutorial.common.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CommonUtils {


	public static final String INVALID_EMAIL_ADDRESS = "INVALID_EMAIL_ADDRESS";
	public static final String INVALID_PHONE_NUMBER = "INVALID_PHONE_NUMBER";

	
	
	public static Boolean  isEmailValid(String emailAddress) {
		
//		ErrorMessages errorMessages = new ErrorMessages();
		 
	 	if(Objects.isNull(emailAddress)) {
			return false;
	 	}else if (emailAddress.trim().length()==0) {
			return false;
		}
		
		
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
		        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";		
		 boolean matched = Pattern.compile(regexPattern).matcher(emailAddress).matches();
	
		 return matched;

	}

	public static boolean isPhoneNumberValid(String str)
	{

//	    	ErrorMessages errorMessages = new ErrorMessages();

		if(Objects.isNull(str)) {
			return false;
		}else if (str.trim().length()==0) {
			return false;
		}


		// Regex to check string contains only digits
		String regex = "[0-9]+";
		Pattern p = Pattern.compile(regex);

		if (!(!Objects.isNull(str) && !str.isBlank())) {
			return false;
		}
		Matcher m = p.matcher(str);

		return m.matches();
	}
	
	public static boolean isValidDob(String dob){

		try {
			LocalDate dobDate = LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

			return dobDate.isBefore(LocalDate.now());
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	    

	    

	    
	    public static  boolean isValidPassword(String password) {
	    	
	    	 String regex = "^(?=.*[0-9])"
                     + "(?=.*[a-z])(?=.*[A-Z])"
                     + "(?=.*[@#$%^&+=])"
                     + "(?=\\S+$).{8,20}$";

     
	    	 Pattern p = Pattern.compile(regex);

      
	    	 if (password == null) {
	    		 return false;
	    	 }

      
	    	 Matcher m = p.matcher(password);

	    	return m.matches();
	    	
	    }


}
