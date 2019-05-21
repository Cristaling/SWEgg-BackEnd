package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberData;
import io.github.cristaling.swegg.backend.core.member.MemberReview;
import io.github.cristaling.swegg.backend.core.member.MemberReviewSummary;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.MemberReviewRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.ServiceActionResult;
import io.github.cristaling.swegg.backend.utils.enums.ErrorMessages;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberReviewService {

    private MemberReviewRepository memberReviewRepository;
    private UserRepository userRepository;
    private JobRepository jobRepository;
    private EmailSenderService emailSenderService;

    @Autowired
    public MemberReviewService(MemberReviewRepository memberReviewRepository, UserRepository userRepository, JobRepository jobRepository, EmailSenderService emailSenderService) {
        this.memberReviewRepository = memberReviewRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.emailSenderService = emailSenderService;
    }

    public ServiceActionResult<MemberReviewSummary> addMemberReview(Member reviewer, String reviewedEmail, String text, int stars){

        ServiceActionResult<MemberReviewSummary> result = new ServiceActionResult<>();

        if(stars==0){
            result.setError(ErrorMessages.REVIEW_STARS_MISSING);
            return result;
        }

        Member reviewed = this.userRepository.getMemberByEmail(reviewedEmail);

        if(reviewed == null){
            result.setError(ErrorMessages.USER_DOES_NOT_EXIST);
            return result;
        }

        MemberReview review = this.memberReviewRepository.getMemberReviewByReviewerAndReviewed(reviewer,reviewed);

        MemberReview memberReview = new MemberReview();
        memberReview.setReviewed(reviewed);
        memberReview.setReviewer(reviewer);
        memberReview.setText(text);
        memberReview.setStars(stars);
        memberReview.setDateGiven(new Date());

        if(review != null){
            memberReview.setUuid(review.getUuid());
            this.memberReviewRepository.save(memberReview);
            this.memberReviewRepository.flush();
            result.setResult(this.getMemberReviewSummary(review));
            return result;
        }

        List<Job> jobs = this.jobRepository.getJobsByOwnerAndEmployee(reviewer, reviewed);

        if(jobs.size() == 0){
            result.setError(ErrorMessages.JOB_DOES_NOT_EXIST);
            return result;
        }

        boolean isOk = false;
        for(Job job : jobs) {
            if (job.getJobStatus().equals(JobStatus.DONE)) {
                isOk = true;
            }
        }
        if(!isOk){
            result.setError(ErrorMessages.JOB_IS_NOT_DONE);
            return result;
        }

        result.setResult(this.getMemberReviewSummary(memberReview));

        this.memberReviewRepository.save(memberReview);
        this.memberReviewRepository.flush();

        emailSenderService.sendReviewNotificationToMember(memberReview);

        return result;
    }

    public List<MemberReviewSummary> getMemberLastReviews(String email){
        Member reviewed = this.userRepository.getMemberByEmail(email);
        if(reviewed == null){
            return null;
        }
        List<MemberReview> memberReviews = this.memberReviewRepository.findTop5ByReviewedOrderByDateGivenDesc(reviewed);
        return memberReviews.stream().map(this::getMemberReviewSummary).collect(Collectors.toList());
    }

    private MemberReviewSummary getMemberReviewSummary(MemberReview memberReview) {
        MemberReviewSummary memberReviewSummary = new MemberReviewSummary();

        memberReviewSummary.setUuid(memberReview.getUuid());
        memberReviewSummary.setDateGiven(memberReview.getDateGiven());

        Member reviewer = memberReview.getReviewer();

        memberReviewSummary.setReviewerEmail(reviewer.getEmail());
        MemberData memberData = reviewer.getMemberData();
        memberReviewSummary.setReviewerFirstName(memberData.getFirstName());
        memberReviewSummary.setReviewerLastName(memberData.getLastName());
        memberReviewSummary.setStars(memberReview.getStars());
        memberReviewSummary.setText(memberReview.getText());

        return memberReviewSummary;
    }
}
