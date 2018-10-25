package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.service.JobService;
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

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/add")
    public ResponseEntity addJob(@RequestBody JobAddRequest jobAddRequest) {
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
