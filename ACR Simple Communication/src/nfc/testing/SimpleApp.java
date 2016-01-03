package nfc.testing;

import java.util.Scanner;

public class SimpleApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("What would you like to do?");
		
		System.out.println("1. Add/Remove a new guest");
		System.out.println("2. Add/Redeem cash for a guest");
		System.out.println("3. Bar application");
		System.out.println();
		
		int choice = (new Scanner(System.in)).nextInt();
		
		switch(choice) {
		case 1:
			guestApp();
			break;
		case 2:
			cashApp();
			break;
		case 3:
			barApp();
			break;
		default:
			System.out.println("Unvalid entry. Please try again");
		}
	}
	
	public static void guestApp() {
		System.out.println();
		System.out.println("1. Add a new guest");
		System.out.println("2. Get information on a wristband");
		System.out.println();
		
		int choice = (new Scanner(System.in)).nextInt();
		
		switch(choice) {
		case 1:
			System.out.println("Guest name: ");
			String name = (new Scanner(System.in)).nextLine();

			
			System.out.println("Please place a wristband on the terminal...");
		
		}
	}
	
	public static void cashApp() {
		
	}
	
	public static void barApp() {
		
	}

}
