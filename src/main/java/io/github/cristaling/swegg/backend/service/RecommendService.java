package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.notifications.Notification;
import io.github.cristaling.swegg.backend.core.recommendations.Recommend;
import io.github.cristaling.swegg.backend.repositories.RecommendRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.ServiceActionResult;
import io.github.cristaling.swegg.backend.utils.enums.ErrorMessages;
import io.github.cristaling.swegg.backend.web.responses.RecommendForProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RecommendService {

    private RecommendRepository recommendRepository;
    private EmailSenderService emailSenderService;
    private UserService userService;
    private NotificationService notificationService;
    private UserRepository userRepository;

    @Autowired
    public RecommendService(RecommendRepository recommendRepository,
                            EmailSenderService emailSenderService,
                            UserService userService,
                            NotificationService notificationService,
                            UserRepository userRepository) {
        this.recommendRepository = recommendRepository;
        this.emailSenderService = emailSenderService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    public ServiceActionResult<List<Recommend>> addRecommend(List<String> recieversEmails, String recommendedEmail, String memberEmail) {

        ServiceActionResult<List<Recommend>> serviceActionResult = new ServiceActionResult<>();
        for (String recieverEmail : recieversEmails) {
            if (recommendRepository.getRecommendByRecommendedEmailAndRecommenderEmailAndReceiver(
                    recommendedEmail,
                    memberEmail,
                    recieverEmail) != null) {
                serviceActionResult.setError(ErrorMessages.RECOMMENDATION_ALREADY_EXISTS);
                return serviceActionResult;
            }
        }

        if (recieversEmails.size() == 0 ||
                recommendedEmail == null ||
                memberEmail == null) {
            serviceActionResult.setError(ErrorMessages.PLEASE_SPECIFY_EMAIL);
            return serviceActionResult;
        }

        List<Recommend> recommends = new ArrayList<>();

        for (String recieverEmail : recieversEmails) {
            Recommend recommend = new Recommend();
            recommend.setReceiver(recieverEmail);
            recommend.setRecommendedEmail(recommendedEmail);
            recommend.setRecommenderEmail(memberEmail);

            recommendRepository.saveAndFlush(recommend);

            RecommendForProfile recommendForProfile = new RecommendForProfile();
            recommendForProfile.setRecommenderEmail(memberEmail);
            recommendForProfile.setRecommendedSummary(userService.getProfileInternal(recieverEmail));
            recommendForProfile.setRecommenderFirstName(userService.getProfileInternal(memberEmail).getFirstName());
            recommendForProfile.setRecommenderLastName(userService.getProfileInternal(memberEmail).getLastName());

            notificationService.sendDataSecured(userRepository.getMemberByEmail(memberEmail), "recommend/add", recommendForProfile);

            Notification notification = new Notification();
            notification.setDate(new Date());
            notification.setMember(userRepository.getMemberByEmail(memberEmail));
            notification.setRead(false);
            notification.setText("You just got recommended a new member : " + userRepository.getMemberByEmail(recommendedEmail).getMemberData().getLastName());
            this.notificationService.addNotification(notification);

            emailSenderService.sendNewRecommendationToMember(recommend);

            recommends.add(recommend);
        }
        serviceActionResult.setResult(recommends);

        return serviceActionResult;

    }

    public ServiceActionResult<List<RecommendForProfile>> getRecommendations(Member member) {
        ServiceActionResult<List<RecommendForProfile>> recommendationList = new ServiceActionResult<>();
        List<RecommendForProfile> recommendForProfileList = new ArrayList<>();
        List<Recommend> recommends = recommendRepository.getRecommendsByRecommendedEmail(member.getEmail());
        for (Recommend recommend : recommends) {
            RecommendForProfile recommendForProfile = new RecommendForProfile();
            recommendForProfile.setRecommenderEmail(recommend.getRecommenderEmail());
            recommendForProfile.setRecommendedSummary(userService.getProfileInternal(recommend.getReceiver()));
            recommendForProfile.setRecommenderFirstName(userService.getProfileInternal(recommend.getRecommenderEmail()).getFirstName());
            recommendForProfile.setRecommenderLastName(userService.getProfileInternal(recommend.getRecommenderEmail()).getLastName());
            recommendForProfileList.add(recommendForProfile);
        }
        recommendationList.setResult(recommendForProfileList);
        return recommendationList;
    }

}
