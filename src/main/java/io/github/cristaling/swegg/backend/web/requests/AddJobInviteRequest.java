package io.github.cristaling.swegg.backend.web.requests;

public class AddJobInviteRequest {

    private String email;
    private String jobUUID;

    public AddJobInviteRequest(String email, String jobUUID) {
        this.email = email;
        this.jobUUID = jobUUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobUUID() {
        return jobUUID;
    }

    public void setJobUUID(String jobUUID) {
        this.jobUUID = jobUUID;
    }
}
