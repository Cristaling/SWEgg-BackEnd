package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.job.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

}
