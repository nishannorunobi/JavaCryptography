package com.nishan.application.server;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import com.nishan.crypto.ApplicationUtil;
import com.nishan.crypto.Constants;
import com.nishan.crypto.Crypto;

public final class Server extends Crypto{
	protected PublicKey clientPublicKey;
	
	public Server() {
		
	}

	public static void main(String[] args) {
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
				break;
			case 2:
				generateSecretKey(Constants.SERVER_SECRET_KEY_LOCATION);
				break;
			case 5:
				receiveSessionKey();
				break;
			default:
				break;
			}
			finish();
			showMenu();
		}
	}

	private void generateKeyPair() {
		generatePublicPrivateKey(Constants.SERVER_PUBLIC_KEY_LOCATION,Constants.SERVER_PRIVATE_KEY_LOCATION);
	}

	@Override
	protected void showMyMenu() {
		System.out.println("3.Fetch Client public key");
		System.out.println("4.Send secret key to Client");
		System.out.println("5.Receive Session key of Client");
		System.out.println("6.Type and hit ENTER_KEY to send message to Client");
		System.out.println("7.Print message sent by Client");
	}

	public void receiveSessionKey() {
		byte[] encryptedSessionKey = ApplicationUtil.readFileContent(Constants.ENCRYPTED_SESSION_KEY_LOCATION);
		try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		
		try {
			byte [] decryptedSessionKey = cipher.doFinal(encryptedSessionKey);
			ApplicationUtil.saveKeyToFileKey(Constants.SERVER_SESSION_KEY_LOCATION, decryptedSessionKey);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
	}

}
