package io.github.cristaling.swegg.backend.web.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.cristaling.swegg.backend.core.abilities.Ability;
import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import io.github.cristaling.swegg.backend.utils.enums.JobType;

import java.util.List;
import java.util.UUID;

public class JobWithAbilities {

    private UUID uuid;

    @JsonIgnore
    private Member owner;
    @JsonIgnore
    private Member employee;

    private JobType jobType;

    private JobStatus jobStatus;

    private String title;

    private String description;

    private List<Ability> abilities;

    public JobWithAbilities() {
    }

    public JobWithAbilities(JobType jobType, JobStatus jobStatus, String title, String description) {
        this.jobType = jobType;
        this.jobStatus = jobStatus;
        this.title = title;
        this.description = description;
    }

    public JobWithAbilities(Job job) {
        this.uuid=job.getUuid();
        this.jobType = job.getJobType();
        this.jobStatus = job.getJobStatus();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.owner=job.getOwner();
        this.employee=job.getEmployee();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public Member getEmployee() {
        return employee;
    }

    public void setEmployee(Member employee) {
        this.employee = employee;
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

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }
}
