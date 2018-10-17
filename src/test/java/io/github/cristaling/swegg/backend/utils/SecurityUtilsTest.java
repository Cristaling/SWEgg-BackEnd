package io.github.cristaling.swegg.backend.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class SecurityUtilsTest {

	private UUID uuid;

	@Before
	public void setUp() throws Exception {
		this.uuid = UUID.randomUUID();
	}

	@Test
	public void toTokenAndBack() {
		String token = SecurityUtils.getTokenByUUID(this.uuid.toString());
		String uuidReturn = SecurityUtils.getUUIDByToken(token);
		assert uuidReturn.equals(uuid.toString());
	}

}