package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.abilities.Ability;
import io.github.cristaling.swegg.backend.core.abilities.AbilityUse;
import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobSummary;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.repositories.AbilityRepository;
import io.github.cristaling.swegg.backend.repositories.AbilityUseRepository;
import io.github.cristaling.swegg.backend.repositories.JobApplicationRepository;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import io.github.cristaling.swegg.backend.web.requests.JobAddRequest;
import io.github.cristaling.swegg.backend.web.requests.JobUpdateStatusRequest;
import io.github.cristaling.swegg.backend.web.responses.JobWithAbilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobService {

	private JobRepository jobRepository;
	private UserRepository userRepository;
	private JobApplicationRepository jobApplicationRepository;
	private AbilityUseRepository abilityUseRepository;
	private AbilityRepository abilityRepository;
	private EmailSenderService emailSenderService;

	private AbilityService abilityService;

	@Autowired
	public JobService(JobRepository jobRepository, UserRepository userRepository, JobApplicationRepository jobApplicationRepository, AbilityUseRepository abilityUseRepository, AbilityRepository abilityRepository, EmailSenderService emailSenderService, AbilityService abilityService) {
		this.jobRepository = jobRepository;
		this.userRepository = userRepository;
		this.jobApplicationRepository = jobApplicationRepository;
		this.abilityUseRepository = abilityUseRepository;
		this.abilityRepository = abilityRepository;
		this.emailSenderService = emailSenderService;
		this.abilityService = abilityService;
	}

	private JobWithAbilities addAbilitiesToJob(Job job) {
		JobWithAbilities jobWithAbilities = new JobWithAbilities(job);

		List<AbilityUse> uses = abilityUseRepository.getAbilityUsesByJob(job);
		List<Ability> abilities = new ArrayList<>();
		for (AbilityUse use : uses) {
			abilities.add(use.getAbility());
		}
		jobWithAbilities.setAbilities(abilities);
		return jobWithAbilities;
	}

	private List<JobWithAbilities> addAbilitiesToJobs(List<Job> jobs) {
		List<JobWithAbilities> jobWithAbilitiesList = new ArrayList<>();
		for (Job job : jobs) {
			jobWithAbilitiesList.add(addAbilitiesToJob(job));
		}
		return jobWithAbilitiesList;
	}

	public JobWithAbilities addJob(JobAddRequest jobAddRequest, Member member) {
//        if (jobRepository.getJobsByOwnerAndJobStatus(member, jobAddRequest.getJobStatus()).size() >7) {
//            return null;
//        }
		if (jobAddRequest.getTitle().length() < 5 || jobAddRequest.getDescription().length() < 5) {
			return null;
		}

		Job job = new Job(jobAddRequest);
		job.setOwner(member);


		jobRepository.save(job);
		jobRepository.flush();
		if (jobAddRequest.getAbilities().size() != 0) {
			for (String abilityName : jobAddRequest.getAbilities()) {
				AbilityUse abilityUse = new AbilityUse();

				Ability ability = this.abilityService.getAbilityByName(abilityName);

				abilityUse.setAbility(ability);
				abilityUse.setJob(job);

				this.abilityUseRepository.save(abilityUse);
			}
		}
		return addAbilitiesToJob(job);
	}

	public List<JobWithAbilities> getAll() {
		return addAbilitiesToJobs(jobRepository.findAll());
	}

	public List<JobSummary> getJobSummaries(String title, int page, int count) {
		List<Job> jobs;
		if ("".equals(title)) {
			if (page < 0) {
				jobs = this.jobRepository.findAll();
			} else {
				jobs = this.jobRepository.findAll(PageRequest.of(page, count)).getContent();
			}
		} else {
			if (page < 0) {
				jobs = this.jobRepository.getJobsByTitleContainingIgnoreCase(title);
			} else {
				jobs = this.jobRepository.getJobsByTitleContainingIgnoreCase(PageRequest.of(page, count), title).getContent();
			}
		}
		List<JobWithAbilities> jobWithAbilitiesList = addAbilitiesToJobs(jobs);
		List<JobSummary> jobSummaries = new ArrayList<>();
		for (JobWithAbilities jobWithAbilities : jobWithAbilitiesList) {
			jobSummaries.add(new JobSummary(jobWithAbilities));
		}
		return jobSummaries;
	}

	public List<JobSummary> getUserJobs(String email, Member userByToken) {
		if (!userByToken.getEmail().equals(email)) {
			return null;
		}
		Member member = userRepository.getMemberByEmail(email);
		List<Job> jobList = jobRepository.getAllJobsForUser(member.getUuid());
		List<JobSummary> jobSummaryList = new ArrayList<>();
		for (Job job : jobList
				) {
			jobSummaryList.add(new JobSummary(job));
		}
		return jobSummaryList;
	}


	private JobSummary getSummary(Job job) {
		JobWithAbilities jobWithAbilities = addAbilitiesToJob(job);
		return new JobSummary(jobWithAbilities);
	}

	public JobWithAbilities getJob(UUID uuid) {
		return addAbilitiesToJob(this.jobRepository.getOne(uuid));
	}

	public List<JobSummary> getTopRelevantJobs(List<Ability> abilities) {

		Map<Job, Long> map = abilities.stream()
				.flatMap(
						ability -> this.abilityUseRepository.getAbilityUsesByAbility(ability).stream()
				)
				.collect(
						Collectors.groupingBy(
								AbilityUse::getJob,
								Collectors.mapping(
										AbilityUse::getAbility,
										Collectors.counting()
								)
						)
				);

		List<JobSummary> jobs = map.entrySet().stream()
				.sorted(
						(o1, o2) -> o2.getValue().compareTo(o1.getValue())
				)
				.map(entry -> entry.getKey())
				.map(JobSummary::new)
				.collect(Collectors.toList());

		int listSize = jobs.size();

		if (listSize == 0) {
			return new ArrayList<>();
		}

		return jobs.subList(0, Math.min(5, listSize));
	}

	public boolean selectEmployeeForJob(Member owner, String jobUUID, String employeeEmail) {
		Job job = this.jobRepository.getOne(UUID.fromString(jobUUID));
		if (!job.getOwner().getEmail().equals(owner.getEmail()))
			return false;
		Member employee = this.userRepository.getMemberByEmail(employeeEmail);
		if (employee == null)
			return false;
		job.setEmployee(employee);
		job.setJobStatus(JobStatus.ACCEPTED);
		this.jobRepository.save(job);
		this.jobRepository.flush();

		emailSenderService.sendJobInviteNotificationToMember(job);

        return true;
    }

    public List<JobSummary> getOpenJobsForOwner(String email){
        Member owner = this.userRepository.getMemberByEmail(email);
        List<Job> jobList= jobRepository.getJobsByOwnerAndJobStatus(owner,JobStatus.OPEN);
        List<JobSummary> jobSummaries=new ArrayList<>();
        for(Job job : jobList){
            jobSummaries.add(new JobSummary(job));
        }
        return jobSummaries;

    }

	public JobSummary updateJobStatus(JobUpdateStatusRequest jobUpdateStatusRequest) {
		Job job=jobRepository.getByUuid(jobUpdateStatusRequest.getJobId());
		job.setJobStatus(jobUpdateStatusRequest.getJobStatus());
		jobRepository.save(job);
		return new JobSummary(job);
	}

	public JobWithAbilities updateJob(String uuid, JobAddRequest jobAddRequest) {
		Job job= jobRepository.getByUuid(UUID.fromString(uuid));
		job.setJobStatus(jobAddRequest.getJobStatus());
		job.setDescription(jobAddRequest.getDescription());
		job.setTitle(jobAddRequest.getTitle());
		jobRepository.save(job);
		return new JobWithAbilities(job);
	}
}
