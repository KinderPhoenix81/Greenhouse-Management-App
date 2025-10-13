package greenhouseManagementSystem;

//Imports
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

//Class used to act as a storage area for all components of the project
//Only one instance of this class will be created, as that's all that is needed
public class Management {
	//Creating fields for this class
	private ArrayList<Employee> employeeList;
	private ArrayList<Employee> onDutyEmployeeList;
	private ArrayList<Order> allOrders;
	private ArrayList<Order> outgoingOrders;
	private ArrayList<Order> receivedOrders;
	private ArrayList<Invoice> allInvoices;
	private ArrayList<Invoice> unpaidInvoices;
	private ArrayList<Invoice> paidInvoices;
	private ArrayList<ProductItem> fullInventory;
	private ArrayList<ProductItem> currentInventory;
	private ArrayList<ProductItem> seasonalInventory;
	private ArrayList<ProductItem> clearanceInventory;

	//Database fields
	private String filePath = "jdbc:sqlite:GreenhouseManagement";
	private Connection dbConn;
	
	//Parameterless constructor for management object
	public Management() {
		//Initializing all lists to be empty lists
		employeeList = new ArrayList<Employee>();
		onDutyEmployeeList = new ArrayList<Employee>();
		allOrders = new ArrayList<Order>();
		outgoingOrders = new ArrayList<Order>();
		receivedOrders = new ArrayList<Order>();
		allInvoices = new ArrayList<Invoice>();
		unpaidInvoices = new ArrayList<Invoice>();
		paidInvoices = new ArrayList<Invoice>();
		fullInventory = new ArrayList<ProductItem>();
		currentInventory = new ArrayList<ProductItem>();
		seasonalInventory = new ArrayList<ProductItem>();
		clearanceInventory = new ArrayList<ProductItem>();
		
		//Filling the lists with initialization methods
		this.InitialLoadEmployees(employeeList);
		this.InitialLoadOnDutyEmployees(employeeList);
		this.InitialLoadInventory(fullInventory);
		this.InitialLoadCurrentInventory(fullInventory);
		this.InitialLoadSeasonalInventory(fullInventory);
		this.InitialLoadClearanceInventory(fullInventory);
		this.InitialLoadOrders(allOrders, fullInventory);
		this.InitialLoadOrderStatusLists(allOrders);
		this.InitialLoadInvoices(allInvoices);
		this.InitialLoadInvoicePaymentLists(allInvoices);
	}
	
	//Connect to the database
	public Connection ConnectDatabase() {
		try {
			//Attempt connection
			Connection connection = DriverManager.getConnection(filePath);
			
			//Return connection
			return connection;
			
			//Error with database
		} catch (SQLException e) {
			//Catch errors
			System.out.println("There was an issue connecting to the database.");
			e.printStackTrace();
		}
		
		//Return a blank connection
		return dbConn;
	}
	
	//Load employees from the database and populate the array list
	public void InitialLoadEmployees(ArrayList<Employee> list) {
		//Make the connection
		Connection conn = this.ConnectDatabase();
		//Try catch to select data
		try {
			//Create statement
			Statement statement = conn.createStatement();
			String sql = "Select * from Employee";
			
			//Create a result set
			ResultSet result = statement.executeQuery(sql);
			
			//Assign the result set to an employee for each result
			while(result.next()) {
				//Create fields
				int id = result.getInt("EmployeeID");
				String fName = result.getString("FirstName");
				String lName = result.getString("LastName");
				String phone = result.getString("Phone");
				String email = result.getString("Email");
				int dateOfBirth = result.getInt("DateOfBirth");
				String employStat = result.getString("EmploymentStatus");
				String position = result.getString("Position");
				Boolean onDuty = Boolean.parseBoolean(result.getString("OnDuty"));
				String openBase = result.getString("Opener");
				String closeBase = result.getString("Closer");
				Double wage = result.getDouble("Wage");
				
				//Parse booleans
				Boolean open = Boolean.parseBoolean(openBase);
				Boolean close = Boolean.parseBoolean(closeBase);
				
				//Parse date
				String orderDateString = String.valueOf(dateOfBirth);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				LocalDate dob = LocalDate.parse(orderDateString, formatter);
				
				//Create an employee based on this info
				Employee employee = new Employee(fName + " " + lName, phone, email, id, dob, employStat, position, wage, open, close, onDuty);
				
				//Add this employee to the overall list
				list.add(employee);

			}
			
		} catch (SQLException e) {
			//Print errors
			e.printStackTrace();
		}
		//Debug
		//System.out.println("Employee Count: " + employeeList.size());
	}
	
	//Place all employees that are on duty into the on duty list
	public void InitialLoadOnDutyEmployees(ArrayList<Employee> list) {
		//Scan the employee list
		for(Employee employee : list) {
			
			//Check to see their on duty value
			if(employee.getOnDuty() == true) {
				//Add the employee to the list
				onDutyEmployeeList.add(employee);
			}
		}
		
		//Debug
		//System.out.println("On Duty Employees: " + onDutyEmployeeList.size() + "\n---------------------\n");
	}
	
	//Load product items and put them into the inventory
	public void InitialLoadInventory(ArrayList<ProductItem> list) {
		//Make a connection
		Connection conn = this.ConnectDatabase();
		//Try catch to manage data
		try {
			//Create statement
			Statement statement = conn.createStatement();
			String sql = "Select ProductID, ProductName, Category, Round(OrderCost, 2) as Cost, Round(PurchasePrice, 2) as Price, "
					+ "QuantityInStock, Perishable, InStock, ClearanceItem, SeasonalItem from ProductItem";
			
			//Create a result set
			ResultSet result = statement.executeQuery(sql);
			
			//Assign the result set to a product for each result
			while(result.next()) {
				//Create fields
				int id = result.getInt("ProductID");
				String name = result.getString("ProductName");
				String category = result.getString("Category");
				Double orderCost = result.getDouble("Cost");
				Double purchase = result.getDouble("Price");
				int quantity = result.getInt("QuantityInStock");
				Boolean perish = Boolean.parseBoolean(result.getString("Perishable"));
				Boolean stock = Boolean.parseBoolean(result.getString("InStock"));
				Boolean clearance = Boolean.parseBoolean(result.getString("ClearanceItem"));
				Boolean seasonal = Boolean.parseBoolean(result.getString("SeasonalItem"));

				//Create a product item based on above info
				ProductItem item = new ProductItem(name, category, id, quantity, orderCost, purchase, perish, stock, clearance, seasonal);
				
				//Add to the list
				list.add(item);
			}
		} catch (SQLException e) {
			//Print error
			e.printStackTrace();
		}
		
	//Debug
	//System.out.println("Inventory Item Count: " + fullInventory.size());
	}
	
	//Place all current inventory items into the current inventory list
	public void InitialLoadCurrentInventory(ArrayList<ProductItem> list) {
		//Search the list
		for(ProductItem product : list) {
			//Check the in stock quantity
			if(product.getStockNum() > 0) {
				//Add to the list
				currentInventory.add(product);
			}
		}
		
		//Debug
		//System.out.println("Current inventory items: " + currentInventory.size());
	}
		
	//Place all seasonal inventory itmes into the seasonal list
	public void InitialLoadSeasonalInventory(ArrayList <ProductItem> list) {
		//Search the list
		for(ProductItem product : list) {
			//Check the seasonal boolean
			if(product.getSeasonalStatus() == true) {
				//Add to the list
				seasonalInventory.add(product);
			}
		}
		
		//Debug
		//System.out.println("Seasonal inventory items: " + seasonalInventory.size());
	}
	
	//Place all clearance inventory items into the seasonal list
	public void InitialLoadClearanceInventory(ArrayList <ProductItem> list) {
		//Search list
		for(ProductItem product : list) {
			//Check clearance boolean
			if(product.getClearanceStatus() == true) {
				//Add to list
				clearanceInventory.add(product);
			}
		}
		
		//Debug
		//System.out.println("Clearance inventory items: " + clearanceInventory.size()  + "\n---------------------\n");
	}
	
	
	//Load order and order items to populate those orders with items
	public void InitialLoadOrders(ArrayList<Order> list, ArrayList<ProductItem> inventory) {
		//Make a connection
		Connection conn  = this.ConnectDatabase();
		
		//Try catch for errors
		try {
			//Create a statement
			Statement loadOrders = conn.createStatement();
			String sqlOrders = "Select OrderID, Round(OrderTotal, 2) as Total, OrderDate, Received from [Order]";
			
			//Create a result set
			ResultSet orderResults = loadOrders.executeQuery(sqlOrders);
			
			//While there are orders present in the system, get some info
			while(orderResults.next()) {
				//Create fields
				int id = orderResults.getInt("OrderID");
				double total = orderResults.getDouble("Total");
				int date = orderResults.getInt("OrderDate");
				String receivedBase = orderResults.getString("Received");
				
				//Parse the boolean-- issue existed where all values would be false, regardless if supposed to be true
				Boolean received = Boolean.parseBoolean(receivedBase.toLowerCase());
				
				//Parse the date
				String orderDateString = String.valueOf(date);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				LocalDate orderDate = LocalDate.parse(orderDateString, formatter);
				
				//Create a list for product items, we need this to instantiate an order
				ArrayList<ProductItem> orderItems = new ArrayList<ProductItem>();
				
				//Create a new sql query
				Statement loadOrderItems = conn.createStatement();
				String sqlOrderItems = "Select ProductID, Quantity, Round(OrderPrice, 2) as Price from OrderItems Where OrderID = " + id;
				
				//Create a new result set
				ResultSet orderItemResults = loadOrderItems.executeQuery(sqlOrderItems);
				
				//While there are items in this specific order...
				while(orderItemResults.next()) {
					//Get the necessary fields for list of order items
					int productID = orderItemResults.getInt("ProductID");
					int quantity = orderItemResults.getInt("Quantity");
					double price = orderItemResults.getDouble("Price");
					
					//Loop through product list in inventory
					for(ProductItem orderItem : inventory) {
						//Looking to match the inventory item to the order item by product id
						if(orderItem.getProductID() == productID) {
							//The match has been successful, create a new product item for the existing order
							ProductItem orderedItem = new ProductItem(productID, price, quantity);
							//Add this product item to the current order
							orderItems.add(orderedItem);
							
							//Debug
							//System.out.println("Order: " + id + "; Ordered Item: " + productID);
						}
					}
				}
				//Debug
				//System.out.println("");
				
				//Now we have everything to create an order, and add it to the total orders list
				Order order = new Order(id, orderDate, total, orderItems, received);
				list.add(order);
			}
			
			//Debug
			//System.out.println("Total Orders: " + allOrders.size());
			
		} catch (SQLException e) {
			//Print out errors
			e.printStackTrace();
		}
	}
	
	//Load orders that have different received values
	public void InitialLoadOrderStatusLists(ArrayList<Order> list) {
		//Check to see the received condition for each order
		for(Order order : list) {
			if(order.getOrderStatus() == true) {
				//Assign to received list
				receivedOrders.add(order);
			} else {
				//Assign to outgoing list
				outgoingOrders.add(order);
			}
		}
		//Debug
		//System.out.println("Received orders: " + receivedOrders.size() + "\nOutgoing orders: " + outgoingOrders.size() + "\n---------------------\n");
		
	}
	
	//Load invoices and populate the invoices lists
	public void InitialLoadInvoices(ArrayList<Invoice> list) {
		//Make a connection
		Connection conn = this.ConnectDatabase();
		//Try catch for errors
		try {
			//Create a statement
			Statement statement = conn.createStatement();
			String sql = "Select InvoiceID, ROUND(InvoiceTotal, 2) as Total, InvoiceDate, InvoiceDueDate, Paid from Invoice";
			
			//Create a result set
			ResultSet results = statement.executeQuery(sql);
			
			//While there is an invoice present in the results
			while(results.next()) {
				//Create fields
				int id = results.getInt("InvoiceID");
				double total = results.getDouble("Total");
				int date = results.getInt("InvoiceDate");
				int dueDate = results.getInt("InvoiceDueDate");
				String paidBase = results.getString("Paid");
				
				//Parse boolean
				Boolean paid = Boolean.parseBoolean(paidBase);
				
				//Parse the dates
				String invoiceDateString = String.valueOf(date);
				String invoiceDueDateString = String.valueOf(dueDate);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				LocalDate invoiceDate = LocalDate.parse(invoiceDateString, formatter);
				LocalDate invoiceDueDate = LocalDate.parse(invoiceDueDateString, formatter);
				
				//Create an invoice object
				Invoice invoice = new Invoice(id, total, invoiceDate, invoiceDueDate, paid);
				
				//Add to the list
				list.add(invoice);
			}
			
			//Debug
			//System.out.println("Total invoices: " + allInvoices.size());
			
		} catch (SQLException e) {
			//Print errors
			e.printStackTrace();
		}
	}
	
	//Load the paid and unpaid invoices into their respective lists
	public void InitialLoadInvoicePaymentLists(ArrayList<Invoice> list) {
		//Loop through the list
		for(Invoice invoice : list) {
			//Check the value
			if(invoice.getPaid() == true) {
				//Add to paid invoice list
				paidInvoices.add(invoice);
			} else {
				//Add to unpaid invoice list
				unpaidInvoices.add(invoice);
			}
		}
		
		//Debug
		//System.out.println("Paid invoices: " + paidInvoices.size() + "\nUnpaid Invoices: " + unpaidInvoices.size());
	}
	
	//Unloads all employee lists and the on duty composite
	public void UnloadEmployees(Composite inner) {
		//Clear the contents of the lists
		onDutyEmployeeList.clear();
		employeeList.clear();
		
		//For each control in the composite...
		for(Control controls : inner.getChildren()) {
			//Dispose of it
			controls.dispose();
		}
	}
	
	//Unloads all invoice lists and their composite contents
	public void UnloadInvoices(Composite unpaid, Composite paid) {
		//Clear the contents of the invoices
		unpaidInvoices.clear();
		paidInvoices.clear();
		allInvoices.clear();
		
		//For each control in the composite...
		for(Control controls : unpaid.getChildren()) {
			//Dispose of it
			controls.dispose();
		}
		
		//For each control in the composite...
		for(Control controls : paid.getChildren()) {
			//Dispose of it
			controls.dispose();
		}
	}
	
	//Unloads all order lists and their composite contents
	public void UnloadOrders(Composite outgoing, Composite received) {
		//Clear the contents of the order lists
		outgoingOrders.clear();
		receivedOrders.clear();
		allOrders.clear();
		
		//For each control in the composite...
		for(Control controls : outgoing.getChildren()) {
			//Dispose of it
			controls.dispose();
		}
		
		//For each control in the composite...
		for(Control controls : received.getChildren()) {
			//Dispose of it
			controls.dispose();
		}
	}
	
	//Unloads all product lists and their composite contents
	public void UnloadProducts(Composite currentInventory) {
		//Clear the contents of the product lists
		fullInventory.clear();
		this.currentInventory.clear();
		clearanceInventory.clear();
		seasonalInventory.clear();
		
		//For each control in the composite...
		for(Control controls : currentInventory.getChildren()) {
			//Dispose of it
			controls.dispose();
		}
	}
	
	//Getters for each list 
	public ArrayList<Employee> getOnDutyList() {
		return onDutyEmployeeList;
	}
	
	public ArrayList<Employee> getFullEmployeeList() {
		return employeeList;
	}
	
	public ArrayList<ProductItem> getFullInventory() {
		return fullInventory;
	}
	
	public ArrayList<ProductItem> getCurrentInventory() {
		return currentInventory;
	}
	
	public ArrayList<ProductItem> getSeasonalInventory() {
		return seasonalInventory;
	}
	
	public ArrayList<ProductItem> getClearanceInventory() {
		return clearanceInventory;
	}
	
	public ArrayList<Order> getAllOrders() {
		return allOrders;
	}
	
	public ArrayList<Order> getOutgoingOrders() {
		return outgoingOrders;
	}
	
	public ArrayList<Order> getReceivedOrders() {
		return receivedOrders;
	}
	
	public ArrayList<Invoice> getAllInvoices() {
		return allInvoices;
	}
	
	public ArrayList<Invoice> getUnpaidInvoices() {
		return unpaidInvoices;
	}
	
	public ArrayList<Invoice> getPaidInvoices() {
		return paidInvoices;
	}
	
}