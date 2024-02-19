package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	private static Logger log = LogManager.getLogger(LoadDataDB.class);

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		try {
			Connection conn = ConnectionFactory.conn();

			String query = "delete from InvoiceItem";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.executeUpdate();
			query = "delete from Item";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			query = "delete from Invoice";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			query = "delete from Store";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			query = "delete from Email";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			query = "delete from Person";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			query = "delete from Address";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			query = "delete from State";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			query = "delete from Country";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			ps.close();
			conn.close();

			log.info("Database cleared successfully.");
		} catch (SQLException e) {
			log.error("ERR: Failure to clear database");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city,
			String state, String zip, String country) {
		Connection conn = ConnectionFactory.conn();
		int addressId = InvoiceData.addAddress(street, city, state, zip, country);
		try {
			String query = "select personId from Person where personCode = ? and lastName = ? and firstName = ? and addressId = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.setString(2, lastName);
			ps.setString(3, firstName);
			ps.setInt(4, addressId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				log.info("Person check successful: Person exists already");
			} else {
				query = "INSERT INTO Person (personCode, lastName, firstName, addressId) VALUES (?, ?, ?, ?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, personCode);
				ps.setString(2, lastName);
				ps.setString(3, firstName);
				ps.setInt(4, addressId);
				ps.executeUpdate();
				log.info("Person added successfully.");
			}
			conn.close();
			ps.close();
			rs.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to add Person (from addPerson)");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 *
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		// TODO: implement

	}

	/**
	 * Adds a store record to the database managed by the person identified by the
	 * given code.
	 *
	 * @param storeCode
	 * @param managerCode
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addStore(String storeCode, String managerCode, String street, String city, String state,
			String zip, String country) {
		int managerId = -1;
		Connection conn = ConnectionFactory.conn();
		int addressId = InvoiceData.addAddress(street, city, state, zip, country);
		// Fetches the personId given the managerCode
		try {
			String query = "select personId from Person where personCode = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, managerCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				managerId = rs.getInt("personId");
			}
			// If no rs.next(), the flag value for an error is -1.
			ps.close();
			rs.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to fetch managerId");
			throw new RuntimeException(e);
		}

		try {
			String query = "select storeId from Store where storeCode = ? and managerId = ? and addressId = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, storeCode);
			ps.setInt(2, managerId);
			ps.setInt(3, addressId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				log.info("Store check successful: Store exists already");
			} else {
				query = "INSERT INTO Store (addressId, managerId, storeCode) VALUES (?, ?, ?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, addressId);
				ps.setInt(2, managerId);
				ps.setString(3, storeCode);
				ps.executeUpdate();
				log.info("Store added successfully.");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to add store");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a product record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>unit</code> and <code>pricePerUnit</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addProduct(String code, String name, String unit, double pricePerUnit) {

		Connection conn = ConnectionFactory.conn();
		try {
			String query = "select itemId from Item where ItemCode = ? and itemName = ? and unit = ? and unitPrice = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, code);
			ps.setString(2, name);
			ps.setString(3, unit);
			ps.setDouble(4, pricePerUnit);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				log.info("Item check successful: Item exists already");
			} else {
				query = "INSERT INTO Item (itemCode, itemType, itemName, unit, price, unitPrice) VALUES (?, ?, ?, ?, ?, ?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, "P");
				ps.setString(3, name);
				ps.setString(4, unit);
				ps.setDouble(5, pricePerUnit);
				ps.setDouble(6, pricePerUnit);
				ps.executeUpdate();
				log.info("Item added successfully.");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to add Item");
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds an equipment record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>modelNumber</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */
	public static void addEquipment(String code, String name, String modelNumber) {
		Connection conn = ConnectionFactory.conn();

		try {
			String query = "select itemId from Item where ItemCode = ? and itemName = ? and model = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, code);
			ps.setString(2, name);
			ps.setString(3, modelNumber);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				log.info("Item check successful: Item exists already");
			} else {
				query = "INSERT INTO Item (itemCode, itemType, itemName, model) VALUES (?, ?, ?, ?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, "E");
				ps.setString(3, name);
				ps.setString(4, modelNumber);
				ps.executeUpdate();
				log.info("Item added successfully.");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to add Item");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a service record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>costPerHour</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */
	public static void addService(String code, String name, double costPerHour) {

		Connection conn = ConnectionFactory.conn();

		try {
			String query = "select itemId from Item where ItemCode = ? and itemName = ? and hourlyRate = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, code);
			ps.setString(2, name);
			ps.setDouble(3, costPerHour);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				log.info("Item check successful: Item exists already");
			} else {
				query = "INSERT INTO Item (itemCode, itemType, itemName, hourlyRate) VALUES (?, ?, ?, ?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, "S");
				ps.setString(3, name);
				ps.setDouble(4, costPerHour);
				ps.executeUpdate();
				log.info("Item added successfully.");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to add Item");
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds an invoice record to the database with the given data.
	 *
	 * @param invoiceCode
	 * @param storeCode
	 * @param customerCode
	 * @param salesPersonCode
	 * @param invoiceDate
	 */
	public static void addInvoice(String invoiceCode, String storeCode, String customerCode, String salesPersonCode,
			String invoiceDate) {

		int customerId = -1;
		Connection conn = ConnectionFactory.conn();

		// Fetches the personId given the managerCode
		try {
			String query = "select personId from Person where personCode = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, customerCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				customerId = rs.getInt("personId");
			}
			// If no rs.next(), the flag value for an error is -1.
			ps.close();
			rs.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to fetch customerId in invoice");
			throw new RuntimeException(e);
		}

		int salesPersonId = -1;
		// Fetches the personId given the managerCode
		try {
			String query = "select personId from Person where personCode = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, salesPersonCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				salesPersonId = rs.getInt("personId");
			}
			// If no rs.next(), the flag value for an error is -1.
			ps.close();
			rs.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to fetch salesPersonId in invoice");
			throw new RuntimeException(e);
		}

		int storeId = -1;
		// Fetches the storeId given the managerCode
		try {
			String query = "select storeId from Store where storeCode = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, storeCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				storeId = rs.getInt("storeId");
			}
			// If no rs.next(), the flag value for an error is -1.
			ps.close();
			rs.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to fetch storeId in invoice");
			throw new RuntimeException(e);
		}

		try {
			String query = "Select invoiceId from Invoice where customerId = ? and salesPersonId = ? and invoiceCode = ? and storeId = ? and date = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, customerId);
			ps.setInt(2, salesPersonId);
			ps.setString(3, invoiceCode);
			ps.setInt(4, storeId);
			ps.setString(5, invoiceDate);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				query = "INSERT INTO Invoice (invoiceCode, storeId, customerId, salesPersonId, date) VALUES (?, ?, ?, ?, ?)";
				ps = conn.prepareStatement(query);
				ps.setString(1, invoiceCode);
				ps.setInt(2, storeId);
				ps.setInt(3, customerId);
				ps.setInt(4, salesPersonId);
				ps.setString(5, invoiceDate);
				ps.executeUpdate();
				log.info("Invoice added successfully.");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to add Invoice");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular product (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified quantity.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param quantity
	 */
	public static void addProductToInvoice(String invoiceCode, String itemCode, int quantity) {
		
		int invoiceId = -1;
		int itemId = -1;
		Connection conn = ConnectionFactory.conn();
		
		try {
			String query = "select invoiceId from Invoice where invoiceCode = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				invoiceId = rs.getInt("invoiceId");
			}
			// If no rs.next(), the flag value for an error is -1.
			ps.close();
			rs.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to fetch InvoiceId in invoice");
			throw new RuntimeException(e);
		}
		
		try {
			String query = "select itemId from Item where itemCode = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			}
			// If no rs.next(), the flag value for an error is -1.
			ps.close();
			rs.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to fetch itemId in item");
			throw new RuntimeException(e);
		}
		
		
		try {
			String query = "select invoiceItemId from InvoiceItem where invoiceId = ? and itemId = ? and amountPurchased = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setInt(3, quantity);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				query = "INSERT INTO InvoiceItem (invoiceId, itemId, amountPurchased) VALUES (?, ?, ?)";
				ps = conn.prepareStatement(query);
				ps.setInt(1, invoiceId);
				ps.setInt(2, itemId);
				ps.setInt(3, quantity);
				ps.executeUpdate();
				log.info("InvoiceItem added successfully.");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to add InvoiceItem");
			throw new RuntimeException(e);
		}
		

	}

	/**
	 * Adds a particular equipment <i>purchase</i> (identified by
	 * <code>itemCode</code>) to a particular invoice (identified by
	 * <code>invoiceCode</code>) at the given <code>purchasePrice</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param purchasePrice
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double purchasePrice) {
			
		int invoiceId = -1;
		int itemId = -1;
		Connection conn = ConnectionFactory.conn();
		
		try {
			String query = "select invoiceId from Invoice where invoiceCode = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				invoiceId = rs.getInt("invoiceId");
			}
			// If no rs.next(), the flag value for an error is -1.
			ps.close();
			rs.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to fetch InvoiceId in invoice");
			throw new RuntimeException(e);
		}
		
		try {
			String query = "select itemId from Item where itemCode = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			}
			// If no rs.next(), the flag value for an error is -1.
			ps.close();
			rs.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to fetch itemId in item");
			throw new RuntimeException(e);
		}
		
		try {
			String query = "select invoiceItemId from InvoiceItem where invoiceId = ? and itemId = ? and purchasedPrice = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, purchasePrice);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				query = "INSERT INTO InvoiceItem (invoiceId, itemId, purchasedPrice, equipmentType) VALUES (?, ?, ?, ?)";
				ps = conn.prepareStatement(query);
				ps.setInt(1, invoiceId);
				ps.setInt(2, itemId);
				ps.setDouble(3, purchasePrice);
				ps.setString(4, "P");
				ps.executeUpdate();
				log.info("InvoiceItem added successfully.");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to add InvoiceItem");
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a particular equipment <i>lease</i> (identified by
	 * <code>itemCode</code>) to a particular invoice (identified by
	 * <code>invoiceCode</code>) with the given 30-day <code>periodFee</code> and
	 * <code>beginDate/endDate</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param amount
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double periodFee, String beginDate,
			String endDate) {
		
	int invoiceId = -1;
	int itemId = -1;
	Connection conn = ConnectionFactory.conn();
	
	try {
		String query = "select invoiceId from Invoice where invoiceCode = ?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, invoiceCode);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			invoiceId = rs.getInt("invoiceId");
		}
		// If no rs.next(), the flag value for an error is -1.
		ps.close();
		rs.close();
	} catch (SQLException e) {
		log.error("ERR: Failure to fetch InvoiceId in invoice");
		throw new RuntimeException(e);
	}
	
	try {
		String query = "select itemId from Item where itemCode = ?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, itemCode);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			itemId = rs.getInt("itemId");
		}
		// If no rs.next(), the flag value for an error is -1.
		ps.close();
		rs.close();
	} catch (SQLException e) {
		log.error("ERR: Failure to fetch itemId in item");
		throw new RuntimeException(e);
	}
	
	try {
		String query = "select invoiceItemId from InvoiceItem where invoiceId = ? and itemId = ? and leased30DayFee = ? and leasedStartDate = ? and leasedEndDate = ?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1, invoiceId);
		ps.setInt(2, itemId);
		ps.setDouble(3, periodFee);
		ps.setString(4, beginDate);
		ps.setString(5, endDate);
		ResultSet rs = ps.executeQuery();

		if (!rs.next()) {
			query = "INSERT INTO InvoiceItem (invoiceId, itemId, leased30DayFee, leasedStartDate, leasedEndDate, equipmentType ) VALUES (?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, periodFee);
			ps.setString(4, beginDate);
			ps.setString(5, endDate);
			ps.setString(6, "L");
			ps.executeUpdate();
			log.info("InvoiceItem added successfully.");
		}
		rs.close();
		ps.close();
		conn.close();
	} catch (SQLException e) {
		log.error("ERR: Failure to add InvoiceItem");
		throw new RuntimeException(e);
	}


	}

	/**
	 * Adds a particular service (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified number of hours.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param billedHours
	 */
	public static void addServiceToInvoice(String invoiceCode, String itemCode, double billedHours) {
		
	int invoiceId = -1;
	int itemId = -1;
	Connection conn = ConnectionFactory.conn();
	
	try {
		String query = "select invoiceId from Invoice where invoiceCode = ?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, invoiceCode);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			invoiceId = rs.getInt("invoiceId");
		}
		// If no rs.next(), the flag value for an error is -1.
		ps.close();
		rs.close();
	} catch (SQLException e) {
		log.error("ERR: Failure to fetch InvoiceId in invoice");
		throw new RuntimeException(e);
	}
	
	try {
		String query = "select itemId from Item where itemCode = ?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, itemCode);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			itemId = rs.getInt("itemId");
		}
		// If no rs.next(), the flag value for an error is -1.
		ps.close();
		rs.close();
	} catch (SQLException e) {
		log.error("ERR: Failure to fetch itemId in item");
		throw new RuntimeException(e);
	}
	
	try {
		String query = "select invoiceItemId from InvoiceItem where invoiceId = ? and itemId = ? and hoursWorked = ?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1, invoiceId);
		ps.setInt(2, itemId);
		ps.setDouble(3, billedHours);
		ResultSet rs = ps.executeQuery();

		if (!rs.next()) {
			query = "INSERT INTO InvoiceItem (invoiceId, itemId, hoursWorked) VALUES (?, ?, ?)";
			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, billedHours);
			ps.executeUpdate();
			log.info("InvoiceItem added successfully.");
		}
		rs.close();
		ps.close();
		conn.close();
	} catch (SQLException e) {
		log.error("ERR: Failure to add InvoiceItem");
		throw new RuntimeException(e);
	}



	}

	/**
	 * Adds an address to the database : Helper method for AddPerson
	 * 
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static int addAddress(String street, String city, String state, String zip, String country) {
		int addressId = -1;
		Connection conn = ConnectionFactory.conn();
		int countryId = InvoiceData.addCountry(country);
		int stateId = InvoiceData.addState(state);
		try {
			String query = "Select addressId from Address where street = ? and city = ? and zip = ? and stateId = ? and countryId = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, zip);
			ps.setInt(4, stateId);
			ps.setInt(5, countryId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				addressId = rs.getInt("addressId");
				log.info("Address check successful: Address exists already");
			} else {
				query = "INSERT INTO Address (street, city, zip, stateId, countryId) VALUES (?, ?, ?, ?, ?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, zip);
				ps.setInt(4, stateId);
				ps.setInt(5, countryId);
				ps.executeUpdate();
				ResultSet generatedKeys = ps.getGeneratedKeys();
				generatedKeys.next();
				addressId = generatedKeys.getInt(1);
				log.info("Address added successfully.");
			}
			conn.close();
			ps.close();
			rs.close();
		} catch (SQLException e) {
			log.error("ERR: Failure to add Address (from addPerson)");
			throw new RuntimeException(e);
		}
		return addressId;
	}

	/**
	 * This helper function checks if a country exists already.
	 * 
	 * @param country
	 * @return
	 */
	public static int addCountry(String country) {
		Connection conn = ConnectionFactory.conn();
		int countryId = -1;
		try {
			String query = "Select countryId from Country where country = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, country);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				countryId = rs.getInt("countryId");
				rs.close();
			} else {
				rs.close();
				query = "INSERT INTO Country (country) VALUES (?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, country);
				ps.executeUpdate();
				
				ResultSet generatedKeys = ps.getGeneratedKeys();
				generatedKeys.next();
				countryId = generatedKeys.getInt(1);
								
				generatedKeys.close();	
				
			}
		
		
			ps.close();
			
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: Country check unsuccessful");
			throw new RuntimeException(e);
		}
		return countryId;
	}

	/**
	 * This helper function checks if a state exists already.
	 * 
	 * @param country
	 * @return
	 */
	public static int addState(String state) {
		Connection conn = ConnectionFactory.conn();
		int stateId = -1;
		try {
			String query = "Select stateId from State where state = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, state);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				stateId = rs.getInt("stateId");
			} else if (stateId == -1) {
				query = "INSERT INTO State (state) VALUES (?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, state);
				ps.executeUpdate();
				ResultSet generatedKeys = ps.getGeneratedKeys();
				generatedKeys.next();
				stateId = generatedKeys.getInt(1);
								
				generatedKeys.close();	
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			log.error("ERR: State check unsuccessful");
			throw new RuntimeException(e);
		}
		return stateId;
	}
}
