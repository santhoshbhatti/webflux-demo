package com.piedpiper.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piedpiper.webflux.dto.Response;
import com.piedpiper.webflux.exceptions.ValidationException;
import com.piedpiper.webflux.service.ReactiveMathService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("exception-handling")
public class ReactiveExceptionManagementController {
	@Autowired
	ReactiveMathService reactiveMathService;

	@GetMapping("square/{number}/non-reactive")
	public Mono<Response> squareNonReactive(@PathVariable("number") Integer number){
		if(number > 30 || number < 10) {
			throw new ValidationException("number should be LT 30 and GT 10");
		}
		
		return reactiveMathService.square(number);
	}
	
	@GetMapping("square/{number}/reactive")
	public Mono<Response> squareReactive(@PathVariable("number") Integer number){
		return Mono.just(number)
		.handle((num,sink) -> {
			if(num > 30 || num < 10) {
				sink.error(new ValidationException("ErrorSignal: number should be LT 30 and GT 10"));
			}else {
				sink.next(num);
			}
		})
		.cast(Integer.class)
		.flatMap(num -> reactiveMathService.square(num));
	}
	
	/*Branching and sending different response in case of validation failure*/
	@GetMapping("square/{number}/branchOnError")
	public Mono<ResponseEntity<Response>> branchOnValidationError(@PathVariable("number") Integer number){
		return Mono.just(number)
				.filter(num -> num > 10 && num <30)
				.flatMap(reactiveMathService::square)
				.map(res -> ResponseEntity.ok().body(res))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}
}
