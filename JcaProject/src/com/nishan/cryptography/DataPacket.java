package com.nishan.cryptography;


public class DataPacket {
	
	private String message;
	private byte[] messageStream;
	private byte[] encryptedMessageStream;
	private byte[] digestedStream;
	private byte[] messageDigestStream;
	private byte[] encryptedMessageDigestStream;
	private byte[] signatureStream;
	private byte[] packetStream;
	private byte[] encryptedPacketStream;
	
	public DataPacket() {
		
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public byte[] getMessageStream() {
		return messageStream;
	}
	public void setMessageStream(byte[] messageStream) {
		this.messageStream = messageStream;
	}
	public byte[] getEncryptedMessageStream() {
		return encryptedMessageStream;
	}
	public void setEncryptedMessageStream(byte[] encryptedMessageStream) {
		this.encryptedMessageStream = encryptedMessageStream;
	}
	public byte[] getDigestedStream() {
		return digestedStream;
	}
	public void setDigestedStream(byte[] digestedStream) {
		this.digestedStream = digestedStream;
	}
	public byte[] getMessageDigestStream() {
		return messageDigestStream;
	}
	public void setMessageDigestStream(byte[] messageDigestStream) {
		this.messageDigestStream = messageDigestStream;
	}
	public byte[] getEncryptedMessageDigestStream() {
		return encryptedMessageDigestStream;
	}
	public void setEncryptedMessageDigestStream(byte[] encryptedMessageDigestStream) {
		this.encryptedMessageDigestStream = encryptedMessageDigestStream;
	}
	public byte[] getSignatureStream() {
		return signatureStream;
	}
	public void setSignatureStream(byte[] signatureStream) {
		this.signatureStream = signatureStream;
	}
	public byte[] getPacketStream() {
		return packetStream;
	}
	public void setPacketStream(byte[] packetStream) {
		this.packetStream = packetStream;
	}
	public byte[] getEncryptedPacketStream() {
		return encryptedPacketStream;
	}
	public void setEncryptedPacketStream(byte[] encryptedPacketStream) {
		this.encryptedPacketStream = encryptedPacketStream;
	}
	
}
