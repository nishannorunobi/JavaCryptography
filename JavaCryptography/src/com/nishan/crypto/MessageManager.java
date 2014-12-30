package com.nishan.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class MessageManager {
	private static String DIGEST_ALGORITHM = "SHA-1";
	private static final int DIGESTED_MESSAGE_LENGTH = 20;
	private String sendMessage;
	private byte[] messageStream;
	private byte[] digestedStream;
	private byte[] appendedStream;
	private byte[] encryptedAppendedStream;
	private byte[] digestedReceiveStream;
	private byte[] receivedStream;
	private byte[] checkingDigestSteam;
	private String receiveMessage;
	public MessageManager(String message) {
		this.sendMessage = message;
		messageStream = message.getBytes();
	}

	public MessageManager() {
	}
	
	public void saveDigestedStream(String location){
		ApplicationUtil.saveKeyToFileKey(location, digestedStream);
	}

	public void saveMessageStream(String location){
		ApplicationUtil.saveKeyToFileKey(location, messageStream);
	}

	public void makeDigest(){
		try {
			MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
			digest.update(messageStream);
			digestedStream = digest.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void digestReceiveStream(){
		try {
			MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
			digest.update(receivedStream);
			checkingDigestSteam = digest.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void makeAppend(){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stream.write(digestedStream);
			stream.write(messageStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		appendedStream = stream.toByteArray();
	}

	public void splitMessage(byte[] decryptedText){
		digestedReceiveStream = new byte[DIGESTED_MESSAGE_LENGTH];
		System.arraycopy(decryptedText, 0, digestedReceiveStream, 0, DIGESTED_MESSAGE_LENGTH);

		int messageLen = decryptedText.length - DIGESTED_MESSAGE_LENGTH;
		receivedStream = new byte[messageLen];
		System.arraycopy(decryptedText, DIGESTED_MESSAGE_LENGTH, receivedStream, 0, messageLen);
	}

	public void saveDigestedReceivekStream(String location){
		ApplicationUtil.saveKeyToFileKey(location, digestedReceiveStream);
	}

	public void saveReceivedStream(String location){
		ApplicationUtil.saveKeyToFileKey(location, receivedStream);
	}

	public byte[] getMessageStream() {
		return messageStream;
	}

	public void setMessageStream(byte[] messageStream) {
		this.messageStream = messageStream;
	}

	public void setDigestedStream(byte[] digestedStream) {
		this.digestedStream = digestedStream;
	}

	public byte[] getAppendedStream() {
		return appendedStream;
	}
	
	public byte[] getEncryptedAppendedStream(){
		return encryptedAppendedStream;
	}
	
	public void readEncryptedAppendedStream(String fileName) {
		encryptedAppendedStream = ApplicationUtil.readFileContent(fileName);
	}

	public byte[] getDigestedReceiveStream() {
		return digestedReceiveStream;
	}

	public byte[] getReceivedStream() {
		return receivedStream;
	}

	public byte[] getCheckingDigestStream() {
		return checkingDigestSteam;
	}

	public String getReceivedMessage(){
		return receiveMessage;
	}
	
	public void setSendMessage(String message){
		this.sendMessage = message;
		this.messageStream = message.getBytes();
	}
	
	public void setReceivedMessage(){
		try {
			if(receivedStream != null)
				receiveMessage = new String(receivedStream,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}	
