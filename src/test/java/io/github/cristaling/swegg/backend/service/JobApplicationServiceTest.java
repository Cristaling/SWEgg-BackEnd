package io.github.cristaling.swegg.backend.service;

import com.github.javafaker.Faker;
import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.repositories.JobApplicationRepository;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import io.github.cristaling.swegg.backend.utils.enums.JobType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobApplicationServiceTest {

	private Faker faker = new Faker();

	private JobRepository jobRepository;
	private JobApplicationRepository jobApplicationRepository;
	private UserRepository userRepository;
	private EmailSenderService emailSenderService;

	private JobApplicationService jobApplicationService;

	@Before
	public void setUp() throws Exception {
		this.jobRepository = mock(JobRepository.class);
		this.jobApplicationRepository = mock(JobApplicationRepository.class);
		this.userRepository = mock(UserRepository.class);
		this.emailSenderService = mock(EmailSenderService.class);

		this.jobApplicationService = new JobApplicationService(jobApplicationRepository,
				userRepository,
				jobRepository,
				emailSenderService);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addJobApplication() {
		Job job = new Job();
		job.setTitle(this.faker.chuckNorris().fact());
		job.setDescription(this.faker.chuckNorris().fact());
		job.setJobStatus(JobStatus.OPEN);
		job.setJobType(JobType.SINGLE);
		job.setOwner(mock(Member.class));

		when(jobRepository.getOne(job.getUuid())).thenReturn(job);

		Member member = mock(Member.class);
		boolean successful = this.jobApplicationService.addJobApplication(member, job.getUuid());

		assert successful;
	}

	@Test
	public void getAll() {
		List<JobApplication> jobApplications = mock(ArrayList.class);

		when(jobApplications.size()).thenReturn(100);
		when(jobApplicationRepository.findAll()).thenReturn(jobApplications);

		assertEquals(100, this.jobApplicationService.getAll().size());
	}

	@Test
	public void getApplicationsForApplicant() {
		Member applicator = mock(Member.class);

		when(this.userRepository.getMemberByEmail("TestEmail")).thenReturn(applicator);

		List<JobApplication> jobApplications = mock(ArrayList.class);

		when(jobApplications.size()).thenReturn(100);
		when(jobApplicationRepository.getJobApplicationsByApplicant(applicator)).thenReturn(jobApplications);

		assertEquals(100, this.jobApplicationService.getApplicationsForApplicant("TestEmail").size());
	}

	@Test
	public void getApplicationsForJob() {
		Job job = new Job();
		job.setTitle(this.faker.chuckNorris().fact());
		job.setDescription(this.faker.chuckNorris().fact());
		job.setJobStatus(JobStatus.OPEN);
		job.setJobType(JobType.SINGLE);
		job.setOwner(mock(Member.class));

		when(this.jobRepository.getOne(job.getUuid())).thenReturn(job);

		List<JobApplication> jobApplications = mock(ArrayList.class);

		when(jobApplications.size()).thenReturn(100);
		when(jobApplicationRepository.getJobApplicationsByJob(job)).thenReturn(jobApplications);

		assertEquals(100, this.jobApplicationService.getApplicationsForJob(job.getUuid()).size());
	}
}