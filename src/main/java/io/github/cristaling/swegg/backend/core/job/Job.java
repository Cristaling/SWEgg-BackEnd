package io.github.cristaling.swegg.backend.core.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import io.github.cristaling.swegg.backend.utils.enums.JobType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "jobs")
public class Job {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID uuid;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "user_uuid", nullable = false)
	@JsonIgnore
	private Member owner;

	@Enumerated(EnumType.STRING)
	private JobType jobType;

	@Enumerated(EnumType.STRING)
	private JobStatus jobStatus;

	private String title;

	private String description;

	public UUID getUuid() {
		return uuid;
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
