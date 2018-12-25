package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.abilities.Ability;
import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobSummary;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.AbilityService;
import io.github.cristaling.swegg.backend.service.JobService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.JobType;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.JobAddRequest;
import io.github.cristaling.swegg.backend.web.responses.JobWithAbilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/job")
public class JobController {

	private JobService jobService;
	private AbilityService abilityService;

	private SecurityService securityService;

	@Autowired
	public JobController(JobService jobService, AbilityService abilityService, SecurityService securityService) {
		this.jobService = jobService;
		this.abilityService = abilityService;
		this.securityService = securityService;
	}

	@PostMapping
	public ResponseEntity addJob(@RequestHeader("Authorization") String token, @RequestBody JobAddRequest jobAddRequest) {

		if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		Member userByToken = securityService.getUserByToken(token);

		JobWithAbilities job = jobService.addJob(jobAddRequest, userByToken);

		if (job == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@GetMapping("/summaries")
	public ResponseEntity getJobSummaries(@RequestHeader("Authorization") String token,
	                                      @RequestParam(defaultValue = "") String title,
	                                      @RequestParam(defaultValue = "0") int page,
	                                      @RequestParam(defaultValue = "10") int count) {

		if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		List<JobSummary> summaries = this.jobService.getJobSummaries(title, page, count);

		return new ResponseEntity(summaries, HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity getJob(@RequestHeader("Authorization") String token, String jobUUID) {
		if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		UUID uuid = UUID.fromString(jobUUID);
		JobWithAbilities job = this.jobService.getJob(uuid);

		if (job == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity(job, HttpStatus.OK);
	}

	@GetMapping("/types")
	public ResponseEntity getJobTypes() {
		return new ResponseEntity(JobType.values(), HttpStatus.OK);
	}

	@GetMapping("/related")
	public ResponseEntity getRelatedJobs(@RequestHeader("Authorization") String token, @RequestParam("email") String email) {
		if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		Member userByToken = securityService.getUserByToken(token);
		if (userByToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		List<JobSummary> jobSummaryList = jobService.getUserJobs(email, userByToken);
		if (jobSummaryList == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity(jobSummaryList, HttpStatus.OK);
	}

	@GetMapping("/top")
	public ResponseEntity getTopRelevantJobs(@RequestHeader("Authorization") String token) {
		if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		Member userByToken = securityService.getUserByToken(token);

		List<Ability> userAbilities = this.abilityService.getMemberAbilities(userByToken);
		List<Job> result = this.jobService.getTopRelevantJobs(userAbilities);

		return new ResponseEntity(result, HttpStatus.OK);
	}

}
