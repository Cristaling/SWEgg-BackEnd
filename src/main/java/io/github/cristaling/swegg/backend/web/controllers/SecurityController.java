package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.web.requests.LoginRequest;
import io.github.cristaling.swegg.backend.web.requests.SocialLoginRequest;
import io.github.cristaling.swegg.backend.web.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/security")
public class SecurityController {

	private SecurityService securityService;

	@Autowired
	public SecurityController(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * @param request Contains the email and password to login
	 * @return Contains a token for accessing the API
	 */
	@RequestMapping("/login")
	public ResponseEntity
	login(@RequestBody LoginRequest request) {
		String token = this.securityService.login(request.getEmail(), request.getPassword());
		if (token == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity(new LoginResponse(token), HttpStatus.OK);
	}

	/**
	 * @param request Contains the email and password to login
	 * @return Contains a token for accessing the API
	 */
	@RequestMapping("/social/login")
	public ResponseEntity
	socialLogin(@RequestBody SocialLoginRequest request) {
		String token = this.securityService.socialLogin(request.getToken());
		if (token == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity(new LoginResponse(token), HttpStatus.OK);
	}

	@RequestMapping("/verify")
	public ResponseEntity
	verifyAccount(@RequestBody String token){
		return securityService.verifyUserAccount(token);
	}

}
