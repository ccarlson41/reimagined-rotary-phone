package com.fmt;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class formats the input given into XML and JSON files
 */
public class FormatData {

	/**
	 * Makes and formats the people into XML and JSON Files
	 * 
	 * @param outputNameXML
	 * @param outputNameJSON
	 * @param people
	 */
	public static void outputPerson(String outputNameXML, String outputNameJSON, List<Person> people) {

		XStream xstream = new XStream(new DomDriver());

		PrintWriter pw;
		try {
			pw = new PrintWriter(new File(outputNameXML));

			pw.println("<?xml version=\"1.0\" ?>");

			String xml = xstream.toXML(people).replaceAll("list", "persons").replaceAll("email", "emails")
					.replaceAll("string", "email").replaceAll("com.fmt.Person", "person");
			pw.println(xml);

			pw.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PrintWriter pw2;
		try {
			pw2 = new PrintWriter(new File(outputNameJSON));
			String json = gson.toJson(people);
			JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
			String prettyJsonString = gson.toJson(jsonElement);
			pw2.println(prettyJsonString);
			pw2.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}

	}

	/**
	 * Makes and formats the stores into XML and JSON Files
	 * 
	 * @param outputNameXML
	 * @param outputNameJSON
	 * @param Store
	 */
	public static void outputStore(String outputNameXML, String outputNameJSON, List<Store> Store) {

		XStream xstream = new XStream(new DomDriver());

		PrintWriter pw;
		try {
			pw = new PrintWriter(new File(outputNameXML));

			pw.println("<?xml version=\"1.0\" ?>");

			String xml = xstream.toXML(Store).replaceAll("list", "Stores");
			pw.println(xml);

			pw.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PrintWriter pw2;
		try {
			pw2 = new PrintWriter(new File(outputNameJSON));
			String json = gson.toJson(Store);
			JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
			String prettyJsonString = gson.toJson(jsonElement);
			pw2.println(prettyJsonString);
			pw2.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}

	}

	/**
	 * Makes and formats the items into XML and JSON Files
	 * 
	 * @param outputNameXML
	 * @param outPutNameJSON
	 * @param Item
	 */
	public static void outputItem(String outputNameXML, String outPutNameJSON, List<Item> Item) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("equipment", Equipment.class);
		xstream.alias("product", Product.class);
		xstream.alias("service", Service.class);
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File(outputNameXML));

			pw.println("<?xml version=\"1.0\" ?>");

			String xml = xstream.toXML(Item).replaceAll("list", "Items");

			pw.println(xml);

			pw.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PrintWriter pw2;
		try {
			pw2 = new PrintWriter(new File(outPutNameJSON));
			String json = gson.toJson(Item);
			JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
			String prettyJsonString = gson.toJson(jsonElement);
			pw2.println(prettyJsonString);
			pw2.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}

	}

}
