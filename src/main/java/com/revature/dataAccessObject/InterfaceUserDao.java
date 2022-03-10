package com.revature.dataAccessObject;

import java.time.LocalDate;
//import java.util.Date;
import java.util.List;

import com.revature.account.Account;
import com.revature.exception.AccountHasValueException;
import com.revature.exception.MaxSecurityLevelException;
import com.revature.exception.MinSecurityLevelException;
import com.revature.exception.NotEnoughAccountsException;
import com.revature.user.User;

public interface InterfaceUserDao {
	
	User createNewUser(User bob) throws MaxSecurityLevelException, MinSecurityLevelException;
	boolean removeUser(User bob) throws AccountHasValueException, NotEnoughAccountsException;
	User updateUser(User bob) throws MaxSecurityLevelException, MinSecurityLevelException;
	
	User changeUsername(User bob, String username);
	User changePassword(User bob, String passwordHash);
	User changeAccessLevel(User bob,int accessLevel) throws MaxSecurityLevelException, MinSecurityLevelException;
	User changeVerified(User bob,boolean verified);
	User changeEmail(User bob,String email);
	User changePhoneNumber(User bob,String phoneNumber);
	User changeFirstName(User bob,String fName);
	User changeLastName(User bob, String lName);
	User changeAddress(User bob, String address);
	User changeAccounts(User bob, List<Account> accounts);
	User changeDoB(User bob, LocalDate dob);
	
	List<User> findAll();
	User findByID(int id);
	User findByUsername(String username);
	
	
}
