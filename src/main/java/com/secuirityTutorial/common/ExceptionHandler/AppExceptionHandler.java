package com.secuirityTutorial.common.ExceptionHandler;



import com.secuirityTutorial.authentication.dto.ErrorMessage;
import com.secuirityTutorial.common.exception.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;


//@RestControllerAdvice
public class AppExceptionHandler {


	@ExceptionHandler(value = ServiceException.class)
	public ResponseEntity<Object> handleServiceException(ServiceException ex){
		
		
		String erroMessageDescription = ex.getMessage();
	
		ErrorMessage errorMessage = new ErrorMessage(erroMessageDescription, ex.getIndexError());
		
		return new ResponseEntity<Object>(errorMessage,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	
	@ExceptionHandler(value= MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,WebRequest request){
		
		Map<String, String> resp = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach( (error)->{
			
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			
			resp.put(fieldName, message);
			
			});
		return new ResponseEntity<Map<String, String>>(resp,new HttpHeaders(),HttpStatus.BAD_REQUEST);
		
	}
	
	
	
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> handleGeneralException(Exception ex){
		
		
//		String erroMessageDescription = ex.getLocalizedMessage();
		String erroMessageDescription = "PROCESS_ERROR";
		
//		if(erroMessageDescription == null)
//			erroMessageDescription = ex.toString();
		
		ErrorMessage errorMessage = new ErrorMessage(erroMessageDescription, null);
		
		return new ResponseEntity<Object>(errorMessage,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
}
