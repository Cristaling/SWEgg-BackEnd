package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.service.UserService;
import io.github.cristaling.swegg.backend.web.requests.UpdateProfileRequest;
import io.github.cristaling.swegg.backend.web.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

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
     * @return returns status 404 if there are no user with this email
     */
    @GetMapping("/profile")
    public ResponseEntity getProfile(String email, @RequestHeader("Authorization") String token) {
        Member userByToken = null;
        if (!token.equals("")) {
            userByToken = securityService.getUserByToken(token);
        }
        ProfileResponse profileResponse = userService.getProfile(email, userByToken);
        if (profileResponse != null)
            return new ResponseEntity(profileResponse, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/")
    public ResponseEntity uploadProfilePicture(@RequestParam("file") MultipartFile file, @RequestParam("email") String email, @RequestHeader("Authorization") String token) {
        Member userByToken = null;
        if (!token.equals("")) {
            userByToken = securityService.getUserByToken(token);
        }

        if (userByToken == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        boolean result=false;
        try {
           result=userService.uploadPhoto(file,email,userByToken);
        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(result==false){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        try {
            return new ResponseEntity(Base64.getEncoder().withoutPadding().encodeToString(file.getBytes()), HttpStatus.OK);
        } catch (IOException e) {
            return  new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/")
    public ResponseEntity updateProfile(@RequestBody UpdateProfileRequest profileRequest, @RequestHeader("Authorization") String token) {
        Member userByToken = null;
        if (!token.equals("")) {
            userByToken = securityService.getUserByToken(token);
        }

        if (userByToken == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        ProfileResponse profileResponse = userService.updateProfile(profileRequest, userByToken);

        if (profileResponse == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(profileResponse, HttpStatus.OK);


    }

    @GetMapping("/profile-picture")
    public ResponseEntity getProfilePicture(String email, @RequestHeader("Authorization") String token) throws IOException {
        Member userByToken = null;
        if (!token.equals("")) {
            userByToken = securityService.getUserByToken(token);
        }

        if (userByToken == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        byte[] profilePicture= userService.getPic(email,userByToken);
        if(profilePicture == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(Base64.getEncoder().withoutPadding().encodeToString(profilePicture), HttpStatus.OK);
    }

    @GetMapping("/picture")
    public ResponseEntity getProfilePicture(String email) throws IOException {

        byte[] profilePicture= userService.getPic(email);

        if(profilePicture == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(Base64.getEncoder().withoutPadding().encodeToString(profilePicture), HttpStatus.OK);
    }
}
