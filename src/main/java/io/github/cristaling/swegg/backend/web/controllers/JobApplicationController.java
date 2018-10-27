package io.github.cristaling.swegg.backend.web.controllers;


import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.service.JobApplicationService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.UserRole;
import io.github.cristaling.swegg.backend.web.requests.JobApplicationAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/jobapplication")
public class JobApplicationController {

    private JobApplicationService jobApplicationService;

    private SecurityService securityService;

    @Autowired
    public JobApplicationController(JobApplicationService jobApplicationService, SecurityService securityService) {
        this.jobApplicationService = jobApplicationService;
        this.securityService = securityService;
    }

    @PostMapping("/add")
    public ResponseEntity addJobApplication(@RequestHeader("token") String token, @RequestBody JobApplicationAddRequest jobApplicationAddRequest) {

        if (!securityService.canAccessRole(token, UserRole.PROVIDER)) {
            return new ResponseEntity("User doesnt have permission", HttpStatus.UNAUTHORIZED);
        }
        JobApplication jobApplication = this.jobApplicationService.addJobApplication(jobApplicationAddRequest);

        if (jobApplication == null) {
            return new ResponseEntity("JobApplication for Job by User already exists", HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public List<JobApplication> getAllApplications() {
        return jobApplicationService.getAll();
    }
}
