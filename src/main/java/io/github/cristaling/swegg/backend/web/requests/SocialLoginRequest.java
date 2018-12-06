package io.github.cristaling.swegg.backend.web.requests;

public class SocialLoginRequest {

	String token;

	public SocialLoginRequest() {
	}

	public SocialLoginRequest(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
