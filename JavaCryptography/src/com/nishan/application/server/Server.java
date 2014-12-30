package com.nishan.application.server;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.nishan.crypto.ApplicationMenu;
import com.nishan.crypto.ApplicationUtil;
import com.nishan.crypto.Constants;
import com.nishan.crypto.CryptographHelper;
import com.nishan.crypto.IntegrityManager;
import com.nishan.crypto.MessageManager;
import com.nishan.crypto.PublicPrivateKeyGenerator;
import com.nishan.crypto.SecretKeyManager;
import com.nishan.crypto.Services;

public final class Server extends CryptographHelper  implements Services{
	protected PublicKey clientPublicKey;
	public String PUBLIC_KEY_LOCATION;
	private static final String PRIVATE_KEY_LOCATION = "..\\JavaCryptography\\server\\private.key";
	private static final String SERVER_SESSION_KEY_LOCATION = "..\\JavaCryptography\\server\\session.key";
	private static final String SECRET_KEY_LOCATION = "..\\JavaCryptography\\server\\server_secret.key";
	private static Server server;
	private ApplicationMenu menu;
	public static Server getInstance(){
		if(server == null){
			server = new Server();
		}
		return server;
	}

	public Server() {

	}

	public static void main(String[] args) throws IOException {
		Server server = Server.getInstance();
		server.startProgram();
	}

	@Override
	protected void startProgram() {
		secretKeyManager = new SecretKeyManager();
		messageManager = new MessageManager();
		integrityManager = new IntegrityManager(messageManager);;
		PUBLIC_KEY_LOCATION = SERVER_PUBLIC_KEY_LOCATION;
		menu = new ApplicationMenu();
		menu.takeUserInput(server);
	}

	public void readSharedPublicKey() {
		this.clientPublicKey = (PublicKey) ApplicationUtil.readSavedKey(CLIENT_PUBLIC_KEY_LOCATION);
	}

	public void sendSecretKey() {
		try{
			secretKeyManager.encryptSecretKey(clientPublicKey);
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


	public void generateKeyPair() {
		PublicPrivateKeyGenerator generator = new PublicPrivateKeyGenerator();
		this.publicKey = generator.getPublicKey();
		generator.savePublicKey(PUBLIC_KEY_LOCATION);
		this.privateKey = generator.getPrivateKey();
		generator.savePrivateKey(PRIVATE_KEY_LOCATION);
	}

	public void receiveSessionKey() {
		if(privateKey == null){
			System.err.println("No private key found to decrypt");
			return;
		}
		secretKeyManager.decryptSessionKey(privateKey);
		secretKeyManager.saveDecryptedSessionKey(SERVER_SESSION_KEY_LOCATION);
		this.sessionKey = secretKeyManager.getSessionKey();
	}

	@Override
	public void generateSecretKey() {
		generateSecretKey(SECRET_KEY_LOCATION);
	}

	@Override
	public void putSendMessageToSend() {
		putSendMessageToSend(Constants.SERVER_PLAIN_TEXT_LOCATION);
	}

	@Override
	public void digestAndSendMessage() {
		digestAndSendMessage(Constants.SERVER_DIGESTED_TEXT_LOCATION);
	}

	@Override
	public void receiveMessageDigested() {
		receiveMessageDigested(Constants.SERVER_PLAIN_TEXT_LOCATION,Constants.SERVER_DIGESTED_TEXT_LOCATION);
	}

	@Override
	public void showMenu() {
		menu.showServerMenu();
	}



}
