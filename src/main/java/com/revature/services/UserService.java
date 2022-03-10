package com.revature.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.account.Account;
import com.revature.dataAccessObject.InterfaceUserDao;
import com.revature.dataAccessObject.UserDao;
import com.revature.exception.AccountHasValueException;
import com.revature.exception.MaxSecurityLevelException;
import com.revature.exception.MinSecurityLevelException;
import com.revature.exception.NotEnoughAccountsException;
import com.revature.user.User;

public class UserService {

	public InterfaceUserDao Idao = new UserDao();
	Logger logBot= Logger.getLogger(UserService.class);
	
	
	public boolean removeUser(User bob) throws AccountHasValueException, NotEnoughAccountsException {
		return Idao.removeUser(bob);
	};
	public User updateUser(User bob) throws MaxSecurityLevelException, MinSecurityLevelException {
		return Idao.updateUser(bob);
	};
	
	public User changeUsername(User bob, String username) {
		if(bob==null || username ==null) {
			logBot.warn("Can't change a username for a null username or user");
			return null;
		}
		
		return Idao.changeUsername(bob, username);
	}
	public User changeAccessLevel(User bob,int accessLevel) throws MaxSecurityLevelException, MinSecurityLevelException{
		if(bob==null) {
			logBot.warn("Can't change an access level for a null user");
			return null;
		}
		return Idao.changeAccessLevel(bob, accessLevel);
	};
	public User changeVerified(User bob,boolean verified) {
		if(bob==null) {
			logBot.warn("Can't change a verification for a null user");
			return null;
		}
		return Idao.changeVerified(bob, verified);
	};
	public User changeEmail(User bob,String email) {
		if(bob==null) {
			logBot.warn("Can't change an email for a null user");
			return null;
		}
		return Idao.changeEmail(bob, email);
	};
	public User changePhoneNumber(User bob,String phoneNumber) {
		if(bob==null) {
			logBot.warn("Can't change a phone number for a null user");
			return null;
		}
		return Idao.changePassword(bob, phoneNumber);
	};
	public User changeFirstName(User bob,String fName) {
		if(bob==null || fName ==null) {
			logBot.warn("Can't change a first name for a null name or user");
			return null;
		}
		return Idao.changeFirstName(bob, fName);
	};
	public User changeLastName(User bob, String lName) {
		if(bob==null || lName ==null) {
			logBot.warn("Can't change a last name for a null name or user");
			return null;
		}
		return Idao.changeLastName(bob, lName);
	};
	public User changeAddress(User bob, String address) {
		if(bob==null ) {
			logBot.warn("Can't change a username for a null user");
			return null;
		}
		return Idao.changeAddress(bob, address);
	};
	public User changeAccounts(User bob, List<Account> accounts) {
		if(bob==null || accounts ==null) {
			logBot.warn("Can't change accounts for a null account or user");
			return null;
		}
		return Idao.changeAccounts(bob, accounts);
	};
	public User changeDoB(User bob, LocalDate dob) {
		if(bob==null || dob ==null) {
			logBot.warn("Can't change a date of birth for a null date or user");
			return null;
		}
		return Idao.changeDoB(bob, dob);
	};
	
	
	
	public User register(User u) throws MaxSecurityLevelException, MinSecurityLevelException {
		if((findByUsername(u.getUsername()))==null) {
			User result=Idao.createNewUser(u);
			return result;
		}
		else {
			logBot.error("Can't register "+u.getUsername()+ " it's not a unique username.");
			//throw new RegisterUserFailedException("Failed to register code");
			return null;
		}
		
	}
	
	public User findByUsername(String username) {
		User done = Idao.findByUsername(username);
		return done;
	}
	public User findById(int id) {
		User done = Idao.findByID(id);
		return done;
	}
	public List<User> findAll() {
		return Idao.findAll();
	}
	public User Login(String username, String password) {
		String hashString="";
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			//System.out.println("digest: "+digest.toString());
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

//			System.out.println("Bytes: "+hash);
			hashString=Base64.getEncoder().encodeToString(hash);
			
		} 
		catch (NoSuchAlgorithmException e) {
			logBot.warn("No algorithm to hash the passwords");			
			e.printStackTrace();
			return null;
		}
		//System.out.println("Typed: "+hashString);
		
		User done=findByUsername(username);
		
		if(done==null) {
			logBot.info("No username");
			return null;
		}	
		if(done.isVerified()) {
			if(done.getPasswordHash()!=null&&done.getPasswordHash().equals(hashString) ) {
				logBot.info("Succesfully logged in");
				return done;
			}
			else {
				logBot.info("Bad Password");
				return null;
			}
		}
		else {
			logBot.info("Can't log in with out verifying the account");
			return null;
		}
		
		
	}
	
	public User changePassword(User u, String newPassword) {
		String hashString="";
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(newPassword.getBytes(StandardCharsets.UTF_8));
			hashString=Base64.getEncoder().encodeToString(hash);
			
		} 
		catch (NoSuchAlgorithmException e) {
			logBot.warn("No algorithm to hash the passwords");			
			e.printStackTrace();
			return null;
		}	
		u=Idao.changePassword(u, hashString);		
		return u;		
	}
	
	
	
	
}
