package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberReview;
import io.github.cristaling.swegg.backend.repositories.MemberReviewRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MemberReviewService {

    private MemberReviewRepository memberReviewRepository;
    private UserRepository userRepository;

    @Autowired
    public MemberReviewService(MemberReviewRepository memberReviewRepository, UserRepository userRepository) {
        this.memberReviewRepository = memberReviewRepository;
        this.userRepository = userRepository;
    }

    public MemberReview addMemberReview(Member reviewer, String reviewedEmail, String text, int stars){
        MemberReview memberReview = new MemberReview();
        memberReview.setReviewed(this.userRepository.getMemberByEmail(reviewedEmail));
        memberReview.setReviewer(reviewer);
        memberReview.setText(text);
        memberReview.setStars(stars);
        memberReview.setDateGiven(new Date());

        this.memberReviewRepository.save(memberReview);
        this.memberReviewRepository.flush();

        return memberReview;
    }
}
