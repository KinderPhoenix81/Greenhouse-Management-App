package greenhouseManagementSystem;

//Imports
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;

public class ManageInventoryDialog extends Dialog {

	//Fields of the dialog class
	protected Object result;
	protected Shell shlManageInventory;
	private ArrayList<ProductItem> productList;
	private static Boolean productUpdated;
	private Text productNameTextbox;
	private Text categoryTextbox;
	private Text priceTextbox;
	private Text quantityInStockTextbox;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ManageInventoryDialog(Shell parent, int style, ArrayList<ProductItem> productList) {
		super(parent, style);
		setText("SWT Dialog");
		this.productList = productList;
		productUpdated = false;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlManageInventory.open();
		shlManageInventory.layout();
		Display display = getParent().getDisplay();
		while (!shlManageInventory.isDisposed()) {
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
		shlManageInventory = new Shell(getParent(), getStyle());
		shlManageInventory.setMinimumSize(new Point(700, 450));
		shlManageInventory.setSize(450, 300);
		shlManageInventory.setText("Manage Inventory");
		
		//Descriptor label for the dialog
		Label lblModifyInventory = new Label(shlManageInventory, SWT.NONE);
		lblModifyInventory.setBounds(10, 10, 91, 15);
		lblModifyInventory.setText("Modify Inventory");
		
		//Exit button
		Button btnExit = new Button(shlManageInventory, SWT.NONE);
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//closes shell
				shlManageInventory.dispose();
			}
		});
		btnExit.setText("Cancel");
		btnExit.setBounds(599, 10, 75, 25);
		
		//Composite to store a product's information when edited
		Composite editInfoComposite = new Composite(shlManageInventory, SWT.NONE);
		editInfoComposite.setBounds(298, 60, 376, 314);
		
		//Create an outer composite
		ScrolledComposite editInventory_outerComposite = new ScrolledComposite(shlManageInventory, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		editInventory_outerComposite.setExpandHorizontal(true);
		editInventory_outerComposite.setBounds(10, 31, 282, 370);
		
		//Create an inner composite to store the content
		Composite editInventory_innerComposite = new Composite(editInventory_outerComposite, SWT.NONE);
		editInventory_innerComposite.setLayout(null);
		editInventory_innerComposite.setBounds(10, 31, 282, 370);
		
		//Set the content of the outer composite to match the inner
		editInventory_outerComposite.setContent(editInventory_innerComposite);
		editInventory_outerComposite.setMinSize(editInventory_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate the inner panel with actual data
		int productHeight = PopulateAllProducts(editInventory_innerComposite, productList, editInfoComposite);
		editInventory_innerComposite.setSize(282, productHeight);
		
		//Clarification label on dialog
		Label lblProductInformation = new Label(shlManageInventory, SWT.NONE);
		lblProductInformation.setText("Product Information");
		lblProductInformation.setBounds(435, 39, 117, 15);

	}
	
	//Populate the product list window
	public int PopulateAllProducts(Composite inner, ArrayList<ProductItem> productList, Composite editPanel) {
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
			edit.setText("Edit");
			edit.setBounds(210, index * 20 + 35, 50, 15);
			
			//Set data to the button to differentiate them
			edit.setData((product));

			//Add an event listener for each edit button
			edit.addListener(SWT.Selection, e -> {
				ProductItem selectedProduct = (ProductItem) ((Button) e.widget).getData();
				PopulateEditingPanel(shlManageInventory, editPanel, selectedProduct);
			});
			
			//Update height
			height = index * 20 + 35;
		}
		
		//Return height
		return height + 25;
	}
	
	//Method to populate the edit panel upon clicking a product to edit
	private void PopulateEditingPanel(Shell shell, Composite panel, ProductItem product) {
		//Remove the previous content, allowing another product to be selected and populate data
		for(Control control : panel.getChildren()) {
			control.dispose();
		}
		//Descriptor labels for each product field
		Label lblProductName = new Label(panel, SWT.NONE);
		lblProductName.setBounds(10, 13, 77, 15);
		lblProductName.setText("Product Name");
		
		Label lblProductID = new Label(panel, SWT.NONE);
		lblProductID.setBounds(10, 40, 77, 15);
		lblProductID.setText("Product ID");
		
		Label productID = new Label(panel, SWT.NONE);
		productID.setBounds(345, 40, 25, 21);
		productID.setText(String.valueOf(product.getProductID()));
		
		Label lblCategory = new Label(panel, SWT.NONE);
		lblCategory.setText("Category");
		lblCategory.setBounds(10, 67, 77, 15);
		
		Label lblPurchasePrice = new Label(panel, SWT.NONE);
		lblPurchasePrice.setText("Purchase Price");
		lblPurchasePrice.setBounds(10, 94, 77, 15);
		
		Label lblQuantityInStock = new Label(panel, SWT.NONE);
		lblQuantityInStock.setText("Quantity In Stock");
		lblQuantityInStock.setBounds(10, 121, 100, 15);
		
		//Textboxes to modify existing data
		productNameTextbox = new Text(panel, SWT.BORDER);
		productNameTextbox.setBounds(186, 10, 180, 21);
		productNameTextbox.setText(product.getName());
		
		categoryTextbox = new Text(panel, SWT.BORDER);
		categoryTextbox.setBounds(266, 64, 100, 21);
		categoryTextbox.setText(product.getCategory());
		
		priceTextbox = new Text(panel, SWT.BORDER);
		priceTextbox.setBounds(266, 91, 100, 21);
		priceTextbox.setText(String.valueOf(product.getPrice()));
		
		quantityInStockTextbox = new Text(panel, SWT.BORDER);
		quantityInStockTextbox.setBounds(266, 118, 100, 21);
		quantityInStockTextbox.setText(String.valueOf(product.getStockNum()));
		
		//Combo boxes and their labels for a product's information
		Combo perishableCombo = new Combo(panel, SWT.READ_ONLY);
		perishableCombo.setBounds(275, 147, 91, 23);
		String[] trueFalse = {"true", "false"};
		perishableCombo.setItems(trueFalse);
		perishableCombo.select(getIndex(trueFalse, product.getPerishable().toString()));
		
		Combo clearanceCombo = new Combo(panel, SWT.READ_ONLY);
		clearanceCombo.setBounds(275, 176, 91, 23);
		clearanceCombo.setItems(trueFalse);
		clearanceCombo.select(getIndex(trueFalse, product.getClearanceStatus().toString()));
		
		Combo seasonalCombo = new Combo(panel, SWT.READ_ONLY);
		seasonalCombo.setBounds(275, 205, 91, 23);
		seasonalCombo.setItems(trueFalse);
		seasonalCombo.select(getIndex(trueFalse, product.getSeasonalStatus().toString()));
		
		Label lblPerishable = new Label(panel, SWT.NONE);
		lblPerishable.setBounds(10, 150, 55, 15);
		lblPerishable.setText("Perishable");
		
		Label lblClearance = new Label(panel, SWT.NONE);
		lblClearance.setText("Clearance");
		lblClearance.setBounds(10, 179, 55, 15);
		
		Label lblSeasonal = new Label(panel, SWT.NONE);
		lblSeasonal.setText("Seasonal");
		lblSeasonal.setBounds(10, 208, 55, 15);
		
		//Submit changes button
		Button btnSubmitChanges = new Button(panel, SWT.NONE);
		btnSubmitChanges.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {	
				//Boolean to hold value for blank fields present
				Boolean blankFieldPresent = false;
				
				//Verify all fields have data
				if(productNameTextbox.getText().isBlank() || categoryTextbox.getText().isBlank() || priceTextbox.getText().isBlank()
						|| quantityInStockTextbox.getText().isBlank()) {
					
					//Message the user to review data
					MessageBox blankFieldMessage = new MessageBox(shlManageInventory);
					blankFieldMessage.setText("Warning");
					blankFieldMessage.setMessage("One or more of your fields are blank, please try again.");
					blankFieldMessage.open();
					
					//Update boolean
					blankFieldPresent = true;
				}
					if(!blankFieldPresent) {
						
						//Validate data
						try {
							//Retrieve and parse input
							String productName = productNameTextbox.getText();
							String category = categoryTextbox.getText();
							Double price = Double.parseDouble(priceTextbox.getText());
							int quantity = Integer.parseInt(quantityInStockTextbox.getText());
							Boolean inStock;
							
							//Perform some checks
							if(price <= 0) {
								//Cause an error
								throw new NumberFormatException("");
							}
							if(quantity < 0) {
								//Cause an error
								throw new NumberFormatException("");
							}
							if(quantity > 0) {
								//Set a value for inStock variable
								inStock = true;
							} else {
								//Set a value for inStock variable
								inStock = false;
							}
							
							//Connect to the database
							try {
								//Make a connection
								Connection connection = DriverManager.getConnection("jdbc:sqlite:GreenhouseManagement");
								//Make an sql update statement
								String sql = "Update ProductItem "
										+ "Set ProductName = ?, Category = ?, PurchasePrice = ?, QuantityInStock = ?, "
										+ "Perishable = ?, InStock = ?, ClearanceItem = ?, SeasonalItem = ? "
										+ "Where ProductID = ?";
								
								//Make a prepared statement
								PreparedStatement inserter = connection.prepareStatement(sql);
								inserter.setString(1, productName);
								inserter.setString(2, category);
								inserter.setDouble(3, price);
								inserter.setInt(4, quantity);
								inserter.setString(5, perishableCombo.getText());
								inserter.setString(6, inStock.toString().toLowerCase());
								inserter.setString(7,  clearanceCombo.getText().toLowerCase());
								inserter.setString(8, seasonalCombo.getText().toLowerCase());
								inserter.setInt(9, product.getProductID());
								
								//Execute update
								inserter.executeUpdate();
								
								//Message to show success
								MessageBox success = new MessageBox(shell);
								success.setText("Success");
								success.setMessage("The product was successfully updated!");
								success.open();
								
								//Assign the employee updated boolean to true
								productUpdated = true;
								
								//Close out of window
								shell.dispose();
								
							} catch (SQLException sqle) {
								//Print out error
								sqle.printStackTrace();
								
								//Catch error
								MessageBox sqlError = new MessageBox(shell);
								sqlError.setText("Error");
								sqlError.setMessage("It seems that we ran into an issue on our end with the database, please try again later.");
								sqlError.open();
							}

						} catch (NumberFormatException nfe) {
							//Print error
							nfe.printStackTrace();
							
							//Catch error
							MessageBox parseError = new MessageBox(shell);
							parseError.setText("Error");
							parseError.setMessage("One of the values entered was not of correct typing or incorrect length, causing an issue, please double check, and re-enter your data.");
							parseError.open();
						}
					}
				}
			});
		
		//Submit button cont.
		btnSubmitChanges.setBounds(266, 279, 100, 25);
		btnSubmitChanges.setText("Submit Changes");
	}
	
	//Get index helper to help populate the combo boxes with data
	//This also allows me to pre-select the options the employee already had into the combo boxes
	private int getIndex(String[] array, String value) {
		//Loop through array
		for(int index = 0; index < array.length; index++) {
			//Check to see if the current value matches the case
			if(array[index].equals(value)) {
				return index;
			}
		}
		
		//Return an invalid value
		return -1;
	}
	
	//Getter for the productUpdated boolean
	public static Boolean getUpdated() {
		return productUpdated;
	}
}


