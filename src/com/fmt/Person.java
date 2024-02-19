package com.fmt;

import java.util.Comparator;
import java.util.List;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with people, such as constructing, getting, setting, and converting to
 * string
 */

public class Person {
	private final String personCode;
	private final String lastName;
	private final String firstName;
	private final Address address;
	private final Integer addressId;
	private List<String> email;

	public Person(String personCode, String lastName, String firstName, Address address, List<String> email) {
		this.personCode = personCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.address = address;
		this.email = email;
		this.addressId = null;
	}

	public Person(Integer personId, String personCode, String lastName, String firstName, Address address,
			List<String> email) {
		this.personCode = personCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.address = address;
		this.email = email;
		this.addressId = null;
	}

	public Person(Integer personId, String personCode, String lastName, String firstName, Integer addressId) {
		this.personCode = personCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.addressId = addressId;
		this.email = null;
		this.address = null;
	}

	public String toString() {
		return this.personCode + ", " + this.lastName + ", " + this.firstName + ", " + this.address + ", " + this.email;
	}

	public List<String> getEmail() {
		return email;
	}

	public void setEmail(List<String> email) {
		this.email = email;
	}

	public String getPersonCode() {
		return personCode;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public Address getAddress() {
		return address;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public static final Comparator<Person> byCode = new Comparator<>() {
		public int compare(Person p1, Person p2) {
			return p1.getPersonCode().compareTo(p2.getPersonCode());
		}
	};

	public int compareTo(Person otherPerson) {
		return this.personCode.compareTo(otherPerson.personCode);
	}

}
