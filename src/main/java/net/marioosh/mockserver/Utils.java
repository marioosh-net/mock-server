package net.marioosh.mockserver;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;


public class Utils {
	/**
	 * inputstream -> string
	 * konwersja pliku podanego jako imputstream do stringa
	 * 
	 * @param in
	 * @return
	 */
	public static String inputStreamtoString(InputStream in) {
		try {
			return IOUtils.toString(in, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
		return null;
	}	
}