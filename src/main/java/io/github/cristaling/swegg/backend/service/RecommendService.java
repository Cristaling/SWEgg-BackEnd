package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.recommendations.Recommend;
import io.github.cristaling.swegg.backend.repositories.RecommendRepository;
import io.github.cristaling.swegg.backend.utils.ServiceActionResult;
import io.github.cristaling.swegg.backend.utils.enums.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendService {

    private RecommendRepository recommendRepository;
    private EmailSenderService emailSenderService;

    @Autowired
    public RecommendService(RecommendRepository recommendRepository, EmailSenderService emailSenderService) {
        this.recommendRepository = recommendRepository;
        this.emailSenderService = emailSenderService;
    }

    public ServiceActionResult<List<Recommend>> addRecommend(List<String> recieversEmails, String recommendedEmail, String memberEmail) {

        ServiceActionResult<List<Recommend>> serviceActionResult = new ServiceActionResult<>();
        for(String recieverEmail : recieversEmails) {
            if (recommendRepository.getRecommendByRecommendedEmailAndRecommenderEmailAndReceiver(
                    recommendedEmail,
                    memberEmail,
                    recieverEmail) != null) {
                serviceActionResult.setError(ErrorMessages.RECOMMENDATION_ALREADY_EXISTS);
                return serviceActionResult;
            }
        }

        if (recieversEmails.size()==0 ||
                recommendedEmail == null ||
                memberEmail == null) {
            serviceActionResult.setError(ErrorMessages.PLEASE_SPECIFY_EMAIL);
            return serviceActionResult;
        }

        List<Recommend> recommends=new ArrayList<>();

        for(String recieverEmail : recieversEmails) {
            Recommend recommend = new Recommend();
            recommend.setReceiver(recieverEmail);
            recommend.setRecommendedEmail(recommendedEmail);
            recommend.setRecommenderEmail(memberEmail);

            recommendRepository.saveAndFlush(recommend);

            emailSenderService.sendNewRecommendationToMember(recommend);

            recommends.add(recommend);
        }
        serviceActionResult.setResult(recommends);

        return serviceActionResult;

    }

}
