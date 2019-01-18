package io.github.cristaling.swegg.backend.web.responses;

import io.github.cristaling.swegg.backend.core.member.MemberData;

public class RecommendForProfile {

    ProfileResponse recommendedSummary;
    String recommenderLastName;
    String recommenderFirstName;
    String recommenderEmail;

    public RecommendForProfile() {
    }

    public RecommendForProfile(ProfileResponse recommendedSummary, String recommenderLastName, String recommenderFirstName, String recommenderEmail) {
        this.recommendedSummary = recommendedSummary;
        this.recommenderLastName = recommenderLastName;
        this.recommenderFirstName = recommenderFirstName;
        this.recommenderEmail = recommenderEmail;
    }

    public ProfileResponse getRecommendedSummary() {
        return recommendedSummary;
    }

    public void setRecommendedSummary(ProfileResponse recommendedSummary) {
        this.recommendedSummary = recommendedSummary;
    }

    public String getRecommenderLastName() {
        return recommenderLastName;
    }

    public void setRecommenderLastName(String recommenderLastName) {
        this.recommenderLastName = recommenderLastName;
    }

    public String getRecommenderFirstName() {
        return recommenderFirstName;
    }

    public void setRecommenderFirstName(String recommenderFirstName) {
        this.recommenderFirstName = recommenderFirstName;
    }

    public String getRecommenderEmail() {
        return recommenderEmail;
    }

    public void setRecommenderEmail(String recommenderEmail) {
        this.recommenderEmail = recommenderEmail;
    }
}
