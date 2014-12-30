package com.nishan.crypto;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

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
	public static final int RSA_KEY_LENGTH =2048;
	private KeyGenerator keyGenerator = null;
	private SecretKey secretKey = null;
	protected Cipher cipher;
	//private PublicKey publicKey;
	public SecretKeyManager() {
		initilizeGenerator();
	}

	private void initilizeGenerator() {
		try {
			keyGenerator = KeyGenerator.getInstance(DES_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		keyGenerator.init(DES_KEY_LENGTH);
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
	public void encryptSecretKey(Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		if(secretKey == null){
			System.err.println("No Secret key found");
			return;
		}
		encryptedSecretKey = cipher.doFinal(secretKey.getEncoded());
	}
	byte [] decryptedSessionKey;
	public void decryptSessionKey(Key key) {
		byte[] encryptedSessionKey = ApplicationUtil.readFileContent(CryptographHelper.ENCRYPTED_SESSION_KEY_LOCATION);
		try {
			cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			decryptedSessionKey = cipher.doFinal(encryptedSessionKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
