package io.github.cristaling.swegg.backend.web.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LoginResponse extends ResponseEntity {

	private String token;

	public LoginResponse(HttpStatus status) {
		super(status);
	}

	public LoginResponse(String token) {
		super(HttpStatus.OK);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
