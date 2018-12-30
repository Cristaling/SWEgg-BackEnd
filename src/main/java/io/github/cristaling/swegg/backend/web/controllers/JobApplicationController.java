package io.github.cristaling.swegg.backend.web.controllers;


import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.JobApplicationService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.JobAddRequest;
import io.github.cristaling.swegg.backend.web.requests.JobApplicationAddRequest;
import io.github.cristaling.swegg.backend.web.requests.UUIDRequest;
import io.github.cristaling.swegg.backend.web.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/job-application")
public class JobApplicationController {

    private JobApplicationService jobApplicationService;

    private SecurityService securityService;

    @Autowired
    public JobApplicationController(JobApplicationService jobApplicationService, SecurityService securityService) {
        this.jobApplicationService = jobApplicationService;
        this.securityService = securityService;
    }

    @PostMapping
    public ResponseEntity addJobApplication(@RequestHeader("Authorization") String token, @RequestBody UUIDRequest request) {

        if (!securityService.canAccessRole(token, MemberRole.PROVIDER)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Member member = securityService.getUserByToken(token);

        if (this.jobApplicationService.addJobApplication(member, request.getUuid())) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/getByParticipant/{email}")
    public List<JobApplicationAddRequest> getApplicationsForParticipantEmail(@PathVariable String email) {
        List<JobApplication> jobApplications;
        List<JobApplicationAddRequest> jobApplicationAddRequests = new ArrayList<>();
        jobApplications = jobApplicationService.getApplicationsForApplicant(email);
        for (JobApplication jobApplication : jobApplications) {
            JobApplicationAddRequest jobApplicationAddRequest = new JobApplicationAddRequest(jobApplication);
            jobApplicationAddRequests.add(jobApplicationAddRequest);
        }
        return jobApplicationAddRequests;
    }

    @GetMapping(value = "/getByJob/{uuid}")
    public List<ProfileResponse> getApplicationsForJob(@PathVariable UUID uuid) {
        List<JobApplication> jobApplications;
        List<ProfileResponse> jobApplicationAddRequests = new ArrayList<>();
        jobApplications = jobApplicationService.getApplicationsForJob(uuid);
        for (JobApplication jobApplication : jobApplications) {
            JobApplicationAddRequest jobApplicationAddRequest = new JobApplicationAddRequest(jobApplication);
            jobApplicationAddRequests.add
                    (new ProfileResponse(jobApplicationAddRequest.getApplicant().getMemberData(),
                            jobApplicationAddRequest.getApplicant().getEmail()));
        }
        return jobApplicationAddRequests;
    }
}
