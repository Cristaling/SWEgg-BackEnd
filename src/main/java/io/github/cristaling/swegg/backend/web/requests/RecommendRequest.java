package io.github.cristaling.swegg.backend.web.requests;

import java.util.List;

public class RecommendRequest {

    private String recommendedEmail;
    private List<String> receiver;

    public RecommendRequest() {
    }

    public RecommendRequest(String recommenderEmail, List<String> receiver) {
        this.recommendedEmail = recommendedEmail;
        this.receiver = receiver;
    }

    public String getRecommendedEmail() {
        return recommendedEmail;
    }

    public void setRecommendedEmail(String recommendedEmail) {
        this.recommendedEmail = recommendedEmail;
    }

    public List<String> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<String> receiver) {
        this.receiver = receiver;
    }
}
