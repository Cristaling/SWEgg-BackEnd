package io.github.cristaling.swegg.backend.web.controllers;


import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.service.JobApplicationService;
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

    @Autowired
    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/add")
    public ResponseEntity addJobApplication(@RequestBody JobApplicationAddRequest jobApplicationAddRequest) {
        JobApplication jobApplication = this.jobApplicationService.addJobApplication(jobApplicationAddRequest);
        if (jobApplication == null) {
            return new ResponseEntity("JobApplication for Job by Member already exists", HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping("/get-all")
    public List<JobApplication> getAllApplications() {
        return jobApplicationService.getAll();
    }
}
