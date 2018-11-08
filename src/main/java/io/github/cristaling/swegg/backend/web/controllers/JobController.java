package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobSummary;
import io.github.cristaling.swegg.backend.service.JobService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.JobAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/job")
public class JobController {

	private JobService jobService;

	private SecurityService securityService;

	@Autowired
	public JobController(JobService jobService, SecurityService securityService) {
		this.jobService = jobService;
		this.securityService = securityService;
	}

	@PostMapping("/add")
	public ResponseEntity addJob(@RequestHeader("Authorization") String token, @RequestBody JobAddRequest jobAddRequest) {

		if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		Job job = jobService.addJob(jobAddRequest);

		if (job == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@GetMapping("/summaries")
	public ResponseEntity getJobSummaries(@RequestHeader("Authorization") String token) {

		if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		List<JobSummary> summaries = this.jobService.getJobSummaries();

		return new ResponseEntity(summaries, HttpStatus.OK);
	}

	@GetMapping
	public List<Job> getAllApplications() {
		return jobService.getAll();
	}

}
