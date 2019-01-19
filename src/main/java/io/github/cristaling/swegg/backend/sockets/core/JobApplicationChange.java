package io.github.cristaling.swegg.backend.sockets.core;

import io.github.cristaling.swegg.backend.core.job.JobSummary;

public class JobApplicationChange {

    String applicantEmail;
    JobSummary jobSummary;

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public JobSummary getJobSummary() {
        return jobSummary;
    }

    public void setJobSummary(JobSummary jobSummary) {
        this.jobSummary = jobSummary;
    }
}
