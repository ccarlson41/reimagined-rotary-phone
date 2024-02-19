package com.fmt;

import java.io.File;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with loading data from a CSV file and converting them into different
 * objects
 */

public class LoadDataCSV {

	/**
	 * Loads Each Person from csv file into a list
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<Person> loadPeople(String fileName) {

		List<Person> people = new ArrayList<>();

		try {
			Scanner s = new Scanner(new File(fileName));
			int numLines = Integer.parseInt(s.nextLine());
			for (int i = 0; i < numLines; i++) {
				String nextLine = s.nextLine();
				String tokens[] = nextLine.split(",");

				Address a = new Address(tokens[3], tokens[4], tokens[5], tokens[6], tokens[7]);

				List<String> emails = new ArrayList<>();

				for (int j = 8; j < tokens.length; j++) {
					emails.add(tokens[j]);
				}

				Person p = new Person(tokens[0], tokens[1], tokens[2], a, emails);
				people.add(p);
			}
			s.close();

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return people;
	}

	/**
	 * Loads Each Store from csv file into a list
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<Store> loadStore(String fileName, List<Person> persons) {

		List<Store> Store = new ArrayList<>();

		try {
			Scanner s = new Scanner(new File(fileName));
			int numLines = Integer.parseInt(s.nextLine());
			for (int i = 0; i < numLines; i++) {
				String nextLine = s.nextLine();
				String tokens[] = nextLine.split(",");
				Address a = new Address(tokens[2], tokens[3], tokens[4], tokens[5], tokens[6]);

				Person p = null;
				for (Person person : persons) {
					if (tokens[1].equals(person.getPersonCode())) {
						p = new Person(tokens[1], person.getLastName(), person.getFirstName(), person.getAddress(),
								person.getEmail());
					}
				}

				Store s1 = new Store(tokens[0], p, a);
				Store.add(s1);
			}
			s.close();

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return Store;
	}

	/**
	 * Loads Each Items from csv file into a list
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<Item> loadItem(String fileName) {

		List<Item> items = new ArrayList<>();

		try {
			Scanner s = new Scanner(new File(fileName));
			int numLines = Integer.parseInt(s.nextLine());
			for (int i = 0; i < numLines; i++) {
				String nextLine = s.nextLine();
				String tokens[] = nextLine.split(",");

				if (tokens[1].equals("E")) {
					Item p = new Equipment(tokens[0], tokens[2], tokens[3]);
					items.add(p);
				} else if (tokens[1].equals("P")) {
					Item p = new Product(tokens[0], tokens[2], tokens[3], Double.parseDouble(tokens[4]));
					items.add(p);
				} else if (tokens[1].equals("S")) {
					Item p = new Service(tokens[0], tokens[2], Double.parseDouble(tokens[3]));
					items.add(p);
				}

			}
			s.close();

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		return items;
	}

	/**
	 * Loads Each Invoice from csv file into a list
	 * 
	 * @param fileName
	 * @param stores
	 * @param persons
	 * @return
	 */
	public static List<Invoice> loadInvoice(String fileName, List<Store> stores, List<Person> persons) {

		List<Invoice> invoice = new ArrayList<>();

		try {
			Scanner s1 = new Scanner(new File(fileName));
			int numLines = Integer.parseInt(s1.nextLine());
			for (int j = 0; j < numLines; j++) {
				String nextLine = s1.nextLine();
				String tokens[] = nextLine.split(",");

				Store store = null;
				for (Store s : stores) {
					if (tokens[1].equals(s.getStoreCode())) {
						store = new Store(tokens[1], s.getManager(), s.getAddress());
					}
				}

				Person customer = null;
				for (Person person : persons) {
					if (tokens[2].equals(person.getPersonCode())) {
						customer = new Person(tokens[2], person.getLastName(), person.getFirstName(),
								person.getAddress(), person.getEmail());
					}
				}

				Person salesPerson = null;
				for (Person person : persons) {
					if (tokens[3].equals(person.getPersonCode())) {
						salesPerson = new Person(tokens[1], person.getLastName(), person.getFirstName(),
								person.getAddress(), person.getEmail());
					}
				}

				Invoice i = new Invoice(tokens[0], store, customer, salesPerson, LocalDate.parse(tokens[4]));
				invoice.add(i);
			}

			s1.close();

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		return invoice;
	}

	/**
	 * Loads the invoice items from a csv file into a list
	 * 
	 * @param fileName
	 * @param invoices
	 * @param items
	 * @return
	 */
	public static void loadInvoiceItems(String fileName, List<Invoice> invoices, List<Item> items, List<Store> stores) {

		try {
			Scanner s1 = new Scanner(new File(fileName));
			int numLines = Integer.parseInt(s1.nextLine());
			for (int j = 0; j < numLines; j++) {

				String nextLine = s1.nextLine();
				String tokens[] = nextLine.split(",");

				Item e = null;
				for (Item item : items) {

					if (tokens[1].equals(item.getCode())) {

						if (item instanceof Equipment && tokens[2].equals("P")) {
							e = new EquipmentPurchased(item.getCode(), item.getName(), item.getModel(),
									Double.parseDouble(tokens[3]));
						} else if ((item instanceof Equipment && tokens[2].equals("L"))) {
							e = new EquipmentLeased(item.getCode(), item.getName(), item.getModel(),
									Double.parseDouble(tokens[3]), LocalDate.parse(tokens[4]),
									LocalDate.parse(tokens[5]));
						} else if (item instanceof Product) {
							e = new Product(item.getCode(), item.getName(), item.getUnit(), item.getUnitPrice(),
									Double.parseDouble(tokens[2]));
						} else {
							e = new Service(item.getCode(), item.getName(), item.getHourlyRate(),
									Double.parseDouble(tokens[2]));
						}

						for (Invoice invoice : invoices) {
							if (tokens[0].equals(invoice.getInvoiceCode())) {
								invoice.getAddItem(e);
							}

						}

					}
				}

			}
			for (Store store : stores) {
				for (Invoice invoice : invoices) {
					if (store.getStoreCode().equals(invoice.getStore().getStoreCode())) {
						store.getAddInvoice(invoice);
					}
				}
			}
			s1.close();

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
