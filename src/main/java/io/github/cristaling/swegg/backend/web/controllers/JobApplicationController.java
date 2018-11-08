package io.github.cristaling.swegg.backend.web.controllers;


import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.service.JobApplicationService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.JobApplicationAddRequest;
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
@RequestMapping("/api/job-application")
public class JobApplicationController {

    private JobApplicationService jobApplicationService;

    private SecurityService securityService;

    @Autowired
    public JobApplicationController(JobApplicationService jobApplicationService, SecurityService securityService) {
        this.jobApplicationService = jobApplicationService;
        this.securityService = securityService;
    }

    @PostMapping
    public ResponseEntity addJobApplication(@RequestHeader("Authorization") String token, @RequestBody JobApplicationAddRequest jobApplicationAddRequest) {

        if (!securityService.canAccessRole(token, MemberRole.PROVIDER)) {
            return new ResponseEntity("Member doesnt have permission", HttpStatus.UNAUTHORIZED);
        }
        JobApplication jobApplication = this.jobApplicationService.addJobApplication(jobApplicationAddRequest);

        if (jobApplication == null) {
            return new ResponseEntity("JobApplication for Job by Member already exists", HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping
    public List<JobApplication> getAllApplications() {
        return jobApplicationService.getAll();
    }
}
