package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.service.UserService;
import io.github.cristaling.swegg.backend.web.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;
    private SecurityService securityService;

    @Autowired
    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    /**
     * @param email -> email for user
     * @return ProfileController -> all data needed for a profile status 200
     * @return returns status 404 if there are no user with this email
     */
    @GetMapping("/profile")
    public ResponseEntity getProfile(String email, @RequestHeader("Authorization") String token) {
        Member userByToken = null;
        if (!token.equals("")) {
            userByToken = securityService.getUserByToken(token);
        }
        ProfileResponse profileResponse= userService.getProfile(email,userByToken);
        if(profileResponse!=null)
            return new ResponseEntity(profileResponse, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
