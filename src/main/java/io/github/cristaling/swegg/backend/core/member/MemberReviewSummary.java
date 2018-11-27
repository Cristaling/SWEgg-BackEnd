package io.github.cristaling.swegg.backend.core.member;

import java.util.Date;
import java.util.UUID;

public class MemberReviewSummary {
    private UUID uuid;
    private String reviewerEmail;
    private String reviewerFirstName;
    private String reviewerLastName;

    private String text;
    private Date dateGiven;

    public MemberReviewSummary() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    public String getReviewerFirstName() {
        return reviewerFirstName;
    }

    public void setReviewerFirstName(String reviewerFirstName) {
        this.reviewerFirstName = reviewerFirstName;
    }

    public String getReviewerLastName() {
        return reviewerLastName;
    }

    public void setReviewerLastName(String reviewerLastName) {
        this.reviewerLastName = reviewerLastName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateGiven() {
        return dateGiven;
    }

    public void setDateGiven(Date dateGiven) {
        this.dateGiven = dateGiven;
    }
}
