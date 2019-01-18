package io.github.cristaling.swegg.backend.schedulers;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.EmailSenderService;
import io.github.cristaling.swegg.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class NewsletterTask {

    private EmailSenderService emailSenderService;
    private UserService userService;

    @Autowired
    public NewsletterTask(EmailSenderService emailSenderService, UserService userService) {
        this.emailSenderService = emailSenderService;
        this.userService = userService;
    }

    List<Job> jobs=new ArrayList<>();
    @Scheduled(cron = "0 8 * * *")
//    @Scheduled(fixedRate = 60000)
    public void sendNews(){
        System.out.println("Merge");
        List<Member> subscribers = this.userService.getMembersForSubscriptions();

        subscribers.forEach(member -> {
            emailSenderService.sendnewsletter(member);
        });


        jobs.clear();
    }

    public void addJob(Job job) {
        jobs.add(job);
    }
}
