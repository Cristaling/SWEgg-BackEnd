package io.github.cristaling.swegg.backend.web.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LoginResponse {

	private String token;

	public LoginResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
