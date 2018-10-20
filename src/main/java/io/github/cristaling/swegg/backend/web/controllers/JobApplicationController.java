package io.github.cristaling.swegg.backend.web.controllers;


import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.repositories.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/jobapplication")
public class JobApplicationController {

    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobApplicationController(JobApplicationRepository jobApplicationRepository){
        this.jobApplicationRepository=jobApplicationRepository;
    }

    @RequestMapping("/addapplication")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addJobApplication(@RequestBody JobApplication jobApplication){
        jobApplicationRepository.save(jobApplication);
    }

}