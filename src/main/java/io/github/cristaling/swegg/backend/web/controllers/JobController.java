package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.member.Member;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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

    @PostMapping
    public ResponseEntity addJob(@RequestHeader("Authorization") String token, @RequestBody JobAddRequest jobAddRequest) {

        if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Member userByToken = securityService.getUserByToken(token);

        Job job = jobService.addJob(jobAddRequest, userByToken);

        System.out.println(jobAddRequest.toString());

        if (job == null) {
            //return new ResponseEntity("Job from this Owner with this status already exists", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/summaries")
    public ResponseEntity getJobSummaries(@RequestHeader("Authorization") String token,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int count) {

        if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<JobSummary> summaries = this.jobService.getJobSummaries(page, count);

        return new ResponseEntity(summaries, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity getJob(@RequestHeader("Authorization") String token, String jobUUID) {
        if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        UUID uuid = UUID.fromString(jobUUID);
        Job job = this.jobService.getJob(uuid);

        if (job == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(this.jobService.getJob(uuid), HttpStatus.OK);
    }

	@GetMapping("/related")
	public ResponseEntity getRelatedJobs(@RequestHeader("Authorization") String token,@RequestParam("email") String email) {
        if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Member userByToken =securityService.getUserByToken(token);
        if (userByToken == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        List<JobSummary> jobSummaryList= jobService.getUserJobs(email,userByToken);
        if(jobSummaryList==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(jobSummaryList,HttpStatus.OK);
	}
}
