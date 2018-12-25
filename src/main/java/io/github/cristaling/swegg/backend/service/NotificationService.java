package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.notifications.Notification;
import io.github.cristaling.swegg.backend.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

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
	}

	public void addNotification(Member member, String text) {
		Notification notification = new Notification(member, new Date(), text);
		addNotification(notification);
	}

	public void addNotification(Notification notification) {
		this.notificationRepository.save(notification);
	}

}
