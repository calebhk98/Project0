package com.revature;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revature.account.Account;
import com.revature.dataAccessObject.UserDao;
import com.revature.exception.MaxSecurityLevelException;
import com.revature.exception.MinSecurityLevelException;
import com.revature.services.UserService;
import com.revature.user.User;

public class UserServiceTest {

	private UserService userv;
	private UserDao mockDao;
	private User dummy;

	@Before
	public void setup() {
		userv = new UserService();
		mockDao =mock(UserDao.class);		
		userv.Idao = mockDao;
		dummy=new User();
		dummy.setAccounts(new LinkedList <Account>());
		dummy.setUserID(5);
		
	}
	@Test
	public void toRegisterUser_returnNewPkAsId(){
		
		
		try {
			dummy=new User("spongebob");
			dummy.setAccessLevel(0);
			Random r =new Random();
			int fakePk=r.nextInt(100);
			dummy.setUserID(fakePk);
			
			when (mockDao.createNewUser(dummy)).thenReturn(dummy);
			User registeredUser = userv.register(dummy);
			//System.out.println(registeredUser.getId());
			
			assertEquals(dummy, registeredUser);
			
			userv.register(dummy);
			
		} catch (MaxSecurityLevelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MinSecurityLevelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

	/*@Test(expected=RegisterUserFailedException.class)
	public void test_name(){
		dummy.setId(1);
		
		when(mockDao.findByUsername(dummy.getUsername())).thenReturn(dummy);
		
		userv.register(dummy);
	}
	
	@Test
	public void toRegisterUser_returnNewPkAsId(){
		dummy=new User(0,"spongebob","pass", "Customer", new LinkedList<Integer>());
		
		Random r =new Random();
		int fakePk=r.nextInt(100);
		System.out.println(fakePk);

		when(mockDao.findByUsername(dummy.getUsername())).thenReturn(new User());
		
		when (mockDao.insert(dummy)).thenReturn(fakePk);
		
		User registeredUser = userv.register(dummy);
		System.out.println(registeredUser.getId());
		
		assertEquals(fakePk, registeredUser.getId());
		
		userv.register(dummy);
	}*/
	
	
	
	@After
	public void tearDown() {		
		userv=null;
		mockDao=null;
		dummy=null;		
	}
	
}
