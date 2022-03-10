package com.revature.people;

import java.util.ArrayList;
import java.util.List;

public class Employee extends Person{
	protected static List<Employee> Employees=new ArrayList<Employee>();
	protected int EmployeeNumber;
	
	public Employee(String username, String password) {
		super(username, password, 1);
		this.EmployeeNumber=Employees.size();
		Employees.add(this);
	}
	
	public void Verify(Person bob) {
		bob.verified=true;
	}

}
