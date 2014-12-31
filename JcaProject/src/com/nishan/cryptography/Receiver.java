package com.nishan.cryptography;

import java.io.UnsupportedEncodingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

public class Receiver {
	private DataPacket dataPacket;
	private CredentialManager credentialManager;
	private static final int DIGESTED_MESSAGE_LENGTH = 20;
	public Receiver() {
		dataPacket = new DataPacket();
	}

	public void receive(String fileName){
		read(fileName);
	}

	private void read(String fileName){
		byte[] dataPacketStream = ApplicationUtil.readFileContent(fileName);
		dataPacket.setEncryptedMessageDigestStream(dataPacketStream); // will remove for authentication
		dataPacket.setPacketStream(dataPacketStream);
	}


	public void seperateSignature(){
		byte[] encrypteddDgestAndMessage = dataPacket.getPacketStream();
		dataPacket.setEncryptedMessageDigestStream(encrypteddDgestAndMessage);
	}


	public void decryptMessageAndDigest(SecretKey sessionKey) throws IllegalBlockSizeException, BadPaddingException{
		byte[] encryptedMessageDigest = dataPacket.getEncryptedMessageDigestStream();
		credentialManager = new CredentialManager(sessionKey);
		byte[] decryptedDigestMessage = credentialManager.decrypt(encryptedMessageDigest);
		dataPacket.setMessageDigestStream(decryptedDigestMessage);
	}

	public void splitMessageAndDigest(){
		byte[] decryptedText = dataPacket.getMessageDigestStream();
		byte[] digestedReceiveStream = new byte[DIGESTED_MESSAGE_LENGTH];
		System.arraycopy(decryptedText, 0, digestedReceiveStream, 0, DIGESTED_MESSAGE_LENGTH);
		dataPacket.setDigestedStream(digestedReceiveStream);
		int messageLen = decryptedText.length - DIGESTED_MESSAGE_LENGTH;
		byte[] receivedStream = new byte[messageLen];
		System.arraycopy(decryptedText, DIGESTED_MESSAGE_LENGTH, receivedStream, 0, messageLen);
		dataPacket.setMessageStream(receivedStream);
		try {
			dataPacket.setMessage(new String(receivedStream,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public DataPacket getDataPacket(){
		return dataPacket;
	}
}
