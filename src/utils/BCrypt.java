package utils;

import java.io.UnsupportedEncodingException;

public class BCrypt extends java.lang.Object {
	private static final int BCRYPT_SALT_LEN = 16;

	public static String bCrypt(String plain_password) {
		String pw_hash = BCrypt.hashpw(plain_password, BCRYPT_SALT_LEN);
		if (BCrypt.checkpw(plain_password, pw_hash)) {
		    System.out.println("It matches");
		} else {
		    System.out.println("It does not match");
		}
		return pw_hash;
	}

	private static String hashpw(String plain_password, int salt) {
		byte[] password;
		try {
			password = plain_password.getBytes("UTF-8");
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < password.length; i++) {
				String hex = Integer.toHexString(0xff & password[i]);
				if(hex.length() == 1) hexString.append('0');
					hexString.append(hex);
			  	}
			return hexString.toString();
		} catch (UnsupportedEncodingException uee) {
			  throw new AssertionError("UTF-8 is not supported");
		}
	}

	private static boolean checkpw(String plain_password, String pw_hash) {
		if(plain_password.matches(pw_hash)) {
			return true;
		}
		return false;
	}

	
}