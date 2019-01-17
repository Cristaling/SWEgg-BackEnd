package io.github.cristaling.swegg.backend.web.requests;

public class RecommendRequest {

    private String recommendedEmail;
    private String receiver;

    public RecommendRequest() {
    }

    public RecommendRequest(String recommenderEmail, String receiver) {
        this.recommendedEmail = recommendedEmail;
        this.receiver = receiver;
    }

    public String getRecommendedEmail() {
        return recommendedEmail;
    }

    public void setRecommendedEmail(String recommendedEmail) {
        this.recommendedEmail = recommendedEmail;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
