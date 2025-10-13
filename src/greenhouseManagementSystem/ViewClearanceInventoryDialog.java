package greenhouseManagementSystem;

//Imports
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

public class ViewClearanceInventoryDialog extends Dialog {

	protected Object result;
	protected Shell shlClearanceItems;
	private ArrayList<ProductItem> inventory;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ViewClearanceInventoryDialog(Shell parent, int style, ArrayList<ProductItem> productList) {
		super(parent, style);
		setText("SWT Dialog");
		inventory = productList;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(Color lightRed, Color lightGreen) {
		createContents(lightRed, lightGreen);
		shlClearanceItems.open();
		shlClearanceItems.layout();
		Display display = getParent().getDisplay();
		while (!shlClearanceItems.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents(Color lightRed, Color lightGreen) {
		shlClearanceItems = new Shell(getParent(), getStyle());
		shlClearanceItems.setMinimumSize(new Point(700, 450));
		shlClearanceItems.setSize(683, 450);
		shlClearanceItems.setText("Clearance Items");
		
		//Context label for dialog
		Label lblViewingClearanceInventory = new Label(shlClearanceItems, SWT.NONE);
		lblViewingClearanceInventory.setBounds(10, 10, 150, 15);
		lblViewingClearanceInventory.setText("Viewing Clearance Inventory");
		
		//Exit button
		Button btnExit = new Button(shlClearanceItems, SWT.NONE);
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//close shell
				shlClearanceItems.dispose();
			}
		});
		btnExit.setText("Exit");
		btnExit.setBounds(599, 10, 75, 25);
		
		//Outer composite container for inner composite
		ScrolledComposite clearanceInventory_outerComposite = new ScrolledComposite(shlClearanceItems, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		clearanceInventory_outerComposite.setBounds(10, 66, 651, 335);
		clearanceInventory_outerComposite.setExpandHorizontal(true);
		
		//Inner composite container for actual content
		Composite clearanceInventory_innerComposite = new Composite(clearanceInventory_outerComposite, SWT.NONE);
		clearanceInventory_innerComposite.setLayout(null);
		clearanceInventory_innerComposite.setBounds(10, 66, 651, 335);
		
		//Match content for the inner and outer composite
		clearanceInventory_outerComposite.setContent(clearanceInventory_innerComposite);
		clearanceInventory_outerComposite.setMinSize(clearanceInventory_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populating the inner composite with actual content
		int clearanceHeight = PopulateClearanceProducts(clearanceInventory_innerComposite, inventory, lightRed, lightGreen);
		clearanceInventory_innerComposite.setSize(651, clearanceHeight);
		
		//Headers for each product
		Label lblProductName = new Label(shlClearanceItems, SWT.NONE);
		lblProductName.setBounds(10, 45, 77, 15);
		lblProductName.setText("Product Name");
		
		Label lblId = new Label(shlClearanceItems, SWT.NONE);
		lblId.setBounds(185, 45, 25, 15);
		lblId.setText("ID");
		
		Label lblCategory = new Label(shlClearanceItems, SWT.NONE);
		lblCategory.setText("Category");
		lblCategory.setBounds(220, 45, 48, 15);
		
		Label lblQuantity = new Label(shlClearanceItems, SWT.NONE);
		lblQuantity.setText("Quantity");
		lblQuantity.setBounds(285, 45, 48, 15);
		
		Label lblOrderCost = new Label(shlClearanceItems, SWT.NONE);
		lblOrderCost.setText("Order Cost");
		lblOrderCost.setBounds(350, 45, 57, 15);
		
		Label lblPurchasePrice = new Label(shlClearanceItems, SWT.NONE);
		lblPurchasePrice.setText("Purchase Price");
		lblPurchasePrice.setBounds(418, 45, 82, 15);
		
		Label lblInStock = new Label(shlClearanceItems, SWT.NONE);
		lblInStock.setText("In Stock?");
		lblInStock.setBounds(525, 45, 57, 15);
		
		Label lblPerishable = new Label(shlClearanceItems, SWT.NONE);
		lblPerishable.setText("Perishable?");
		lblPerishable.setBounds(583, 45, 75, 15);
	}
	
	//Populate clearance items into this window
	public int PopulateClearanceProducts(Composite inner, ArrayList<ProductItem> productList, Color lightRed, Color lightGreen) {
		//Assign the list
		ArrayList<ProductItem> productListing = productList;
		
		//Create a height
		int height = 0;
		
		//Loop through each object
		for(int index = 0; index < productListing.size(); index++) {
			//Create a product
			ProductItem product = productListing.get(index);
			
			//Add controls
			Label name = new Label(inner, SWT.None);
			name.setText(product.getName());
			name.setBounds(2, index * 20 + 5, 170, 15);
			
			Label id = new Label(inner, SWT.None);
			id.setText(String.valueOf(product.getProductID()));
			id.setBounds(175, index * 20 + 5, 30, 20);
			
			Label cat = new Label(inner, SWT.None);
			cat.setText(product.getCategory());
			cat.setBounds(210, index * 20 + 5, 60, 15);
			
			Label numInStock = new Label(inner, SWT.None);
			numInStock.setText(String.valueOf(product.getStockNum()));
			numInStock.setBounds(275, index * 20 + 5, 50, 15);
			
			Label cost = new Label(inner, SWT.None);
			cost.setText("$" + String.valueOf(product.getCost()));
			cost.setBounds(340, index * 20 + 5, 50, 15);
			
			Label price = new Label(inner, SWT.None);
			price.setText("$" + String.valueOf(product.getPrice()));
			price.setBounds(408, index * 20 + 5, 50, 15);
			
			Label inStock= new Label(inner, SWT.None);
			inStock.setAlignment(SWT.CENTER);
			inStock.setText(TabbedGrouping.capitalizeFirst(product.getInStock().toString()));
			inStock.setBounds(510, index * 20 + 5, 50, 20);
			
			Label perish = new Label(inner, SWT.None);
			perish.setAlignment(SWT.CENTER);
			perish.setText(TabbedGrouping.capitalizeFirst(product.getPerishable().toString()));
			perish.setBounds(573, index * 20 + 5, 50, 20);

			//Add colors for in stock
			if(!product.getInStock()) {
				//Set the color to red
				inStock.setBackground(lightRed);
			}
			
			//Add colors for perishable
			if(product.getPerishable()) {
				//Set color to red
				perish.setBackground(lightRed);
			} else {
				//Set color to green
				perish.setBackground(lightGreen);
			}
			
			//Adjust height
			height = 20 * index + 5;
		}
		
		//Return a height
		return height + 25;
	}
}
