package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.core.job.JobInvite;
import io.github.cristaling.swegg.backend.core.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobInviteRepository extends JpaRepository<JobInvite, UUID> {
    JobInvite getByJob_UuidAndMemberEmail(UUID jobUUID, String email);
    List<JobInvite> findAllByMember(Member member);
    Page<JobInvite> getJobInviteByMember(Pageable pageRequest, Member member);
}
