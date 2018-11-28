package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberData;
import io.github.cristaling.swegg.backend.repositories.UserDataRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.web.requests.UpdateProfileRequest;
import io.github.cristaling.swegg.backend.web.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

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
        ProfileResponse profileResponse = new ProfileResponse(userData,user.getEmail());
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

        MemberData userDataUpdated=user.getMemberData();
        userDataUpdated.setBirthDate(profileRequest.getBirthDate());
        userDataUpdated.setFirstName(profileRequest.getFirstName());
        userDataUpdated.setLastName(profileRequest.getLastName());
        userDataUpdated.setTown(profileRequest.getTown());

        userDataRepository.save(userDataUpdated);
        userDataRepository.flush();

        ProfileResponse profileResponse = new ProfileResponse(userDataUpdated,user.getEmail());

        return profileResponse;
    }

    @Transactional
    public boolean uploadPhoto(MultipartFile file, String email,Member userByToken) throws IOException {
        Member user = userRepository.getMemberByEmail(email);
        if(user==null){
            return false;
        }
        if(!userByToken.getEmail().equals(email)){
            return false;
        }
        MemberData memberData = user.getMemberData();
        memberData.setPicture(file.getBytes());
        userDataRepository.save(memberData);
        return true;
    }

    public byte[] getPic(String email, Member userByToken){
        Member user = userRepository.getMemberByEmail(email);
        if(user==null){
            return null;
        }
        if(!userByToken.getEmail().equals(email)){
            return null;
        }
        return user.getMemberData().getPicture();
    }
}
