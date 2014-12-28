package com.nishan.application.client;

import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.nishan.crypto.ApplicationUtil;
import com.nishan.crypto.Constants;
import com.nishan.crypto.Crypto;

public final class Client extends Crypto{
	protected PublicKey serverPublicKey;
	public Client() {

	}

	public static void main(String[] args) {
		new Client().run();
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
				generateSecretKey(Constants.CLIENT_SECRET_KEY_LOCATION);
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
				sendMessageToServer();
				break;
			case 7:
				receiveMessageFromServer();
			default:
				break;
			}
			finish();
			showMenu();
		}
	}

	private void sendMessageToServer() {
		try {
			cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
			System.out.print("type :");
			String srt = new Scanner(System.in).nextLine();
			byte [] encodedStream = srt.getBytes();
			ApplicationUtil.saveKeyToFileKey(Constants.CLIENT_PLAIN_TEXT_LOCATION, encodedStream);
			if(sessionKey != null){
				cipher.init(Cipher.ENCRYPT_MODE, sessionKey,ivParameterSpec);
			}
			byte[] encryptedText = cipher.doFinal(encodedStream);
			ApplicationUtil.saveKeyToFileKey(Constants.ENCRYPTED_PLAIN_TEXT_LOCATION, encryptedText);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void receiveMessageFromServer() {
		try {
			cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
			byte[] encryptedText = ApplicationUtil.readFileContent(Constants.ENCRYPTED_PLAIN_TEXT_LOCATION);
			if(sessionKey != null){
				cipher.init(Cipher.DECRYPT_MODE, sessionKey,ivParameterSpec);
			}
			byte[] decryptedText = cipher.doFinal(encryptedText);
			String str = new String(decryptedText,"UTF-8");
			System.out.print("received text: ");System.err.println(str);
			ApplicationUtil.saveKeyToFileKey(Constants.CLIENT_PLAIN_TEXT_LOCATION, decryptedText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readSharedPublicKey() {
		this.serverPublicKey = (PublicKey) ApplicationUtil.readSavedKey(Constants.SERVER_PUBLIC_KEY_LOCATION);
	}

	private void sendSecretKey() {
		try{
			cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
			byte[] encryptedSecretKey = cipher.doFinal(secretKey.getEncoded());
			ApplicationUtil.saveKeyToFileKey(Constants.ENCRYPTED_SESSION_KEY_LOCATION, encryptedSecretKey);
			sessionKey = secretKey;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void receiveSessionKey() {
		byte[] encryptedSessionKey = ApplicationUtil.readFileContent(Constants.ENCRYPTED_SESSION_KEY_LOCATION);
		try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte [] decryptedSessionKey = cipher.doFinal(encryptedSessionKey);
			ApplicationUtil.saveKeyToFileKey(Constants.CLIENT_SESSION_KEY_LOCATION, decryptedSessionKey);
			sessionKey = new SecretKeySpec(decryptedSessionKey, DES_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateKeyPair() {
		generatePublicPrivateKey(Constants.CLIENT_PUBLIC_KEY_LOCATION,Constants.CLIENT_PRIVATE_KEY_LOCATION);
	}

	@Override
	protected void loadMyMenu() {
		menu.add("3.Fetch Server public key");
		//System.out.println("3.Fetch Server public key");
		menu.add("4.Send secret key to Server");
		//System.out.println("4.Send secret key to Server");
		menu.add("5.Receive Session key of Client");
		//System.out.println("5.Receive Session key of Client");
		menu.add("6.Type and hit ENTER_KEY to send message to Server");
		//System.out.println("6.Type and hit ENTER_KEY to send message to Server");
		menu.add("7.Print message sent by Server");
		//System.out.println("7.Print message sent by Server");
	}
}