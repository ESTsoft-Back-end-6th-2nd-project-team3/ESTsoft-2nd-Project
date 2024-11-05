package com.estsoft.estsoft2ndproject.exception;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(Long userId) {
		super("User not found with ID: " + userId);
	}
}
