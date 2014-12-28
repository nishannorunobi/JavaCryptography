package com.nishan.application.server;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.nishan.crypto.ApplicationUtil;
import com.nishan.crypto.Constants;
import com.nishan.crypto.Crypto;

public final class Server extends Crypto{
	protected PublicKey clientPublicKey;

	public Server() {

	}

	public static void main(String[] args) throws IOException {
		new Server().run();
	}

	@Override
	protected void run() {
		init();
		int option = 100;
		while(option > 0){
			try {
				String input = new Scanner(System.in).nextLine();
				option = new Integer(input);
			} catch (Exception e) {
				option = 100;
				System.out.println("Invalid input,try again!");
			}
			switch (option) {
			case 0:
				System.out.println("thanks");
				break;
			case 1:
				generateKeyPair();
				menu.set(1, "");
				break;
			case 2:
				generateSecretKey(Constants.SERVER_SECRET_KEY_LOCATION);
				menu.set(2, "");
				break;
			case 3:
				readSharedPublicKey();
				menu.set(3, "");
				break;
			case 4:
				sendSecretKey();
				menu.set(4, "");
				menu.set(5, "");
				break;
			case 5:
				receiveSessionKey();
				menu.set(3, "");
				menu.set(4, "");
				menu.set(5, "");
				break;
			case 6:
				putSendMessageToSend(Constants.SERVER_PLAIN_TEXT_LOCATION);
				break;
			case 7:
				digestAndSendMessage(Constants.SERVER_DIGESTED_TEXT_LOCATION);
				break;
			case 8:
				receiveMessageDigested(Constants.SERVER_PLAIN_TEXT_LOCATION,Constants.SERVER_DIGESTED_TEXT_LOCATION);
				break;
			default:
				break;
			}
			finish();
			showMenu();
		}
	}

	private void readSharedPublicKey() {
		this.clientPublicKey = (PublicKey) ApplicationUtil.readSavedKey(Constants.CLIENT_PUBLIC_KEY_LOCATION);
	}

	private void sendSecretKey() {
		try{
			cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
			byte[] encryptedSecretKey = cipher.doFinal(secretKey.getEncoded());
			ApplicationUtil.saveKeyToFileKey(Constants.ENCRYPTED_SESSION_KEY_LOCATION, encryptedSecretKey);
			sessionKey = secretKey;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void generateKeyPair() {
		generatePublicPrivateKey(Constants.SERVER_PUBLIC_KEY_LOCATION,Constants.SERVER_PRIVATE_KEY_LOCATION);
	}

	@Override
	protected void loadMyMenu() {
		menu.add("3.Fetch Client public key");
		menu.add("4.Send secret key to Client");
		menu.add("5.Receive Session key of Client");
		menu.add("6.Input the message to send to Client");
		menu.add("7.Digest and send the given message to Client");
		menu.add("8.Recieve and print message sent by Client");
	}

	public void receiveSessionKey() {
		byte[] encryptedSessionKey = ApplicationUtil.readFileContent(Constants.ENCRYPTED_SESSION_KEY_LOCATION);
		try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte [] decryptedSessionKey = cipher.doFinal(encryptedSessionKey);
			ApplicationUtil.saveKeyToFileKey(Constants.SERVER_SESSION_KEY_LOCATION, decryptedSessionKey);
			sessionKey = new SecretKeySpec(decryptedSessionKey, DES_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}
