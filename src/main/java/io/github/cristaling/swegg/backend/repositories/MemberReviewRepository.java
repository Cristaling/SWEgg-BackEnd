package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberReviewRepository extends JpaRepository<MemberReview, UUID> {
    MemberReview getMemberReviewByReviewerAndReviewed(Member reviewer, Member reviewed);
    List<MemberReview> findTop5ByReviewedOrderByDateGivenDesc(Member reviewed);
}
