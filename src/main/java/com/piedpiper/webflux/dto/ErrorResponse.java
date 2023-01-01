package com.piedpiper.webflux.dto;


import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
	
	private String errorCode;
	private String errorMessage;
	private Date dateTime;
	

}
