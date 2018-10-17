package io.github.cristaling.swegg.backend.utils;

public class SecurityUtils {

	private static String API_TOKEN_SECRET = "Aseara ti-am luat basma";

	//TODO Implement
	public static String getTokenByUUID(String uuid) {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < Math.max(API_TOKEN_SECRET.length(), uuid.length()); i++) {

			int secretIndex = i % API_TOKEN_SECRET.length();
			int uuidIndex = i % uuid.length();

			char secretChar = API_TOKEN_SECRET.charAt(secretIndex);
			char uuidChar = uuid.charAt(uuidIndex);
			char resultChar = (char) (secretChar + uuidChar);

			stringBuilder.append(resultChar);
		}
		return stringBuilder.toString();
	}

	//TODO Implement
	public static String getUUIDByToken(String token) {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < Math.max(API_TOKEN_SECRET.length(), token.length()); i++) {

			int secretIndex = i % API_TOKEN_SECRET.length();
			int tokenIndex = i % token.length();

			char secretChar = API_TOKEN_SECRET.charAt(secretIndex);
			char tokenChar = token.charAt(tokenIndex);
			char resultChar = (char) (tokenChar - secretChar);

			stringBuilder.append(resultChar);
		}
		return stringBuilder.toString();
	}

}
