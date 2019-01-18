package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.recommendations.Recommend;
import io.github.cristaling.swegg.backend.service.RecommendService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.ServiceActionResult;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.RecommendRequest;
import io.github.cristaling.swegg.backend.web.responses.RecommendForProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    private RecommendService recommendService;
    private SecurityService securityService;

    @Autowired
    public RecommendController(RecommendService recommendService, SecurityService securityService) {
        this.recommendService = recommendService;
        this.securityService = securityService;
    }

    @PostMapping()
    public ResponseEntity saveRecommend(@RequestHeader("Authorization") String token, @RequestBody RecommendRequest recommendRequest) {
        if (!this.securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Member member = this.securityService.getUserByToken(token);
        ServiceActionResult<List<Recommend>> serviceActionResult = this.recommendService.addRecommend(recommendRequest.getReceiver(),
                recommendRequest.getRecommendedEmail(),
                member.getEmail());
        if (!serviceActionResult.isSuccessful()) {
            return new ResponseEntity(serviceActionResult, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(serviceActionResult.getResult(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity getRecommends(@RequestHeader("Authorization") String token) {
        if (!this.securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Member member = this.securityService.getUserByToken(token);
        ServiceActionResult<List<RecommendForProfile>> serviceActionResult = this.recommendService.getRecommendations(member);
        if (!serviceActionResult.isSuccessful()) {
            return new ResponseEntity(serviceActionResult, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(serviceActionResult.getResult(), HttpStatus.OK);
    }

}
