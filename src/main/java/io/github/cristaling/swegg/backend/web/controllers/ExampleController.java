package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/example")
public class ExampleController {

	private SecurityService securityService;

	@Autowired
	public ExampleController(SecurityService securityService) {
		this.securityService = securityService;
	}

	@RequestMapping("/test")
	public ResponseEntity testLogin(@RequestHeader("token") String token, @RequestBody Member request) {
		if (!this.securityService.canAccessRole(token, MemberRole.PROVIDER)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity("Good Job", HttpStatus.OK);
	}

}
