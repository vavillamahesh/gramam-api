package com.gramam.api.common.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gramam.api.schemes.exception.SchemeNotFoundException;

/**
 * Renders errors as RFC 9457 problem details. Spring's own request errors (bad parameter types,
 * failed validation) are handled by the base class and come back as 400 problem details too.
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(SchemeNotFoundException.class)
	ProblemDetail handleSchemeNotFound(SchemeNotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setTitle("Scheme not found");
		return problem;
	}
}
