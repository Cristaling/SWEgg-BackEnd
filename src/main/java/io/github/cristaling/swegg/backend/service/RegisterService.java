package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberData;
import io.github.cristaling.swegg.backend.repositories.UserDataRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private UserRepository userRepository;
    private UserDataRepository userDataRepository;
    private EmailSenderService emailSenderService;

    @Autowired
    public RegisterService(UserRepository userRepository, UserDataRepository userDataRepository, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
        this.emailSenderService = emailSenderService;
    }

    /**
     *
     * @param registerRequest contains all the data for a new user
     * @return null-> if the email already exists
     *         user-> if it was successful
     *  Save a userData and a user in db
     */
    public Member registerUserAccount(RegisterRequest registerRequest) {
        Member memberEx=userRepository.getMemberByEmail(registerRequest.getEmail());
        if ( memberEx!= null) {
            return null;
        }
        MemberData memberData = new MemberData();
        memberData.setBirthDate(registerRequest.getBirthDate());
        memberData.setFirstName(registerRequest.getFirstName());
        memberData.setLastName(registerRequest.getLastName());
        memberData.setTown(registerRequest.getTown());
        Member member = new Member();
        member.setEmail(registerRequest.getEmail());
        member.setPassword(registerRequest.getPassword());
        member.setMemberData(memberData);
        member.setVerified(false);
        member.setRole(MemberRole.CLIENT);
        memberData.setMember(member);
        userRepository.save(member);
        userRepository.flush();

        userDataRepository.save(memberData);
        userDataRepository.flush();

        emailSenderService.sendConfirmationMail(
                userRepository.getMemberByEmailAndPassword(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()));
        emailSenderService.sendConfirmationMail(member);

        return member;

    }
}
