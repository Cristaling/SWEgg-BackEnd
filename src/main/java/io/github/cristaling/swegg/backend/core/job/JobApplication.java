package io.github.cristaling.swegg.backend.core.job;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.web.requests.JobApplicationAddRequest;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "job_applications")
public class JobApplication {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID uuid;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_uuid", nullable = false)
	private Member applicant;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "job_uuid", nullable = false)
	private Job job;

	public JobApplication() {
	}

	public JobApplication(Member applicant, Job job) {
		this.applicant = applicant;
		this.job = job;
	}

	public JobApplication(JobApplicationAddRequest jobApplicationAddRequest){
		this.job=jobApplicationAddRequest.getJob();
		this.applicant=jobApplicationAddRequest.getApplicant();
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Member getApplicant() {
		return applicant;
	}

	public void setApplicant(Member applicant) {
		this.applicant = applicant;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
}
