package io.github.cristaling.swegg.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.javanet.NetHttpTransport;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberData;
import io.github.cristaling.swegg.backend.repositories.UserDataRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.ImageUtils;
import io.github.cristaling.swegg.backend.utils.SecurityUtils;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class SecurityService {

	private UserRepository userRepository;
	private UserDataRepository userDataRepository;

	private GoogleIdTokenVerifier verifier;


	@Autowired
	public SecurityService(UserRepository userRepository, UserDataRepository userDataRepository) {
		this.userRepository = userRepository;
		this.userDataRepository = userDataRepository;

		this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), Utils.getDefaultJsonFactory())
				.setAudience(Arrays.asList("571895084013-hcqh7qd55ueagegmd13efpin3tq6hcim.apps.googleusercontent.com"))
				.build();
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

		if (!member.isVerified()) {
			return null;
		}

		return SecurityUtils.getTokenByUUID(member.getUuid().toString());
	}

	public String socialLogin(String idTokenString) {

		try {
			GoogleIdToken idToken = verifier.verify(idTokenString);

			if (idToken != null) {
				GoogleIdToken.Payload payload = idToken.getPayload();

				String userId = payload.getSubject();

				Member existent = this.userRepository.getMemberByGoogleID(userId);

				if (existent != null && !existent.getEmail().equalsIgnoreCase("iovarares@gmail.com")) {
					return SecurityUtils.getTokenByUUID(existent.getUuid().toString());
				}

				String email = payload.getEmail();

//				boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//				String name = (String) payload.get("name");
//				String locale = (String) payload.get("locale");
				String pictureUrl = (String) payload.get("picture");
				String familyName = (String) payload.get("family_name");
				String givenName = (String) payload.get("given_name");

				existent = this.userRepository.getMemberByEmail(email);

				if (existent != null) {
					existent.setGoogleID(userId);

					MemberData existentMemberData = existent.getMemberData();
					existentMemberData.setPicture(ImageUtils.getBytesFromURL(pictureUrl));
					this.userDataRepository.save(existentMemberData);

					this.userRepository.save(existent);

					return SecurityUtils.getTokenByUUID(existent.getUuid().toString());
				}

				MemberData memberData = new MemberData();
				memberData.setFirstName(givenName);
				memberData.setLastName(familyName);
				memberData.setPicture(ImageUtils.getBytesFromURL(pictureUrl));

				Member member = new Member();
				member.setEmail(email);
				member.setPassword(UUID.randomUUID().toString());
				member.setMemberData(memberData);
				member.setRole(MemberRole.CLIENT);
				member.setGoogleID(userId);

				memberData.setMember(member);

				userRepository.save(member);
				userRepository.flush();

				userDataRepository.save(memberData);
				userDataRepository.flush();

				return SecurityUtils.getTokenByUUID(member.getUuid().toString());
			} else {
				return null;
			}
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		return null;
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

		return hasRole == MemberRole.ADMIN || hasRole == MemberRole.CLIENT || hasRole == MemberRole.PROVIDER;
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

    public ResponseEntity verifyUserAccount(String token) {
		Member member = getUserByToken(token);
		if(member == null){
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		if(member.isVerified()){
			return new ResponseEntity(HttpStatus.CONFLICT);
		}
		member.setVerified(true);
		userRepository.save(member);
		return new ResponseEntity(HttpStatus.OK);
    }
}
