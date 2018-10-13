package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.web.requests.LoginRequest;
import io.github.cristaling.swegg.backend.web.responses.LoginResponse;
import io.github.cristaling.swegg.backend.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/security")
public class LoginController {

	private SecurityService securityService;

	@Autowired
	public LoginController(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * @param request Contains the username and password to login
	 * @return Contains a token for accessing the API
	 */
	@RequestMapping("/login")
	public @ResponseBody
	LoginResponse
	login(@RequestBody LoginRequest request) {
		String token = this.securityService.login(request.getUsername(), request.getPassword());
		if (token == null) {
			return new LoginResponse(HttpStatus.UNAUTHORIZED);
		}
		return new LoginResponse(token);
	}

}
