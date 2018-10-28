package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberData;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.web.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private SecurityService securityService;

    @Autowired
    public UserService(UserRepository userRepository, SecurityService securityService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    /**
     *
     * @param email ->email for the user you want to see the profile
     * @param userByToken -> user that is logged in
     * @return profileRepsonse that contains data about the user
     *         null if the email is not in the db
     */
    public ProfileResponse getProfile(String email, Member userByToken) {
        Member user = userRepository.getMemberByEmail(email);
        if(user==null){
            return null;
        }
        MemberData userData = user.getMemberData();
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
