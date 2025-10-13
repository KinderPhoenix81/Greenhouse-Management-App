package greenhouseManagementSystem;

import java.time.*;
import java.util.ArrayList;

public class Order {
	//Setting fields of an order object
	private int orderID;
	private LocalDate orderDate;
	private double orderTotal;
	private ArrayList<ProductItem> orderItems;
	private Boolean received;
	
	//Constructor for an order of items
	public Order(int orderID, LocalDate orderDate, double orderTotal, ArrayList<ProductItem> orderItems) {
		//Create the order object with the specified values in the parameters
		this.orderID = orderID;
		this.orderDate = orderDate;
		this.orderTotal = orderTotal;
		this.orderItems = orderItems;
		received = false;
	}
	
	//Constructor for an order of items coming from the database
	public Order(int orderID, LocalDate orderDate, double orderTotal, ArrayList<ProductItem> orderItems, Boolean received) {
		//Create the order object with the specified values in the parameters
		this.orderID = orderID;
		this.orderDate = orderDate;
		this.orderTotal = orderTotal;
		this.orderItems = orderItems;
		this.received = received;
	}
	
	//Getters for each field of an order
	public int getID() {
		return orderID;
	}
	
	public double getTotal() {
		return orderTotal;
	}
	
	public LocalDate getDate() {
		return orderDate;
	}
	
	public ArrayList<ProductItem> getOrderItems() {
		return orderItems;
	}
	
	public Boolean getOrderStatus() {
		return received;
	}
}
