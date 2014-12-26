package com.nishan.application.client;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
				break;
			case 2:
				generateSecretKey(Constants.CLIENT_SECRET_KEY_LOCATION);
				break;
			case 3:
				readSharedPublicKey();
				break;
			case 4:
				sendSecretKey();
				break;
			default:
				break;
			}
			finish();
			showMenu();
		}
	}

	private void readSharedPublicKey() {
		this.serverPublicKey = (PublicKey) ApplicationUtil.readSavedKey(Constants.SERVER_PUBLIC_KEY_LOCATION);
	}

	private void sendSecretKey() {
		try{
		try {
			cipher = Cipher.getInstance(RSA_ALGORITHM);
		} catch (NoSuchPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			byte[] encryptedSecretKey = cipher.doFinal(secretKey.getEncoded());
			ApplicationUtil.saveKeyToFileKey(Constants.ENCRYPTED_SESSION_KEY_LOCATION, encryptedSecretKey);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}catch(NoSuchAlgorithmException e){
			System.out.println("Error");
		}
	}

	private void generateKeyPair() {
		generatePublicPrivateKey(Constants.CLIENT_PUBLIC_KEY_LOCATION,Constants.CLIENT_PRIVATE_KEY_LOCATION);
	}

	@Override
	protected void showMyMenu() {
		System.out.println("3.Fetch Server public key");
		System.out.println("4.Send secret key to Server");
		System.out.println("5.Receive Session key of Client");
		System.out.println("6.Type and hit ENTER_KEY to send message to Server");
		System.out.println("7.Print message sent by Server");
	}
}