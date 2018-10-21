package io.github.cristaling.swegg.backend.service;


import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.repositories.JobApplicationRepository;
import io.github.cristaling.swegg.backend.web.requests.JobApplicationAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }


    /**
     * @param jobApplicationAddRequest JobApplication to be addded
     * @return jobApplication added, null if exists already an application
     */
    public JobApplication addJobApplication(JobApplicationAddRequest jobApplicationAddRequest) {

        if (jobApplicationRepository.getJobApplicationByApplicantAndJob(jobApplicationAddRequest.getApplicant(), jobApplicationAddRequest.getJob()) != null) {
            return null;
        }
        JobApplication jobApplication = new JobApplication();

        jobApplication.setApplicant(jobApplicationAddRequest.getApplicant());
        jobApplication.setJob(jobApplicationAddRequest.getJob());

        jobApplicationRepository.save(jobApplication);

        return jobApplication;
    }

    public List<JobApplication> getAll() {
        return jobApplicationRepository.findAll();
    }
}
