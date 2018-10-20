package io.github.cristaling.swegg.backend.web.controllers;


import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.repositories.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/jobapplication")
public class JobApplicationController {

    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobApplicationController(JobApplicationRepository jobApplicationRepository){
        this.jobApplicationRepository=jobApplicationRepository;
    }

    @PostMapping("/addapplication")
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplication addJobApplication(@RequestBody JobApplication jobApplication){
        jobApplicationRepository.save(jobApplication);
        return jobApplication;
    }

    @GetMapping("/getallapplications")
    public List<JobApplication> getAllApplications(){
        return  jobApplicationRepository.findAll();
    }
}
