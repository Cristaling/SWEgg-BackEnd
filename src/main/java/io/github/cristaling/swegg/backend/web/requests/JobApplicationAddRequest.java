package io.github.cristaling.swegg.backend.web.requests;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.user.User;

import java.util.UUID;

public class JobApplicationAddRequest {

    private User applicant;

    private Job job;

    public JobApplicationAddRequest() {
    }

    public JobApplicationAddRequest(User applicant, Job job) {
        this.applicant = applicant;
        this.job = job;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
