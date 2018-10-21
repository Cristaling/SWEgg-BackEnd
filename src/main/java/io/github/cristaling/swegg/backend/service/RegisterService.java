package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.user.User;
import io.github.cristaling.swegg.backend.core.user.UserData;
import io.github.cristaling.swegg.backend.repositories.UserDataRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.DateUtils;
import io.github.cristaling.swegg.backend.web.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private UserRepository userRepository;
    private UserDataRepository userDataRepository;

    @Autowired
    public RegisterService(UserRepository userRepository, UserDataRepository userDataRepository) {
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
    }


    public User registerUserAccount(RegisterRequest registerRequest) {
        if(userRepository.getUserByUsername(registerRequest.getEmail())!=null){
            return null;
        }
        UserData userData = new UserData();
        userData.setBirthDate(DateUtils.parseLocalDateFromString(registerRequest.getBirthDate()));
        userData.setEmail(registerRequest.getEmail());
        userData.setFirstName(registerRequest.getFirstName());
        userData.setLastName(registerRequest.getLastName());
        userData.setTown(registerRequest.getTown());
        User user = new User();
        user.setUsername(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setUserData(userData);
        userData.setUser(user );
        userRepository.save(user);
        userRepository.flush();
        userDataRepository.save(userData);
        userDataRepository.flush();

        return user;

    }
}
