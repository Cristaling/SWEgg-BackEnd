package io.github.cristaling.swegg.backend.web.requests;

import io.github.cristaling.swegg.backend.core.member.Member;

import java.util.Date;

public class AddReviewRequest {

    private String reviewedEmail;
    private String text;
    private int stars;

    public AddReviewRequest(){}

    public AddReviewRequest(String reviewedEmail, String text, int stars) {
        this.reviewedEmail = reviewedEmail;
        this.text = text;
        this.stars = stars;
    }

    public String getReviewedEmail() { return reviewedEmail; }

    public void setReviewedEmail(String reviewedEmail) { this.reviewedEmail = reviewedEmail; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public int getStars() { return stars; }

    public void setStars(int stars) { this.stars = stars; }

}
