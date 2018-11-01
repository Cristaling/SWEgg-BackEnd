package io.github.cristaling.swegg.backend.web.requests;

import io.github.cristaling.swegg.backend.core.member.Member;

import java.util.Date;

public class AddReviewRequest {

    private Member reviewed;
    private Member reviewer;
    private String text;
    private int stars;
    private Date dateGiven;

    public AddReviewRequest(Member reviewed, Member reviewer, String text, int stars, Date dateGiven) {
        this.reviewed = reviewed;
        this.reviewer = reviewer;
        this.text = text;
        this.stars = stars;
        this.dateGiven = dateGiven;
    }

    public Member getReviewed() { return reviewed; }

    public void setReviewed(Member reviewed) { this.reviewed = reviewed; }

    public Member getReviewer() { return reviewer; }

    public void setReviewer(Member reviewer) { this.reviewer = reviewer; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public int getStars() { return stars; }

    public void setStars(int stars) { this.stars = stars; }

    public Date getDateGiven() { return dateGiven; }

    public void setDateGiven(Date dateGiven) { this.dateGiven = dateGiven; }
}
