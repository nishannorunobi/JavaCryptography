package com.nishan.cryptography;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CredentialManager {
	public static final String DES_CIPHER_ALGORITHM = "DESede/CBC/PKCS5Padding";
	private Cipher cipher;
	private IvParameterSpec ivParameterSpec;
	private SecretKey sessionKey;
	public CredentialManager(SecretKey sessionKey) {
		this.sessionKey = sessionKey;
		ivParameterSpec = new IvParameterSpec(new byte[]{0,0,0,0,0,0,0,0});
		try {
			cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] encrypt(byte[] message){
		try {
			try {
				cipher.init(Cipher.ENCRYPT_MODE,sessionKey,ivParameterSpec);
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] encryptedMessage = null;
		try {
			encryptedMessage = cipher.doFinal(message);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return encryptedMessage;
	}

	public byte[] decrypt(byte[] encryptedMessage) throws IllegalBlockSizeException, BadPaddingException{
		try {
			cipher.init(Cipher.DECRYPT_MODE,sessionKey,ivParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

		byte[] decryptedMessage = null;
		decryptedMessage = cipher.doFinal(encryptedMessage);
		return decryptedMessage;
	}

}
