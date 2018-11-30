package io.github.cristaling.swegg.backend.web.responses;

import io.github.cristaling.swegg.backend.core.member.MemberData;

import java.io.Serializable;
import java.util.Date;

public class ProfileResponse implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String town;

    public ProfileResponse(String email, String firstName, String lastName, Date birthDate, String town) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.town = town;
    }

    public ProfileResponse() {
    }

    public ProfileResponse(MemberData memberData, String email) {
        this.email= email;
        this.birthDate=memberData.getBirthDate();
        this.firstName=memberData.getFirstName();
        this.lastName=memberData.getLastName();
        this.town=memberData.getTown();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
