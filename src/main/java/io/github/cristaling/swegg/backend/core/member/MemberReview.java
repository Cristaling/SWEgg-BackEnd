package io.github.cristaling.swegg.backend.core.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "member_reviews")
public class MemberReview {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JsonIgnore
    private Member reviewer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JsonIgnore
    private Member reviewed;

    @NotNull
    private String text;
    @NotNull
    private int stars;
    @NotNull
    private Date dateGiven;

    public UUID getUuid() { return uuid; }

    public void setUuid(UUID uuid) { this.uuid = uuid; }

    public Member getReviewer() { return reviewer; }

    public void setReviewer(Member reviewer) { this.reviewer = reviewer; }

    public Member getReviewed() { return reviewed; }

    public void setReviewed(Member reviewed) { this.reviewed = reviewed; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public int getStars() { return stars; }

    public void setStars(int stars) { this.stars = stars; }

    public Date getDateGiven() { return dateGiven; }

    public void setDateGiven(Date dateGiven) { this.dateGiven = dateGiven; }
}
