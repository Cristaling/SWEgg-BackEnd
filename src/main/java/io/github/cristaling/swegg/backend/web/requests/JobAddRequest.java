package io.github.cristaling.swegg.backend.web.requests;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import io.github.cristaling.swegg.backend.utils.enums.JobType;

public class JobAddRequest {

    private Member owner;

    private JobType jobType;

    private JobStatus jobStatus;

    private String title;

    private String description;

    public JobAddRequest(Member owner, JobType jobType, JobStatus jobStatus, String title, String description) {
        this.owner = owner;
        this.jobType = jobType;
        this.jobStatus = jobStatus;
        this.title = title;
        this.description = description;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
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
