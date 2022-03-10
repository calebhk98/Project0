package com.revature.people;

import java.util.ArrayList;
import java.util.List;

public class Admin extends Person{
	protected static List<Admin> Admins=new ArrayList<Admin>();
	protected int AdminNumber;
	
	public Admin(String username, String password) {
		super(username, password, 2);
		this.AdminNumber=Admins.size();
		Admins.add(this);
	}

}
