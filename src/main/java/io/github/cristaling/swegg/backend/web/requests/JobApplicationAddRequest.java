package io.github.cristaling.swegg.backend.web.requests;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.member.Member;

public class JobApplicationAddRequest {

    private Member applicant;

    private Job job;

    public JobApplicationAddRequest() {
    }

    public JobApplicationAddRequest(Member applicant, Job job) {
        this.applicant = applicant;
        this.job = job;
    }

    public Member getApplicant() {
        return applicant;
    }

    public void setApplicant(Member applicant) {
        this.applicant = applicant;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
