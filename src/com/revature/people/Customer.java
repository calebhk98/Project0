package com.revature.people;

import java.util.ArrayList;
import java.util.List;

public class Customer extends Person{
	protected static List<Customer> Customers=new ArrayList<Customer>();
	protected int CustomerNumber;
	
	public Customer(String username, String password) {
		super(username, password, 0);
		this.CustomerNumber=Customers.size();
		Customers.add(this);
	}
	
	
}
