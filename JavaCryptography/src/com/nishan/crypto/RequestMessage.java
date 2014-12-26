package com.nishan.crypto;

import java.io.Serializable;

public class RequestMessage extends Message implements Serializable{

	public RequestMessage(String id, String content) {
		super(id, content);
		
	}


}
