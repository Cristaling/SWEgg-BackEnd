package io.github.cristaling.swegg.backend.web.requests;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import io.github.cristaling.swegg.backend.utils.enums.JobType;

import java.util.List;

public class JobAddRequest {

    private JobType jobType;

    private JobStatus jobStatus;

    private String title;

    private String description;

    private List<String> abilities;

    public JobAddRequest() {
    }

    public JobAddRequest(JobType jobType, JobStatus jobStatus, String title, String description) {
        this.jobType = jobType;
        this.jobStatus = jobStatus;
        this.title = title;
        this.description = description;
    }

    private JobAddRequest(Job job) {
        this.jobType = job.getJobType();
        this.jobStatus = job.getJobStatus();
        this.title = job.getTitle();
        this.description = job.getDescription();
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

    public List<String> getAbilities() {
        return abilities;
    }
}
