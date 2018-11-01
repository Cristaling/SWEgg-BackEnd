package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.MemberReview;
import io.github.cristaling.swegg.backend.repositories.MemberReviewRepository;
import io.github.cristaling.swegg.backend.web.requests.AddReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberReviewService {

    private MemberReviewRepository memberReviewRepository;

    @Autowired
    public MemberReviewService(MemberReviewRepository memberReviewRepository) {
        this.memberReviewRepository = memberReviewRepository;
    }

    public MemberReview addMemberReview(AddReviewRequest addReviewRequest){
        MemberReview memberReview = new MemberReview();
        memberReview.setReviewed(addReviewRequest.getReviewed());
        memberReview.setReviewer(addReviewRequest.getReviewer());
        memberReview.setText(addReviewRequest.getText());
        memberReview.setStars(addReviewRequest.getStars());
        memberReview.setDateGiven(addReviewRequest.getDateGiven());

        return memberReview;

    }
}
