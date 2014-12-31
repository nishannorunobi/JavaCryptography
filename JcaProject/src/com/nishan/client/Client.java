package com.nishan.client;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.nishan.cryptography.ApplicationMenu;
import com.nishan.cryptography.ApplicationUtil;
import com.nishan.cryptography.AuthenticationManager;
import com.nishan.cryptography.DataPacket;
import com.nishan.cryptography.IntegrityManager;
import com.nishan.cryptography.ParentServices;
import com.nishan.cryptography.PublicPrivateKeyGenerator;
import com.nishan.cryptography.Receiver;
import com.nishan.cryptography.SecretKeyManager;
import com.nishan.cryptography.Sender;
import com.nishan.cryptography.Services;

public class Client extends ParentServices implements Services{
	protected PublicKey serverPublicKey;
	public String PUBLIC_KEY_LOCATION;
	private static Services service;
	
	private static final String PRIVATE_KEY_LOCATION = "..\\JcaProject\\client\\private.key";
	private static final String CLIENT_SESSION_KEY_LOCATION = "..\\JcaProject\\client\\session.key";
	private static final String SECRET_KEY_LOCATION = "..\\JcaProject\\client\\client_secret.key";
	private static final String PLAIN_TEXT_LOCATION = "..\\JcaProject\\client\\plain.txt";
	private static final String DIGESTED_TEXT_LOCATION = "..\\JcaProject\\client\\digest.txt";
	public static Services getInstance(){
		if(service == null){
			service = new Client();
		}
		return service;
	}

	public Client() {

	}

	public static void main(String[] args) throws IOException {
		service = Client.getInstance();
		service.execute();
	}

	@Override
	public void execute() {
		menu = new ApplicationMenu();
		PUBLIC_KEY_LOCATION = CLIENT_PUBLIC_KEY_LOCATION;
		keyPairGenerator = new PublicPrivateKeyGenerator();
		service.showMenu(menu);
	}

	@Override
	public void generatePublicKey() {
		this.publicKey = keyPairGenerator.getPublicKey();
		keyPairGenerator.savePublicKey(PUBLIC_KEY_LOCATION);
	}

	@Override
	public void generatePrivateKey() {
		this.privateKey = keyPairGenerator.getPrivateKey();
		keyPairGenerator.savePrivateKey(PRIVATE_KEY_LOCATION);
	}

	SecretKeyManager sendSecretKeyManager;
	@Override
	public void generateSecretKey() {
		sendSecretKeyManager = new SecretKeyManager();
		this.secretKey = sendSecretKeyManager.getSecretKey();
		sendSecretKeyManager.saveSecretKey(SECRET_KEY_LOCATION);
	}

	@Override
	public void getSharedPublicKey() {
		this.serverPublicKey = (PublicKey) ApplicationUtil.readSavedKey(SERVER_PUBLIC_KEY_LOCATION);
	}

	@Override
	public void sendSecretKey() {
		try{
			sendSecretKeyManager.encryptSecretKey(serverPublicKey);
		} catch (InvalidKeyException e) {
			System.err.println(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			//e.printStackTrace();
			System.err.println(e.getMessage());
		} catch (NoSuchPaddingException e) {
			//e.printStackTrace();
			System.err.println(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			//e.printStackTrace();
			System.err.println(e.getMessage());
		} catch (BadPaddingException e) {
			//e.printStackTrace();
			System.err.println(e.getMessage());
		}
		sendSecretKeyManager.saveEncryptedSecretKey(ENCRYPTED_SESSION_KEY_LOCATION);
		sessionKey = secretKey;
	}

	@Override
	public void receiveSessionKey() {
		if(privateKey == null){
			System.err.println("No private key found to decrypt");
			return;
		}
		SecretKeyManager secretKeyManager = new SecretKeyManager();
		secretKeyManager.receiveEncryptedSessionKey(ENCRYPTED_SESSION_KEY_LOCATION);
		try {
			try {
				secretKeyManager.decryptSessionKey(privateKey);
			} catch (InvalidKeyException e) {
				System.err.println("Invalid private key");
			}
		} catch (IllegalBlockSizeException e) {
			System.err.println("Message is tempered");
			return;
		} catch (BadPaddingException e) {
			System.err.println("Message is tempered");
			return;
		}
		secretKeyManager.saveDecryptedSessionKey(CLIENT_SESSION_KEY_LOCATION);
		this.sessionKey = secretKeyManager.getSessionKey();
	}

	@SuppressWarnings("resource")
	@Override
	public void takeUserInput() {
		senddata = new DataPacket();
		System.out.print("Type :");
		String srt = new Scanner(System.in).nextLine();
		senddata.setMessage(srt);
		senddata.setMessageStream(srt.getBytes());
		ApplicationUtil.saveKeyToFileKey(PLAIN_TEXT_LOCATION, senddata.getMessageStream());
	}

	@Override
	public void digestUserInput() {
		IntegrityManager integrityManager = new IntegrityManager();
		integrityManager.digest(senddata.getMessageStream());
		senddata.setDigestedStream(integrityManager.getDigest());
		ApplicationUtil.saveKeyToFileKey(DIGESTED_TEXT_LOCATION, senddata.getDigestedStream());
	}
	
	@Override
	public void makeSignature() {
		AuthenticationManager authenticationManager = new AuthenticationManager();
		authenticationManager.encrypt(privateKey, senddata.getMessageStream());
		senddata.setSignatureStream(authenticationManager.getSignedMessage());
	}

	@Override
	public void send() {
		sender = new Sender(senddata);
		sender.appendMessageAndDigest();
		sender.encryptMessageAndDigest(sessionKey);
		//sender.attachSignature();
		sender.send(ENCRYPTED_PLAIN_TEXT_LOCATION);
	}
	
	@Override
	public void receiveDataPacket() {
		Receiver receiver = new Receiver();
		receiver.receive(ENCRYPTED_PLAIN_TEXT_LOCATION);
		//receiver.seperateSignature();
		try {
			receiver.decryptMessageAndDigest(sessionKey);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.err.println("Message is tempered");
			return;
		}
		receiver.splitMessageAndDigest();
		receiveData = receiver.getDataPacket();
	}

	@Override
	public void printReceivedMessage() {
		ApplicationUtil.saveKeyToFileKey(PLAIN_TEXT_LOCATION, receiveData.getMessageStream());
		System.out.print("Received text:");System.err.println(receiveData.getMessage());
	}

	@Override
	public void checkIntegrity() {
		IntegrityManager integrityManager = new IntegrityManager();
		integrityManager.digest(receiveData.getMessageStream());
		boolean flag = integrityManager.isMessageTempered(receiveData.getDigestedStream());
		if(flag){
			System.err.println("Truddy altered the message sent by Server");
		}else{
			System.out.println("Perfect message");
		}
	}

	@Override
	public void checkAuthentication() {
		AuthenticationManager authenticationManager = new AuthenticationManager();
		authenticationManager.decrypt(serverPublicKey, receiveData.getSignatureStream());
		boolean flag = authenticationManager.virifySignature(receiveData.getMessageStream());
		if(flag){
			System.err.println("verified");
		}else{
			System.err.println("Not varified");
		}
	}

	@Override
	public void showMenu(ApplicationMenu menu) {
		menu.takeUserInput(service);
	}

	@Override
	public void close() {
		System.exit(0);
	}

}
