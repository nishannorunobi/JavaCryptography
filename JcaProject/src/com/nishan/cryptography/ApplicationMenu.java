package com.nishan.cryptography;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.nishan.server.Server;

public class ApplicationMenu {
	protected ArrayList<String> menu;

	public ApplicationMenu() {
		menu = new ArrayList<String>();
	}

	private void loadMenu(String pairName) {
		menu.add("exit");
		menu.add("Generate public key");
		menu.add("Generate private key");
		menu.add("Generate secret key");
		menu.add(String.format("Load %s's public key",pairName));
		menu.add(String.format("Send secret key to %s",pairName));
		menu.add(String.format("Receive secret key of %s \n-------------------",pairName));
		menu.add("Take User input");
		menu.add("Digest given message");
		menu.add("Sign the given message");
		menu.add(String.format("Send message to %s \n-------------------",pairName));
		menu.add(String.format("Receive data packet from %s",pairName));
		menu.add(String.format("Print message sent by %s",pairName));
		menu.add("Check Integrity of received message");
		menu.add("Check Authentication of received message");
	}

	public void showMenu(Services service){
		String pairName = "";
		if(service instanceof Server)
		{			
			pairName = "Client";
		}else{
			pairName = "Server";
		}
		loadMenu(pairName);
		showMenu(menu);
	}

	private void showMenu(List<String> menu){
		int index = 0;
		for(String txt:menu){
			if (txt != null && !txt.isEmpty()){
				System.out.println(index+"."+txt);
			}
			index++;
		}
		//System.err.println("Option : ");
	}

	@SuppressWarnings("resource")
	public void takeUserInput(Services service){
		showMenu(service);
		int option = 100;
		while(option > 0){
			try {
				String input = new Scanner(System.in).nextLine();
				option = new Integer(input);
			} catch (Exception e) {
				option = 100;
				System.out.println("Invalid input,try again!");
				continue;
			}
			if(menu.get(option).equals("")){
				System.out.println("Choose correct option");
				continue;
			}
			switch (option) {
			case 0:
				System.out.println("thanks");
				service.close();
				break;
			case 1:
				service.generatePublicKey();
				menu.set(1, "");
				break;
			case 2:
				service.generatePrivateKey();
				menu.set(2, "");
				break;
			case 3:
				service.generateSecretKey();
				menu.set(3, "");
				break;
			case 4:
				service.getSharedPublicKey();
				menu.set(4, "");
				break;
			case 5:
				service.sendSecretKey();
				menu.set(4, "");
				menu.set(5, "");
				menu.set(6, "");
				break;
			case 6:
				service.receiveSessionKey();
				menu.set(4, "");
				menu.set(5, "");
				menu.set(6, "");
				break;
			case 7:
				service.takeUserInput();
				break;
			case 8:
				service.digestUserInput();
				break;
			case 9:
				service.makeSignature();
				break;
			case 10:
				service.send();
				break;
			case 11:
				service.receiveDataPacket();
				break;
			case 12:
				service.printReceivedMessage();
				break;
			case 13:
				service.checkIntegrity();
				break;
			case 14:
				service.checkIntegrity();
				break;
			default:
				System.err.println("input given among choices");
				break;
			}
			clear();
			showMenu(menu);
		}
	}

	private void clear() {
		System.out.println("Done");
		System.out.println();
		clearConsole();
	}

	public final static void clearConsole()
	{
		try
		{
			System.out.flush();
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
}
