package com.nishan.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public abstract class Crypto {
	protected PublicKey publicKey;
	protected PrivateKey privateKey;
	protected SecretKey secretKey;
	protected SecretKey sessionKey;
	protected Cipher cipher;
	protected final String RSA_ALGORITHM = "RSA";
	//protected final String RSA_CIPHER_ALGORITHM = "RSA/CBC/PKCS5Padding";
	protected final int RSA_KEY_LENGTH =2048;
	protected final String DES_ALGORITHM = "DES";
	protected final String DES_CIPHER_ALGORITHM = "DESede/CBC/PKCS5Padding";
	protected final int DES_KEY_LENGTH = 56;
	private static Message requestMessage;
	private static Message responseMessage;
	
	protected abstract void run();
	protected abstract void showMyMenu();
	
	protected void showMenu(){
		System.out.println("0.exit");
		System.out.println("1.Generate and save public,private(RSA) keypair");
		System.out.println("2.Generate and save secret(DES) key");
		showMyMenu();
	}
	
	protected void finish(){
		System.out.println("Done");
		System.out.println();
		clearConsole();
	}

	public final static void clearConsole()
	{
	    try
	    {
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e)
	    {
	        //  Handle any exceptions.
	    }
	}
	
	protected void init() {
		cipher = ApplicationUtil.initializeCipher(DES_CIPHER_ALGORITHM);
		showMenu();
	}
}
