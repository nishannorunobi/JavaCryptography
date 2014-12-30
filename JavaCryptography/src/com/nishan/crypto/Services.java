package com.nishan.crypto;

public interface Services {
	public void generateSecretKey();
	public void putSendMessageToSend();
	public void readSharedPublicKey();
	public void digestAndSendMessage();
	public void receiveMessageDigested();
	public void sendSecretKey();
	public void receiveSessionKey();
	public void generateKeyPair();
	public void showMenu();
}
