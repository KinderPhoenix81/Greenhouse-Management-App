package greenhouseManagementSystem;

//Imports
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;

import java.util.ArrayList;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class PlaceOrderDialog extends Dialog {
	//Fields for this dialog class; All labels in the order totals column are here for easy access to nested methods
	protected Object result;
	protected Shell shlNewOrder;
	private ArrayList<ProductItem> productList;
	private ArrayList<ProductItem> orderItems;
	private ArrayList<Order> allOrders;
	private int orderedItems;
	private static Boolean orderPlaced;
	private Text quantityText;
	private Label totalItemsLabel;
	private Label totalCostLabel;
	private Label totalTaxesLabel;
	private Label totalSubtotalLabel;
	private Label shippingMethodLabel;
	private Label totalShippingCostLabel;
	private Label totalDeliveryTimeLabel;
	private Label totalTrackingNumberLabel;
	private Label totalOrderNumberLabel;
	private Label totalInvoiceNumberLabel;
	private Label totalOrderTotalLabel;



	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PlaceOrderDialog(Shell parent, int style, ArrayList<ProductItem> productList, ArrayList<Order> allOrders) {
		super(parent, style);
		setText("SWT Dialog");
		
		//Set some default values
		this.productList = productList;
		this.allOrders = allOrders;
		this.orderItems = new ArrayList<ProductItem>();
		orderedItems = 0;
		orderPlaced = false;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(Color lightYellow) {
		createContents(lightYellow);
		
		//Random number object for tracking number
		Random random = new Random();
		
		//Initalize control values-- these are fields, as they will be modified frequently
		totalItemsLabel = new Label(shlNewOrder, SWT.NONE);
		totalItemsLabel.setAlignment(SWT.RIGHT);
		totalItemsLabel.setBounds(619, 56, 55, 15);
		totalItemsLabel.setText("0");
		
		totalCostLabel = new Label(shlNewOrder, SWT.NONE);
		totalCostLabel.setText("0");
		totalCostLabel.setAlignment(SWT.RIGHT);
		totalCostLabel.setBounds(619, 97, 55, 15);
		
		totalTaxesLabel = new Label(shlNewOrder, SWT.NONE);
		totalTaxesLabel.setText("0");
		totalTaxesLabel.setAlignment(SWT.RIGHT);
		totalTaxesLabel.setBounds(619, 118, 55, 15);
		
		totalSubtotalLabel = new Label(shlNewOrder, SWT.NONE);
		totalSubtotalLabel.setText("0");
		totalSubtotalLabel.setAlignment(SWT.RIGHT);
		totalSubtotalLabel.setBounds(619, 139, 55, 15);
		
		shippingMethodLabel = new Label(shlNewOrder, SWT.NONE);
		shippingMethodLabel.setText("Ground");
		shippingMethodLabel.setAlignment(SWT.RIGHT);
		shippingMethodLabel.setBounds(619, 179, 55, 15);
		
		totalShippingCostLabel = new Label(shlNewOrder, SWT.NONE);
		totalShippingCostLabel.setText("0");
		totalShippingCostLabel.setAlignment(SWT.RIGHT);
		totalShippingCostLabel.setBounds(619, 200, 55, 15);
		
		totalDeliveryTimeLabel = new Label(shlNewOrder, SWT.NONE);
		totalDeliveryTimeLabel.setText("0 Days");
		totalDeliveryTimeLabel.setAlignment(SWT.RIGHT);
		totalDeliveryTimeLabel.setBounds(619, 221, 55, 15);
		
		totalTrackingNumberLabel = new Label(shlNewOrder, SWT.NONE);
		totalTrackingNumberLabel.setText("GH-" + String.valueOf((allOrders.size() + 1)  * random.nextInt(10000)));
		totalTrackingNumberLabel.setAlignment(SWT.RIGHT);
		totalTrackingNumberLabel.setBounds(604, 242, 70, 15);
		
		totalOrderNumberLabel = new Label(shlNewOrder, SWT.NONE);
		totalOrderNumberLabel.setText(String.valueOf(allOrders.size() + 1));
		totalOrderNumberLabel.setAlignment(SWT.RIGHT);
		totalOrderNumberLabel.setBounds(619, 280, 55, 15);
		
		totalInvoiceNumberLabel = new Label(shlNewOrder, SWT.NONE);
		totalInvoiceNumberLabel.setText(String.valueOf(allOrders.size() + 1));
		totalInvoiceNumberLabel.setAlignment(SWT.RIGHT);
		totalInvoiceNumberLabel.setBounds(619, 301, 55, 15);
		
		totalOrderTotalLabel = new Label(shlNewOrder, SWT.NONE);
		totalOrderTotalLabel.setText("N/A");
		totalOrderTotalLabel.setAlignment(SWT.RIGHT);
		totalOrderTotalLabel.setBounds(619, 322, 55, 15);
		
		//Disable visibility on these total values for now
		totalItemsLabel.setVisible(false);
		totalCostLabel.setVisible(false);
		totalTaxesLabel.setVisible(false); 
		totalSubtotalLabel.setVisible(false); 
		shippingMethodLabel.setVisible(false); 
		totalShippingCostLabel.setVisible(false); 
		totalDeliveryTimeLabel.setVisible(false); 
		totalTrackingNumberLabel.setVisible(false); 
		totalOrderNumberLabel.setVisible(false); 
		totalInvoiceNumberLabel.setVisible(false); 
		totalOrderTotalLabel.setVisible(false);
		
		//Context labels for dialog
		Label lblNewLabel = new Label(shlNewOrder, SWT.NONE);
		lblNewLabel.setBounds(365, 223, 70, 15);
		lblNewLabel.setText("Order Items");
		
		Label lblItemDetails = new Label(shlNewOrder, SWT.NONE);
		lblItemDetails.setText("Item Details");
		lblItemDetails.setBounds(365, 12, 70, 15);
		
		shlNewOrder.open();
		shlNewOrder.layout();
		Display display = getParent().getDisplay();
		while (!shlNewOrder.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents(Color lightYellow) {
		shlNewOrder = new Shell(getParent(), getStyle());
		shlNewOrder.setMinimumSize(new Point(700, 450));
		shlNewOrder.setSize(450, 300);
		shlNewOrder.setText("New Order");
		
		//Cancel and submit button
		Button btnCancel = new Button(shlNewOrder, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//closes shell
				shlNewOrder.dispose();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(599, 376, 75, 25);
		
		Button btnSubmit = new Button(shlNewOrder, SWT.NONE);
		btnSubmit.setText("Place Order");
		btnSubmit.setBounds(501, 376, 75, 25);
		
		//Labels for the descriptors in the order total column, along with the lines
		Label lblPleaseSelectThe = new Label(shlNewOrder, SWT.NONE);
		lblPleaseSelectThe.setBounds(10, 10, 171, 15);
		lblPleaseSelectThe.setText("Please select the item(s) to order");
		
		Label label = new Label(shlNewOrder, SWT.SEPARATOR | SWT.VERTICAL);
		label.setBounds(493, 10, 2, 391);
		
		Label lblSummary = new Label(shlNewOrder, SWT.NONE);
		lblSummary.setBounds(501, 10, 55, 15);
		lblSummary.setText("Summary");
		
		Label lblItemTotal = new Label(shlNewOrder, SWT.NONE);
		lblItemTotal.setBounds(501, 56, 58, 15);
		lblItemTotal.setText("Total Items");
		
		Label lblTotalCost = new Label(shlNewOrder, SWT.NONE);
		lblTotalCost.setText("Total Cost");
		lblTotalCost.setBounds(501, 97, 55, 15);
		
		Label lblTaxes = new Label(shlNewOrder, SWT.NONE);
		lblTaxes.setText("Taxes");
		lblTaxes.setBounds(501, 118, 55, 15);
		
		Label lblSubtotal = new Label(shlNewOrder, SWT.NONE);
		lblSubtotal.setText("Subtotal");
		lblSubtotal.setBounds(501, 139, 55, 15);
		
		Label label_1 = new Label(shlNewOrder, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(493, 31, 181, 2);
		
		Label lblShippingMethod = new Label(shlNewOrder, SWT.NONE);
		lblShippingMethod.setBounds(501, 179, 92, 15);
		lblShippingMethod.setText("Shipping Method");
		
		Label lblShippingCost = new Label(shlNewOrder, SWT.NONE);
		lblShippingCost.setText("Shipping Cost");
		lblShippingCost.setBounds(501, 200, 92, 15);
		
		Label lblDeliveryTimeest = new Label(shlNewOrder, SWT.NONE);
		lblDeliveryTimeest.setText("Delivery Time (est.)");
		lblDeliveryTimeest.setBounds(501, 221, 101, 15);
		
		Label lblOrderTotal = new Label(shlNewOrder, SWT.NONE);
		lblOrderTotal.setText("Order Total");
		lblOrderTotal.setBounds(501, 322, 75, 15);
		
		Label lblOrderNumber = new Label(shlNewOrder, SWT.NONE);
		lblOrderNumber.setText("Order Number");
		lblOrderNumber.setBounds(501, 280, 92, 15);
		
		Label lblInvoiceNumber = new Label(shlNewOrder, SWT.NONE);
		lblInvoiceNumber.setBounds(501, 301, 92, 15);
		lblInvoiceNumber.setText("Invoice Number");
		
		Label lblShippingNumber = new Label(shlNewOrder, SWT.NONE);
		lblShippingNumber.setText("Tracking Number");
		lblShippingNumber.setBounds(501, 242, 101, 15);
		
		//Composite to store information about a product ready to be ordered
		Composite productInfo = new Composite(shlNewOrder, SWT.NONE);
		productInfo.setBounds(305, 31, 182, 184);
		
		//Outer composite for the items the user has added to an order
		ScrolledComposite orderItems_outerComposite = new ScrolledComposite(shlNewOrder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		orderItems_outerComposite.setBounds(305, 242, 182, 159);
		orderItems_outerComposite.setExpandHorizontal(true);
		
		//Inner composite for the items the user has added to an order
		Composite orderItems_innerComposite = new Composite(orderItems_outerComposite, SWT.NONE);
		orderItems_outerComposite.setContent(orderItems_innerComposite);
		orderItems_outerComposite.setMinSize(orderItems_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		//Labels for this inner composite for order items
		Label lblProductIDTotalItems = new Label(orderItems_innerComposite, SWT.NONE);
		lblProductIDTotalItems.setBounds(10, 5, 55, 15);
		lblProductIDTotalItems.setText("ID");
		
		Label lblQuantityTotalItems = new Label(orderItems_innerComposite, SWT.NONE);
		lblQuantityTotalItems.setBounds(75, 5, 55, 15);
		lblQuantityTotalItems.setText("Quantity");
		
		//Outer composite for all products list
		ScrolledComposite productChoices_outerComposite = new ScrolledComposite(shlNewOrder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		productChoices_outerComposite.setBounds(10, 31, 289, 370);
		productChoices_outerComposite.setExpandHorizontal(true);

		//Inner composite for actual content
		Composite productChoices_innerComposite = new Composite(productChoices_outerComposite, SWT.NONE);
		productChoices_innerComposite.setLayout(null);
		productChoices_innerComposite.setBounds(10, 31, 289, 370);
		
		//Match the outer composite content to the inner composite content
		productChoices_outerComposite.setContent(productChoices_innerComposite);
		productChoices_outerComposite.setMinSize(productChoices_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate the inner composite content
		int productChoicesHeight = PopulateAllProducts(productChoices_innerComposite, productList, productInfo, orderItems_innerComposite, lightYellow);
		productChoices_innerComposite.setSize(289, productChoicesHeight);
		
		//Event listener for placing an order
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//Make sure the user has at least one item in the order
				if(orderItems.size() >= 1) {
					//Valid order, create an order object & invoice object
					Order order = new Order(allOrders.size() + 1, LocalDate.now(), Double.parseDouble(totalOrderTotalLabel.getText().replace("$", "")), orderItems);
					Invoice invoice = new Invoice(allOrders.size(), Double.parseDouble(totalOrderTotalLabel.getText().replace("$", "")), LocalDate.now(), LocalDate.now().plusDays(21));
					
					//Connect to database
					try (Connection conn = DriverManager.getConnection("jdbc:sqlite:GreenhouseManagement")) {
						
						//Start a transaction- if one insert fails, all will fail
						conn.setAutoCommit(false);
						
						//Try-catch for transaction
						try {
							//Create an sql statement for the order
							String orderSQL = "Insert Into [Order](OrderTotal, OrderDate, Received, InvoiceID) Values "
									+ "(?, ?, ?, ?)";
							
							//Make a prepared statement for order
							PreparedStatement orderInsert = conn.prepareStatement(orderSQL);
							orderInsert.setDouble(1, order.getTotal());
							orderInsert.setString(2, String.valueOf(order.getDate()).replaceAll("-", ""));
							orderInsert.setString(3, "false");
							orderInsert.setInt(4, order.getID());
							
							//Execute update
							orderInsert.executeUpdate();
							
							//Make an sql statement for the invoice
							String invoiceSQL = "Insert Into Invoice(InvoiceTotal, InvoiceDate, InvoiceDueDate, Paid) "
									+ "Values (?, ?, ?, ?)";
							
							//Make a prepared statement for the invoice
							PreparedStatement invoiceInsert = conn.prepareStatement(invoiceSQL);
							invoiceInsert.setDouble(1, invoice.getTotal());
							invoiceInsert.setString(2, String.valueOf(invoice.getDate()).replaceAll("-", ""));
							invoiceInsert.setString(3, String.valueOf(invoice.getDueDate()).replaceAll("-", ""));
							invoiceInsert.setString(4,  "false");
							
							//Execute update
							invoiceInsert.executeUpdate();
							
							//Loop through the order items to add them to the orderItems table
							for(ProductItem product : order.getOrderItems()) {
								//Create a statement
								String orderItemSQL = "Insert Into OrderItems(OrderID, ProductID, Quantity, OrderPrice) "
										+ "Values (?, ?, ?, ?)";
								
								//Make a prepared statement
								PreparedStatement orderItemInsert = conn.prepareStatement(orderItemSQL);
								orderItemInsert.setInt(1, order.getID());
								orderItemInsert.setInt(2, product.getProductID());
								orderItemInsert.setInt(3, product.getOrderAmount());
								orderItemInsert.setDouble(4, product.getCost());
								
								//Execute update
								orderItemInsert.executeUpdate();
							}
							//Commit the transaction
							conn.commit();
							
							//Reset auto-commit
							conn.setAutoCommit(true);
							
							//Close the connection
							conn.close();
							
							//Message to show success
							MessageBox success = new MessageBox(shlNewOrder);
							success.setText("Success");
							success.setMessage("Your order was placed! Feel free to view it and the accompanying invoice at anytime.");
							success.open();
						} catch (SQLException sql) {
							//Print out errors, rollback transaction
							sql.printStackTrace();
							conn.rollback();
							
							//Catch error
							MessageBox sqlError = new MessageBox(shlNewOrder);
							sqlError.setText("Error");
							sqlError.setMessage("Rollback Occurred.");
							sqlError.open();
						}
						//Assign the order placed boolean
						orderPlaced = true;
						
						//Close out of the window
						shlNewOrder.dispose();
						
					} catch (SQLException sqle) {
						//Print error
						sqle.printStackTrace();
						
						//Catch error
						MessageBox sqlError = new MessageBox(shlNewOrder);
						sqlError.setText("Error");
						sqlError.setMessage("It seems that we ran into an issue on our end with the database, please try again later.");
						sqlError.open();
					}
					
				} else {
					//Catch error
					MessageBox parseError = new MessageBox(shlNewOrder);
					parseError.setText("Error");
					parseError.setMessage("You must have at least one product listed to complete this order.");
					parseError.open();
				}
			}
		});
	}
	
	//Method to populate the product choices to order
	public int PopulateAllProducts(Composite inner, ArrayList<ProductItem> productList, Composite individualItemPanel, Composite orderItemsPanel, Color lightYellow) {
		//Create a list of products
		ArrayList<ProductItem> productListing = productList;
		
		//Create a height variable
		int height = 0;
		
		//Create headers
		Label nameHeader = new Label(inner, SWT.None);
		nameHeader.setText("ID");
		nameHeader.setBounds(3, 5, 20, 15);
		
		Label idHeader = new Label(inner, SWT.None);
		idHeader.setText("Product Name");
		idHeader.setBounds(35, 5, 150, 15);
		
		//Loop through list of products
		for(int index = 0; index < productListing.size(); index++ ) {
			//Create a product
			ProductItem product = productListing.get(index);
			
			//Create controls
			Label id = new Label(inner, SWT.None);
			id.setText(String.valueOf(product.getProductID()));
			id.setBounds(3, index * 20 + 35, 20, 15);
			
			Label name = new Label(inner, SWT.None);
			name.setText(product.getName());
			name.setBounds(35, index * 20 + 35, 165, 15);
			
			Button edit = new Button(inner, SWT.None);
			edit.setText("Order");
			edit.setBounds(210, index * 20 + 35, 50, 15);
			
			//Set data to the button to differentiate them
			edit.setData((product));

			//Add an event listener for each edit button
			edit.addListener(SWT.Selection, e -> {
				ProductItem selectedProduct = (ProductItem) ((Button) e.widget).getData();
				PopulateIndividualItemPanel(shlNewOrder, individualItemPanel, selectedProduct, orderItemsPanel);
			});
			
			//Add colors to products if they are currently out of stock
			if(!product.getInStock()) {
				id.setBackground(lightYellow);
			}
			
			//Update height
			height = index * 20 + 35;
		}
		
		//Return height
		return height + 25;
	}
	
	//Popualte the selected item panel
	public void PopulateIndividualItemPanel(Shell shell, Composite individualItemPanel, ProductItem selectedProduct, Composite orderItemsPanel) {
		//Remove the previous content, allowing another product to be selected and populate data
		for(Control control : individualItemPanel.getChildren()) {
			control.dispose();
		}
		
		//Add labels for individual product info
		Label lblProductInfo = new Label(individualItemPanel, SWT.NONE);
		lblProductInfo.setBounds(56, 10, 66, 15);
		lblProductInfo.setText("Product Info");
		
		Label lblProductID = new Label(individualItemPanel, SWT.NONE);
		lblProductID.setBounds(10, 31, 55, 15);
		lblProductID.setText("ID:");
		
		Label productID = new Label(individualItemPanel, SWT.NONE);
		productID.setAlignment(SWT.RIGHT);
		productID.setBounds(135, 31, 45, 15);
		productID.setText(String.valueOf(selectedProduct.getProductID()));
		
		Label lblProductName = new Label(individualItemPanel, SWT.NONE);
		lblProductName.setText("Name:");
		lblProductName.setBounds(10, 52, 50, 15);
		
		Label productName = new Label(individualItemPanel, SWT.NONE);
		productName.setAlignment(SWT.RIGHT);
		productName.setBounds(65, 52, 115, 15);
		productName.setText(selectedProduct.getName());
		
		Label lblOrderCost = new Label(individualItemPanel, SWT.NONE);
		lblOrderCost.setBounds(10, 73, 66, 15);
		lblOrderCost.setText("Order Cost:");
		
		Label orderCost = new Label(individualItemPanel, SWT.NONE); 
		orderCost.setAlignment(SWT.RIGHT);
		orderCost.setBounds(135, 73, 45, 15);
		orderCost.setText("$" + String.valueOf(selectedProduct.getCost()));
		
		//User input controls to change quantity and add an item
		Button btnAddItem = new Button(individualItemPanel, SWT.NONE);
		btnAddItem.setBounds(114, 149, 66, 25);
		btnAddItem.setText("Add Item");
		
		Label lblQuantity = new Label(individualItemPanel, SWT.NONE);
		lblQuantity.setBounds(10, 94, 55, 15);
		lblQuantity.setText("Quantity:");
		
		quantityText = new Text(individualItemPanel, SWT.BORDER);
		quantityText.setBounds(114, 91, 66, 21);
		quantityText.setText("0");
		
		//Add an event listener to the add button
		btnAddItem.addListener(SWT.Selection, e -> {
			//Declare a quantity variable
			int itemQuantity = 0;
			
			//Check the user input for quantity
			try {
				//Parse the quantity
				itemQuantity = Integer.parseInt(quantityText.getText());
				
				//Validate the value
				if(itemQuantity == 0 || itemQuantity < 0) {
					//Throw an error
					throw new NumberFormatException("");
				}
				
				//Add the item to the total order items list
				Label productIDTotalItems = new Label(orderItemsPanel, SWT.NONE);
				productIDTotalItems.setBounds(10, 31 + orderedItems * 15, 45, 15);
				productIDTotalItems.setText(String.valueOf(selectedProduct.getProductID()));
				
				Label quantityTotalItems = new Label(orderItemsPanel, SWT.NONE);
				quantityTotalItems.setBounds(75, 31 + orderedItems * 15, 45, 15);
				quantityTotalItems.setText(String.valueOf(itemQuantity));
				
				//Clear contents of the order items details field
				for(Control control : individualItemPanel.getChildren()) {
					control.dispose();
				}
				
				//If this is the first item being added to the order...
				if(orderedItems == 0) {
					//Enable visibility for the order total(s) labels
					EnableVisibleTotals();
				}

				//Add the items to the order total(s) column
				UpdateTotals(itemQuantity, selectedProduct.getCost());
				
				//Set the order amount for the product
				selectedProduct.setOrderAmount(itemQuantity);
				
				//Add the ordered item into the field
				orderItems.add(selectedProduct);
				
				//Increment ordered items field
				orderedItems++;
				
				//Set the size of the composite to properly scroll
				orderItemsPanel.setSize(182, orderedItems * 20 + 30);
				
			} catch (NumberFormatException nfe) {
				//Print error
				nfe.printStackTrace();
				
				//Catch error
				MessageBox parseError = new MessageBox(shell);
				parseError.setText("Error");
				parseError.setMessage("The value entered for 'Quantity' was not a proper integer, please try again.");
				parseError.open();
				
				//Clear textbox
				quantityText.setText("");
				itemQuantity = 0;
			}
		});
	}
	
	//Enable visibility for the values on the order totals column;
	public void EnableVisibleTotals() {
		totalItemsLabel.setVisible(true);
		totalCostLabel.setVisible(true);
		totalTaxesLabel.setVisible(true); 
		totalSubtotalLabel.setVisible(true); 
		shippingMethodLabel.setVisible(true); 
		totalShippingCostLabel.setVisible(true); 
		totalDeliveryTimeLabel.setVisible(true); 
		totalTrackingNumberLabel.setVisible(true); 
		totalOrderNumberLabel.setVisible(true); 
		totalInvoiceNumberLabel.setVisible(true); 
		totalOrderTotalLabel.setVisible(true); 
	}
	
	//Update totals method
	public void UpdateTotals(int quantity, double cost) {
		//Get the values of the labels for the total(s) column
		int totalItems = Integer.parseInt(totalItemsLabel.getText());
		double totalProductCost = Double.parseDouble(totalCostLabel.getText().replace("$", ""));
		double totalProductTaxes;
		double totalSubtotal;
		double totalShippingCost;
		int deliveryTime;
		double orderTotal;
		
		//Set values for order total
		totalItems += quantity;
		totalProductCost += quantity * cost;
		totalProductTaxes = totalProductCost * 0.05;
		totalSubtotal = totalProductCost + totalProductTaxes;
		totalShippingCost = totalProductCost * 0.04;
		deliveryTime = (int) Math.ceil(orderedItems / 3) + 1;
		orderTotal = totalSubtotal + totalShippingCost;
		
		//Set the values of the labels, set the format so no floating decimal points in any numeric values
		totalItemsLabel.setText(String.valueOf(totalItems));
		totalCostLabel.setText("$" + String.format("%.2f", totalProductCost));
		totalTaxesLabel.setText("$" + String.format("%.2f", totalProductTaxes));
		totalSubtotalLabel.setText("$" + String.format("%.2f", totalSubtotal));
		totalShippingCostLabel.setText("$" + String.format("%.2f", totalShippingCost));
		totalDeliveryTimeLabel.setText(String.valueOf(deliveryTime) + " day(s)"); 
		totalOrderTotalLabel.setText("$" + String.format("%.2f", orderTotal)); 
	}
	
	//Getter for the orderPlaced boolean
	public static Boolean getOrderPlaced() {
		return orderPlaced;
	}
}
