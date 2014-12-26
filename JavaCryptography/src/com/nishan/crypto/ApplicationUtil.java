package com.nishan.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public final class ApplicationUtil {
	
	public static Key[] get(){
		KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		keyGen.initialize(1024);
		KeyPair keyPair = keyGen.genKeyPair();
		PublicKey p = keyPair.getPublic();
		PrivateKey pr = keyPair.getPrivate();
		//Key[] keyArray = 
		return new Key[]{p,pr};
	}
	
	public static PrivateKey generatePrivateKey(String keyAlgorithm, int numBits){
		KeyPair keyPair = getKeyPair(keyAlgorithm,numBits);
		PrivateKey privKey = keyPair.getPrivate();
		return privKey;
	}

	public static PublicKey generatePublicKey(String keyAlgorithm, int    numBits){
		KeyPair keyPair = getKeyPair(keyAlgorithm,numBits);
		PublicKey pubKey = keyPair.getPublic();
		return pubKey;
	}

	private static KeyPair getKeyPair(String keyAlgorithm, int numBits){
		KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance(keyAlgorithm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		keyGen.initialize(numBits);
		KeyPair keyPair = keyGen.genKeyPair();
		return keyPair;
	}

	public static void saveKeyToFileKey(String fileName,byte[] encodedStream){
		byte[] content = encodedStream;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			PrintWriter writer = new PrintWriter(fos);
			String str = new BASE64Encoder().encode(content);
			writer.write(str);
			writer.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*public static void saveKeyToFileKey(String fileName,Key key){
		byte[] content = key.getEncoded();
		saveKeyToFileKey(fileName,content);
	}*/

	public static SecretKey generateSecretKey(String algorithmName, int keysize) {
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(algorithmName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		keyGenerator.init(keysize);
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey;
	}

	public static Cipher initializeCipher(String algorithmName) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return cipher;
	}
	public static Key readSavedKey(String fileName){
		Key key = null;
		try{

			FileInputStream fis = new FileInputStream(new File(fileName));
			byte[] keyBytes = new byte[fis.available()];
			fis.read(keyBytes);
			fis.close();
			String pubKey = new String(keyBytes, "UTF-8");
			BASE64Decoder decoder = new BASE64Decoder();
			keyBytes = decoder.decodeBuffer(pubKey);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			key = keyFactory.generatePublic(spec);
		}catch(Exception e){
			e.printStackTrace();
		}
		return key;
	}
	
	public static byte[] readFileContent(String fileName){
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] keyBytes = null;
		try {
			keyBytes = new byte[fis.available()];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fis.read(keyBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String content = null;
		try {
			content = new String(keyBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			keyBytes = decoder.decodeBuffer(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keyBytes;
	}
}
