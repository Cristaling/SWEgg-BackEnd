package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.user.User;
import io.github.cristaling.swegg.backend.service.RegisterService;
import io.github.cristaling.swegg.backend.web.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class RegisterController {

    private RegisterService registerService;


    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    /**
     * @param registerRequest (email, pass, birth date, town, first name, last name)
     * @return Status 409 if email already exists
     *         Status 200 if a user was saved successfully
     */
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody RegisterRequest registerRequest) {
        User registered = this.registerService.registerUserAccount(registerRequest);
        if (registered == null) {
            return new ResponseEntity("Email already exists",HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
