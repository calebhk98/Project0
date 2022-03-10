package com.revature;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revature.account.Account;
import com.revature.dataAccessObject.AccountDao;
import com.revature.exception.AccountHasValueException;
import com.revature.exception.NotEnoughAccountsException;
import com.revature.services.AccountService;
import com.revature.user.User;

public class AccountServiceTest {
	private AccountService aserv;
	private AccountDao mockDao;
	
	
	@Before
	public void setup() {
		aserv=new AccountService();
		mockDao=mock(AccountDao.class);
		
		aserv.Idao=mockDao;
		
	}
	
	

	@Test
	public void testfindAll_returnAcoountList() {
		

		User bob=new User();
		bob.setUserID(42);
		
		List<Account> bobsAccounts=new LinkedList<>();
		
		Account a = new Account(bob, 100);
		Account b=new Account(bob, 100);
		
		
		bobsAccounts.add(a);
		bobsAccounts.add(b);		
		
		//bob.setAccounts(bobsAccounts);
		
		when(mockDao.findAll()).thenReturn(bobsAccounts);
		
		List<Account> returnedAccounts =aserv.findAll();
		
		
		assertEquals(bobsAccounts, returnedAccounts);
	}
	
	
	
	@Test
	public void testaddAccount_returnAcoount() {
		
		User bob=new User();
		bob.setUserID(42);
				
		Account a = new Account(bob, 100);
		a.setID(10);
		
		
		
		//bob.setAccounts(bobsAccounts);
		
		when(mockDao.addAccount(a)).thenReturn(a);
		
		Account returnedAccounts =aserv.addAccount(a);
		
		
		assertEquals(a, returnedAccounts);
	}
	
	
	@Test
	public void testUpdate_returnAcoountList() {
		
		User bob=new User();
		bob.setUserID(42);
				
		Account a = new Account(bob, 100);
		a.setID(10);
		
		
		
		//bob.setAccounts(bobsAccounts);
		
		when(mockDao.update(a)).thenReturn(a);
		
		Account returnedAccounts =aserv.update(a);
		
		
		assertEquals(a, returnedAccounts);
	}
	
	
	@Test
	public void testFindByID_returnAcoountList() {
		
		User bob=new User();
		bob.setUserID(42);
				
		Account a = new Account(bob, 100);
		a.setID(10);
		
		
		
		//bob.setAccounts(bobsAccounts);
		
		when(mockDao.findById(a.getID())).thenReturn(a);
		
		Account returnedAccounts =aserv.findById(a.getID());
		
		
		assertEquals(a, returnedAccounts);
	}
	
	
	@Test
	public void testFindByOwner_returnAcoountList() {
		
		User bob=new User();
		bob.setUserID(42);
		
		List<Account> bobsAccounts=new LinkedList<>();
		
		Account a = new Account(bob, 100);
		Account b=new Account(bob, 100);
		
		
		bobsAccounts.add(a);
		bobsAccounts.add(b);
		
		//bob.setAccounts(bobsAccounts);
		
		when(mockDao.findByOwner(bob)).thenReturn(bobsAccounts);
		
		List<Account> returnedAccounts =aserv.findByOwner(bob);
		
		
		assertEquals(bobsAccounts, returnedAccounts);
	}
	

	@Test
	public void testFindByOwnerID_returnAcoountList() {
		
		User bob=new User();
		bob.setUserID(42);
		
		List<Account> bobsAccounts=new LinkedList<>();
		
		Account a = new Account(bob, 100);
		Account b=new Account(bob, 100);
		
		
		bobsAccounts.add(a);
		bobsAccounts.add(b);
		
		//bob.setAccounts(bobsAccounts);
		
		when(mockDao.findByOwnerID(bob.getUserID())).thenReturn(bobsAccounts);
		
		List<Account> returnedAccounts =aserv.findByOwnerID(bob.getUserID());
		
		
		assertEquals(bobsAccounts, returnedAccounts);
	}
	

	@Test
	public void testRemoveAccount_returnBoolean() {
		
		User bob=new User();
		bob.setUserID(42);
		
		
		Account a = new Account(bob, 100);
		
		
		
		
		
		boolean returnedAccounts;
		try {
			when(mockDao.removeAccount(a)).thenReturn(true);
			returnedAccounts = aserv.removeAccount(a);
			assertEquals(true, returnedAccounts);
		} catch (AccountHasValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnoughAccountsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	@Test
	public void testSetBalance_returnInt() {
		
		User bob=new User();
		bob.setUserID(42);
		
		Account a = new Account(bob, 100);
		int x=500;
		
		
		//bob.setAccounts(bobsAccounts);
		
		when(mockDao.setBalance(a,x)).thenReturn(x);
		
		int returnedAccounts =aserv.setBalance(a,x);
		
		
		assertEquals(x, returnedAccounts);
	}
	
	
	
	@After
	public void tearDown() {
		aserv=null;
		mockDao=null;
	}
	
	
	
}
