package com.rickandmorty.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.JsonSyntaxException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(JsonSyntaxException.class)
    public ResponseEntity<ErrorResponse> handleJsonSyntax(JsonSyntaxException ex) {
        ErrorResponse error = new ErrorResponse("400", "Bad Request", "Malformed request syntax: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    	LOG.error("Recurso no econtrado", ex);
        ErrorResponse error = new ErrorResponse("404","Not Found", "Requested resource not found: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException ex) {
	    ErrorResponse error = new ErrorResponse(String.valueOf(ex.getStatusCode().value()),"Error externo", ex.getResponseBodyAsString());
	    return new ResponseEntity<>(error, ex.getStatusCode());
	}
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse("500", "Internal Server Error", "Server encountered an unexpected condition: " + ex.getMessage() );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}