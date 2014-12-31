package com.nishan.cryptography;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class IntegrityManager{
	private byte[] digest;
	private MessageDigest messageDigest;
	public IntegrityManager() {
		try {
			messageDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}

	public void digest(byte[] message){
		if(messageDigest == null){
			return;
		}
		messageDigest.update(message);
		this.digest = messageDigest.digest();
	}
	
	public byte[] getDigest(){
		return digest;
	}
	
	public boolean isMessageTempered(byte[] receiveDigest){
		if(messageDigest == null){
			return true;
		}
		if(Arrays.equals(digest, receiveDigest)){
			return false;
		}
		return true;
	}
}
