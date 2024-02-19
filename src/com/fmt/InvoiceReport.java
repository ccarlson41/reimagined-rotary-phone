package com.fmt;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class formats the data
 * to make an invoice report
 */
public class InvoiceReport {

	/**
	 * Formats the data to make an Invoice report using Linked Lists.
	 * 
	 * @param <T>
	 * 
	 * @param persons
	 * @param invoices
	 * @param stores
	 */
	public static <T> void outputInvoiceReport(LinkedList<Invoice> byCustomer,LinkedList<Invoice> byTotal, LinkedList<Invoice> byStore, List<Store> stores) {
		String invCode = null;
		String store = null;
		String customer = null;
		String salesPerson = null;
		double total = 0;
		
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Sales by Customer                                                       |");
		System.out.println("+-------------------------------------------------------------------------+)");
		System.out.println("Sale       Store      Customer             Salesperson          Total     )");
		for (Invoice invoice: byCustomer) {
			invCode = invoice.getInvoiceCode();
			store = invoice.getStore().getStoreCode();
			customer = invoice.getCustomer().getLastName() + ", " + invoice.getCustomer().getFirstName();
			salesPerson = invoice.getSalesPerson().getLastName() + ", " + invoice.getSalesPerson().getFirstName();
			total = invoice.getTotal();
			System.out.printf("%-10s %-10s %-19s  %-18s $%6.2f\n", invCode, store, customer, salesPerson, total);
		}

		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Sales by Total                                                      |");
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("Sale       Store      Customer             Salesperson          Total     ");
		for (Invoice invoice: byTotal) {
			invCode = invoice.getInvoiceCode();
			store = invoice.getStore().getStoreCode();
			customer = invoice.getCustomer().getLastName() + ", " + invoice.getCustomer().getFirstName();
			salesPerson = invoice.getSalesPerson().getLastName() + ", " + invoice.getSalesPerson().getFirstName();
			total = invoice.getTotal();
			System.out.printf("%-10s %-10s %-19s  %-18s $%6.2f\n", invCode, store, customer, salesPerson, total);
		}
		

		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Sales by Store                                                       |");
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("Sale       Store      Customer             Salesperson          Total     ");
		for (Invoice invoice: byStore) {
			invCode = invoice.getInvoiceCode();
			store = invoice.getStore().getStoreCode();
			customer = invoice.getCustomer().getLastName() + ", " + invoice.getCustomer().getFirstName();
			salesPerson = invoice.getSalesPerson().getLastName() + ", " + invoice.getSalesPerson().getFirstName();
			total = invoice.getTotal();
			System.out.printf("%-10s %-10s %-19s  %-18s $%6.2f\n", invCode, store, customer, salesPerson, total);
		}

	}
	
	/*
	 * Formats the data to print to the standard output using traditional sorting mathods. 
	 */

	public static void outputInvoiceItems(List<Invoice> invoices, List<Store> stores) {

		Collections.sort(invoices, Collections.reverseOrder(Invoice.byTotal));
		System.out
				.println("+----------------------------------------------------------------------------------------+");
		System.out
				.println("| Summary Report - By Total                                                              |");
		System.out
				.println("+----------------------------------------------------------------------------------------+");
		System.out.println("Invoice #  Store      Customer                       Num Items          Tax       Total");

		int totalItems = 0;
		double totalTax = 0.0;
		double totalTotal = 0.0;

		for (Invoice i : invoices) {
			totalItems += i.getNumItems();
			totalTax += i.getTotalTax();
			totalTotal += i.getTotal();

			String name = i.getCustomer().getLastName() + ", " + i.getCustomer().getFirstName();

			System.out.printf("%-10s %-10s %-29s  %-15d $ %6.2f $ %10.2f\n", i.getInvoiceCode(),
					i.getStore().getStoreCode(), name, i.getNumItems(), i.getTotalTax(), i.getTotal());
		}

		Collections.sort(stores, Store.byLastFirstTotal);
		System.out
				.println("+----------------------------------------------------------------------------------------+");
		System.out.printf("                                                     %d              $%8.2f $  %6.2f\n\n",
				totalItems, totalTax, totalTotal);
		System.out.printf("+----------------------------------------------------------------+\r\n"
				+ "| Store Sales Summary Report                                     |\r\n"
				+ "+----------------------------------------------------------------+\r\n"
				+ "Store      Manager                        # Sales    Grand Total    \n");

		totalTotal = 0.0;
		int totalNumSales = 0;
		for (Store s : stores) {

			totalNumSales += s.getNumItems();
			totalTotal += s.getTotal();

			String managerName = s.getManager().getLastName() + ", " + s.getManager().getFirstName();

			System.out.printf("%-10s %-30s $ %-7d $   %8.2f\n", s.getStoreCode(), managerName, s.getNumItems(),
					s.getTotal());
		}
		System.out.println("+----------------------------------------------------------------+");
		System.out.printf("                                          %d            $  %.2f\n", totalNumSales,
				totalTotal);

		Collections.sort(invoices, Invoice.byInvoiceCode);

		for (Invoice invoice : invoices) {
			System.out.printf("\n\nInvoice  #%s\n", invoice.getInvoiceCode());
			System.out.printf("Store    %s\n", invoice.getStore().getStoreCode());
			System.out.printf("Date      %s\n", invoice.getInvoiceDate());
			System.out.printf("Customer:\n");
			System.out.printf("%s, %s (%s : [%s])\n", invoice.getCustomer().getLastName(),
					invoice.getCustomer().getLastName(), invoice.getCustomer().getPersonCode(),
					invoice.getCustomer().getEmail());
			System.out.printf("	%s\n", invoice.getCustomer().getAddress().getStreet());
			System.out.printf("	%s %s %s %s\n\n", invoice.getCustomer().getAddress().getCity(),
					invoice.getCustomer().getAddress().getState(), invoice.getCustomer().getAddress().getZip(),
					invoice.getCustomer().getAddress().getCountry());
			System.out.printf("Sales Person: \n");
			System.out.printf("%s, %s (%s : [%s])\n", invoice.getSalesPerson().getLastName(),
					invoice.getSalesPerson().getLastName(), invoice.getSalesPerson().getPersonCode(),
					invoice.getSalesPerson().getEmail());
			System.out.printf("	%s\n", invoice.getSalesPerson().getAddress().getStreet());
			System.out.printf("	%s %s %s %s\n\n", invoice.getSalesPerson().getAddress().getCity(),
					invoice.getSalesPerson().getAddress().getState(), invoice.getSalesPerson().getAddress().getZip(),
					invoice.getSalesPerson().getAddress().getCountry());
			System.out.printf("Item                                                               Total\n");
			System.out.printf("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=-\n");

			double total = 0.0;
			double tax = 0.0;
			for (Item item : invoice.getPurchased()) {
				total += item.getTotal();
				tax += item.getTax();
				if (item instanceof EquipmentLeased) {
					System.out.printf("%s	    	(Lease) %s-%s\r\n", item.getCode(), item.getName(),
							item.getModel());
					System.out.printf("	%d days (%s -> %s) @ $%.2f/ 30 days\r\n",
							((EquipmentLeased) item).getDaysLeased(), ((EquipmentLeased) item).getStartDate(),
							((EquipmentLeased) item).getEndDate(), ((EquipmentLeased) item).getThirtyDayFee());
					System.out.printf("                                                              $  %.2f\r\n",
							item.getTotal());

				} else if (item instanceof EquipmentPurchased) {
					System.out.printf("%s          (Purchase) %s-%s \r\n", item.getCode(), item.getName(),
							item.getModel());
					System.out.printf("                                                             $   %.2f\r\n",
							item.getTotal());
				} else if (item instanceof Product) {
					System.out.printf("%s          (Product)  %s\r\n", item.getCode(), item.getName());
					System.out.printf("	%.0f @ $%.2f/%s\r\n", ((Product) item).getAmountPurchased(),
							((Product) item).getUnitPrice(), ((Product) item).getUnit());
					System.out.printf("                                                             $   %.2f\r\n",
							item.getTotal());
				} else {
					System.out.printf("%s          (Service)  %s\r\n", item.getCode(), item.getName());
					System.out.printf("	%.2f @ $%.2f/%s\r\n", ((Service) item).getHoursWorked(),
							((Service) item).getUnit(), ((Service) item).getHourlyRate());
					System.out.printf("                                                             $   %.2f\r\n",
							item.getTotal());
				}
			}

			System.out.printf("                                                             -=-=-=-=-=-\n");
			System.out.printf("                                                    Subtotal $  %.2f\n", total);
			System.out.printf("                                                         Tax $    %.2f\n", tax);
			System.out.printf("                                                 Grand Total $  %.2f\n", total + tax);

		}

	}

	public static <T> void main(String args[]) {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);

		List<Store> stores = LoadDataDB.loadStores();
		List<Invoice> invoices = LoadDataDB.loadInvoices();
		
		LinkedList<Invoice> listByCustomer = new LinkedList<Invoice>(Invoice.byCustomer);
		LinkedList<Invoice> listByTotal = new LinkedList<Invoice>(Invoice.byTotal);
		LinkedList<Invoice> listByStore = new LinkedList<Invoice>(Invoice.byStore);

		for(Invoice i: invoices) {
			listByCustomer.add(i);
			listByTotal.add(i);
			listByStore.add(i);
		}
				
		InvoiceReport.outputInvoiceReport(listByCustomer, listByTotal, listByStore, stores);

	}
}
