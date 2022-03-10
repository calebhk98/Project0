package com.revature.main;

//import java.util.ArrayList;
//import java.util.List;
import java.util.Scanner;

import com.revature.people.Admin;
import com.revature.people.Customer;
import com.revature.people.Employee;
import com.revature.people.Person;

public class Driver {

	public static void main(String[] args) {
			Menu();
	}
			

	
	
	public static void login() {}
	public static void accountCreation() {
		Scanner newLine=new Scanner(System.in);
		
		System.out.println("Type a username:");
		String userName=newLine.nextLine();
		System.out.println("Type a password:");
		String password=newLine.nextLine();
		System.out.println("Type a phoneNumber, or q to skip:");
		String phoneNumber=newLine.nextLine();
		System.out.println("Type an email, or q to skip:");
		String email=newLine.nextLine();
		System.out.println("Choose an access level:");
		System.out.println("1. Customer");
		System.out.println("2. Employee");
		System.out.println("3. Admin");
		String s=newLine.nextLine();
		newLine.close();
		int choice;
		try {
			choice=Integer.parseInt(s);
			choice--;
		}
		catch(Exception e){
			choice=0;
		}
		Person bob;
		switch(choice) {
		case 0:bob=new Customer(userName,password);break;
		case 1:bob=new Employee(userName,password);break;
		case 2:bob=new Admin(userName,password);break;
		default:bob=new Customer(userName,password);break;
		}
		bob.setEmail(email);
		bob.setPhoneNumber(phoneNumber);
		
		
	}
	public static void details() {}
	public static void Menu() {
		System.out.println("       Menu       ");
		System.out.println("------------------");
		System.out.println("1. Login");
		System.out.println("2. Request an account");
		System.out.println("3. Details");
		Scanner newLine=new Scanner(System.in);
		String s=newLine.nextLine();
		newLine.close();
		int choice;
		try {
			choice=Integer.parseInt(s);
		}
		catch(Exception e){
			choice=0;
		}
		switch(choice) {
		case 1:login();break;
		case 2:accountCreation();break;
		case 3:details();break;
		default:
			System.out.println("That's not a choice. Try again.");
			Menu();
		}
	}
}