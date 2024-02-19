package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with loading data from a database and converting them into different
 * objects
 */

public class LoadDataDB {

	private static Logger log = LogManager.getLogger(LoadDataDB.class);

	/**
	 * Loads in a person from the database using their person ID
	 * 
	 * @param personId
	 * @return
	 */
	public static Person loadPersonById(int personId) {
		Connection conn = ConnectionFactory.conn();
		Person p = null;
		List<String> emails = loadEmailsByPersonId(personId);

		try {
			String query = "select P.personCode, P.lastName, P.firstName, A.street, A.city, A.zip, S.state, C.country from Person P join Address A on P.addressId = A.addressId join State S on S.stateId = A.stateId join Country C on C.countryId = A.countryId where P.personId = ?";
			PreparedStatement ps = conn.prepareCall(query);
			ps.setInt(1, personId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String personCode = rs.getString("personCode");
				String lastName = rs.getString("lastName");
				String firstName = rs.getString("firstName");
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zip");
				String country = rs.getString("country");

				Address a = new Address(street, city, state, zip, country);
				p = new Person(personCode, lastName, firstName, a, emails);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to load person");
			throw new RuntimeException(e);
		}
		return p;
	}

	/**
	 * Loads in a persons emails using their person ID
	 * 
	 * @param personId
	 * @return
	 */
	public static List<String> loadEmailsByPersonId(int personId) {
		List<String> emails = new ArrayList<>();

		Connection conn = ConnectionFactory.conn();
		try {
			String query = "SELECT emails FROM Email E where E.personId = ?";
			PreparedStatement ps = conn.prepareCall(query);
			ps.setInt(1, personId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String email = rs.getString("emails");
				emails.add(email);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return emails;

	}

	/**
	 * Loads in the store using the store ID
	 * 
	 * @param storeId
	 * @return
	 */
	public static Store loadStoreById(int storeId) {
		Connection conn = ConnectionFactory.conn();
		Store s = null;

		try {
			String query = "SELECT S.storeCode, S.managerId, A.city, A.street, A.city, A.zip, St.state, C.country FROM Store S join Address A on S.addressId = A.addressId join State St on St.stateId = A.stateId join Country C on C.countryId = A.countryId where S.storeId = ?";
			PreparedStatement ps = conn.prepareCall(query);
			ps.setInt(1, storeId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String storeCode = rs.getString("storeCode");
				Person manager = loadPersonById(rs.getInt("managerId"));
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zip");
				String country = rs.getString("country");
				Address a = new Address(street, city, state, zip, country);
				s = new Store(storeCode, manager, a);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to load store");
			throw new RuntimeException(e);
		}
		return s;
	}

	/**
	 * Loads in every store, even if they are not in any invoice
	 * 
	 * @return
	 */
	public static List<Store> loadStores() {
		Connection conn = ConnectionFactory.conn();
		List<Store> s = new ArrayList<>();
		
		try {
			String query = "SELECT S.storeCode, S.managerId, A.city, A.street, A.city, A.zip, St.state, C.country FROM Store S join Address A on S.addressId = A.addressId join State St on St.stateId = A.stateId join Country C on C.countryId = A.countryId";
			PreparedStatement ps = conn.prepareCall(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String storeCode = rs.getString("storeCode");
				Person manager = loadPersonById(rs.getInt("managerId"));
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zip");
				String country = rs.getString("country");
				Address a = new Address(street, city, state, zip, country);
				Store store = new Store(storeCode, manager, a);
				s.add(store);
			}
			List<Invoice> i = loadInvoices();

			for (Store store : s) {
				for (Invoice invoice : i) {
					if (store.getStoreCode().equals(invoice.getStore().getStoreCode())) {
						store.getAddInvoice(invoice);
					}
				}
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to load stores list");
			throw new RuntimeException(e);
		}
		return s;
	}

	/**
	 * Loads in all items in an invoice, using the invoice ID
	 * 
	 * @param invoiceId
	 * @return
	 */
	public static List<Item> loadItemsByInvoiceId(int invoiceId) {
		Connection conn = ConnectionFactory.conn();
		List<Item> purchasedItems = new ArrayList<>();
		Item i = null;
		try {
			String query = "Select It.itemCode, It.itemId, It.itemType, It.itemName, IIt.equipmentType, It.model, It.price, IIt.leased30DayFee, IIt.leasedStartDate, IIt.leasedEndDate, It.unit, It.unitPrice, IIt.amountPurchased, It.hourlyRate, IIt.hoursWorked from Item It join InvoiceItem IIt on It.itemId = IIt.itemId join Invoice Inv on Inv.invoiceId = IIt.invoiceId where Inv.invoiceId = ?";
			PreparedStatement ps = conn.prepareCall(query);
			ps.setInt(1, invoiceId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int itemId = rs.getInt("itemId");
				String itemCode = rs.getString("itemCode");
				String itemType = rs.getString("itemType");
				String itemName = rs.getString("itemName");
				if (itemType.equals("E")) {
					String equipmentType = rs.getString("equipmentType");
					String model = rs.getString("model");
					if (equipmentType.equals("P")) {
						double purchasePrice = rs.getDouble("price");
						i = new EquipmentPurchased(itemCode, itemName, model, purchasePrice);
					} else {
						double thirtyDayFee = rs.getDouble("leased30DayFee");
						LocalDate startDate = LocalDate.parse(rs.getString("leasedStartDate"));
						LocalDate endDate = LocalDate.parse(rs.getString("leasedEndDate"));
						i = new EquipmentLeased(itemCode, itemName, model, thirtyDayFee, startDate, endDate);
					}
				} else if (itemType.equals("P")) {
					String unit = rs.getString("unit");
					double unitPrice = rs.getDouble("unitPrice");
					double amountPurchased = rs.getDouble("amountPurchased");
					i = new Product(itemCode, itemName, unit, unitPrice, amountPurchased);
				} else {
					double hourlyRate = rs.getDouble("hourlyRate");
					double hoursWorked = rs.getDouble("hoursWorked");
					i = new Service(itemCode, itemName, hourlyRate, hoursWorked);
				}
				purchasedItems.add(i);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to load invoice items");
			throw new RuntimeException(e);
		}
		return purchasedItems;
	}

	/**
	 * Loads in all Invoices and then adds them to a list
	 * 
	 * @return
	 */
	public static List<Invoice> loadInvoices() {
		List<Invoice> invoices = new ArrayList<>();

		Connection conn = ConnectionFactory.conn();
		Invoice invoice = null;

		try {
			String query = "select invoiceCode, storeId, customerId, salesPersonId, date, invoiceId from Invoice";
			PreparedStatement ps = conn.prepareCall(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String invoiceCode = rs.getString("invoiceCode");
				Store store = LoadDataDB.loadStoreById(rs.getInt("storeId"));
				Person customer = LoadDataDB.loadPersonById(rs.getInt("customerId"));
				Person salesPerson = LoadDataDB.loadPersonById(rs.getInt("salesPersonId"));
				LocalDate date = LocalDate.parse(rs.getString("date"));
				List<Item> purchasedItems = loadItemsByInvoiceId(rs.getInt("invoiceId"));
				invoice = new Invoice(invoiceCode, store, customer, salesPerson, date);

				for (Item i : purchasedItems) {
					invoice.getAddItem(i);
				}
				invoices.add(invoice);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to load invoices");
			throw new RuntimeException(e);
		}
		return invoices;
	}

}
