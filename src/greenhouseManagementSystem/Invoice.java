package greenhouseManagementSystem;

import java.time.*;

public class Invoice {
	//Setting fields of an invoice
	private int invoiceID;
	private double invoiceTotal;
	private LocalDate invoiceDate;
	private LocalDate invoiceDueDate;
	private Boolean paid;
	
	//Constructor for invoice object
	public Invoice(int invoiceID, double invoiceTotal, LocalDate invoiceDate, LocalDate invoiceDueDate) {
		//Assigning values for the invoice object based on constructor
		this.invoiceID = invoiceID;
		this.invoiceTotal = invoiceTotal;
		this.invoiceDate = invoiceDate;
		this.invoiceDueDate = invoiceDueDate;
		this.paid = false;
	}
	
	//Constructor for invoice object from database
	public Invoice(int invoiceID, double invoiceTotal, LocalDate invoiceDate, LocalDate invoiceDueDate, Boolean paid) {
		//Assigning values for the invoice object based on constructor
		this.invoiceID = invoiceID;
		this.invoiceTotal = invoiceTotal;
		this.invoiceDate = invoiceDate;
		this.invoiceDueDate = invoiceDueDate;
		this.paid = paid;
	}
	
	//Getters for fields of an invoice object
	public int getID() {
		return invoiceID;
	}
	
	public double getTotal() {
		return invoiceTotal;
	}
	
	public LocalDate getDate() {
		return invoiceDate;
	}
	
	public LocalDate getDueDate() {
		return invoiceDueDate;
	}
	
	public Boolean getPaid() {
		return paid;
	}
}
