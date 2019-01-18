package io.github.cristaling.swegg.backend.web.requests;

import io.github.cristaling.swegg.backend.utils.enums.JobStatus;

import java.util.UUID;

public class JobUpdateStatusRequest {
    private UUID jobId;
    private JobStatus jobStatus;

    public JobUpdateStatusRequest() {
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}
