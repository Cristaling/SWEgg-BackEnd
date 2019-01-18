package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobInvite;
import io.github.cristaling.swegg.backend.core.job.JobSummary;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.notifications.Notification;
import io.github.cristaling.swegg.backend.repositories.JobInviteRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.sockets.core.JobInviteChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobInviteService {

    private JobInviteRepository jobInviteRepository;
    private UserRepository userRepository;
    private EmailSenderService emailSenderService;
    private NotificationService notificationService;

    @Autowired
    public JobInviteService(JobInviteRepository jobInviteRepository,
                            UserRepository userRepository,
                            EmailSenderService emailSenderService,
                            NotificationService notificationService) {
        this.jobInviteRepository = jobInviteRepository;
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.notificationService = notificationService;
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

        JobInviteChange jobInviteChange=new JobInviteChange();
        jobInviteChange.setJobSummary(new JobSummary(job));
        notificationService.sendDataSecured(job.getOwner(),"jobinvite/add", jobInviteChange);

        Notification notification= new Notification();
        notification.setDate(new Date());
        notification.setMember(member);
        notification.setRead(false);
        notification.setText("You just got invited to a job : " + job.getTitle());
        this.notificationService.addNotification(notification);

        emailSenderService.sendJobInviteNotificationToMember(jobInvite);

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
