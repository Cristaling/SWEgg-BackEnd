package io.github.cristaling.swegg.backend.web.requests;

import io.github.cristaling.swegg.backend.core.user.User;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import io.github.cristaling.swegg.backend.utils.enums.JobType;

public class JobAddRequest {

    private User owner;

    private JobType jobType;

    private JobStatus jobStatus;

    private String title;

    private String description;

    public JobAddRequest(User owner, JobType jobType, JobStatus jobStatus, String title, String description) {
        this.owner = owner;
        this.jobType = jobType;
        this.jobStatus = jobStatus;
        this.title = title;
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
