package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.core.job.JobInvite;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberReview;
import io.github.cristaling.swegg.backend.core.recommendations.Recommend;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private UserRepository userRepository;
    private JavaMailSender emailSender;
    private Environment environment;

    @Autowired
    public EmailSenderService(UserRepository userRepository, JavaMailSender emailSender, Environment environment) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.environment = environment;
    }

    public void sendJobSelectionNotificationToMember(Job job) {
        Member member = job.getEmployee();
        sendEmailToEmployee(member.getEmail(),
                "You got the job!",
                "You applied to a job and you were chosen by its owner. Follow this link to remember the jobs details : " +
                        environment.getProperty("domain.name") +"jobs/?job="+ job.getUuid());
    }

    public void sendNewRecommendationToMember(Recommend recommend){
        sendEmailToEmployee(recommend.getReceiver(),
                "You have a new notification!",
                "Someone has recommended a new user to you. Follow this link to see that users profile page. : " +
                        environment.getProperty("domain.name")+"/user-profile/" + recommend.getRecommendedEmail());
    }

    private void sendEmailToEmployee(String email, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        new Thread(() -> {
            emailSender.send(message);
        }).start();
    }

    public void sendConfirmationMail(Member member) {
        sendEmailToEmployee(member.getEmail(),
                "Thank you for joining LaNegru!",
                "Please follow the link down below to confirm your account on the website : " +
                        environment.getProperty("user.confirmation.link") + SecurityUtils.getTokenByUUID(member.getUuid().toString()));
    }

    public void sendReviewNotificationToMember(MemberReview review) {
        Member reviewer = review.getReviewer();
        sendEmailToEmployee(review.getReviewed().getEmail(),
                "You have received a new review!",
                "User : " + reviewer.getMemberData().getFirstName() + " " + reviewer.getMemberData().getLastName() +
                        " with email : " + reviewer.getEmail() +
                        " has just given you a review!");
    }

    public void sendJobApplicationNotificationToMember(JobApplication jobApplication) {
        Member applicant = jobApplication.getApplicant();
        Job job = jobApplication.getJob();
        sendEmailToEmployee(job.getOwner().getEmail(),
                "One of your jobs has received a new application!",
                "User : " + applicant.getMemberData().getFirstName() + " " + applicant.getMemberData().getLastName() +
                        " with email : " + applicant.getEmail() +
                        " has just applied for one of your jobs!" +
                        " Job title: " + job.getTitle());
    }

    public void sendJobInviteNotificationToMember(JobInvite jobInvite) {
        Member member = jobInvite.getMember();
        Job job = jobInvite.getJob();
        sendEmailToEmployee(jobInvite.getMember().getEmail(),
                "You have received a job application invite!",
                "User : " + member.getMemberData().getFirstName() + " " + member.getMemberData().getLastName() +
                        " with email : " + member.getEmail() +
                        " has just send you a job invite for one of his jobs!" +
                        " Job title: " + job.getTitle());
    }
}
