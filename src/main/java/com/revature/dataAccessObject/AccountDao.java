package com.revature.dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.account.Account;
import com.revature.exception.AccountHasValueException;
import com.revature.exception.NotEnoughAccountsException;
import com.revature.user.User;
import com.revature.util.ConnectionUtil;

public class AccountDao implements InterfaceAccountDao{

	private static Logger logBot= Logger.getLogger(AccountDao.class);
	
	@Override
	public List<Account> findAll() {
		List<Account> allAccounts=new ArrayList<>();
		Connection conn = ConnectionUtil.myConnection();
		String schema =ConnectionUtil.getSchema();
		String sql = "select * from "+schema+".accounts";
		try {
			PreparedStatement mySta=conn.prepareStatement(sql);
			ResultSet rs =mySta.executeQuery();
			while(rs.next()) {				
				int balance=rs.getInt("balance");
				int id=rs.getInt("id");
				User bob=new User();
				Account acc=new Account(bob, balance);
				acc.setID(id);				
				List<User> owners=getOwners(acc);
				acc.setOwners(owners);	
				allAccounts.add(acc);
			}
			logBot.info("Returned all accounts");
			return(allAccounts);			
		} catch (SQLException e) {
			logBot.warn("Unable to get all accounts");
			e.printStackTrace();
		}		
		return null;
	}

	@Override
	public Account addAccount(Account account) {
		Account bill= new Account(account);
		if(account.getOwners().size()<=0) {
			logBot.error("Can't add accounts with no users");
			return null;
		}
		if(account.getBalance()<0) {
			logBot.error("Can't add accounts with negative balance");
			return null;
		}
		
		String schema=ConnectionUtil.getSchema();
		String sql="insert into "+schema+".accounts (balance) values (?) returning accounts.id;";
		Connection conn=ConnectionUtil.myConnection();
		
		try {
			
			PreparedStatement mySta=conn.prepareStatement(sql);
			mySta.setInt(1, account.getIntBalance());
			ResultSet rs =mySta.executeQuery();
			if(rs.next()) {
				int id=rs.getInt("id");
				bill.setID(id);				
			}
			
			for(User bob : account.getOwners()) {
				addOwner(bill, bob);
			}
			logBot.info("Added account");
			return bill;
			
		} catch (SQLException e) {
			logBot.error("Unable to add account");
			e.printStackTrace();
		}		
		return null;
	}

	@Override
	public Account addOwner(Account account, User owner) {
		if(account.getID()<=0) {
			logBot.error("Can't add a owner to an account with no ID");
			return null;
		}	
		if(owner.getUserID()<=0) {
			logBot.error("Can't add a owner with no ID to an account");
			return null;
		}	
		
		Connection conn=ConnectionUtil.myConnection();
		String schema=ConnectionUtil.getSchema();
		String sql="insert into "+schema+".user_accounts_jt (account_owner, account) values (?,?);";
		String existSql="select * from  "+schema+".user_account_data where \"Account ID\" =? and id=?;";		
		try {
			PreparedStatement myExistingSta=conn.prepareStatement(existSql);
			myExistingSta.setInt(1, account.getID());
			myExistingSta.setInt(2, owner.getUserID());	
//			System.out.println(myExistingSta.toString());
			ResultSet rs=myExistingSta.executeQuery();
			if (rs.next()){
				logBot.warn("Can't add an owner to an account they already own");
				return null;
			}						
			PreparedStatement mySta=conn.prepareStatement(sql);			
			mySta.setInt(1, owner.getUserID());
			mySta.setInt(2, account.getID());
//			System.out.println(mySta.toString());
			mySta.execute();
			logBot.info("Added "+owner.getUserID()+" as an owner of account "+account.getID());			
			return account;			
		} catch (SQLException e) {
			logBot.error("Unable to add as owner");
			e.printStackTrace();
		}		
		return null;
	}
	
	@Override
	public Account removeOwner(Account account, User owner) {
		Account result=new Account(account);
		if(account.getOwners().size()<2) {			
			return null;
		}
		if(account.getID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		if(owner.getUserID()<= 0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		
		Connection conn=ConnectionUtil.myConnection();
		String schema=ConnectionUtil.getSchema();
		String sql="delete from "+schema+".accounts where id = ?;";
		String existSql="select * from  "+schema+".user_account_data where \"Account ID\" =? and id=?;)";		
		try {
			PreparedStatement myExistingSta=conn.prepareStatement(existSql);
			myExistingSta.setInt(1, account.getID());
			myExistingSta.setInt(2, owner.getUserID());			
			ResultSet rs=myExistingSta.executeQuery();
			if (!rs.next()){
				logBot.warn("Can't delete an owner to an account they don't own");
				return null;
			}						
			PreparedStatement mySta=conn.prepareStatement(sql);			
			mySta.setInt(1, owner.getUserID());
			mySta.setInt(2, account.getID());
			mySta.execute();
			
			if(result.getOwners().contains(owner)) {
				result.removeOwner(owner);
			}
			
			logBot.info("Added "+owner.getUserID()+"as an owner of account "+account.getID());			
			return result;			
		} catch (SQLException e) {
			logBot.error("Unable to add as owner");
			e.printStackTrace();
		}		
		return null;
	}
	
	@Override
	public Account setOwner(Account account, User owner) {
		if(owner.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}		
		if(account.getID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Account result=new Account(account);
		Connection conn=ConnectionUtil.myConnection();
		String schema=ConnectionUtil.getSchema();
		String insertSql="insert into "+schema+".user_accounts_jt (account_owner, account) values (?,?)";		
		String deleteSql="delete from "+schema+".user_accounts_jt where account = ?;";
		try {								
			PreparedStatement mySta=conn.prepareStatement(deleteSql);	
			mySta.setInt(1, account.getID());
			mySta.execute();			
			mySta=conn.prepareStatement(insertSql);			
			mySta.setInt(1, owner.getUserID());
			mySta.setInt(2, account.getID());
			mySta.execute();			
			List<User> owners=new ArrayList<>();
			owners.add(owner);
			result.setOwners(owners);
			logBot.info("Set "+owner.getUserID()+"as owner of account "+account.getID());			
			return result;			
		} catch (SQLException e) {
			logBot.error("Unable to add as owner");
			e.printStackTrace();
		}		
		return null;		
	}
	
	public Account setOwners(Account account, List<User> owners) {
		for(User bob: owners) {
			if(bob.getUserID()<=0) {
				logBot.error("Can't join a user who has no ID");
				return null;
			}	
		}	
		if(account.getID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Account result=new Account(account);
		Connection conn=ConnectionUtil.myConnection();
		String schema=ConnectionUtil.getSchema();
		String insertSql="insert into "+schema+".user_accounts_jt (account_owner, account) values (?,?)";		
		String deleteSql="delete from "+schema+".user_accounts_jt where account = ?;";
		try {								
			PreparedStatement mySta=conn.prepareStatement(deleteSql);	
			mySta.setInt(1, account.getID());
			mySta.execute();			
			
			for(User bob : owners) {
				mySta=conn.prepareStatement(insertSql);			
				mySta.setInt(1, bob.getUserID());
				mySta.setInt(2, account.getID());
				mySta.execute();
			}
				result.setOwners(owners);
			logBot.info("Set multiple users as owners of account "+account.getID());			
			return result;			
		} catch (SQLException e) {
			logBot.error("Unable to add as owner");
			e.printStackTrace();
		}		
		return null;		
	}
	
	
	
	@Override
	public Account update(Account account) {
		if(account.getID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		setBalance(account, account.getIntBalance());
		setOwners(account, account.getOwners());		
		logBot.info("Updated account "+account.getID());
		return account;
	}

	@Override
	public Account findById(int ID) {
		if(ID<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		
		Connection conn = ConnectionUtil.myConnection();
		String schema =ConnectionUtil.getSchema();
		String sql = "select * from "+schema+".user_account_data where \"Account ID\" = ?";
		List<User> owners=new ArrayList<>();
			
		try {
			PreparedStatement myState= conn.prepareStatement(sql);
			myState.setInt(1, ID);
			ResultSet rs = myState.executeQuery();
			User bob=new User();
			int balance=0;
			Account acc;
			int AccoutId=0;
			
			while (rs.next()) {
				
				AccoutId=rs.getInt("Account ID");				
				balance=rs.getInt("balance");				
				int userID=rs.getInt("id");
				String username=rs.getString("username");
				String passwordHash=rs.getString("pwd");
				String email=rs.getString("email");
				int accessLevel=rs.getInt("roll"); //0=customer, 1=employee, 2=admin
				String fName=rs.getString("first_name");
				String lName=rs.getString("last_name");
				String address=rs.getString("address");
				String phoneNumber=rs.getString("phone_number");
				boolean verified=rs.getBoolean("verified");
				LocalDate dob=rs.getDate("dob").toLocalDate();				
				
				bob=new User(username, accessLevel);
				bob.setPasswordHash(passwordHash);
				bob.setUserID(userID);
				bob.setEmail(email);
				bob.setFirstName(fName);
				bob.setLastName(lName);
				bob.setAddress(address);
				bob.setPhoneNumber(phoneNumber);
				bob.setVerified(verified);
				bob.setDob(dob);	
				bob.setAccounts(findByOwnerID(userID));
				owners.add(bob);

			}
			if(owners.size()==0) {
				return null;
			}
			else if(owners.size()>1) {
				acc=new Account(owners, balance);
				
			}else {
				acc=new Account(bob, balance);
				
			}
			acc.setID(AccoutId);
			
			logBot.info("Found the account"+ AccoutId);
			return acc;
		} catch (SQLException e) {
			logBot.error("Unable to get the account "+ ID);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Account> findByOwner(User owner) {
		if(owner.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}		
		Connection conn = ConnectionUtil.myConnection();
		String schema =ConnectionUtil.getSchema();
		String sql = "select * from "+schema+".user_account_data where id = ?";
		List<Account> accounts=new ArrayList<>();
		List<Account> realAccounts=new ArrayList<>();
			
		try {
			PreparedStatement myState= conn.prepareStatement(sql);
			myState.setInt(1, owner.getUserID());
//			System.out.println(myState.toString());
			ResultSet rs = myState.executeQuery();
			

			while (rs.next()) {
				User bob=new User();
				int AccoutId=rs.getInt("Account ID");
				int balance=rs.getInt("balance");
				
				
				
				int userID=rs.getInt("id");
				String username=rs.getString("username");
				String passwordHash=rs.getString("pwd");
				String email=rs.getString("email");
				int accessLevel=rs.getInt("roll"); //0=customer, 1=employee, 2=admin
				String fName=rs.getString("first_name");
				String lName=rs.getString("last_name");
				String address=rs.getString("address");
				String phoneNumber=rs.getString("phone_number");
				boolean verified=rs.getBoolean("verified");
				LocalDate dob=null;
				if(rs.getDate("dob")!=null) {
					dob=rs.getDate("dob").toLocalDate();	
				}			
				
				bob=new User(username,  accessLevel);
				bob.setPasswordHash(passwordHash);
				bob.setUserID(userID);
				bob.setEmail(email);
				bob.setFirstName(fName);
				bob.setLastName(lName);
				bob.setAddress(address);
				bob.setPhoneNumber(phoneNumber);
				bob.setVerified(verified);
				bob.setDob(dob);	
				
				Account acc=new Account(bob, balance);
				if(AccoutId==0) {
					acc=addAccount(acc);
				}else {
					acc.setID(AccoutId);					
				}
				accounts.add(acc);
					
			}
			for (Account i : accounts) {
				List<User> owners= getOwners(i);
				i.setOwners(owners);
				realAccounts.add(i);
				//System.out.println("Account Id:"+i.getID()+" has a balance of "+i.getBalance());
			}
			
			
			logBot.info("Found the accounts of user "+ owner.getUserID());
			return realAccounts;
		} catch (SQLException e) {
			logBot.error("Unable to get the accounts of user"+ owner.getUserID());
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	@Override
	public List<Account> findByOwnerID(int ID) {
		if(ID<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}		
		Connection conn = ConnectionUtil.myConnection();
		String schema =ConnectionUtil.getSchema();
		String sql = "select * from "+schema+".user_account_data where id = ?";
		List<Account> accounts=new ArrayList<>();
			
		try {
			PreparedStatement myState= conn.prepareStatement(sql);
			myState.setInt(1, ID);
			ResultSet rs = myState.executeQuery();
			
			while (rs.next()) {
				User bob=new User();
				int AccoutId=rs.getInt("Account ID");
				int balance=rs.getInt("balance");
				
				int userID=rs.getInt("id");
				String username=rs.getString("username");
				String passwordHash=rs.getString("pwd");
				String email=rs.getString("email");
				int accessLevel=rs.getInt("roll"); //0=customer, 1=employee, 2=admin
				String fName=rs.getString("first_name");
				String lName=rs.getString("last_name");
				String address=rs.getString("address");
				String phoneNumber=rs.getString("phone_number");
				boolean verified=rs.getBoolean("verified");
				LocalDate dob=null;
				if(rs.getDate("dob")!=null) {
					dob=rs.getDate("dob").toLocalDate();	
				}			
				
				bob=new User(username, accessLevel);
				bob.setPasswordHash(passwordHash);
				bob.setUserID(userID);
				bob.setEmail(email);
				bob.setFirstName(fName);
				bob.setLastName(lName);
				bob.setAddress(address);
				bob.setPhoneNumber(phoneNumber);
				bob.setVerified(verified);
				bob.setDob(dob);	
				
				
				//Technically O(n^2) time but better than an infinte time
				for(Account i: accounts) {
					if(i.getID()==userID) {
						i.addOwner(bob);
					}
					else {
						Account acc=new Account(bob, balance);
						acc.setID(AccoutId);
						accounts.add(acc);
					}
				}		
			}
			logBot.info("Found the accounts of user "+ ID);
			return accounts;
		} catch (SQLException e) {
			logBot.error("Unable to get the accounts of user"+ ID);
			e.printStackTrace();
		}
		return null;
		
		
	}



	@Override
	public int setBalance(Account account, int balance) {
		if(account.getID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return -1;
		}		
		Connection conn = ConnectionUtil.myConnection();
		String schema =ConnectionUtil.getSchema();
		String sql = "update "+schema+".accounts set balance = ? where id = ?;";
		
		try {
			PreparedStatement mySta=conn.prepareStatement(sql);
			mySta.setInt(1, balance);
			mySta.setInt(2, account.getID());
			//System.out.println(mySta.toString());
			mySta.execute();			
			logBot.info("Updated balance from account "+account.getID());
			return balance;
			
			
		} catch (SQLException e) {
			logBot.warn("Unable to update the balance of account "+account.getID());
			e.printStackTrace();
		}
		return -1;
	}

	
	@Override
	public int getBalance(Account account) {
		Connection conn = ConnectionUtil.myConnection();
		String schema =ConnectionUtil.getSchema();
		String sql = "select * from "+schema+".accounts where id = ?";
		
		try {
			PreparedStatement mySta=conn.prepareStatement(sql);
			mySta.setInt(1, account.getID());
			ResultSet rs= mySta.executeQuery();
			if(rs.next()) {
				logBot.info("Returned balance from account "+account.getID());
				return rs.getInt("balance");
			}
			
		} catch (SQLException e) {
			logBot.warn("Unable to return the balance of account "+account.getID());
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getBalanceFromId(int ID) {
		if(ID<=0) {
			logBot.error("Can't join a user who has no ID");
			return -1;
		}	
		
		Connection conn = ConnectionUtil.myConnection();
		String schema =ConnectionUtil.getSchema();
		String sql = "select * from "+schema+".accounts where id = ?";
		
		try {
			PreparedStatement mySta=conn.prepareStatement(sql);
			mySta.setInt(1, ID);
			ResultSet rs= mySta.executeQuery();
			if(rs.next()) {
				logBot.info("Returned balance from account "+ID);
				return rs.getInt("balance");
			}
			
		} catch (SQLException e) {
			logBot.warn("Unable to return the balance of account "+ID);
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<User> getOwners(Account account) {
		
		//System.out.println("Please be running");
		if(account.getID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		
		//Slightly complicated, there may be a bug, but not sure what or where
		
		Connection conn = ConnectionUtil.myConnection();
		String schema =ConnectionUtil.getSchema();
		String sql = "select * from "+schema+".user_account_data where \"Account ID\" = ?";
		List<User> owners=new ArrayList<>();
			
		try {
			PreparedStatement myState= conn.prepareStatement(sql);
			myState.setInt(1, account.getID());			
			ResultSet rs = myState.executeQuery();
			
			while (rs.next()) {
				User bob=new User();				
				int userID=rs.getInt("id");
				String username=rs.getString("username");
				String passwordHash=rs.getString("pwd");
				String email=rs.getString("email");
				int accessLevel=rs.getInt("roll"); //0=customer, 1=employee, 2=admin
				String fName=rs.getString("first_name");
				String lName=rs.getString("last_name");
				String address=rs.getString("address");
				String phoneNumber=rs.getString("phone_number");
				boolean verified=rs.getBoolean("verified");
				LocalDate dob=null;
				if(rs.getDate("dob")!=null) {
					dob=rs.getDate("dob").toLocalDate();	
				}
				
				List<Account> accounts=findByOwnerID(userID);
				
				bob=new User(username, accessLevel);
				bob.setPasswordHash(passwordHash);
				bob.setUserID(userID);
				bob.setEmail(email);
				bob.setFirstName(fName);
				bob.setLastName(lName);
				bob.setAddress(address);
				bob.setPhoneNumber(phoneNumber);
				bob.setVerified(verified);
				bob.setDob(dob);	
				bob.setAccounts(accounts);				
				owners.add(bob);				
			}
			logBot.info("Found the owners of account "+ account.getID());
			return owners;
		} catch (SQLException e) {
			logBot.error("Unable to get the owners of account "+ account.getID());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean removeAccount(Account account) throws AccountHasValueException, NotEnoughAccountsException {
		if(account.getID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return false;
		}	
		
		if(account.getBalance()>0) {
			logBot.error("Can't remove an account with positive value");
			throw new AccountHasValueException();
		}
		if(account.getOwners()!=null) {

			if(!account.getOwners().isEmpty()) {
				for(User bob : account.getOwners()) {
					if(bob.getAccounts().size()==1) {
						
						throw new NotEnoughAccountsException();
					}
				}
			}
		}
		String schema=ConnectionUtil.getSchema();
		String sql ="delete from "+schema+".accounts where id = ?;";
		Connection conn = ConnectionUtil.myConnection();
		
		try {
			PreparedStatement mySta= conn.prepareStatement(sql);
			mySta.setInt(1, account.getID());
			mySta.execute();
			logBot.info("Deleted account "+account.getID());
			return true;
		} catch (SQLException e) {
			logBot.warn("Unable to delect account "+account.getID());
			e.printStackTrace();
		}
		return false;
	}
	

}
