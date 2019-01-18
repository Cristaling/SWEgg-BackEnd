package io.github.cristaling.swegg.backend.sockets.core;

import io.github.cristaling.swegg.backend.core.job.JobSummary;

public class JobApplicationChange {

    JobSummary jobSummary;

    public JobSummary getJobSummary() {
        return jobSummary;
    }

    public void setJobSummary(JobSummary jobSummary) {
        this.jobSummary = jobSummary;
    }
}
