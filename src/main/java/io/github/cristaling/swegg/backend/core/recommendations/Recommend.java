package io.github.cristaling.swegg.backend.core.recommendations;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "recommendations")
public class Recommend {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    private String recommenderEmail;
    private String recommendedEmail;
    private String receiver;

    public Recommend() {
    }

    public Recommend(String recommenderEmail, String recommendedEmail, String receiver) {
        this.recommenderEmail = recommenderEmail;
        this.recommendedEmail = recommendedEmail;
        this.receiver = receiver;
    }

    public String getRecommenderEmail() {
        return recommenderEmail;
    }

    public void setRecommenderEmail(String recommenderEmail) {
        this.recommenderEmail = recommenderEmail;
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
