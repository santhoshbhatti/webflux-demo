package com.piedpiper.webflux.errorhandling;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.piedpiper.webflux.dto.ErrorResponse;
import com.piedpiper.webflux.exceptions.ValidationException;

@RestControllerAdvice
public class ErrorHandlerAdvice {

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorResponse> validationErrorHandler(ValidationException exception) {
		var errResp =  ErrorResponse.builder()
				.dateTime(new Date())
				.errorCode("701")
				.errorMessage(exception.getMessage())
				.build();
		
		return ResponseEntity.badRequest().body(errResp);
	}
}
