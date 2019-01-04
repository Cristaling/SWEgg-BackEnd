package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.NotificationService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

	private NotificationService notificationService;

	private SecurityService securityService;

	@Autowired
	public NotificationController(NotificationService notificationService, SecurityService securityService) {
		this.notificationService = notificationService;
		this.securityService = securityService;
	}

	@GetMapping("/all")
	public ResponseEntity getAllNotifications(@RequestHeader("Authorization") String token) {
		if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		Member userByToken = securityService.getUserByToken(token);

		return new ResponseEntity(this.notificationService.getMemberNotifications(userByToken), HttpStatus.OK);
	}

	@GetMapping("/unread")
	public ResponseEntity getUnreadNotifications(@RequestHeader("Authorization") String token) {
		if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		Member userByToken = securityService.getUserByToken(token);

		return new ResponseEntity(this.notificationService.getUnreadNotifications(userByToken), HttpStatus.OK);
	}

	@GetMapping("/read")
	public ResponseEntity getUnreadNotifications(@RequestHeader("Authorization") String token, String uuid) {
		if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		Member userByToken = securityService.getUserByToken(token);
		UUID notificationID = UUID.fromString(uuid);

		this.notificationService.setNotificationRead(userByToken, notificationID);

		return new ResponseEntity(HttpStatus.OK);
	}

}
