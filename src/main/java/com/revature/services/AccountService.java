package com.revature.services;

import java.util.List;

import org.apache.log4j.Logger;

import com.revature.account.Account;
import com.revature.dataAccessObject.AccountDao;
import com.revature.dataAccessObject.InterfaceAccountDao;
import com.revature.exception.AccountHasValueException;
import com.revature.exception.NotEnoughAccountsException;
import com.revature.user.User;

public class AccountService {
	public InterfaceAccountDao Idao=new AccountDao();
	Logger logBot= Logger.getLogger(UserService.class);
	
	public List<Account> findAll(){
		return Idao.findAll();
	};
	public Account addAccount(Account account) {
		if(account==null) {
			logBot.warn("Can't add a null account");
			return null;
		}
		return Idao.addAccount(account);

	};
	public Account update(Account account) {
		if(account==null) {
			logBot.warn("Can't update a null account");
			return null;
		}
		return Idao.update(account);
		
	};
		
	public Account findById(int ID) {
		return Idao.findById(ID);
	};
	public List<Account> findByOwner(User owner){
		if(owner==null) {
			logBot.warn("Can't fnd accounts of null user");
			return null;
		}
		return Idao.findByOwner(owner);
	};
	public List<Account> findByOwnerID(int ID){
		return Idao.findByOwnerID(ID);
	};
//	public int getBalance(Account account) {
//		if(account==null) {
//			logBot.warn("Can't add a null account");
//			return -1;
//		}
//		return Idao.getBalance(account);
//	};
//	public int getBalanceFromId(int ID) {
//		return Idao.getBalanceFromId(ID);
//	};
//	public List<User> getOwners(Account account){
//		if(account==null) {
//			logBot.warn("Can't find owners of a null account");
//			return null;
//		}
//		return Idao.getOwners(account);
//	};
	
	public boolean removeAccount(Account account) throws AccountHasValueException, NotEnoughAccountsException{
		if(account==null) {
			logBot.warn("Can't remove a null account");
			return false;
		}
		return Idao.removeAccount(account);
	};
//	public Account addOwner(Account account, User owner) {
//		if(account==null || owner ==null) {
//			logBot.warn("Can't add a null owner or account");
//			return null;
//		}
//		return Idao.addOwner(account, owner);
//	};
	public int setBalance(Account account, int balance) {
		if(account==null) {
			logBot.warn("Can't set a blance for a null account");
			return -1;
		}
		return Idao.setBalance(account, balance);
	};
//	public Account removeOwner(Account account, User owner) {
//		if(account==null || owner == null) {
//			logBot.warn("Can't add a null account");
//			return null;
//		}
//		return Idao.removeOwner(account, owner);
//	};
//	public Account setOwner(Account account, User owner) {
//		if(account==null || owner == null) {
//			logBot.warn("Can't add a null account");
//			return null;
//		}
//		return Idao.setOwner(account, owner);
//	};
//	public Account setOwners(Account account, List<User> owners) {
//		return Idao.setOwners(account, owners);
//	};
//	
//	

}
