package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    List<Job> getJobsByOwnerAndJobStatus(Member Owner, JobStatus jobStatus);
    List<Job> getJobsByOwnerAndEmployee(Member owner, Member employee);
    @Query(value = "Select distinct * from jobs where (jobs.user_uuid= ?1 and (job_status != 'DONE' OR job_status != 'DRAFT') ) or (jobs.employee_uuid =?1 and jobs.job_status = 'ACCEPTED')", nativeQuery = true)
    List<Job> getAllJobsForUser( UUID userId);
    List<Job>getByOwner(Member member);
    List<Job> getJobsByTitleContainingIgnoreCase(String title);
    Page<Job> getJobsByTitleContainingIgnoreCase(Pageable pageRequest, String title);
//    @Query(value = "select * from jobs j inner join ability_uses a on j.uuid = a.job_uuid where j.uuid=?1", nativeQuery = true)
//    Job getJobWithAbilities(UUID jobId);
}
