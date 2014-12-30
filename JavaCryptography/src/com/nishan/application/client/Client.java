package com.nishan.application.client;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.nishan.crypto.ApplicationMenu;
import com.nishan.crypto.ApplicationUtil;
import com.nishan.crypto.CryptographHelper;
import com.nishan.crypto.IntegrityManager;
import com.nishan.crypto.MessageManager;
import com.nishan.crypto.PublicPrivateKeyGenerator;
import com.nishan.crypto.SecretKeyManager;
import com.nishan.crypto.Services;

public final class Client extends CryptographHelper implements Services{
	private PublicKey serverPublicKey;
	private String PUBLIC_KEY_LOCATION;
	private static final String PRIVATE_KEY_LOCATION = "..\\JavaCryptography\\client\\private.key";
	private static final String SECRET_KEY_LOCATION = "..\\JavaCryptography\\client\\client_secret.key";
	private static final String CLIENT_SESSION_KEY_LOCATION = "..\\JavaCryptography\\client\\session.key";
	private static final String PLAIN_TEXT_LOCATION = "..\\JavaCryptography\\client\\plain.txt";
	private static final String DIGESTED_TEXT_LOCATION = "..\\JavaCryptography\\client\\digest.txt";
	
	private ApplicationMenu menu;
	private static Client client;
	
	public static Client getInstance(){
		if(client == null)
			client = new Client();
		return client;
	}
	public Client() {

	}

	public static void main(String[] args) {
		Client client = Client.getInstance();
		client.startProgram();
	}

	@Override
	protected void startProgram() {
		secretKeyManager = new SecretKeyManager();
		messageManager = new MessageManager();
		integrityManager = new IntegrityManager(messageManager);
		PUBLIC_KEY_LOCATION = CLIENT_PUBLIC_KEY_LOCATION;
		menu = new ApplicationMenu();
		menu.takeUserInput(client);
	}

	public void generateSecretKey(){
		generateSecretKey(SECRET_KEY_LOCATION);
	}
	public void putSendMessageToSend() {
		putSendMessageToSend(PLAIN_TEXT_LOCATION);
	}
	
	public void readSharedPublicKey() {
		this.serverPublicKey = (PublicKey) ApplicationUtil.readSavedKey(SERVER_PUBLIC_KEY_LOCATION);
	}

	public void digestAndSendMessage(){
		digestAndSendMessage(DIGESTED_TEXT_LOCATION);
	}
	
	public void receiveMessageDigested(){
		receiveMessageDigested(PLAIN_TEXT_LOCATION,DIGESTED_TEXT_LOCATION);
	}
	
	public void sendSecretKey() {
		try {
			secretKeyManager.encryptSecretKey(serverPublicKey);
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
		secretKeyManager.saveEncryptedSecretKey(ENCRYPTED_SESSION_KEY_LOCATION);
		sessionKey = secretKey;
	}

	public void receiveSessionKey() {
		if(privateKey == null){
			System.err.println("No private key found to decrypt");
			return;
		}
		secretKeyManager.decryptSessionKey(privateKey);
		secretKeyManager.saveDecryptedSessionKey(CLIENT_SESSION_KEY_LOCATION);
		this.sessionKey = secretKeyManager.getSessionKey();
	}
	
	public void generateKeyPair() {
		PublicPrivateKeyGenerator generator = new PublicPrivateKeyGenerator();
		this.publicKey = generator.getPublicKey();
		generator.savePublicKey(PUBLIC_KEY_LOCATION);
		this.privateKey = generator.getPrivateKey();
		generator.savePrivateKey(PRIVATE_KEY_LOCATION);
	}
	
	@Override
	public void showMenu() {
		menu.showClientMenu();
	}

}