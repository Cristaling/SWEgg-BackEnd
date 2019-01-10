package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberReview;
import io.github.cristaling.swegg.backend.core.member.MemberReviewSummary;
import io.github.cristaling.swegg.backend.service.MemberReviewService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.ServiceActionResult;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.AddReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/review")
public class MemberReviewController {

    private MemberReviewService memberReviewService;
    private SecurityService securityService;

    @Autowired
    public MemberReviewController(MemberReviewService memberReviewService, SecurityService securityService) {
        this.memberReviewService = memberReviewService;
        this.securityService = securityService;
    }

    @PostMapping
    public ResponseEntity addReview(@RequestHeader("Authorization") String token, @RequestBody AddReviewRequest addReviewRequest){
        if (!this.securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Member reviewer = this.securityService.getUserByToken(token);
        String reviewedEmail = addReviewRequest.getReviewedEmail();
        String reviewText = addReviewRequest.getText();
        int stars = addReviewRequest.getStars();
        ServiceActionResult<MemberReviewSummary> serviceActionResult = this.memberReviewService.addMemberReview(reviewer, reviewedEmail, reviewText, stars);
        if(!serviceActionResult.isSuccessful()){
            return new ResponseEntity(serviceActionResult,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(serviceActionResult.getResult(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getMemberLastReviews(@RequestHeader("Authorization") String token, String email){
        if (!this.securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        List<MemberReviewSummary> memberReviews = this.memberReviewService.getMemberLastReviews(email);
        if(memberReviews == null){
            return new ResponseEntity("Could not find email", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(memberReviews, HttpStatus.OK);
    }
}
