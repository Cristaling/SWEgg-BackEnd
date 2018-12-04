package io.github.cristaling.swegg.backend.web.responses;

import java.util.List;

public class EndorsementResponse {

	private String ability;
	private List<String> emails;

	public EndorsementResponse(String ability, List<String> emails) {
		this.ability = ability;
		this.emails = emails;
	}

	public EndorsementResponse() {
	}

	public String getAbility() {
		return ability;
	}

	public void setAbility(String ability) {
		this.ability = ability;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
}
