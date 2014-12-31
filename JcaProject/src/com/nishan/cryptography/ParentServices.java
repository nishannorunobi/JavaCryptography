package com.nishan.cryptography;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

public abstract class ParentServices {
	protected PublicKey publicKey;
	protected PrivateKey privateKey;
	protected SecretKey secretKey;
	protected SecretKey sessionKey;
	//protected SecretKeyManager secretKeyManager;
	//protected IntegrityManager integrityManager;
	protected PublicPrivateKeyGenerator keyPairGenerator;
	//protected AuthenticationManager authenticationManager;
	protected Sender sender;
	protected DataPacket senddata;
	protected DataPacket receiveData;
	protected ApplicationMenu menu;
	public static final String SERVER_PUBLIC_KEY_LOCATION = "..\\JcaProject\\common\\server_public.key";
	public static final String CLIENT_PUBLIC_KEY_LOCATION = "..\\JcaProject\\common\\client_public.key";
	public static final String ENCRYPTED_SESSION_KEY_LOCATION = "..\\JcaProject\\common\\encrypted_session.key";
	public static final String ENCRYPTED_PLAIN_TEXT_LOCATION = "..\\JcaProject\\common\\encrypted_plain.txt";
}
