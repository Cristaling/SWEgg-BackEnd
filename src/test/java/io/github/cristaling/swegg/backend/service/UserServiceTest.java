package io.github.cristaling.swegg.backend.service;

import com.github.javafaker.Faker;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberData;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.UserDataRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.TestingUtils;
import io.github.cristaling.swegg.backend.web.requests.UpdateProfileRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

	private UserRepository userRepository;
	private UserDataRepository userDataRepository;
	private JobRepository jobRepository;

	private UserService userService;

	@Before
	public void setUp() throws Exception {
		this.userRepository = mock(UserRepository.class);
		this.userDataRepository = mock(UserDataRepository.class);
		this.jobRepository = mock(JobRepository.class);

		this.userService = new UserService(this.userRepository, this.userDataRepository, this.jobRepository);
	}

	@Test
	public void updateProfile() {
		Member member = TestingUtils.getFakeMember();

		MemberData spy = Mockito.spy(member.getMemberData());
		member.setMemberData(spy);

		when(this.userRepository.getMemberByEmail(member.getEmail())).thenReturn(member);

		UpdateProfileRequest request = new UpdateProfileRequest();
		request.setBirthDate(Faker.instance().date().birthday());
		request.setFirstName(Faker.instance().name().firstName());
		request.setLastName(Faker.instance().name().lastName());
		request.setEmail(member.getEmail());
		request.setTown("TestTown");

		this.userService.updateProfile(request, member);

		verify(spy).setTown("TestTown");
//		assertEquals("TestTown", spy.getTown());
	}

}