package com.revature.dataAccessObject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
//import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.account.Account;
import com.revature.exception.AccountHasValueException;
import com.revature.exception.MaxSecurityLevelException;
import com.revature.exception.MinSecurityLevelException;
import com.revature.services.AccountService;
import com.revature.user.User;
import com.revature.util.ConnectionUtil;

public class UserDao implements InterfaceUserDao{
	
	private static Logger logBot=Logger.getLogger(UserDao.class);
	private static AccountService aservice=new AccountService();

	@Override
	public User createNewUser(User bob) throws MaxSecurityLevelException, MinSecurityLevelException {
		
		if(bob.getUsername()==null) {
			logBot.error("Can't create a user wil null values");
			return null;
		}
		if(bob.getPasswordHash()==null) {
			logBot.error("Can't create a user wil null values");
			return null;
		}
		if(bob.getFirstName()==null) {
			logBot.error("Can't create a user wil null values");
			return null;
		}
		if(bob.getLastName()==null) {
			logBot.error("Can't create a user wil null values");
			return null;
		}
		
		Connection conn =ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
//		String sql ="insert into "+schema+".users(username, pwd, email, roll, first_name, last_name, "+
//				"address, phone_number, verified, dob, date_of_joining)";
		String sql ="insert into "+schema+".users (username, pwd, roll, first_name, last_name) "
				+ "values (?, ?, ?, ?, ?) returning "+schema+".users.id;";
		try {
			PreparedStatement myStatment= conn.prepareStatement(sql);
			
			myStatment.setString(1, bob.getUsername());
			myStatment.setString(2, bob.getPasswordHash());
			myStatment.setInt(3, bob.getAccessLevel());
			myStatment.setString(4, bob.getFirstName());
			myStatment.setString(5, bob.getLastName());
			
//			System.out.println(myStatment.toString());//Uncomment if throwing errors to see what is being queried
			
			ResultSet rs=myStatment.executeQuery();
			if(rs.next()) {
				int id=rs.getInt(1);
				bob.setUserID(id);
				updateUser(bob);
				return bob;				
			}
		} catch (SQLException e) {
			logBot.error("Database Error, unable to run");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean removeUser(User bob) {
		if(bob.getUserID()<=0) {
			logBot.error("Can't remove a user who has no ID");
			return false;
		}	if(bob.getAccounts()!=null) {
			for(Account acc : bob.getAccounts()) {
				if(acc.getOwners().size()>1) {
					continue;
				}
				if(acc.getBalance()>0) {
					logBot.warn("Can't delete a user who still has money in their account");
					//throw new AccountHasValueException();
				}
			}
		}
		
		
		Connection conn =ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql ="delete from "+schema+".users where id = ?;";
		PreparedStatement myStatment;
		try {
			myStatment = conn.prepareStatement(sql);
			myStatment.setInt(1, bob.getUserID());
//			System.out.println(myStatment.toString());//Uncomment to fix bugs
			myStatment.execute();
			logBot.info("Deleted user "+bob.getUserID());			
			return true;			
		} catch (SQLException e) {
			logBot.error("Database Error, unable to run");
			e.printStackTrace();
		}				
		return false;
	}

	@Override
	public User updateUser(User bob) throws MaxSecurityLevelException, MinSecurityLevelException {
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		User result;
		
		result=changeUsername(bob, bob.getUsername());
		result=changePassword(bob, bob.getPasswordHash());
		result=changeAccessLevel(bob, bob.getAccessLevel());
		result=changeVerified(bob, bob.isVerified());
		result=changeEmail(bob, bob.getEmail());
		result=changePhoneNumber(bob, bob.getPhoneNumber());
		result=changeFirstName(bob, bob.getFirstName());
		result=changeLastName(bob, bob.getLastName());
		result=changeAddress(bob, bob.getAddress());
		result=changeAccounts(bob, bob.getAccounts());
		result=changeDoB(bob, bob.getDob());
		
		
		
		return result;
		
	}

	
	
	@Override
	public User changeUsername(User bob, String username) {		
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		if(username == null) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql="update "+schema+".users set username = ? where id = ?;";
		
		try {
			PreparedStatement myStatment=conn.prepareStatement(sql);
			myStatment.setString(1, username);
			myStatment.setInt(2, bob.getUserID());	
//			System.out.println(myStatment.toString());//Run if error
			
			myStatment.execute();
			logBot.info("Username for account "+bob.getUserID()+" sucessfully changed to "+username);
			bob.setUsername(username);
			return bob;			
		} catch (SQLException e) {
			logBot.error("Failed to change the username");
			return null;
		}
			
	}

	@Override
	public User changePassword(User bob, String password) {		
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql="update "+schema+".users set pwd = ? where id = ?;";
		
		try {
			PreparedStatement myStatment=conn.prepareStatement(sql);
			myStatment.setString(1, password);
			myStatment.setInt(2, bob.getUserID());	
//			System.out.println(myStatment.toString());//Run if error
			
			myStatment.execute();
			logBot.info("Password Hash for account "+bob.getUserID()+" sucessfully changed to "+password);
			bob.setPassword(password);
			return bob;			
		} catch (SQLException e) {
			logBot.error("Failed to change the password");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User changeAccessLevel(User bob, int accessLevel) throws MaxSecurityLevelException, MinSecurityLevelException {
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql="update "+schema+".users set roll = ? where id = ?;";
		
		try {
			PreparedStatement myStatment=conn.prepareStatement(sql);
			myStatment.setInt(1, accessLevel);
			myStatment.setInt(2, bob.getUserID());	
//			System.out.println(myStatment.toString());//Run if error
			
			myStatment.execute();
			logBot.info("Access Level for account "+bob.getUserID()+" sucessfully changed to "+accessLevel);
			bob.setAccessLevel(accessLevel);
			return bob;			
		} catch (SQLException e) {
			logBot.error("Failed to change the access Level");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User changeVerified(User bob, boolean verified) {
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql="update "+schema+".users set verified = ? where id = ?;";
		
		try {
			PreparedStatement myStatment=conn.prepareStatement(sql);
			myStatment.setBoolean(1, verified);
			myStatment.setInt(2, bob.getUserID());	
//			System.out.println(myStatment.toString());//Run if error
			
			myStatment.execute();
			logBot.info("Account "+bob.getUserID()+" verification sucessfully changed to "+verified);
			bob.setVerified(verified);
			return bob;			
		} catch (SQLException e) {
			logBot.error("Failed to change the verification");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User changeEmail(User bob, String email) {
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql="update "+schema+".users set email = ? where id = ?;";
		
		try {
			PreparedStatement myStatment=conn.prepareStatement(sql);
			myStatment.setString(1, email);
			myStatment.setInt(2, bob.getUserID());	
//			System.out.println(myStatment.toString());//Run if error
			
			myStatment.execute();
			logBot.info("Email for account "+bob.getUserID()+" sucessfully changed to "+email);
			bob.setEmail(email);
			return bob;			
		} catch (SQLException e) {
			logBot.error("Failed to change the email");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User changePhoneNumber(User bob, String phoneNumber) {
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql="update "+schema+".users set phone_number = ? where id = ?;";
		
		try {
			PreparedStatement myStatment=conn.prepareStatement(sql);
			myStatment.setString(1, phoneNumber);
			myStatment.setInt(2, bob.getUserID());	
//			System.out.println(myStatment.toString());//Run if error
			
			myStatment.execute();
			logBot.info("Phone number for account "+bob.getUserID()+" sucessfully changed to "+phoneNumber);
			bob.setPhoneNumber(phoneNumber);
			return bob;			
		} catch (SQLException e) {
			logBot.error("Failed to change the phone number");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User changeFirstName(User bob, String fName) {
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql="update "+schema+".users set first_name = ? where id = ?;";
		
		try {
			PreparedStatement myStatment=conn.prepareStatement(sql);
			myStatment.setString(1, fName);
			myStatment.setInt(2, bob.getUserID());	
//			System.out.println(myStatment.toString());//Run if error
			
			myStatment.execute();
			logBot.info("First name for account "+bob.getUserID()+" sucessfully changed to "+fName);
			bob.setFirstName(fName);
			return bob;			
		} catch (SQLException e) {
			logBot.error("Failed to change the first name");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User changeLastName(User bob, String lName) {
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql="update "+schema+".users set last_name = ? where id = ?;";
		
		try {
			PreparedStatement myStatment=conn.prepareStatement(sql);
			myStatment.setString(1, lName);
			myStatment.setInt(2, bob.getUserID());	
//			System.out.println(myStatment.toString());//Run if error
			
			myStatment.execute();
			logBot.info("Last name for account "+bob.getUserID()+" sucessfully changed to "+lName);
			bob.setLastName(lName);
			return bob;			
		} catch (SQLException e) {
			logBot.error("Failed to change the last name");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User changeAddress(User bob, String address) {
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql="update "+schema+".users set address = ? where id = ?;";
		
		try {
			PreparedStatement myStatment=conn.prepareStatement(sql);
			myStatment.setString(1, address);
			myStatment.setInt(2, bob.getUserID());	
//			System.out.println(myStatment.toString());//Run if error
			
			myStatment.execute();
			logBot.info("Address for account "+bob.getUserID()+" sucessfully changed to "+address);
			bob.setAddress(address);
			return bob;			
		} catch (SQLException e) {
			logBot.error("Failed to change the address");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User changeAccounts(User bob, List<Account> accounts) {
		//Delete the current accounts
		//Add the new accounts
		List<Account> newAccounts=new ArrayList<>();
		if(accounts.isEmpty()) {
			logBot.info("Can't set no account as the accounts");
			return null;			
		}
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		for (Account acc : accounts) {
			if(acc.getID()<=0) {
				acc=acc.submitNew();
				newAccounts.add(acc);
				accounts.remove(acc);
			}
			else {
				newAccounts.add(acc);
			}
		}
		
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String deleteSql ="delete from "+schema+".user_accounts_jt where account_owner = ?;";
		
		
		PreparedStatement myStatment;
		try {
			myStatment = conn.prepareStatement(deleteSql);
			myStatment.setInt(1, bob.getUserID());
//			System.out.println(myStatment.toString());//Uncomment to fix bugs
			myStatment.execute();
			
			//Grab all the accounts and delete any that don't have any other owners
			
			
			logBot.info("Deleted all accounts from "+bob.getUserID()+" before adding the new ones");						
		} catch (SQLException e) {
			logBot.error("Database Error, unable to run");
			e.printStackTrace();
		}			
		
		
		//Now add the new accounts
		for(Account idk : newAccounts) {
			String addSql ="insert into "+schema+".user_accounts_jt (account_owner, account) values (?,?);";			
			try {
				myStatment= conn.prepareStatement(addSql);
				
				myStatment.setInt(1, bob.getUserID());
				myStatment.setInt(2, idk.getID());
//				System.out.println(myStatment.toString());//Uncomment if throwing errors to see what is being queried
				
				myStatment.execute();
				
			} catch (SQLException e) {
				logBot.error("Database Error, unable to run");
				e.printStackTrace();
				return null;
			}
			
			
		}
		return bob;
	}

	@Override
	public User changeDoB(User bob, LocalDate dob) {
		if(bob.getUserID()<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		Connection conn = ConnectionUtil.myConnection();
		String schema= ConnectionUtil.getSchema();
		String sql="update "+schema+".users set dob = ? where id = ?;";
		
		try {
			PreparedStatement myStatment=conn.prepareStatement(sql);
			myStatment.setObject(1, dob);
			myStatment.setInt(2, bob.getUserID());	
//			System.out.println(myStatment.toString());//Run if error
			
			myStatment.execute();
			logBot.info("Date of birth for account "+bob.getUserID()+" sucessfully changed to "+dob);
			bob.setDob(dob);
			return bob;			
		} catch (SQLException e) {
			logBot.error("Failed to change the birthday");
			e.printStackTrace();
		}
		return null;
	}

	
	
	@Override
	public List<User> findAll() {
		String schema= ConnectionUtil.getSchema();
		Connection conn =ConnectionUtil.myConnection();		
		String sql = "Select * from "+schema+".users";
		List<User> allUsers=new LinkedList<>();		
		User done=new User();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);					
			//System.out.println(stmt.toString());			
			while(rs.next()) {					
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
				Object olddob=  rs.getObject("dob");
				LocalDate dob=null;
				if(olddob!=null) {

					dob= ((java.sql.Date) olddob).toLocalDate() ;	
				}
				
				//List<Account> accounts= new ArrayList<>();//Going to  add a get all accounts
				
				done=new User(username, accessLevel);
				done.setPasswordHash(passwordHash);
				done.setUserID(userID);
				done.setEmail(email);
				done.setFirstName(fName);
				done.setLastName(lName);
				done.setAddress(address);
				done.setPhoneNumber(phoneNumber);
				done.setVerified(verified);
				done.setDob(dob);	
				List<Account> accounts=  aservice.findByOwner(done);//Going to  add a get all accounts	
				
				done.setAccounts(accounts);
				
				logBot.info("Returned user with ID "+ userID);
				allUsers.add(done);				
			}
		} catch (SQLException e) {
			logBot.error("SQL Exception");
			e.printStackTrace();
			return null;
		}
		
		logBot.info("Returned all users");
		return allUsers;
		
		
	}

	@Override
	public User findByID(int id) {
		if(id<=0) {
			logBot.error("Can't join a user who has no ID");
			return null;
		}	
		String schema= ConnectionUtil.getSchema();
		Connection conn =ConnectionUtil.myConnection();		
		String sql = "Select * from "+schema+".users where id =?";
		User result=new User();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs=stmt.executeQuery();					
			//System.out.println(stmt.toString());			
			if(rs.next()) {					
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
				Object olddob=  rs.getObject("dob");
				LocalDate dob=null;
				if(olddob!=null) {

					dob= ((java.sql.Date) olddob).toLocalDate() ;	
				}
				
				
				
				result=new User(username, accessLevel);
				result.setPasswordHash(passwordHash);
				result.setUserID(userID);
				result.setEmail(email);
				result.setFirstName(fName);
				result.setLastName(lName);
				result.setAddress(address);
				result.setPhoneNumber(phoneNumber);
				result.setVerified(verified);
				result.setDob(dob);	
				

				List<Account> accounts=  aservice.findByOwner(result);
				
				//System.out.println("Acount ID: "+accounts.get(0));
				result.setAccounts(accounts);
				
				logBot.info("Returned user with ID "+ userID);
				return result;
			}
		} catch (SQLException e) {
			logBot.error("SQL Exception");
			e.printStackTrace();
		}
		
	return null;
	}

	@Override
	public User findByUsername(String username) {
		if(username==null) {
			logBot.error("Can't find a user without a username");
			return null;
		}
		String schema= ConnectionUtil.getSchema();
		Connection conn =ConnectionUtil.myConnection();		
		String sql = "Select * from "+schema+".users where upper(username) = upper(?)";
		User result=new User();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs=stmt.executeQuery();					
			//System.out.println(stmt.toString());			
			if(rs.next()) {					
				int userID=rs.getInt("id");
				username=rs.getString("username");
				String passwordHash=rs.getString("pwd");
				String email=rs.getString("email");
				int accessLevel=rs.getInt("roll"); //0=customer, 1=employee, 2=admin
				String fName=rs.getString("first_name");
				String lName=rs.getString("last_name");
				String address=rs.getString("address");
				String phoneNumber=rs.getString("phone_number");
				boolean verified=rs.getBoolean("verified");
				Object olddob=  rs.getObject("dob");
				LocalDate dob=null;
				if(olddob!=null) {

					dob= ((java.sql.Date) olddob).toLocalDate() ;	
				}

				
				//List<Account> accounts= new ArrayList<>();//Going to  add a get all accounts
				
				result=new User(username, accessLevel);
				result.setPasswordHash(passwordHash);
				result.setUserID(userID);
				result.setEmail(email);
				result.setFirstName(fName);
				result.setLastName(lName);
				result.setAddress(address);
				result.setPhoneNumber(phoneNumber);
				result.setVerified(verified);
				result.setDob(dob);	
				List<Account> accounts=  aservice.findByOwner(result);//Going to  add a get all accounts	
				//System.out.println(accounts.toString());
				result.setAccounts(accounts);
				
				
				
				logBot.info("Returned user with username "+ username);
				return result;
			}
			else {
				logBot.info("User doesn't exist");
			}
		} catch (SQLException e) {
			logBot.error("SQL Exception");
			e.printStackTrace();
		}
		
	return null;
	}

}
