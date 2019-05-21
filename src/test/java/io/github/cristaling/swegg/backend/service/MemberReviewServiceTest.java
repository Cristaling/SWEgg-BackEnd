package io.github.cristaling.swegg.backend.service;

import com.github.javafaker.Faker;
import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberReviewSummary;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.MemberReviewRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.ServiceActionResult;
import io.github.cristaling.swegg.backend.utils.TestingUtils;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberReviewServiceTest {

	private MemberReviewRepository memberReviewRepository;
	private UserRepository userRepository;
	private JobRepository jobRepository;
	private EmailSenderService emailSenderService;

	MemberReviewService memberReviewService;

	@Before
	public void setUp() throws Exception {
		this.memberReviewRepository = mock(MemberReviewRepository.class);
		this.userRepository = mock(UserRepository.class);
		this.jobRepository = mock(JobRepository.class);
		this.emailSenderService = mock(EmailSenderService.class);

		this.memberReviewService = new MemberReviewService(this.memberReviewRepository, this.userRepository, this.jobRepository, this.emailSenderService);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addMemberReview() {
		Member reviewed = TestingUtils.getFakeMember();
		Member reviewer = TestingUtils.getFakeMember();

		Mockito.when(this.userRepository.getMemberByEmail(reviewed.getEmail())).thenReturn(reviewed);
		Mockito.when(this.memberReviewRepository.getMemberReviewByReviewerAndReviewed(reviewer, reviewed))
				.thenReturn(null);

		List<Job> jobs = new ArrayList<>();
		Job job = mock(Job.class);

		when(job.getJobStatus()).thenReturn(JobStatus.DONE);
		jobs.add(job);

		when(this.jobRepository.getJobsByOwnerAndEmployee(reviewer, reviewed)).thenReturn(jobs);

		ServiceActionResult<MemberReviewSummary> result = this.memberReviewService.addMemberReview(reviewer,
				reviewed.getEmail(),
				Faker.instance().chuckNorris().fact(),
				4);

		assertEquals(true, result.isSuccessful());
		assertEquals(4, result.getResult().getStars());
	}

}