package io.github.cristaling.swegg.backend.core.member;

import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "members")
public class Member {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID uuid;

	@NotNull
	@Email
	@Column(unique = true)
	private String email;

	@NotNull
	private String password;

	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "member" ,cascade= CascadeType.ALL)
	private MemberData memberData;

	private String googleID;

	public UUID getUuid() {
		return uuid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MemberRole getRole() {
		return role;
	}

	public void setRole(MemberRole role) {
		this.role = role;
	}

	public MemberData getMemberData() {
		return memberData;
	}

	public void setMemberData(MemberData memberData) {
		this.memberData = memberData;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGoogleID() {
		return googleID;
	}

	public void setGoogleID(String googleID) {
		this.googleID = googleID;
	}
}
