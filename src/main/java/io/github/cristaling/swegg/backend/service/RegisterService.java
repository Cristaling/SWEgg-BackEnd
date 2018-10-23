package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.user.User;
import io.github.cristaling.swegg.backend.core.user.UserData;
import io.github.cristaling.swegg.backend.repositories.UserDataRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
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

    /**
     *
     * @param registerRequest contains all the data for a new user
     * @return null-> if the email already exists
     *         user-> if it was successful
     *  Save a userData and a user in db
     */
    public User registerUserAccount(RegisterRequest registerRequest) {
        if (userRepository.getUserByEmail(registerRequest.getEmail()) != null) {
            return null;
        }
        UserData userData = new UserData();
        userData.setBirthDate(registerRequest.getBirthDate());
        userData.setFirstName(registerRequest.getFirstName());
        userData.setLastName(registerRequest.getLastName());
        userData.setTown(registerRequest.getTown());
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setUserData(userData);
        userData.setUser(user);
        userRepository.save(user);
        userRepository.flush();
        userDataRepository.save(userData);
        userDataRepository.flush();

        return user;

    }
}
