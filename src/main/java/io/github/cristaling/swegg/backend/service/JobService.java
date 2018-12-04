package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.abilities.Ability;
import io.github.cristaling.swegg.backend.core.abilities.AbilityUse;
import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobSummary;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.repositories.AbilityUseRepository;
import io.github.cristaling.swegg.backend.repositories.JobApplicationRepository;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.web.requests.JobAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobService {

    private JobRepository jobRepository;
    private UserRepository userRepository;
    private JobApplicationRepository jobApplicationRepository;
    private AbilityUseRepository abilityUseRepository;

    private AbilityService abilityService;

    @Autowired
    public JobService(JobRepository jobRepository, UserRepository userRepository, JobApplicationRepository jobApplicationRepository, AbilityUseRepository abilityUseRepository, AbilityService abilityService) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.abilityUseRepository = abilityUseRepository;
        this.abilityService = abilityService;
    }

    public Job addJob(JobAddRequest jobAddRequest, Member member) {

        if (jobRepository.getJobsByOwnerAndJobStatus(member, jobAddRequest.getJobStatus()).size() >7) {
            return null;
        }
        if(jobAddRequest.getTitle().length() < 5 || jobAddRequest.getDescription().length() < 5){
            return null;
        }

        Job job = new Job(jobAddRequest);
        job.setOwner(member);


        jobRepository.save(job);
        jobRepository.flush();

        for (String abilityName : jobAddRequest.getAbilities()) {
            AbilityUse abilityUse = new AbilityUse();

            Ability ability = this.abilityService.getAbilityByName(abilityName);

            abilityUse.setAbility(ability);
            abilityUse.setJob(job);

            this.abilityUseRepository.save(abilityUse);
        }

        return job;
    }

    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    public List<JobSummary> getJobSummaries(int page, int count) {
        List<Job> jobs;
        if (page < 0) {
            jobs = this.jobRepository.findAll();
        } else {
            jobs = this.jobRepository.findAll(PageRequest.of(page, count)).getContent();
        }
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
