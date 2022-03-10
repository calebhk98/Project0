package com.revature;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.revature.account.Account;
import com.revature.exception.AccountHasValueException;
import com.revature.exception.MaxSecurityLevelException;
import com.revature.exception.MinSecurityLevelException;
import com.revature.exception.NegativeBalanceException;
import com.revature.exception.NegativeValueInputException;
import com.revature.exception.NotEnoughAccountsException;
import com.revature.services.AccountService;
import com.revature.services.UserService;
import com.revature.user.User;



public class Driver {

	static User currentUser=null;
	private static final Scanner input=new Scanner(System.in);
	private static UserService uservice=new UserService();
	private static AccountService aservice=new AccountService();
	//This prevents the IDE from complaining from not closing the scanner
	//Don't close the scanner, as it closes the System.in, JVM will close that
	
	public static void main(String[] args) {
			menu();
	}
			
	public static void login() {		
		System.out.printf("Type a username:");
		String userName=input.nextLine();
		System.out.printf("Type a password:");
		String password=input.nextLine();
		
		
		currentUser=User.Login(userName, password);	
		if(currentUser==null) {
			System.out.println("Wrong username password combination. Try again");
			login();
		}

		returnToMenu();
		
		
			
		
	}
	public static void accountCreation() {
		
		System.out.print("Type a username:");
		String userName=input.nextLine();
//		String userName="calebhk98";
		System.out.printf("Type a password:");
		String password=input.nextLine();
//		String password="hunter98";
		System.out.printf("Type your first name:");
		String fname=input.nextLine();
//		String fname="Caleb";
		System.out.printf("Type your last name:");
		String lname=input.nextLine();
//		String lname="Kirschbaum";
		System.out.printf("Type your address, or q to skip:");
		String address=input.nextLine();
//		String address="1238 screamer rd";
		System.out.printf("Type a phone number, or q to skip:");
		String phoneNumber=input.nextLine();
//		String phoneNumber="931-797-7141";
		boolean properDate=false;
		LocalDate birthDate = null;
		String dob="q";
		List<DateTimeFormatter> formats=new ArrayList<>();	
		formats.add(DateTimeFormatter.ofPattern("M/dd/yyyy"));
		formats.add(DateTimeFormatter.ofPattern("M/dd/yy"));
		formats.add(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		formats.add(DateTimeFormatter.ofPattern("MM/dd/yy"));
		formats.add(DateTimeFormatter.ofPattern("MM/d/yyyy"));
		formats.add(DateTimeFormatter.ofPattern("MM/d/yy"));
		formats.add(DateTimeFormatter.ofPattern("MMM dd yyyy"));
		formats.add(DateTimeFormatter.ofPattern("MMM dd yy"));
		formats.add(DateTimeFormatter.ofPattern("MMM d yyyy"));
		formats.add(DateTimeFormatter.ofPattern("MMM d yy"));
		
		
		
		
//		do {
			properDate=false;
			while(!properDate) {
				System.out.printf("Type your date of birth (mm/dd/yyyy), or q to skip:");
				dob=input.nextLine();
				if(!dob.toLowerCase().equals("q")) {				
					int trys=0;
					do {
						try {
							birthDate= LocalDate.parse(dob, formats.get(trys));
							properDate=true;
							System.out.println("Date choosen: "+birthDate.format(formats.get(6)));
						} catch (DateTimeParseException e) {
							trys++;

							//System.out.println("Trys: "+trys);
							if(trys>=formats.size()) {
								
								System.out.println("Date was improper, try again");
								break;
							}
						}	
					}while(!properDate);
						
				}
				else {
					break;
				}
			}
		
		
		System.out.printf("Type an email, or q to skip:");
		String email=input.nextLine();
//		String email="calebhk98@gmail.com";
		
		System.out.println("Choose an access level:");
		System.out.println("1. Customer");
		System.out.println("2. Employee");
		System.out.println("3. Admin");
		String s=input.nextLine();
		int choice;
		try {
			choice=Integer.parseInt(s);
			choice--;
		}
		catch(Exception e){
			choice=0;
		}
		User bob;
		
		if(choice>2 || choice<0) {
			choice=0;
		}
		bob =new User(userName,0);
		bob.setPassword(password);
		
		bob.setFirstName(fname);
		bob.setLastName(lname);	
		if(!email.toLowerCase().equals("q")) {
			bob.setEmail(email);
		}
		if(!phoneNumber.toLowerCase().equals("q")){
			bob.setPhoneNumber(phoneNumber);			
		}		
		if(!address.toLowerCase().equals("q")) {
			bob.setAddress(address);			
		}
		if(!dob.toLowerCase().equals("q")) {				
			bob.setDob(birthDate);						
		}
		Account acc=new Account(bob);
		bob.addAcount(acc);
		bob.createNew();
		
		
		//bob=bob.createNew(bob);
		System.out.println("Your account is being processed. Please come back later to log in."
				+ "\nPress enter to go back to the main menu.");
		input.nextLine();
		menu();
	}
	public static void details() {
		System.out.println("       Details       ");
		System.out.println("---------------------");
		System.out.println("Creator: Caleb Kirschbaum");
		System.out.println("Project Name: Project 0, Banking Application");
		System.out.println("Original date made: 2/17/21");
		System.out.println("Last Update: 2/26/21");
		System.out.println("Press enter to return to the main menu.");
		input.nextLine();
		menu();
		
	}
	public static void menu() {
		System.out.println("       Main Menu       ");
		System.out.println("-----------------------");
		System.out.println("1. Login");
		System.out.println("2. Request an account");
		System.out.println("3. Details");
		String s="2";
		int choice;
		try {
			s = input.nextLine();
			choice=Integer.parseInt(s);
		}
		catch(Exception e){
			choice=0;
		}
		switch(choice) {
		case 1:
			login();
			break;
		case 2:
			accountCreation();
			break;
		case 3:
			details();
			break;
		default:
			System.out.println("That's not a choice. Try again.");
			menu();
		}
	}
	public static void cutomerMenu() {
		System.out.println("       Customer Menu       ");
		System.out.println("---------------------------");
		System.out.println("1. Withdraw");
		System.out.println("2. Deposit");
		System.out.println("3. Transfer");
		System.out.println("4. View Profile info");
		System.out.println("5. View Account info");
		System.out.println("6. Main Menu");
		String s="2";
		int choice;
		try {
			s = input.nextLine();
			choice=Integer.parseInt(s);
		}
		catch(Exception e){
			choice=0;
		}
		switch(choice) {
		case 1:
			withdrawMenu();
			break;
		case 2:
			depositMenu();
			break;
		case 3:
			transferMenu();
			break;
		case 4:
			profileMenu();
			break;
		case 5:
			accountMenu();
			break;
		case 6:
			menu();
			break;
		default:
			System.out.println("That's not a choice. Try again.");
			cutomerMenu();
		}
		
		
	}
	public static void employeeMenu() {
		System.out.println("       Employee Menu       ");
		System.out.println("---------------------------");
		System.out.println("1. Withdraw");
		System.out.println("2. Deposit");
		System.out.println("3. Transfer");
		System.out.println("4. View Profile info");
		System.out.println("5. View Account info");
		System.out.println("6. Approve/Deny");
		System.out.println("7. Main Menu");
		String s="2";
		int choice;
		try {
			s = input.nextLine();
			choice=Integer.parseInt(s);
		}
		catch(Exception e){
			choice=0;
		}
		switch(choice) {
		case 1:
			withdrawMenu();
			break;
		case 2:
			depositMenu();
			break;
		case 3:
			transferMenu();
			break;
		case 4:
			profileMenu();
			break;
		case 5:
			accountMenu();
			break;
		case 6:
			approveMenu();
			break;
		case 7:
			menu();
			break;
		default:
			System.out.println("That's not a choice. Try again.");
			employeeMenu();
		}
		
		
	}
	public static void adminMenu() {
		System.out.println("       Admin Menu       ");
		System.out.println("------------------------");
		System.out.println("1. Withdraw");
		System.out.println("2. Deposit");
		System.out.println("3. Transfer");
		System.out.println("4. View Profile info");
		System.out.println("5. View Account info");
		System.out.println("6. Approve/Deny");
		System.out.println("7. Cancel Menu");
		System.out.println("8. Main Menu");
		String s="2";
		int choice;
		try {
			s = input.nextLine();
			choice=Integer.parseInt(s);
		}
		catch(Exception e){
			choice=0;
		}
		switch(choice) {
		case 1:
			withdrawMenu();
			break;
		case 2:
			depositMenu();
			break;
		case 3:
			transferMenu();
			break;
		case 4:
			profileMenu();
			break;
		case 5:
			accountMenu();
			break;
		case 6:
			approveMenu();
			break;
		case 7:
			cancelMenu();
			break;
		case 8:
			menu();
			break;
		default:
			System.out.println("That's not a choice. Try again.");
			adminMenu();
		}
		
		
	}
	
	public static void returnToMenu() {
		switch(currentUser.getAccessLevel()) {
		case 0:cutomerMenu(); break;
		case 1:employeeMenu(); break;
		case 2:adminMenu(); break;
		}
	}

	public static void withdrawMenu() {
		System.out.println("       Withdraw Menu       ");
		System.out.println("------------------------");
		User UserWithdrawing=currentUser;
		int accountWithdrawingID;
		String s="2";
		if(currentUser.getAccessLevel()==2) {
			
			System.out.println("1. Withdraw from personal account");		
			System.out.println("2. Withdraw from separate account");			
			System.out.println("3. Back");
			
			
			int choice;
			try {
				s = input.nextLine();
				choice=Integer.parseInt(s);
			}
			catch(Exception e){
				choice=0;
			}
			switch(choice) {
			case 1:
				break;
			case 2:		
				do {
					System.out.printf("Withdraw ID (q to quit to your personal account): ");				
					try {
						s = input.nextLine();
						if(s.toLowerCase().equals("q")) {
							UserWithdrawing=currentUser;
							break;
						}
						UserWithdrawing=uservice.findById(Integer.parseInt(s));
					}
					catch(NumberFormatException e){
						System.out.println("Not a number");
						UserWithdrawing=null;
					}
				}while(UserWithdrawing==null);
				break;		
			case 3:				
				returnToMenu();
				break;	
			default:
				System.out.println("That's not a choice. Try again.");
				withdrawMenu();
			}
		}
		//System.out.println(UserWithdrawing.toString());
		Account withdrawlAccount=null;
		while(withdrawlAccount==null) {

			System.out.println("Account to withdraw from: ");
			int i=0;
			for(Account acc : UserWithdrawing.getAccounts()) {
				System.out.println(i+". AccountID: "+acc.getID()+" Balance: $"+acc.getBalance());
				i++;
			}
			
			try {				
				s = input.nextLine();
				accountWithdrawingID=Integer.parseInt(s);
				withdrawlAccount=UserWithdrawing.getAccounts().get(accountWithdrawingID);
				 		
			}
			catch(NumberFormatException e){
				System.out.println("Not a number");
			}
			catch(IndexOutOfBoundsException e){
				System.out.println("Not a valid account");
			}
			
		}
		double amount=-1;
		while(amount<0) {
			System.out.printf("Amount to withdraw: ");					
			try {
				s = input.nextLine();
				amount=Double.parseDouble(s);
				withdrawlAccount.withdraw(amount);
				withdrawlAccount.update();
				amount=Math.floor(amount*100.0)/100.0;
				System.out.println("Withdrew $"+amount+" from account "+withdrawlAccount.getID()+" leaving the balance at $"
						+ withdrawlAccount.getBalance());
			} catch (NumberFormatException e) {
				System.out.println("That's not a number");
				amount=-1;
			} catch (NegativeBalanceException e) {
				System.out.println("Balance is only $"+withdrawlAccount.getBalance()+".");
				System.out.println("Withdrawing $"+amount+" would make you go negative.");
				amount=-1;
			} catch (NegativeValueInputException e) {
				System.out.println("Can't take negative money from your account");
				amount=-1;
			}
			
		}
		returnToMenu();
		
	}
	public static void depositMenu() {
		System.out.println("       Deposit Menu       ");
		System.out.println("--------------------------");
		User UserDepositting=currentUser;
		int accountDeposittingID;
		String s="2";
		if(currentUser.getAccessLevel()==2) {
			
			System.out.println("1. Deposit to personal account");		
			System.out.println("2. Deposit to separate account");			
			System.out.println("3. Back");			
			int choice;
			try {
				s = input.nextLine();
				choice=Integer.parseInt(s);
			}
			catch(Exception e){
				choice=0;
			}
			switch(choice) {
			case 1:
				break;
			case 2:		
				do {
					System.out.printf("Deposit ID (q to quit to your personal account): ");				
					try {
						s = input.nextLine();
						if(s.toLowerCase().equals("q")) {
							UserDepositting=currentUser;
							break;
						}
						UserDepositting=uservice.findById(Integer.parseInt(s));
					}
					catch(NumberFormatException e){
						System.out.println("Not a number");
						UserDepositting=null;
					}
				}while(UserDepositting==null);
				break;		
			case 3:				
				returnToMenu();
				break;	
			default:
				System.out.println("That's not a choice. Try again.");
				depositMenu();
			}
		}
		//System.out.println(UserWithdrawing.toString());
		Account depositAccount=null;
		while(depositAccount==null) {
	
			System.out.println("Account to deposit to: ");
			int i=0;
			for(Account acc : UserDepositting.getAccounts()) {
				System.out.println(i+". AccountID: "+acc.getID()+" Balance: $"+acc.getBalance());
				i++;
			}			
			try {				
				s = input.nextLine();
				accountDeposittingID=Integer.parseInt(s);
				depositAccount=UserDepositting.getAccounts().get(accountDeposittingID);				 		
			}
			catch(NumberFormatException e){
				System.out.println("Not a number");
			}
			catch(IndexOutOfBoundsException e){
				System.out.println("Not a valid account");
			}			
		}
		double amount=-1;
		while(amount<0) {
			System.out.printf("Amount to deposit: ");					
			try {
				s = input.nextLine();
				amount=Double.parseDouble(s);
				depositAccount.deposit(amount);
				depositAccount.update();
				amount=Math.floor(amount*100.0)/100.0;
				System.out.println("Depositted $"+amount+" to account "+depositAccount.getID()+" leaving the balance at $"
						+ depositAccount.getBalance());
			} catch (NumberFormatException e) {
				System.out.println("That's not a number");
				amount=-1;
			} catch (NegativeValueInputException e) {
				System.out.println("Can't deposit negative values");
				amount=-1;
			}
			
		}
		returnToMenu();
	
}
	public static void transferMenu() {
		System.out.println("       Transfer Menu       ");
		System.out.println("---------------------------");
		User UserTransfer=currentUser;
		int accountTransferID;
		String s="2";
		if(currentUser.getAccessLevel()==2) {
			
			System.out.println("1. Transfer from personal accounts");		
			System.out.println("2. Transfer from separate account");			
			System.out.println("3. Back");			
			int choice;
			try {
				s = input.nextLine();
				choice=Integer.parseInt(s);
			}
			catch(Exception e){
				choice=0;
			}
			switch(choice) {
			case 1:
				break;
			case 2:		
				do {
					System.out.printf("Transfer ID (q to quit to your personal account): ");				
					try {
						s = input.nextLine();
						if(s.toLowerCase().equals("q")) {
							UserTransfer=currentUser;
							break;
						}
						UserTransfer=uservice.findById(Integer.parseInt(s));
					}
					catch(NumberFormatException e){
						System.out.println("Not a number");
						UserTransfer=null;
					}
				}while(UserTransfer==null);
				break;		
			case 3:				
				returnToMenu();
				break;	
			default:
				System.out.println("That's not a choice. Try again.");
				transferMenu();
			}
		}
		//System.out.println(UserWithdrawing.toString());
		Account transferAccount=null;
		while(transferAccount==null) {
	
			System.out.println("Account to transfer from: ");
			int i=0;
			for(Account acc : UserTransfer.getAccounts()) {
				System.out.println(i+". AccountID: "+acc.getID()+" Balance: $"+acc.getBalance());
				i++;
			}			
			try {				
				s = input.nextLine();
				if(s.toLowerCase().equals("q")) {
					returnToMenu();
				}
				accountTransferID=Integer.parseInt(s);
				transferAccount=UserTransfer.getAccounts().get(accountTransferID);				 		
			}
			catch(NumberFormatException e){
				System.out.println("Not a number");
			}
			catch(IndexOutOfBoundsException e){
				System.out.println("Not a valid account");
			}			
		}
		
		Account transferToAccount=null;
		while(transferToAccount==null) {
	
			System.out.println("Account to transfer to: ");
			//int i=0;
			//for(Account acc : UserTransfer.getAccounts()) {
			//	System.out.println(i+". AccountID: "+acc.getID()+" Balance: $"+acc.getBalance());
			//	i++;
			//}			
			try {				
				s = input.nextLine();
				if(s.toLowerCase().equals("q")) {
					returnToMenu();
				}
				accountTransferID=Integer.parseInt(s);
				transferToAccount= aservice.findById(accountTransferID);				 		
			}
			catch(NumberFormatException e){
				System.out.println("Not a number");
			}
			catch(IndexOutOfBoundsException e){
				System.out.println("Not a valid account");
			}		
			if(transferToAccount==null) {
				System.out.println("Can't transfer to a non existing account");
			}
		}
		
		double amount=-1;
		while(amount<0) {
			System.out.printf("Amount to transfer: ");					
			try {
				s = input.nextLine();
				if(s.toLowerCase().equals("q")) {
					returnToMenu();
				}
				amount=Double.parseDouble(s);
				transferAccount.transferTo(transferToAccount, amount);
				transferAccount.update();
				amount=Math.floor(amount*100.0)/100.0;
				System.out.println("Transfered $"+amount+" to account "+transferAccount.getID()+" leaving the balance at $"
						+ transferAccount.getBalance());
			} catch (NumberFormatException e) {
				System.out.println("That's not a number");
				amount=-1;
			} catch (NegativeValueInputException e) {
				System.out.println("Can't deposit negative values");
				amount=-1;
			} catch (NegativeBalanceException e) {
				System.out.println("Can't transfer negative values");
				amount=-1;
			}
			
		}
		returnToMenu();
	
}
	public static void profileMenu() {
		System.out.println("       Profile       ");
		System.out.println("---------------------");
		User UserProfile=currentUser;
		//int accountTransferID;
		String s="2";
		if(currentUser.getAccessLevel()==2) {
			
			System.out.println("1. Personal Profile");		
			System.out.println("2. Other Personal Profile");			
			System.out.println("3. Back");			
			int choice;
			try {
				s = input.nextLine();
				choice=Integer.parseInt(s);
			}
			catch(Exception e){
				choice=0;
			}
			switch(choice) {
			case 1:
				break;
			case 2:		
				do {
					System.out.printf("ID (q to quit to your personal account): ");				
					try {
						s = input.nextLine();
						if(s.toLowerCase().equals("q")) {
							UserProfile=currentUser;
							break;
						}
						UserProfile=uservice.findById(Integer.parseInt(s));
					}
					catch(NumberFormatException e){
						System.out.println("Not a number");
						UserProfile=null;
					}
				}while(UserProfile==null);
				break;		
			case 3:				
				returnToMenu();
				break;	
			default:
				System.out.println("That's not a choice. Try again.");
				transferMenu();
			}
		}
		
		
		String role="Customer";
		switch(currentUser.getAccessLevel()) {
		case 1: role="Employee";break;
		case 2: role="Admin";break;
		}
		
		/*
		 * 
	protected List<Account> accounts;
		 * */
		System.out.println("User ID:  		"+UserProfile.getUserID());
		System.out.println("Username: 		"+UserProfile.getUsername());
		System.out.println("Role:	 		"+role);
		System.out.println("Verified:		"+UserProfile.isVerified());
		System.out.println("Email:    		"+UserProfile.getEmail());
		System.out.println("Phone number: 		"+UserProfile.getPhoneNumber());
		System.out.println("First name:		"+UserProfile.getFirstName());
		System.out.println("Last name:		"+UserProfile.getLastName());
		System.out.println("Address: 		"+UserProfile.getAddress());
		System.out.println("Date of Birth:		"+UserProfile.getDob());
		
		
			System.out.println("Accounts:	ID		Balance		OwnerUserIDs");
		
		for(Account acc : UserProfile.getAccounts()) {
			System.out.printf("\n	 	"+acc.getID()+"		$"+acc.getBalance()+"		");
			for(User owner: acc.getOwners()) {
				System.out.printf(owner.getUserID()+"	");
			}
		}
		System.out.println("\nChange:");
		System.out.println("1. Username");
		System.out.println("2. Email");
		System.out.println("3. Phone number");
		System.out.println("4. First name");
		System.out.println("5. Last name");
		System.out.println("6. Address");
		System.out.println("7. Date of Birth");
		System.out.println("8. Password");
		System.out.println("9. Home");
		
		
		s="2";
		int choice;
		try {
			s = input.nextLine();
			choice=Integer.parseInt(s);
		}
		catch(Exception e){
			choice=0;
		}
		switch(choice) {
		case 1:
			do {
				try {
					System.out.printf("New Username: ");
					String newUsername=input.nextLine();			
					UserProfile.setUsername(newUsername);
					UserProfile=UserProfile.update();
					if(UserProfile==null) {
						System.out.println("Username already being used");
					}
				} catch (MaxSecurityLevelException e) {
					
				} catch (MinSecurityLevelException e) {
					
				}
			}while(UserProfile==null);
			break;
		case 2:
			do {
				try {
					System.out.printf("New Email: ");
					String newEmail=input.nextLine();			
					UserProfile.setEmail(newEmail);
					UserProfile=UserProfile.update();
					if(UserProfile==null) {
						System.out.println("Email already being used");
					}
				} catch (MaxSecurityLevelException e) {
					
				} catch (MinSecurityLevelException e) {
					
				}
			}while(UserProfile==null);
			break;
		case 3:
			do {
				try {
					System.out.printf("New Phone Number: ");
					String newPhoneNumber=input.nextLine();			
					UserProfile.setPhoneNumber(newPhoneNumber);
					UserProfile=UserProfile.update();
					if(UserProfile==null) {
						System.out.println("Phone Number is bad");
					}
				} catch (MaxSecurityLevelException e) {
					
				} catch (MinSecurityLevelException e) {
					
				}
			}while(UserProfile==null);
			break;
		case 4:
			do {
				try {
					System.out.printf("New First name: ");
					String newFName=input.nextLine();			
					UserProfile.setFirstName(newFName);
					UserProfile=UserProfile.update();
					if(UserProfile==null) {
						System.out.println("First Name is bad");
					}
				} catch (MaxSecurityLevelException e) {
					
				} catch (MinSecurityLevelException e) {
					
				}
			}while(UserProfile==null);
			break;
		case 5:
			do {
				try {
					System.out.printf("New Last name: ");
					String newLName=input.nextLine();			
					UserProfile.setLastName(newLName);
					UserProfile=UserProfile.update();
					if(UserProfile==null) {
						System.out.println("Last Name is bad");
					}
				} catch (MaxSecurityLevelException e) {
					
				} catch (MinSecurityLevelException e) {
					
				}
			}while(UserProfile==null);
			break;
		case 6:
			do {
				try {
					System.out.printf("New Address: ");
					String newAddress=input.nextLine();			
					UserProfile.setAddress(newAddress);
					UserProfile=UserProfile.update();
					if(UserProfile==null) {
						System.out.println("Address is bad");
					}
				} catch (MaxSecurityLevelException e) {
					
				} catch (MinSecurityLevelException e) {
					
				}
			}while(UserProfile==null);
			break;
		case 7:
			boolean properDate=false;
			LocalDate birthDate = null;
			String dob="q";
			List<DateTimeFormatter> formats=new ArrayList<>();	
			formats.add(DateTimeFormatter.ofPattern("M/dd/yyyy"));
			formats.add(DateTimeFormatter.ofPattern("M/dd/yy"));
			formats.add(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			formats.add(DateTimeFormatter.ofPattern("MM/dd/yy"));
			formats.add(DateTimeFormatter.ofPattern("MM/d/yyyy"));
			formats.add(DateTimeFormatter.ofPattern("MM/d/yy"));
			formats.add(DateTimeFormatter.ofPattern("MMM dd yyyy"));
			formats.add(DateTimeFormatter.ofPattern("MMM dd yy"));
			formats.add(DateTimeFormatter.ofPattern("MMM d yyyy"));
			formats.add(DateTimeFormatter.ofPattern("MMM d yy"));
			

				properDate=false;
				while(!properDate) {
					System.out.printf("Type your date of birth (mm/dd/yyyy), or q to skip:");
					dob=input.nextLine();
					if(!dob.toLowerCase().equals("q")) {				
						int trys=0;
						do {
							try {
								birthDate= LocalDate.parse(dob, formats.get(trys));
								UserProfile.setDob(birthDate);
								UserProfile=UserProfile.update();
								properDate=true;
								System.out.println("Date choosen: "+birthDate.format(formats.get(6)));
							} catch (DateTimeParseException e) {
								trys++;
								if(trys>=formats.size()) {									
									System.out.println("Date was improper, try again");
									break;
								}
							} catch (MaxSecurityLevelException e) {
								
							} catch (MinSecurityLevelException e) {
								
							}	
						}while(!properDate);
							
					}
					else {
						break;
					}
				}
		case 8:
			do {
				try {
					System.out.printf("New Password: ");
					String newPassword=input.nextLine();			
					UserProfile.setPassword(newPassword);
					UserProfile=UserProfile.update();
					if(UserProfile==null) {
						System.out.println("Password is bad");
					}
				} catch (MaxSecurityLevelException e) {
					
				} catch (MinSecurityLevelException e) {
					
				}
			}while(UserProfile==null);
			break;
		case 9:
			returnToMenu();
			break;
		default:
			System.out.println("That's not a choice. Try again.");
			profileMenu();
		}
		profileMenu();
		
		
		
		
	}
	public static void accountMenu() {
		
		
	}
	public static void approveMenu() {
		if(currentUser.getAccessLevel()<1) {
			returnToMenu();
		}
		System.out.println("       Approve Menu       ");
		System.out.println("--------------------------");
		List<User> allPeople=uservice.findAll();
		Map<Integer, User> unValidated=new HashMap<>();
		int i=0;
		System.out.println("People	id	roll	first name	last name	username		email				address		phone Number	dob");
		for (User bob: allPeople) {
			if(bob.isVerified()) {
				continue;
			}
			i++;
			System.out.println(i+"	"+bob.getUserID()+"	"+bob.getAccessLevel()+"	"+bob.getFirstName()+"		"+bob.getLastName()+"		"+
					bob.getUsername()+"		"+bob.getEmail()+"		"+"		"+bob.getAddress()+"	"+bob.getPhoneNumber()+"	"+bob.getDob());
			
			unValidated.put(i, bob);
		}
		
		System.out.println("Choose a person to modify (q to quit)");
		String s ="";	
		User bob = null;
		int choice;
		try {
			s = input.nextLine();
			if(s.toLowerCase().equals("q")) {
				returnToMenu();
			}
			choice=Integer.parseInt(s);
			bob=unValidated.get(choice);
		}
		catch(Exception e){
			choice=0;
		}
		if(bob==null) {
			System.out.println("That is not a valid person. Try again");
			approveMenu();
		}
		
		System.out.println("Selected: "+bob.toString());
		System.out.println("Approve or Deny(A/D)");
		s="";
		while(!s.toLowerCase().equals("a") && !s.toLowerCase().equals("d")) {
		try {
			s = input.nextLine();
			if(s.toLowerCase().equals("q")) {
				returnToMenu();
			}
			if((s.toLowerCase().equals("a") )) {
				System.out.println("User was approved");
				bob.approveUser();
				
			}
			else if (s.toLowerCase().equals("d")) {
				System.out.println("User was banned and removed");
				bob.banUser();
			}
			else {
				System.out.println("Bad input");
			}
		}
		catch(Exception e){
			System.out.println("Bad input");
		}

	}
		approveMenu();
		
		
		
		
	}
	public static void cancelMenu() {
		if(currentUser.getAccessLevel()<2) {
			returnToMenu();
		}
		System.out.println("       Cancel Menu       ");
		System.out.println("-------------------------");
		System.out.println("1. Cancel a User");
		System.out.println("2. Cancel an Account");
		System.out.println("3. Home");
		String s="2";
		int choice;
		try {
			s = input.nextLine();
			choice=Integer.parseInt(s);
		}
		catch(Exception e){
			choice=0;
		}
		switch(choice) {
			case 1:
			case 2:break;
		case 3:
			returnToMenu();
			break;
		default:
			System.out.println("That's not a choice. Try again.");
			cancelMenu();
		}
		int decision=choice;
		
		
		System.out.println("What is the ID to be deleted?");
		
		boolean deleted=false;
	
		do {
			System.out.printf("Cancel ID (q to quit to your main menu): ");	
			User userToDelete=null;
			Account accToDelete=null;
			try {
				s = input.nextLine();
				if(s.toLowerCase().equals("q")) {
					returnToMenu();
					break;
				}
				if(decision==1) {
					userToDelete=uservice.findById(Integer.parseInt(s));
					if(userToDelete!=null) {
						deleted=userToDelete.removeUser();
					}
					deleted=false;
				}
				else {
					accToDelete=aservice.findById(Integer.parseInt(s));
					if(accToDelete!=null) {
						deleted=accToDelete.closeAccount();
					}
					deleted=false;
				}
				
			}
			catch(NumberFormatException e){
				System.out.println("Not a number");
			} catch (AccountHasValueException e) {
				System.out.println("Withdrawing all money from account:");
				System.out.println("Withdraw of $"+accToDelete.getBalance());
				try {
					accToDelete.withdrawAll();
				} catch (NegativeBalanceException e1) {
					e1.printStackTrace();
				} catch (NegativeValueInputException e1) {
					e1.printStackTrace();
				}
			} catch (NotEnoughAccountsException e) {
				System.out.println("Can't delete accounts while the user has none left");
			}
		}while(!deleted);
		System.out.println("Item Deleted");
		cancelMenu();
		
	}

}