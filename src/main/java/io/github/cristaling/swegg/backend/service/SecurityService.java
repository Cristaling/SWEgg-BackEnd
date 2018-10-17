package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.user.User;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.SecurityUtils;
import io.github.cristaling.swegg.backend.utils.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class SecurityService {

	private UserRepository userRepository;

	@Autowired
	public SecurityService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * @param username Username
	 * @param password Password hashed with md5 on client
	 * @return Access token or null if username and password are not correct
	 */
	public String login(String username, String password) {

		if (username == null || password == null) {
			return null;
		}

		User user;

		try {
			user = this.userRepository.getUserByUsernameAndPassword(username, password);
		} catch (EntityNotFoundException ex) {
			return null;
		}

		if (user == null) {
			return null;
		}

		return SecurityUtils.getTokenByUUID(user.getUuid().toString());
	}

	/**
	 * @param token User access token
	 * @param role  Role to verify if the user can access
	 * @return True if the user has the permission of the given role
	 */
	public boolean canAccessRole(String token, UserRole role) {

		User user = getUserByToken(token);

		if (user == null) {
			return false;
		}

		UserRole hasRole = user.getRole();

		return hasRole == UserRole.ADMIN || hasRole == role;
	}

	/**
	 * @param token API access token
	 * @return  The user of the corresponding access token or null
	 */
	public User getUserByToken(String token) {
		if (token == null) {
			return null;
		}

		String uuidString = SecurityUtils.getUUIDByToken(token);
		UUID userUUID;

		try {
			userUUID = UUID.fromString(uuidString);
		} catch (IllegalArgumentException ex) {
			return null;
		}

		Optional<User> user = userRepository.findById(userUUID);

		if (!user.isPresent()) {
			return null;
		}

		return user.get();
	}

}
