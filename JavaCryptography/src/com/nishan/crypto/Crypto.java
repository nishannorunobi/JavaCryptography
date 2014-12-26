package com.nishan.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public abstract class Crypto {
	protected PublicKey publicKey;
	protected PrivateKey privateKey;
	protected SecretKey secretKey;
	protected SecretKey sessionKey;
	protected Cipher cipher;
	protected final static String RSA_ALGORITHM = "RSA";
	public final static int RSA_KEY_LENGTH =2048;
	public final static String DES_ALGORITHM = "DESede";
	public final static String DES_CIPHER_ALGORITHM = "DESede/CBC/PKCS5Padding";
	public final static int DES_KEY_LENGTH = 168;
	private static Message requestMessage;
	private static Message responseMessage;
	
	protected abstract void run();
	protected abstract void showMyMenu();
	
	protected void showMenu(){
		System.out.println("0.exit");
		System.out.println("1.Generate and save public,private(RSA) keypair");
		System.out.println("2.Generate and save secret(DES) key");
		showMyMenu();
	}
	
	protected void finish(){
		System.out.println("Done");
		System.out.println();
		clearConsole();
	}

	public final static void clearConsole()
	{
	    try
	    {
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e)
	    {
	        //  Handle any exceptions.
	    }
	}
	
	protected void init() {
		try {
			cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		showMenu();
	}
	
	protected void generatePublicPrivateKey(String publicKeyLocation,String privateKeyLocation){
		KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		keyGen.initialize(RSA_KEY_LENGTH);
		KeyPair keyPair = keyGen.genKeyPair();
		this.publicKey = keyPair.getPublic();
		this.privateKey = keyPair.getPrivate();
		
		///////save in file
		byte[] encodedPublicKey = publicKey.getEncoded();
		ApplicationUtil.saveKeyToFileKey(publicKeyLocation, encodedPublicKey);
		
		byte[] encodedPrivateKey = privateKey.getEncoded();
		ApplicationUtil.saveKeyToFileKey(privateKeyLocation, encodedPrivateKey);
	}
	
	protected void generateSecretKey(String locationToSaveKey) {
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(DES_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		keyGenerator.init(DES_KEY_LENGTH);
		this.secretKey = keyGenerator.generateKey();
		byte[] encodedSecretKey = secretKey.getEncoded();
		ApplicationUtil.saveKeyToFileKey(locationToSaveKey,encodedSecretKey);
	}
}
