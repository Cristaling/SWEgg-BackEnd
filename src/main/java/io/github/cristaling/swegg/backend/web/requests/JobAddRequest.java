package io.github.cristaling.swegg.backend.web.requests;

import io.github.cristaling.swegg.backend.core.user.User;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import io.github.cristaling.swegg.backend.utils.enums.JobType;

import java.util.UUID;

public class JobAddRequest {

    private UUID uuid;

    private User owner;

    private JobType jobType;

    private JobStatus jobStatus;

    public JobAddRequest(UUID uuid, User owner, JobType jobType, JobStatus jobStatus) {
        this.uuid = uuid;
        this.owner = owner;
        this.jobType = jobType;
        this.jobStatus = jobStatus;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
}
