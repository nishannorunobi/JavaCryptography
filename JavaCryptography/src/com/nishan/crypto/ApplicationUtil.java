package com.nishan.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

public final class ApplicationUtil {
	
	

	public static void saveKeyToFileKey(String fileName,byte[] encodedStream){
		byte[] content = encodedStream;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			/*PrintWriter writer = new PrintWriter(fos);
			String str = new BASE64Encoder().encode(content);
			writer.write(str);
			writer.close();*/
			fos.write(encodedStream);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static Key readSavedKey(String fileName){
		Key key = null;
		try{

			FileInputStream fis = new FileInputStream(new File(fileName));
			byte[] keyBytes = new byte[fis.available()];
			fis.read(keyBytes);
			/*String pubKey = new String(keyBytes, "UTF-8");
			BASE64Decoder decoder = new BASE64Decoder();
			keyBytes = decoder.decodeBuffer(pubKey);*/
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			key = keyFactory.generatePublic(spec);
			fis.close();
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
		/*String content = null;
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
		}*/
		return keyBytes;
	}
}
