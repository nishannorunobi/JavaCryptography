package com.nishan.crypto;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class CredentialManager {
	private Cipher cipher;
	private static final String RSA_ALGORITHM = "RSA";
	public CredentialManager() {
		try {
			cipher = Cipher.getInstance(RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

}
