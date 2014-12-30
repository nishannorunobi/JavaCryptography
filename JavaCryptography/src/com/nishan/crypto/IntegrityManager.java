package com.nishan.crypto;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class IntegrityManager {
	public static final String DES_CIPHER_ALGORITHM = "DESede/CBC/PKCS5Padding";
	private Cipher cipher;
	private IvParameterSpec ivParameterSpec;
	private byte[] encryptedMessage;
	private byte[] decryptedMessage;
	private MessageManager messageManager;
	private byte[] receivedDigest;
	private byte[] checkingDigest;

	public IntegrityManager(MessageManager messageManager) {
		this.messageManager = messageManager;
		initialize();
	}

	private void initialize() {
		ivParameterSpec = new IvParameterSpec(new byte[8]);
		try {
			cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void encryptMessage(Key key){
		try {
			//cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key,ivParameterSpec);
			encryptedMessage = cipher.doFinal(messageManager.getAppendedStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void decryptMessage(Key key){
		try {
			//messageManager.readEncryptedAppendedStream(Constants.ENCRYPTED_PLAIN_TEXT_LOCATION);
			//byte[] encryptedAppendedStream = messageManager.getEncryptedAppendedStream();
			//cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key,ivParameterSpec);
			decryptedMessage = cipher.doFinal(encryptedMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void splitDecryptedMessage(){
		if(decryptedMessage == null)
			return;
		messageManager.splitMessage(decryptedMessage);
		receivedDigest = messageManager.getDigestedReceiveStream();
		messageManager.digestReceiveStream();
		checkingDigest = messageManager.getCheckingDigestStream();
	}

	public void saveDigestedReceiveStream(String location){
		messageManager.saveDigestedReceivekStream(location);
	}

	public void saveReceivedStream(String location){
		messageManager.saveReceivedStream(location);
	}

	public boolean isMessageAltered(){
		if(Arrays.equals(receivedDigest, checkingDigest)){
			messageManager.setReceivedMessage();
			return false;
		}
		return true;
	}

	public void send(String fileLocation){
		saveEncryptedMessage(fileLocation);
	}

	public void receive(String fileLocation){
		readEncryptedMessage(fileLocation);
	}
	private void readEncryptedMessage(String fileLocation) {
		encryptedMessage = ApplicationUtil.readFileContent(fileLocation);
	}

	private void saveEncryptedMessage(String location){
		ApplicationUtil.saveKeyToFileKey(location, encryptedMessage);
	}

}
