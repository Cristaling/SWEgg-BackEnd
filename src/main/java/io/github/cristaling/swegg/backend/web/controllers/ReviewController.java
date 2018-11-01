package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.member.MemberReview;
import io.github.cristaling.swegg.backend.service.MemberReviewService;
import io.github.cristaling.swegg.backend.web.requests.AddReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private MemberReviewService memberReviewService;

    @Autowired
    public ReviewController(MemberReviewService memberReviewService) {
        this.memberReviewService = memberReviewService;
    }

    @RequestMapping("/add")
    public ResponseEntity addReview(@RequestHeader("token") String token, @RequestBody AddReviewRequest addReviewRequest){
        MemberReview memberReview = this.memberReviewService.addMemberReview(addReviewRequest);
        if(memberReview == null){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
