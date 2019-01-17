package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.recommendations.Recommend;
import io.github.cristaling.swegg.backend.repositories.RecommendRepository;
import io.github.cristaling.swegg.backend.utils.ServiceActionResult;
import io.github.cristaling.swegg.backend.utils.enums.ErrorMessages;
import io.github.cristaling.swegg.backend.web.requests.RecommendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommendService {

    private RecommendRepository recommendRepository;

    @Autowired
    public RecommendService(RecommendRepository recommendRepository) {
        this.recommendRepository = recommendRepository;
    }

    public ServiceActionResult<Recommend> addRecommend(String recieverEmail, String recommendedEmail, String memberEmail) {

        ServiceActionResult<Recommend> serviceActionResult = new ServiceActionResult<>();

        if (recommendRepository.getRecommendByRecommendedEmailAndRecommenderEmailAndReceiver(
                recommendedEmail,
                memberEmail,
                recieverEmail) == null) {
            serviceActionResult.setError(ErrorMessages.RECOMMENDATION_ALREADY_EXISTS);
            return serviceActionResult;
        }

        if (recieverEmail == null ||
                recommendedEmail == null ||
                memberEmail == null) {
            serviceActionResult.setError(ErrorMessages.PLEASE_SPECIFY_EMAIL);
            return serviceActionResult;
        }

        Recommend recommend = new Recommend();
        recommend.setReceiver(recieverEmail);
        recommend.setRecommendedEmail(recommendedEmail);
        recommend.setRecommenderEmail(memberEmail);

        recommendRepository.saveAndFlush(recommend);

        serviceActionResult.setResult(recommend);

        return serviceActionResult;

    }

}
