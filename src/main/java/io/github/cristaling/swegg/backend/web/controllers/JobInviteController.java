package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobSummary;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.JobInviteService;
import io.github.cristaling.swegg.backend.service.JobService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.AddJobInviteRequest;
import io.github.cristaling.swegg.backend.web.responses.JobWithAbilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/job-invite")
public class JobInviteController {

    private JobService jobService;
    private JobInviteService jobInviteService;
    private SecurityService securityService;

    @Autowired
    public JobInviteController(JobInviteService jobInviteService, SecurityService securityService, JobService jobService) {
        this.jobInviteService = jobInviteService;
        this.securityService = securityService;
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity addJobInvite(@RequestHeader("Authorization") String token, @RequestBody AddJobInviteRequest addJobInviteRequest) {
        if (!this.securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        JobWithAbilities jobWithAbilities = this.jobService.getJob(UUID.fromString(addJobInviteRequest.getJobUUID()));
        if(jobWithAbilities==null){
            return new ResponseEntity("Job does not exist!", HttpStatus.BAD_REQUEST);
        }
        Job job = new Job(jobWithAbilities);
        Member jobOwner = this.securityService.getUserByToken(token);
        if (!job.getOwner().getUuid().equals(jobOwner.getUuid())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if (this.jobInviteService.addJobInvite(job, addJobInviteRequest.getEmail())) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("User already invited!", HttpStatus.BAD_REQUEST);
    }

    /**
     * @param token
     * @param page
     * @param count
     * @return List<JobSummary> due to the fact that on UI there will be displayed the jobs where the user has been invited
     */
    @GetMapping
    public ResponseEntity getJobInvitesByUser(@RequestHeader("Authorization") String token,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int count) {

        if (!this.securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Member member = this.securityService.getUserByToken(token);
        List<JobSummary> summaries = this.jobInviteService.getJobInvitesByUser(member, page, count);
        return new ResponseEntity(summaries, HttpStatus.OK);
    }
}
