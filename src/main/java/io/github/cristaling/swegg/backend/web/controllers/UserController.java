package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.service.UserService;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.UpdateProfileRequest;
import io.github.cristaling.swegg.backend.web.responses.ProfileResponse;
import io.github.cristaling.swegg.backend.web.responses.UserSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

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
        Member userByToken = securityService.getUserByToken(token);

        ProfileResponse profileResponse = userService.getProfile(email, userByToken);
        if (profileResponse != null)
            return new ResponseEntity(profileResponse, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/")
    public ResponseEntity uploadProfilePicture(@RequestParam("file") MultipartFile file, @RequestParam("email") String email, @RequestHeader("Authorization") String token) {
        if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Member userByToken = securityService.getUserByToken(token);

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
        if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Member userByToken =securityService.getUserByToken(token);
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
    public ResponseEntity getProfilePicture(String email) throws IOException {

        byte[] profilePicture= userService.getPic(email);

        if(profilePicture == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(profilePicture, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity getSearchedUsers(@RequestParam("name") String name,
                                           @RequestHeader("Authorization") String token,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int count) {
        if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<UserSummaryResponse> searchedUsers = userService.getSearchedUsers(name, page, count);

        return new ResponseEntity(searchedUsers, HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity getSearchedUsers(@RequestHeader("Authorization") String token) {
        if (!securityService.canAccessRole(token, MemberRole.CLIENT)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Member userByToken = securityService.getUserByToken(token);

        List<ProfileResponse> resultUsers = userService.getMostRecentUsers(userByToken);

        return new ResponseEntity(resultUsers, HttpStatus.OK);
    }

}
