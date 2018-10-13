package io.github.cristaling.swegg.backend.core;

import io.github.cristaling.swegg.backend.utils.enums.UserRole;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

	@Id
	private UUID uuid;
	private String username;
	private String password;
	private UserRole role;

	public User() {
	}

	public User(UUID uuid, String username, String password) {
		this.uuid = uuid;
		this.username = username;
		this.password = password;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
}
