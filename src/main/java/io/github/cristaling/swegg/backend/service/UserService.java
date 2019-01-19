package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberData;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.UserDataRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import io.github.cristaling.swegg.backend.web.requests.UpdateProfileRequest;
import io.github.cristaling.swegg.backend.web.responses.ProfileResponse;
import io.github.cristaling.swegg.backend.web.responses.UserSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserService {

	private UserRepository userRepository;
	private UserDataRepository userDataRepository;

	private JobRepository jobRepository;

	@Autowired
	public UserService(UserRepository userRepository, UserDataRepository userDataRepository, JobRepository jobRepository) {
		this.userRepository = userRepository;
		this.userDataRepository = userDataRepository;
		this.jobRepository = jobRepository;
	}

	/**
	 * @param email       ->email for the user you want to see the profile
	 * @param userByToken -> user that is logged in
	 * @return profileRepsonse that contains data about the user
	 * null if the email is not in the db
	 */
	public ProfileResponse getProfile(String email, Member userByToken) {
		Member user = userRepository.getMemberByEmail(email);
		if (user == null) {
			return null;
		}
		MemberData userData = user.getMemberData();
		ProfileResponse profileResponse = new ProfileResponse(userData, user.getEmail());
		if (userByToken != null && userByToken.getEmail().equals(email)) {
			//TODO it will contains personal data about the user. now there are none
			return profileResponse;
		}
		return profileResponse;
	}

	protected ProfileResponse getProfileInternal(String email) {
		Member user = userRepository.getMemberByEmail(email);
		if (user == null) {
			return null;
		}
		MemberData userData = user.getMemberData();
		return new ProfileResponse(userData, user.getEmail());
	}

	public ProfileResponse updateProfile(UpdateProfileRequest profileRequest, Member userByToken) {
		Member user = userRepository.getMemberByEmail(profileRequest.getEmail());
		if (user == null) {
			return null;
		}
		if (!userByToken.getEmail().equals(profileRequest.getEmail())) {
			return null;
		}

		MemberData userDataUpdated = user.getMemberData();
		userDataUpdated.setBirthDate(profileRequest.getBirthDate());
		userDataUpdated.setFirstName(profileRequest.getFirstName());
		userDataUpdated.setLastName(profileRequest.getLastName());
		userDataUpdated.setTown(profileRequest.getTown());

		userDataRepository.save(userDataUpdated);
		userDataRepository.flush();

		ProfileResponse profileResponse = new ProfileResponse(userDataUpdated, user.getEmail());

		return profileResponse;
	}

	@Transactional
	public boolean uploadPhoto(MultipartFile file, String email, Member userByToken) throws IOException {
		Member user = userRepository.getMemberByEmail(email);
		if (user == null) {
			return false;
		}
		if (!userByToken.getEmail().equals(email)) {
			return false;
		}
		MemberData memberData = user.getMemberData();
		memberData.setPicture(file.getBytes());
		userDataRepository.save(memberData);
		return true;
	}

	public byte[] getPic(String email) throws IOException {

		Member user = userRepository.getMemberByEmail(email);
		if (user == null) {
			return null;
		}
		byte[] picture=user.getMemberData().getPicture();
		if(picture==null){
			File file =  new File("src\\main\\resources\\image\\user-default-image.jpeg");
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			picture = new byte[(int)file.length()];
			fileInputStreamReader.read(picture);
		}
		return picture;
	}

	@Transactional
    public List<UserSummaryResponse> getSearchedUsers(String name, int page, int count) {
		List<UserSummaryResponse> userSummaryResponses = new ArrayList<>();
		List<Member> members= this.userRepository.getMemberByCompleteNameIgnoreCase(PageRequest.of(page, count),
				name.toLowerCase())
				.getContent()
				.stream()
				.filter(Member::isVerified)
				.collect(Collectors.toList());
		for(Member member : members) {
			UserSummaryResponse userSummaryResponse = new UserSummaryResponse();
			userSummaryResponse.setEmail(member.getEmail());
			userSummaryResponse.setFirstName(member.getMemberData().getFirstName());
			userSummaryResponse.setLastName(member.getMemberData().getLastName());
			userSummaryResponses.add(userSummaryResponse);
		}
		return userSummaryResponses;
	}

	public List<ProfileResponse> getMostRecentUsers(Member userByToken) {

		List<Job> jobs = this.jobRepository.getAllByOwnerAndJobStatus(userByToken, JobStatus.DONE);
		List<Job> jobsDone = this.jobRepository.getAllByEmployeeAndJobStatus(userByToken, JobStatus.DONE);

		jobs.addAll(jobsDone);

		List<ProfileResponse> response = jobs.stream()
				.sorted(Comparator.comparing(Job::getDoneDate).reversed())
				.limit(5)
				.map((job) -> job.getOther(userByToken))
				.map((member) -> this.getProfile(member.getEmail(), userByToken))
				.collect(Collectors.toList());

		return response;
	}

	public List<Member> getMembersForSubscriptions(){
		return userRepository.findAll();
	}

}
