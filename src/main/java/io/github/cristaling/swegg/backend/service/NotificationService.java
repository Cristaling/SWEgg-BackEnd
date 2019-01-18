package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.notifications.Notification;
import io.github.cristaling.swegg.backend.repositories.NotificationRepository;
import io.github.cristaling.swegg.backend.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	private NotificationRepository notificationRepository;

	@Autowired
	public NotificationService(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	public List<Notification> getMemberNotifications(Member member) {
		List<Notification> result = this.notificationRepository.getAllByMember(member);

		result.sort(Comparator.comparing(Notification::getDate).reversed());
		return result;
	}

	public List<Notification> getUnreadNotifications(Member member) {
		List<Notification> result = this.notificationRepository.getAllByMemberAndRead(member, false);

		result.sort(Comparator.comparing(Notification::getDate).reversed());
		return result;
	}

	public void setNotificationRead(Member loggedMember, UUID uuid) {
		Notification notification;

		try {
			notification = this.notificationRepository.getOne(uuid);
		} catch (EntityNotFoundException ex) {
			return;
		}

		if (!notification.getMember().getEmail().equals(loggedMember.getEmail())) {
			return;
		}

		notification.setRead(true);
		this.notificationRepository.save(notification);
	}

	public void addNotification(Member member, String text) {
		Notification notification = new Notification(member, new Date(), text);
		addNotification(notification);
	}

	public void addNotification(Notification notification) {
		sendDataSecured(notification.getMember(), "/notifications", notification);
		this.notificationRepository.save(notification);
	}

	/**
	 * @param member Member to send data to by token
	 * @param endpoint Endpoint starting in '/' to where to send data (will pe prefixed by '/events/{token}')
	 * @param body Data to send
	 */
	public void sendDataSecured(Member member, String endpoint, Object body) {

		String token = SecurityUtils.getTokenByUUID(member.getUuid().toString());

		StringBuilder destination = new StringBuilder();
		destination.append("/events/");
		destination.append(token);
		destination.append(endpoint);

		simpMessagingTemplate.convertAndSend(destination.toString(), body);
	}

	/**
	 * @param member Member to send data to by email
	 * @param endpoint Endpoint starting in '/' to where to send data (will pe prefixed by '/events/{token}')
	 * @param body Data to send
	 */
	public void sendDataUnsecured(Member member, String endpoint, Object body) {

		StringBuilder destination = new StringBuilder();
		destination.append("/events/");
		destination.append(member.getEmail());
		destination.append(endpoint);

		simpMessagingTemplate.convertAndSend(destination.toString(), body);
	}

}
