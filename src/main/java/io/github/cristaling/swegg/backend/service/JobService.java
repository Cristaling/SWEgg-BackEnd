package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.core.job.JobSummary;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.repositories.JobApplicationRepository;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.web.requests.JobAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {

    private JobRepository jobRepository;
    private UserRepository userRepository;
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobService(JobRepository jobRepository, UserRepository userRepository, JobApplicationRepository jobApplicationRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public Job addJob(JobAddRequest jobAddRequest, Member member) {

        if (jobRepository.getJobByOwnerAndJobStatus(member, jobAddRequest.getJobStatus()) != null) {
            return null;
        }
        Job job = new Job();
        job.setJobStatus(jobAddRequest.getJobStatus());
        job.setOwner(member);
        job.setJobType(jobAddRequest.getJobType());
        jobRepository.save(job);
        jobRepository.flush();

        return job;
    }

    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    public List<JobSummary> getJobSummaries() {
        List<Job> jobs = this.jobRepository.findAll();
        return jobs.stream().map(this::getSummary).collect(Collectors.toList());
    }

    public List<JobSummary> getUserJobs(String email, Member userByToken){
        if(!userByToken.getEmail().equals(email)){
            return null;
        }
        Member member= userRepository.getMemberByEmail(email);
        List<Job> jobList=jobRepository.getAllJobsForUser(member.getUuid());
        List<JobSummary> jobSummaryList= new ArrayList<>();
        for (Job job: jobList
             ) {
            jobSummaryList.add(new JobSummary(job));
        }
        return jobSummaryList;
    }


    private JobSummary getSummary(Job job) {
        JobSummary jobSummary = new JobSummary();
        jobSummary.setUuid(job.getUuid());
        jobSummary.setTitle(job.getTitle());
        jobSummary.setDescription(job.getDescription());

        Member owner = job.getOwner();

        jobSummary.setOwnerEmail(owner.getEmail());
        jobSummary.setOwnerName(owner.getMemberData().getFirstName() + " " + owner.getMemberData().getLastName());

        return jobSummary;
    }

    public Job getJob(UUID uuid) {
        return this.jobRepository.getOne(uuid);
    }
}
