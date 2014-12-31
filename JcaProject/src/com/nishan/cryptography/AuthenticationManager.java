package com.nishan.cryptography;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class AuthenticationManager{
	private Signature signature;
	private byte[] signedMessage;
	public AuthenticationManager() {
		try {
			signature = Signature.getInstance("SHA1withRSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void encrypt(PrivateKey privateKey, byte[] message){
		try {
			signature.initSign(privateKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			signature.update(message);
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		try {
			signedMessage = signature.sign();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
	}
	
	public void decrypt(PublicKey publicKey,byte[] message){
		try {
			signature.initVerify(publicKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			signature.update(message);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean virifySignature(byte[] signedData){
		boolean flag = false;
		try {
			flag = signature.verify(signedData);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	

	public byte[] getSignedMessage() {
		return signedMessage;
	}

}
