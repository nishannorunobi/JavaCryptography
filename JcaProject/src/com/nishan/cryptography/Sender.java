package com.nishan.cryptography;

import javax.crypto.SecretKey;

public class Sender {
	private DataPacket dataPacket;
	private CredentialManager credentialManager;
	public Sender(DataPacket dataPacket) {
		this.dataPacket = dataPacket;
	}

	public void appendMessageAndDigest(){
		byte[] appended = ApplicationUtil.makeAppend(dataPacket.getDigestedStream(), dataPacket.getMessageStream());
		dataPacket.setMessageDigestStream(appended);
	}

	public void encryptMessageAndDigest(SecretKey sessionKey){
		byte[] messageDigestStream = dataPacket.getMessageDigestStream();
		credentialManager = new CredentialManager(sessionKey);
		byte[] encryptedMessage = credentialManager.encrypt(messageDigestStream);
		dataPacket.setEncryptedMessageDigestStream(encryptedMessage);
		dataPacket.setPacketStream(encryptedMessage);
	}

	public void attachSignature(){
		byte[] appended = ApplicationUtil.makeAppend(dataPacket.getEncryptedMessageDigestStream(), dataPacket.getSignatureStream());
		dataPacket.setPacketStream(appended);
	}
	public void send(String fileName){
		write(fileName);
	}

	private void write(String fileName){
		ApplicationUtil.saveKeyToFileKey(fileName, dataPacket.getPacketStream());
	}

}
