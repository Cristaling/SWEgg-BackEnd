package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.SecurityUtils;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
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
	 * @param email Email
	 * @param password Password hashed with md5 on client
	 * @return Access token or null if email and password are not correct
	 */
	public String login(String email, String password) {

		if (email == null || password == null) {
			return null;
		}

		Member member;

		try {
			member = this.userRepository.getMemberByEmailAndPassword(email, password);
		} catch (EntityNotFoundException ex) {
			return null;
		}

		if (member == null) {
			return null;
		}

		return SecurityUtils.getTokenByUUID(member.getUuid().toString());
	}

	/**
	 * @param token Member access token
	 * @param role  Role to verify if the user can access
	 * @return True if the user has the permission of the given role
	 */
	public boolean canAccessRole(String token, MemberRole role) {

		Member member = getUserByToken(token);

		if (member == null) {
			return false;
		}

		MemberRole hasRole = member.getRole();

		return hasRole == MemberRole.ADMIN || hasRole == role;
	}

	/**
	 * @param token API access token
	 * @return  The user of the corresponding access token or null
	 */
	public Member getUserByToken(String token) {
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

		Optional<Member> user = userRepository.findById(userUUID);

		if (!user.isPresent()) {
			return null;
		}

		return user.get();
	}

}
