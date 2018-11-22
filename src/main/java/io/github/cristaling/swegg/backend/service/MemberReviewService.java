package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberReview;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.MemberReviewRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MemberReviewService {

    private MemberReviewRepository memberReviewRepository;
    private UserRepository userRepository;
    private JobRepository jobRepository;

    @Autowired
    public MemberReviewService(MemberReviewRepository memberReviewRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.memberReviewRepository = memberReviewRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public MemberReview addMemberReview(Member reviewer, String reviewedEmail, String text, int stars){
        Member reviewed = this.userRepository.getMemberByEmail(reviewedEmail);
        if(reviewed == null){
            return null;
        }
        MemberReview review = this.memberReviewRepository.getMemberReviewByReviewerAndReviewed(reviewer,reviewed);
        if(review != null){
            return null;
        }
        Job job = this.jobRepository.getJobByOwnerAndEmployee(reviewer, reviewed);
        if(job == null){
            return null;
        }
        if(!job.getJobStatus().equals(JobStatus.DONE)){
            return null;
        }

        MemberReview memberReview = new MemberReview();
        memberReview.setReviewed(reviewed);
        memberReview.setReviewer(reviewer);
        memberReview.setText(text);
        memberReview.setStars(stars);
        memberReview.setDateGiven(new Date());

        this.memberReviewRepository.save(memberReview);
        this.memberReviewRepository.flush();

        return memberReview;
    }
}
