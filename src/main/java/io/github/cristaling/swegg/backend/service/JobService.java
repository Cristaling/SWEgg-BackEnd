package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.abilities.Ability;
import io.github.cristaling.swegg.backend.core.abilities.AbilityUse;
import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobSummary;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.repositories.AbilityRepository;
import io.github.cristaling.swegg.backend.repositories.AbilityUseRepository;
import io.github.cristaling.swegg.backend.repositories.JobApplicationRepository;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.web.requests.JobAddRequest;
import io.github.cristaling.swegg.backend.web.responses.JobWithAbilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {

    private JobRepository jobRepository;
    private UserRepository userRepository;
    private JobApplicationRepository jobApplicationRepository;
    private AbilityUseRepository abilityUseRepository;
    private AbilityRepository abilityRepository;

    private AbilityService abilityService;

    @Autowired
    public JobService(JobRepository jobRepository, UserRepository userRepository, JobApplicationRepository jobApplicationRepository, AbilityUseRepository abilityUseRepository, AbilityRepository abilityRepository, AbilityService abilityService) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.abilityUseRepository = abilityUseRepository;
        this.abilityRepository = abilityRepository;
        this.abilityService = abilityService;
    }

    private JobWithAbilities addAbilitiesToJob(Job job){
        JobWithAbilities jobWithAbilities=new JobWithAbilities(job);

        List<AbilityUse> uses=abilityUseRepository.getAbilityUsesByJob(job);
        List<Ability> abilities=new ArrayList<>();
        for(AbilityUse use: uses){
            abilities.add(use.getAbility());
        }
        jobWithAbilities.setAbilities(abilities);
        return jobWithAbilities;
    }

    private List<JobWithAbilities> addAbilitiesToJobs(List<Job> jobs){
        List<JobWithAbilities> jobWithAbilitiesList=new ArrayList<>();
        for(Job job : jobs){
            jobWithAbilitiesList.add(addAbilitiesToJob(job));
        }
        return jobWithAbilitiesList;
    }

    public JobWithAbilities addJob(JobAddRequest jobAddRequest, Member member) {

//        if (jobRepository.getJobsByOwnerAndJobStatus(member, jobAddRequest.getJobStatus()).size() >7) {
//            return null;
//        }
        if(jobAddRequest.getTitle().length() < 5 || jobAddRequest.getDescription().length() < 5){
            return null;
        }

        Job job = new Job(jobAddRequest);
        job.setOwner(member);


        jobRepository.save(job);
        jobRepository.flush();
        if(jobAddRequest.getAbilities().size()!=0) {
            for (String abilityName : jobAddRequest.getAbilities()) {
                AbilityUse abilityUse = new AbilityUse();

                Ability ability = this.abilityService.getAbilityByName(abilityName);

                abilityUse.setAbility(ability);
                abilityUse.setJob(job);

                this.abilityUseRepository.save(abilityUse);
            }
        }
        return addAbilitiesToJob(job);
    }

    public List<JobWithAbilities> getAll() {
        return addAbilitiesToJobs(jobRepository.findAll());
    }

    public List<JobSummary> getJobSummaries(String title, int page, int count) {
        List<Job> jobs;
        if("".equals(title)){
            if (page < 0) {
                jobs = this.jobRepository.findAll();
            } else {
                jobs = this.jobRepository.findAll(PageRequest.of(page, count)).getContent();
            }
        } else{
            if (page < 0) {
                jobs = this.jobRepository.getJobsByTitleContainingIgnoreCase(title);
            } else {
                jobs = this.jobRepository.getJobsByTitleContainingIgnoreCase(PageRequest.of(page, count), title).getContent();
            }
        }
        List<JobWithAbilities> jobWithAbilitiesList= addAbilitiesToJobs(jobs);
        List<JobSummary> jobSummaries=new ArrayList<>();
        for(JobWithAbilities jobWithAbilities : jobWithAbilitiesList){
            jobSummaries.add(new JobSummary(jobWithAbilities));
        }
        return jobSummaries;
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
        JobWithAbilities jobWithAbilities=addAbilitiesToJob(job);
        return new JobSummary(jobWithAbilities);
    }

    public JobWithAbilities getJob(UUID uuid) {
        return addAbilitiesToJob(this.jobRepository.getOne(uuid));
    }
}
