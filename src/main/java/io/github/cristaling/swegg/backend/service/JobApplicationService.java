package io.github.cristaling.swegg.backend.service;


import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.repositories.JobApplicationRepository;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.web.requests.JobApplicationAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class JobApplicationService {

    private JobApplicationRepository jobApplicationRepository;
    private UserRepository userRepository;
    private JobRepository jobRepository;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
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

    public List<JobApplication> getApplicationsForApplicant(String email){
        Member applicator = userRepository.getMemberByEmail(email);
        if(applicator==null){
            return null;
        }
        return jobApplicationRepository.getJobApplicationsByApplicant(applicator);
    }

    public List<JobApplication> getApplicationsForJob(UUID uuid){
        try {
            Job job = jobRepository.getOne(uuid);
            return jobApplicationRepository.getJobApplicationsByJob(job);
        } catch (EntityNotFoundException ex) {
            return null;
        }
    }
}
