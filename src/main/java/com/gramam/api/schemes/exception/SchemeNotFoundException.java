package com.gramam.api.schemes.exception;

public class SchemeNotFoundException extends RuntimeException {

	public SchemeNotFoundException(String id) {
		super("No scheme found with id '" + id + "'");
	}
}
