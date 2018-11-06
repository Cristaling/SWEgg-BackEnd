package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberData;
import io.github.cristaling.swegg.backend.repositories.UserDataRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.web.requests.UpdateProfileRequest;
import io.github.cristaling.swegg.backend.web.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserDataRepository userDataRepository;
    @Autowired
    public UserService(UserRepository userRepository, UserDataRepository userDataRepository) {
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
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

    public ProfileResponse updateProfile(UpdateProfileRequest profileRequest, Member userByToken) {
        Member user = userRepository.getMemberByEmail(profileRequest.getEmail());
        if(user==null){
            return null;
        }
        if(!userByToken.getEmail().equals(profileRequest.getEmail())){
            return null;
        }

        MemberData userDataUpdated=userDataRepository.getByMember(user);
        userDataUpdated.setBirthDate(profileRequest.getBirthDate());
        userDataUpdated.setFirstName(profileRequest.getFirstName());
        userDataUpdated.setLastName(profileRequest.getLastName());
        userDataUpdated.setTown(profileRequest.getTown());

        userDataRepository.save(userDataUpdated);
        userDataRepository.flush();

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setEmail(user.getEmail());
        profileResponse.setBirthDate(profileRequest.getBirthDate());
        profileResponse.setFirstName(profileRequest.getFirstName());
        profileResponse.setLastName(profileRequest.getLastName());
        profileResponse.setTown(profileRequest.getTown());

        return profileResponse;

    }
}
