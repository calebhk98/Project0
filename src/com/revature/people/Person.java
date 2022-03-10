package com.revature.people;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Person {
	protected String username;
	protected String passwordHash;
	protected int accessLevel; //0=customer, 1=employee, 2=admin
	public static List<Person> people=new ArrayList<Person>();
	protected int PersonNumber;
	protected boolean verified=false;
	
	protected String email;
	protected String phoneNumber;
	

	public Person(String username, String password, int accessLevel) {
		super();
		this.username = username;
		this.accessLevel = accessLevel;		
		
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			this.passwordHash = Base64.getEncoder().encodeToString(hash);
		} 
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		this.PersonNumber=people.size();
		people.add(this);
		
				
	}
	
	public Person(Person oldPerson) {
		super();
		this.username = oldPerson.username;
		this.accessLevel = oldPerson.accessLevel;
		this.passwordHash = oldPerson.passwordHash;
		
	}
	
	public void upgradeSecurityLevel() {
		
		
		if(this.accessLevel!=2) {
			this.accessLevel++;
			
		}
	}
	public void Fire() {
		this.accessLevel=0;
	}
	
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(passwordHash, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(passwordHash, other.passwordHash) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "Person [username=" + username + ", accessLevel=" + accessLevel + ", email=" + email + ", phoneNumber="
				+ phoneNumber + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
