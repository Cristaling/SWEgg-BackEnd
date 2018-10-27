package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.service.JobService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.UserRole;
import io.github.cristaling.swegg.backend.web.requests.JobAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity addJob(@RequestHeader("token") String token,@RequestBody JobAddRequest jobAddRequest) {

        if (!securityService.canAccessRole(token, UserRole.CLIENT)) {
            return new ResponseEntity("User doesnt have permission",HttpStatus.UNAUTHORIZED);
        }
        Job job = jobService.addJob(jobAddRequest);

        if (job == null) {
            return new ResponseEntity("Job from this Owner with this status already exists", HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Job> getAllApplications() {
        return jobService.getAll();
    }

}
