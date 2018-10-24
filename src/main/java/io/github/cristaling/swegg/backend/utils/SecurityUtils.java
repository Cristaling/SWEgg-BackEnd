package io.github.cristaling.swegg.backend.utils;

import java.util.Base64;

public class SecurityUtils {

	private static String API_TOKEN_SECRET = "Aseara ti-am luat basma";

	//TODO Implement
	public static String getTokenByUUID(String uuid) {
		String resultBeforeEncode = uuid + API_TOKEN_SECRET;
		return Base64.getEncoder().encodeToString(resultBeforeEncode.getBytes());
	}

	//TODO Implement
	public static String getUUIDByToken(String token) {
		String resultAfterDecode = new String(Base64.getDecoder().decode(token));
		return resultAfterDecode.replace(API_TOKEN_SECRET, "");
	}

}
