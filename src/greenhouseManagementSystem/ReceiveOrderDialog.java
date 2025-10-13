package greenhouseManagementSystem;

//Imports
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class ReceiveOrderDialog extends Dialog {
	//Fields of this dialog class
	protected Object result;
	protected Shell shlReceiveOrder;
	private ArrayList<Order> outgoingOrders;
	private static Boolean orderReceived;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ReceiveOrderDialog(Shell parent, int style, ArrayList<Order> outgoingOrders) {
		super(parent, style);
		setText("SWT Dialog");
		this.outgoingOrders = outgoingOrders;
		orderReceived = false;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlReceiveOrder.open();
		shlReceiveOrder.layout();
		Display display = getParent().getDisplay();
		while (!shlReceiveOrder.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlReceiveOrder = new Shell(getParent(), getStyle());
		shlReceiveOrder.setMinimumSize(new Point(700, 450));
		shlReceiveOrder.setSize(450, 300);
		shlReceiveOrder.setText("Receive Order");
		
		//Cancel button
		Button btnCancel = new Button(shlReceiveOrder, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//Close this dialog
				shlReceiveOrder.dispose();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(599, 10, 75, 25);
		
		//Context labels for dialog
		Label lblReceiveAnOrder = new Label(shlReceiveOrder, SWT.NONE);
		lblReceiveAnOrder.setText("Receive an Order");
		lblReceiveAnOrder.setBounds(10, 10, 117, 15);
		
		Label lblOrderDetails = new Label(shlReceiveOrder, SWT.NONE);
		lblOrderDetails.setText("Order Details");
		lblOrderDetails.setAlignment(SWT.CENTER);
		lblOrderDetails.setBounds(435, 39, 117, 15);
		
		//Composite to display individual order content
		Composite orderInfoComposite = new Composite(shlReceiveOrder, SWT.NONE);
		orderInfoComposite.setBounds(298, 60, 376, 300);
	
		//Outer composite to store inner composite data
		ScrolledComposite receiveOrder_outerComposite = new ScrolledComposite(shlReceiveOrder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		receiveOrder_outerComposite.setExpandHorizontal(true);
		receiveOrder_outerComposite.setBounds(10, 31, 282, 370);
		
		//Inner composite to actually store the data
		Composite receiveOrder_innerComposite = new Composite(receiveOrder_outerComposite, SWT.NONE);
		receiveOrder_innerComposite.setLayout(null);
		receiveOrder_innerComposite.setBounds(10, 31, 282, 370);
		
		//Apply the content of the inner composite to the outer composite
		receiveOrder_outerComposite.setContent(receiveOrder_innerComposite);
		receiveOrder_outerComposite.setMinSize(receiveOrder_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		//Populate inner composite
		int outgoingOrderHeight = PopulateOutgoingOrders(receiveOrder_innerComposite, outgoingOrders, orderInfoComposite);
		receiveOrder_innerComposite.setSize(282, outgoingOrderHeight);
	}

	//Populate the outgoing orders window
	public int PopulateOutgoingOrders(Composite inner, ArrayList<Order> orderList, Composite infoPanel) {
		//Create a list of all employees
		ArrayList<Order> orderListing = orderList;
		
		//Create a height variable 
		int height = 0;
		
		//Create headers
		Label idHeader = new Label(inner, SWT.None);
		idHeader.setText("Order ID");
		idHeader.setBounds(3, 5, 60, 15);
		
		Label dateHeader = new Label(inner, SWT.None);
		dateHeader.setText("Order Date");
		dateHeader.setBounds(70, 5, 70, 15);
		
		Label totalHeader = new Label(inner, SWT.None);
		totalHeader.setText("Order Total");
		totalHeader.setBounds(150, 5, 80, 15);
		
		//Loop through list of employees
		for(int index = 0; index < orderListing.size(); index++ ) {
			//Create an employee
			Order order = orderListing.get(index);
			
			//Create controls
			Label id = new Label(inner, SWT.None);
			id.setText(String.valueOf(order.getID()));
			id.setBounds(3, index * 25 + 35, 35, 15);
			
			Label date = new Label(inner, SWT.None);
			date.setText(String.valueOf(order.getDate()));
			date.setBounds(70, index * 25 + 35, 70, 15);
			
			Label total = new Label(inner, SWT.None);
			total.setText("$" + String.valueOf(order.getTotal()));
			total.setBounds(150, index * 25 + 35, 45, 15);
			
			Button receive = new Button(inner, SWT.None);
			receive.setText("Receive");
			receive.setBounds(200, index * 25 + 35, 70, 20);
			
			//Set data to the button to differentiate them
			receive.setData((order));
			
			//Add an event listener for each payment button
			receive.addListener(SWT.Selection, e -> {
				Order selectedOrder = (Order) ((Button) e.widget).getData();
				PopulateInfoPanel(shlReceiveOrder, infoPanel, selectedOrder);
			});
			
			//Update height
			height = index * 20 + 35;
		}
		
		//Return height
		return height + 100;
	}
	
	//Method to populate info panel
	private void PopulateInfoPanel(Shell shell, Composite panel, Order order) {
		//Remove the previous content, allowing another employee to be selected and populate data
		for(Control control : panel.getChildren()) {
			control.dispose();
		}
		
		//Labels for each information piece of an order
		Label lblId = new Label(panel, SWT.NONE);
		lblId.setText("Order ID");
		lblId.setBounds(10, 40, 66, 15);
		
		Label lblTotal = new Label(panel, SWT.NONE);
		lblTotal.setText("Order Total");
		lblTotal.setBounds(10, 67, 102, 15);
		
		Label lblDate = new Label(panel, SWT.NONE);
		lblDate.setText("Order Date");
		lblDate.setBounds(10, 94, 102, 15);
		
		Label lblItemCount = new Label(panel, SWT.NONE);
		lblItemCount.setText("Expected Item Count");
		lblItemCount.setBounds(10, 120, 110, 15);
		
		Label lblItems = new Label(panel, SWT.NONE);
		lblItems.setText("Some items include...");
		lblItems.setBounds(10, 165, 125, 15);
		
		Label idLabel = new Label(panel, SWT.NONE);
		idLabel.setText(String.valueOf(order.getID()));
		idLabel.setBounds(162, 40, 102, 21);
		
		Label totalLabel = new Label(panel, SWT.NONE);
		totalLabel.setText("$" + String.valueOf(order.getTotal()));
		totalLabel.setBounds(162, 67, 102, 21);
		
		Label dateLabel = new Label(panel, SWT.NONE);
		dateLabel.setText(String.valueOf(order.getDate()));
		dateLabel.setBounds(162, 94, 165, 21);
		
		Label ItemCountLabel = new Label(panel, SWT.NONE);
		ItemCountLabel.setText(String.valueOf(order.getOrderItems().size()));
		ItemCountLabel.setBounds(162, 120, 150, 23);
		
		//Some logic to determine how many items to show
		int displayItems = 0;
		//If there are more than 7 items, only show 7 **off by one
		if(order.getOrderItems().size() > 6) {
			displayItems = 6;
		} else {
			//Show every item if there are three or less
			displayItems = order.getOrderItems().size() - 1;
		}
		
		//Create headers for the small list
		Label productIdHeader = new Label(panel, SWT.NONE);
		productIdHeader.setText("Product ID");
		productIdHeader.setBounds(10, 195, 60, 15);
		
		Label productNameHeader = new Label(panel, SWT.NONE);
		productNameHeader.setText("Product Name");
		productNameHeader.setBounds(85, 195, 80, 15);
		
		//Set a variable equal to order items
		ArrayList<ProductItem> orderItems = order.getOrderItems();
		
		//Try catch to loop through the products and get their ids and names
		//We need the database connection just for this small loop, otherwise various lists and other loops would be needed
		try {
			//Make a connection
			Connection connection = DriverManager.getConnection("jdbc:sqlite:GreenhouseManagement");
			//For loop to create some items from the order
			for(int index = 0; index < displayItems; index++) {
				//Create a product id label
				Label productId = new Label(panel, SWT.NONE);
				productId.setText(String.valueOf(orderItems.get(index).getProductID()));
				productId.setBounds(10, index * 15 + 215, 50, 15);
				
				//Create a statement, assign sql with a product id, get the resulting string from ProductName
				Statement statement = connection.createStatement();
				StringBuilder sql = new StringBuilder("Select ProductName from ProductItem Where ProductID = ");
				sql.append(productId.getText());
				ResultSet result = statement.executeQuery(sql.toString());
				String productName = result.getString("ProductName");
				
				//Create a product name label
				Label productNameLabel = new Label(panel, SWT.NONE);
				productNameLabel.setText(productName);
				productNameLabel.setBounds(85, index * 15 + 215, 160, 15);
			}
			
			//Close connection
			connection.close();
			
		} catch (SQLException sqle) {
			//Print error
			sqle.printStackTrace();
		}

		//Receive order button
		Button btnReceiveOrder = new Button(shell, SWT.NONE);
		btnReceiveOrder.setText("Receive Order");
		btnReceiveOrder.setBounds(574, 376, 100, 25);
		btnReceiveOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {	
				//Connect to the database
				try {
					//Make a connection
					Connection connection = DriverManager.getConnection("jdbc:sqlite:GreenhouseManagement");
					//Make an sql update statement
					String sql = "Update [Order] Set Received = 'true' Where OrderID = ?";
					
					//Make a prepared statement
					PreparedStatement inserter = connection.prepareStatement(sql);
					inserter.setString(1, String.valueOf(order.getID()));
					
					//Execute update
					inserter.executeUpdate();
					
					//Message to show success
					MessageBox success = new MessageBox(shell);
					success.setText("Success");
					success.setMessage("The order was successfully received!");
					success.open();
					
					//Assign the order received boolean
					orderReceived = true;
					
					//Close out of window
					shell.dispose();
					
					//Close connection
					connection.close();
					
				} catch (SQLException sqle) {
					//Print out error
					sqle.printStackTrace();
					
					//Catch error
					MessageBox sqlError = new MessageBox(shell);
					sqlError.setText("Error");
					sqlError.setMessage("It seems that we ran into an issue on our end with the database, please try again later.");
					sqlError.open();
							}
					}
				});
			}
	
	//Getter for the boolean order received
	public static Boolean getReceived() {
		return orderReceived;
	}
}
