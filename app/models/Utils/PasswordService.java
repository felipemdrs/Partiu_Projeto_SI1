package models.Utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.directory.api.util.Base64;

public final class PasswordService {

	private static PasswordService instance;

	private PasswordService() { }

	public String encrypt(String plaintext) throws Exception {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch(NoSuchAlgorithmException e) {
			throw new Exception(e.getMessage());
		} 
		try {
			md.update(plaintext.getBytes("UTF-8"));
		} catch(UnsupportedEncodingException e) {
			throw new Exception(e.getMessage());
		}

		byte raw[] = md.digest();
		String hash = String.valueOf(Base64.encode(raw));  
		return hash;
	}

	public static PasswordService getInstance() {
		if(instance == null) {
			instance = new PasswordService(); 
		} 
		return instance;
	}

}
