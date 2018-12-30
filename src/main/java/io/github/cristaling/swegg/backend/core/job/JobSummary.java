package io.github.cristaling.swegg.backend.core.job;

import io.github.cristaling.swegg.backend.core.abilities.Ability;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.web.responses.JobWithAbilities;

import java.util.List;
import java.util.UUID;

public class JobSummary {

    private UUID uuid;
    private String ownerEmail;
    private String ownerName;
    private String title;
    private String description;
    private String employeeName;
    private String employeeEmail;
    private List<Ability> abilities;

    public JobSummary() {
    }

    public JobSummary(Job job) {
        this.uuid = job.getUuid();
        this.description = job.getDescription();
        this.title = job.getTitle();
        this.ownerEmail = job.getOwner().getEmail();
        this.ownerName = job.getOwner().getMemberData().getFirstName() + " " + job.getOwner().getMemberData().getLastName();
        Member employee = job.getEmployee();
        if (employee != null) {
            this.employeeName = employee.getMemberData().getFirstName() + " " + employee.getMemberData().getLastName();
            this.employeeEmail = employee.getEmail();
        }
    }

    public JobSummary(JobWithAbilities jobWithAbilities) {
        this.uuid = jobWithAbilities.getUuid();
        this.description = jobWithAbilities.getDescription();
        this.title = jobWithAbilities.getTitle();
        this.ownerEmail = jobWithAbilities.getOwner().getEmail();
        this.ownerName = jobWithAbilities.getOwner().getMemberData().getFirstName() + " " + jobWithAbilities.getOwner().getMemberData().getLastName();
        this.abilities = jobWithAbilities.getAbilities();
        Member employee = jobWithAbilities.getEmployee();
        if (employee != null) {
            this.employeeName = employee.getMemberData().getFirstName() + " " + employee.getMemberData().getLastName();
            this.employeeEmail = employee.getEmail();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }
}
