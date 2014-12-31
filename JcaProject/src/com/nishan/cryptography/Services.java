package com.nishan.cryptography;

public interface Services {
	public void execute();
	
	public void generatePublicKey();
	public void generatePrivateKey();
	public void generateSecretKey();
	public void getSharedPublicKey();
	public void sendSecretKey();
	public void receiveSessionKey();
	
	public void takeUserInput();
	public void digestUserInput();
	public void makeSignature();
	public void send();
	
	public void receiveDataPacket();
	public void printReceivedMessage();
	public void checkIntegrity();
	public void checkAuthentication();
	
	public void showMenu(ApplicationMenu menu);
	public void close();
}
