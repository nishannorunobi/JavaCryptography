package com.nishan.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public final class PublicPrivateKeyGenerator {
	protected static final String RSA_ALGORITHM = "RSA";
	public static final int RSA_KEY_LENGTH =2048;
	protected PublicKey publicKey;
	protected PrivateKey privateKey;
	private KeyPairGenerator keyGen = null;
	public PublicPrivateKeyGenerator() {
		generatePublicPrivateKey();
	}

	private void generatePublicPrivateKey(){
		KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		keyGen.initialize(RSA_KEY_LENGTH);
		KeyPair keyPair = keyGen.genKeyPair();
		this.publicKey = keyPair.getPublic();
		this.privateKey = keyPair.getPrivate();
	}

	public PublicKey getPublicKey(){
		return publicKey;
	}

	public PrivateKey getPrivateKey(){
		return privateKey;
	}

	public boolean savePublicKey(String publicKeyLocation){
		///////save in file
		byte[] encodedPublicKey = publicKey.getEncoded();
		ApplicationUtil.saveKeyToFileKey(publicKeyLocation, encodedPublicKey);
		return true;
	}
	
	public boolean savePrivateKey(String privateKeyLocation){
		byte[] encodedPrivateKey = privateKey.getEncoded();
		ApplicationUtil.saveKeyToFileKey(privateKeyLocation, encodedPrivateKey);
		return true;
	}
}
