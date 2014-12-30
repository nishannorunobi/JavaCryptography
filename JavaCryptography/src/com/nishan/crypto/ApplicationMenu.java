package com.nishan.crypto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicationMenu {
	protected ArrayList<String> menu;
	public ApplicationMenu() {
		menu = new ArrayList<String>();
	}

	private void loadMenu(String pairName) {

		menu.add("0.exit");
		menu.add("1.Generate and save public,private(RSA) keypair");
		menu.add("2.Generate and save secret(DES) key");
		menu.add(String.format("3.Fetch %s public key",pairName));
		menu.add(String.format("4.Send secret key to %s",pairName));
		menu.add(String.format("5.Receive Session key of %s",pairName));
		menu.add(String.format("6.Input the message to send to %s",pairName));
		menu.add(String.format("7.Digest and send the given message to %s",pairName));
		menu.add(String.format("8.Recieve and print message sent by %s",pairName));
	}

	public void showClientMenu() {
		String pairName = "Server";
		loadMenu(pairName);
		show(menu);
	}
	
	public void showServerMenu() {
		String pairName = "Client";
		loadMenu(pairName);
		show(menu);
	}	
	
	private void show(List<String> menu){
		for(String txt:menu){
			if(txt != null || txt.length() > 0)
			System.out.println(txt);
		}
	}
	
	public void takeUserInput(Services service){
		service.showMenu();
		int option = 100;
		while(option > 0){
			try {
				String input = new Scanner(System.in).nextLine();
				option = new Integer(input);
			} catch (Exception e) {
				option = 100;
				System.out.println("Invalid input,try again!");
			}
			switch (option) {
			case 0:
				System.out.println("thanks");
				break;
			case 1:
				service.generateKeyPair();
				menu.set(1, "");
				break;
			case 2:
				service.generateSecretKey();
				menu.set(2, "");
				break;
			case 3:
				service.readSharedPublicKey();
				menu.set(3, "");
				break;
			case 4:
				service.sendSecretKey();
				menu.set(4, "");
				menu.set(5, "");
				break;
			case 5:
				service.receiveSessionKey();
				menu.set(3, "");
				menu.set(4, "");
				menu.set(5, "");
				break;
			case 6:
				service.putSendMessageToSend();
				break;
			case 7:
				service.digestAndSendMessage();
				break;
			case 8:
				service.receiveMessageDigested();
				break;
			default:
				break;
			}
			ApplicationUtil.done();
			show(menu);
		}
	}
}
