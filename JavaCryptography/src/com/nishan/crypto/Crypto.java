package com.nishan.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public abstract class Crypto {
	protected PublicKey publicKey;
	protected PrivateKey privateKey;
	protected SecretKey secretKey;
	protected SecretKey sessionKey;
	protected Cipher cipher;
	protected static final String DIGEST_ALGORITHM = "SHA-1";
	protected static final String RSA_ALGORITHM = "RSA";
	public static final int RSA_KEY_LENGTH =2048;
	public static final String DES_ALGORITHM = "DESede";
	public static final String DES_CIPHER_ALGORITHM = "DESede/CBC/PKCS5Padding";
	public final static int DES_KEY_LENGTH = 168;
	protected IvParameterSpec ivParameterSpec;
	protected ArrayList<String> menu;
	protected byte[] messageTosend;
	protected byte[] receivedMessage;
	protected byte[] digestedMessage;
	/*private static Message requestMessage;
	private static Message responseMessage;*/

	protected abstract void run();
	protected abstract void loadMyMenu();

	protected void loadMenu(){
		menu = new ArrayList<String>();
		menu.add("0.exit");
		//System.out.println();
		menu.add("1.Generate and save public,private(RSA) keypair");
		//System.out.println();
		menu.add("2.Generate and save secret(DES) key");
		//System.out.println("2.Generate and save secret(DES) key");
		loadMyMenu();
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
			System.out.flush();
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
		loadMenu();
		ivParameterSpec = new IvParameterSpec(new byte[8]);
		try {
			//cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
			cipher = Cipher.getInstance(RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		showMenu();
	}

	protected void showMenu() {
		for(String menuItem:menu){
			if(menuItem != null && !menuItem.isEmpty()){
				System.out.println(menuItem);
			}
			
		}
	}
	protected void generatePublicPrivateKey(String publicKeyLocation,String privateKeyLocation){
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
