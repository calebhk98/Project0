package com.revature.dataAccessObject;


import java.util.List;

import com.revature.account.Account;
import com.revature.exception.AccountHasValueException;
import com.revature.exception.NotEnoughAccountsException;
import com.revature.user.User;

public interface InterfaceAccountDao {
	List<Account> findAll();
	Account addAccount(Account account);
	Account update(Account account);
		
	Account findById(int ID);
	List<Account> findByOwner(User owner);
	int getBalance(Account account);
	int getBalanceFromId(int ID);
	List<User> getOwners(Account account);
	
	boolean removeAccount(Account account) throws AccountHasValueException, NotEnoughAccountsException;
	List<Account> findByOwnerID(int ID);
	Account addOwner(Account account, User owner);
	int setBalance(Account account, int balance);
	Account removeOwner(Account account, User owner);
	Account setOwner(Account account, User owner);
	Account setOwners(Account account, List<User> owners);
}
