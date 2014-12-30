package com.nishan.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.SecretKey;

public abstract class CryptographHelper {
	protected PublicKey publicKey;
	protected PrivateKey privateKey;
	protected SecretKey secretKey;
	protected SecretKey sessionKey;
	protected SecretKeyManager secretKeyManager;
	protected MessageManager messageManager;
	protected IntegrityManager integrityManager;

	public static final String SERVER_PUBLIC_KEY_LOCATION = "..\\JavaCryptography\\common\\server_public.key";
	public static final String CLIENT_PUBLIC_KEY_LOCATION = "..\\JavaCryptography\\common\\client_public.key";
	public static final String ENCRYPTED_SESSION_KEY_LOCATION = "..\\JavaCryptography\\common\\encrypted_session.key";
	public static final String ENCRYPTED_PLAIN_TEXT_LOCATION = "..\\JavaCryptography\\common\\encrypted_plain.txt";
	
	protected abstract void startProgram();

	protected void generateSecretKey(String locationToSaveKey) {
		secretKeyManager = new SecretKeyManager();
		this.secretKey = secretKeyManager.getSecretKey();
		secretKeyManager.saveSecretKey(locationToSaveKey);
	}

	protected void putSendMessageToSend(String messageLocationToWrite) {
		try {
			System.out.print("type :");
			String srt = new Scanner(System.in).nextLine();
			byte [] encodedStream = srt.getBytes();
			messageManager.setSendMessage(srt);
			messageManager.saveMessageStream(messageLocationToWrite);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void digestAndSendMessage(String digestedMessageLocation) {
		if(messageManager == null){
			System.err.println("No User Input found");
		}
		messageManager.makeDigest();
		messageManager.saveDigestedStream(digestedMessageLocation);
		messageManager.makeAppend();
		integrityManager.encryptMessage(sessionKey);
		integrityManager.send(ENCRYPTED_PLAIN_TEXT_LOCATION);
	}


	protected void receiveMessageDigested( String receivedMessageLocation,String receiveDigestedLocation) {
		if(integrityManager == null)
			return;
		integrityManager.receive(ENCRYPTED_PLAIN_TEXT_LOCATION);
		integrityManager.decryptMessage(sessionKey);
		integrityManager.splitDecryptedMessage();
		integrityManager.saveDigestedReceiveStream(receiveDigestedLocation);
		integrityManager.saveReceivedStream(receivedMessageLocation);
		boolean flag = integrityManager.isMessageAltered();
		if(flag){
			System.err.println("Message is tempered");
		}else{
			System.out.print("received text: ");
			System.err.println(messageManager.getReceivedMessage());
		}
	}

}
