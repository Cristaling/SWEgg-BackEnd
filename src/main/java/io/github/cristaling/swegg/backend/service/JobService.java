package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.web.requests.JobAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job addJob(JobAddRequest jobAddRequest) {

        if (jobRepository.getJobByOwnerAndJobStatus(jobAddRequest.getOwner(), jobAddRequest.getJobStatus()) != null) {
            return null;
        }
        Job job = new Job();
        job.setJobStatus(jobAddRequest.getJobStatus());
        job.setOwner(jobAddRequest.getOwner());
        job.setJobType(jobAddRequest.getJobType());
        jobRepository.save(job);
        jobRepository.flush();

        return job;
    }

    public List<Job> getAll() {
        return jobRepository.findAll();
    }
}
