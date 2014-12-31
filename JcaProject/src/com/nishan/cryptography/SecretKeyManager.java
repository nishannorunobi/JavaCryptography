package com.nishan.cryptography;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecretKeyManager {
	public static final String DES_ALGORITHM = "DESede";
	public final static int DES_KEY_LENGTH = 168;
	protected static final String RSA_ALGORITHM = "RSA";
	private KeyGenerator keyGenerator = null;
	private SecretKey secretKey = null;
	protected Cipher cipher;
	public SecretKeyManager() {
		initilizeGenerator();
	}

	private void initilizeGenerator() {
		try {
			keyGenerator = KeyGenerator.getInstance(DES_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//keyGenerator.init(DES_KEY_LENGTH);
		keyGenerator.init(new SecureRandom());
		try {
			cipher = Cipher.getInstance(RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SecretKey getSecretKey(){
		if(keyGenerator == null){
			initilizeGenerator();
		}
		secretKey = keyGenerator.generateKey();
		return secretKey;
	}

	public void saveSecretKey(String locationToSaveKey){
		if(secretKey == null)
			return;
		byte[] encodedSecretKey = secretKey.getEncoded();
		ApplicationUtil.saveKeyToFileKey(locationToSaveKey,encodedSecretKey);
	}

	byte[] encryptedSecretKey;
	public void encryptSecretKey(PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		//cipher = Cipher.getInstance(DES_PADDING_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		if(secretKey == null){
			System.err.println("No Secret key found");
			return;
		}
		encryptedSecretKey = cipher.doFinal(secretKey.getEncoded());
	}
	byte [] decryptedSessionKey;
	public void decryptSessionKey(PrivateKey privateKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		decryptedSessionKey = cipher.doFinal(encryptedSecretKey);
	}

	public byte[] receiveEncryptedSessionKey(String fileName){
		readSecretKey(fileName);
		return encryptedSecretKey;
	}

	private void readSecretKey(String fileName){
		this.encryptedSecretKey = ApplicationUtil.readFileContent(fileName);
	}

	public void saveDecryptedSessionKey(String location){
		ApplicationUtil.saveKeyToFileKey(location, decryptedSessionKey);
	}

	public SecretKey getSessionKey(){
		SecretKey sessionKey = new SecretKeySpec(decryptedSessionKey, DES_ALGORITHM);
		return sessionKey;
	}

	public void saveEncryptedSecretKey(String location){
		ApplicationUtil.saveKeyToFileKey(location, encryptedSecretKey);		
	}

}
