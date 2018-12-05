package io.github.cristaling.swegg.backend.web.requests;

import java.util.UUID;

public class UUIDRequest {

	UUID uuid;

	public UUIDRequest() {
	}

	public UUIDRequest(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}
