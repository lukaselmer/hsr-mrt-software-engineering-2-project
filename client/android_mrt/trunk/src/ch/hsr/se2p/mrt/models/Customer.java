package ch.hsr.se2p.mrt.models;

import android.provider.BaseColumns;

public class Customer implements BaseColumns {
	private Integer id;
	private String firstName;
	private String lastName;
	
	
	public Customer(Integer id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Integer getId() {
		return id;
	}

	public String toString(){
		return lastName + " " + firstName;
	}
}
