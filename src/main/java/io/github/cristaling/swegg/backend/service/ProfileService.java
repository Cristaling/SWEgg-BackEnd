package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.user.User;
import io.github.cristaling.swegg.backend.core.user.UserData;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.web.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private UserRepository userRepository;
    private SecurityService securityService;

    @Autowired
    public ProfileService(UserRepository userRepository, SecurityService securityService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    /**
     *
     * @param email ->email for the user you want to see the profile
     * @param token -> user token if it's logged in
     * @return profileRepsonse that contains data about the user
     *         null if the email is not in the db
     */
    public ProfileResponse getProfile(String email, String token) {
        User user = userRepository.getUserByEmail(email);
        if(user==null){
            return null;
        }
        User userByToken = null;
        if (!token.equals("")) {
            userByToken = securityService.getUserByToken(token);
        }
        UserData userData = user.getUserData();
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setEmail(user.getEmail());
        profileResponse.setBirthDate(userData.getBirthDate());
        profileResponse.setFirstName(userData.getFirstName());
        profileResponse.setLastName(userData.getLastName());
        profileResponse.setTown(userData.getTown());
        if (userByToken != null && userByToken.getEmail().equals(email)) {
        //TODO it will contains personal data about the user. now there are none
            return profileResponse;
        }
        return profileResponse;
    }
}
