package com.fmt;

import java.util.ArrayList;
import java.util.List;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023
 */

public class DataConverter {

	/**
	 * This class utilizes the LoadData and FormatData classes to convert CSV
	 * data into XML and JSON data, then it outputs them to separate files
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		List<Person> people = new ArrayList<>();
		people = LoadDataCSV.loadPeople("data/Persons.csv");

		List<Store> stores = new ArrayList<>();
		stores = LoadDataCSV.loadStore("data/Stores.csv", people);

		List<Item> items = new ArrayList<>();
		items = LoadDataCSV.loadItem("data/Items.csv");
		
		FormatData.outputPerson("data/Persons.xml", "data/Persons.json", people);
		FormatData.outputStore("data/Stores.xml", "data/Stores.json", stores);
		FormatData.outputItem("data/Items.xml", "data/Items.json", items);

	}

}