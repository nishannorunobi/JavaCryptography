package com.nishan.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public final class ApplicationUtil {

	public static void saveKeyToFileKey(String fileName,byte[] encodedStream){
		try {
			byte[] content = encodedStream;
			FileOutputStream fos = null;
			fos = new FileOutputStream(fileName);
			//fos.write(encodedStream);
			PrintWriter writer = new PrintWriter(fos);
			String str = new BASE64Encoder().encode(content);
			writer.write(str);
			writer.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static Key readSavedKey(String fileName){
		Key key = null;
		try{

			FileInputStream fis = new FileInputStream(new File(fileName));
			byte[] keyBytes = new byte[fis.available()];
			fis.read(keyBytes);
			String pubKey = new String(keyBytes, "UTF-8");
			BASE64Decoder decoder = new BASE64Decoder();
			keyBytes = decoder.decodeBuffer(pubKey);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(Crypto.RSA_ALGORITHM);
			key = keyFactory.generatePublic(spec);
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return key;
	}

	public static byte[] readFileContent(String fileName){
		byte[] keyBytes = null;
		try {
			FileInputStream fis = null;
			fis = new FileInputStream(new File(fileName));
			keyBytes = new byte[fis.available()];
			fis.read(keyBytes);

			String content = new String(keyBytes, "UTF-8");
			BASE64Decoder decoder = new BASE64Decoder();
			keyBytes = decoder.decodeBuffer(content);

			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyBytes;
	}
}
