package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.user.User;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    Job getJobByOwnerAndJobStatus(User Owner, JobStatus jobStatus);
}
