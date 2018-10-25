package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.service.ProfileService;
import io.github.cristaling.swegg.backend.web.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class ProfileController {

    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * @param email -> email for user
     * @return ProfileController -> all data needed for a profile status 200
     * @return returns status 404 if there are no user with this email
     */
    @GetMapping("/profile")
    public ResponseEntity getProfile(String email, @RequestHeader("Authorization") String token) {
        ProfileResponse profileResponse= profileService.getProfile(email,token);
        if(profileResponse!=null)
            return new ResponseEntity(profileService.getProfile(email,token), HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
