package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.service.JobInviteService;
import io.github.cristaling.swegg.backend.service.JobService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.AddJobInviteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity addJobInvite(@RequestHeader("Authorization") String token, @RequestBody AddJobInviteRequest addJobInviteRequest){
        if (!this.securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Job job = this.jobService.getJob(UUID.fromString(addJobInviteRequest.getJobUUID()));
        if(job == null){

        }
    }
}
