package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.member.MemberReview;
import io.github.cristaling.swegg.backend.service.MemberReviewService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.AddReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/add")
    public ResponseEntity addReview(@RequestHeader("Authorization") String token, @RequestBody AddReviewRequest addReviewRequest){
        if (!this.securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        MemberReview memberReview = this.memberReviewService.addMemberReview(this.securityService.getUserByToken(token), addReviewRequest.getReviewedEmail(), addReviewRequest.getText(), addReviewRequest.getStars());
        if(memberReview == null){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
