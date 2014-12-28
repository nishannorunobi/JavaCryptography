package com.nishan.crypto;

import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
	protected static final int DIGESTED_MESSAGE_LENGTH = 20;
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
	
	protected void putSendMessageToSend(String messageLocationToWrite) {
		try {
			//cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
			System.out.print("type :");
			String srt = new Scanner(System.in).nextLine();
			byte [] encodedStream = srt.getBytes();
			ApplicationUtil.saveKeyToFileKey(messageLocationToWrite, encodedStream);
			messageTosend = encodedStream;
			/*if(sessionKey!= null){
				cipher.init(Cipher.ENCRYPT_MODE, sessionKey,ivParameterSpec);		
			}
			byte[] encryptedText = cipher.doFinal(encodedStream);
			ApplicationUtil.saveKeyToFileKey(Constants.ENCRYPTED_PLAIN_TEXT_LOCATION, encryptedText);*/

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void digestAndSendMessage(String digestedMessageLocation) {
		try{
			byte[] digestedMessage = getDigest(messageTosend);
			ApplicationUtil.saveKeyToFileKey(digestedMessageLocation, digestedMessage);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			stream.write(digestedMessage);
			stream.write(messageTosend);
			
			cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
			if(sessionKey!= null){
				cipher.init(Cipher.ENCRYPT_MODE, sessionKey,ivParameterSpec);		
			}
			byte[] encryptedText = cipher.doFinal(stream.toByteArray());
			ApplicationUtil.saveKeyToFileKey(Constants.ENCRYPTED_PLAIN_TEXT_LOCATION, encryptedText);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	protected void receiveMessageDigested( String receivedMessageLocation,String receiveDigestedLocation) {
		try {
			cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
			byte[] encryptedText = ApplicationUtil.readFileContent(Constants.ENCRYPTED_PLAIN_TEXT_LOCATION);
			if(sessionKey!= null){
				cipher.init(Cipher.DECRYPT_MODE, sessionKey,ivParameterSpec);
			}
			byte[] decryptedText = cipher.doFinal(encryptedText);
			byte [] digestedReceivedMessage = new byte[DIGESTED_MESSAGE_LENGTH];
			System.arraycopy(decryptedText, 0, digestedReceivedMessage, 0, DIGESTED_MESSAGE_LENGTH);
			ApplicationUtil.saveKeyToFileKey(receiveDigestedLocation, digestedReceivedMessage);

			int messageLen = decryptedText.length - DIGESTED_MESSAGE_LENGTH;
			receivedMessage = new byte[messageLen];
			System.arraycopy(decryptedText, DIGESTED_MESSAGE_LENGTH, receivedMessage, 0, messageLen);
			ApplicationUtil.saveKeyToFileKey(receivedMessageLocation, receivedMessage);
			
			byte[] digest = getDigest(receivedMessage);
			//if(digest != digestedReceivedMessage){
			if(!Arrays.equals(digestedReceivedMessage, digest)){
				// check integrity
				System.err.println("Message is tempered");
				return;
			}
			String str = new String(receivedMessage,"UTF-8");
			System.out.print("received text: ");System.err.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private byte[] getDigest(byte [] message){
		byte[] digested = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
			digest.update(message);
			digested = digest.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return digested;
	}
}
