package com.estsoft.estsoft2ndproject.custonException;

import org.springframework.security.core.AuthenticationException;

public class AdditionalInformationRequireException extends AuthenticationException {
	public AdditionalInformationRequireException(String message) {
		super(message);
	}
}
