package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.member.Member;
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

    public void sendJobInviteNotificationToMember(Job job) {
        Member member = job.getEmployee();
        sendEmailToEmployee(member.getEmail(),
                "You got the job!",
                "You applied to a job and you were chosen by its owner. Follow this link to remember the jobs details : " +
                        environment.getProperty("domain.name") +"jobs/?job="+ job.getUuid());
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
}
