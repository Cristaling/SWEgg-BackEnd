package io.github.cristaling.swegg.backend.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageUtils {

	public static byte[] getBytesFromURL(String urlString) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;

		try {
			URL url = new URL(urlString);
			is = url.openStream ();
			byte[] byteChunk = new byte[1024]; // Or whatever size you want to read in at a time.
			int n;

			while ( (n = is.read(byteChunk)) > 0 ) {
				baos.write(byteChunk, 0, n);
			}
		}
		catch (IOException e) {
			System.err.printf ("Failed while reading bytes from %s: %s", e.getMessage());
			e.printStackTrace ();
			// Perform any other exception handling that's appropriate.
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return baos.toByteArray();
	}

}
