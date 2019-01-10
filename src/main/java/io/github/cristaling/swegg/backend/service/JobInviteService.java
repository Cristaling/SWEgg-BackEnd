package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobInvite;
import io.github.cristaling.swegg.backend.core.job.JobSummary;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.repositories.JobInviteRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobInviteService {

    private JobInviteRepository jobInviteRepository;
    private UserRepository userRepository;

    @Autowired
    public JobInviteService(JobInviteRepository jobInviteRepository, UserRepository userRepository) {
        this.jobInviteRepository = jobInviteRepository;
        this.userRepository = userRepository;
    }

    public boolean addJobInvite(Job job, String email){
        Member member = this.userRepository.getMemberByEmail(email);
        JobInvite jobInvite = this.jobInviteRepository.getByJob_UuidAndMemberEmail(job.getUuid(), email);
        if(jobInvite!=null){
            return false;
        }
        jobInvite = new JobInvite();
        jobInvite.setJob(job);
        jobInvite.setMember(member);
        this.jobInviteRepository.save(jobInvite);
        this.jobInviteRepository.flush();
        return true;
    }

    public List<JobSummary> getJobInvitesByUser(Member member, int page, int count) {
        List<JobInvite> jobInvites;
        if(page<0){
            jobInvites = this.jobInviteRepository.findAllByMember(member);
        }else{
            jobInvites = this.jobInviteRepository.getJobInviteByMember(PageRequest.of(page, count), member).getContent();
        }
        return jobInvites.stream().map(this::getJobSummaryFromJobInvite).collect(Collectors.toList());
    }

    private JobSummary getJobSummaryFromJobInvite(JobInvite jobInvite) {
        Job job = jobInvite.getJob();
        return new JobSummary(job);
    }
}
