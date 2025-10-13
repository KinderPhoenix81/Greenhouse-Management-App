package greenhouseManagementSystem;

//Imports
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

public class ViewSeasonalInventoryDialog extends Dialog {
	//Fields for this dialog class
	protected Object result;
	protected Shell shlSeasonalItems;
	private ArrayList<ProductItem> inventory;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ViewSeasonalInventoryDialog(Shell parent, int style, ArrayList<ProductItem> seasonalItems) {
		super(parent, style);
		setText("SWT Dialog");
		inventory = seasonalItems;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(Color lightRed, Color lightGreen) {
		createContents(lightRed, lightGreen);
		shlSeasonalItems.open();
		shlSeasonalItems.layout();
		Display display = getParent().getDisplay();
		while (!shlSeasonalItems.isDisposed()) {
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
		shlSeasonalItems = new Shell(getParent(), getStyle());
		shlSeasonalItems.setMinimumSize(new Point(700, 450));
		shlSeasonalItems.setSize(450, 300);
		shlSeasonalItems.setText("Seasonal Items");
		
		//Exit button
		Button btnExit = new Button(shlSeasonalItems, SWT.NONE);
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//closes shell
				shlSeasonalItems.dispose();
			}
		});
		btnExit.setText("Exit");
		btnExit.setBounds(599, 10, 75, 25);
		
		//Context label for the dialog
		Label lblViewingSeasonalInventory = new Label(shlSeasonalItems, SWT.NONE);
		lblViewingSeasonalInventory.setBounds(10, 10, 144, 15);
		lblViewingSeasonalInventory.setText("Viewing Seasonal Inventory");
		
		//Outer composite to hold inner composite content
		ScrolledComposite seasonalInventory_outerComposite = new ScrolledComposite(shlSeasonalItems, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		seasonalInventory_outerComposite.setExpandHorizontal(true);
		seasonalInventory_outerComposite.setBounds(10, 66, 651, 335);
		
		//Inner composite for actual content
		Composite seasonalInventory_innerComposite = new Composite(seasonalInventory_outerComposite, SWT.NONE);
		seasonalInventory_innerComposite.setLayout(null);
		seasonalInventory_innerComposite.setBounds(10, 66, 651, 335);
		
		//Matching the content of the inner and outer composites
		seasonalInventory_outerComposite.setContent(seasonalInventory_innerComposite);
		seasonalInventory_outerComposite.setMinSize(seasonalInventory_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate content to the inner composite
		int seasonalHeight = PopulateSeasonalInventory(seasonalInventory_innerComposite, inventory, lightRed, lightGreen);
		seasonalInventory_innerComposite.setSize(651, seasonalHeight);
		
		//Headers for each product's fields
		Label lblProductName = new Label(shlSeasonalItems, SWT.NONE);
		lblProductName.setText("Product Name");
		lblProductName.setBounds(10, 45, 77, 15);
		
		Label lblPerishable = new Label(shlSeasonalItems, SWT.NONE);
		lblPerishable.setText("Perishable?");
		lblPerishable.setBounds(583, 45, 75, 15);
		
		Label lblInStock = new Label(shlSeasonalItems, SWT.NONE);
		lblInStock.setText("In Stock?");
		lblInStock.setBounds(525, 45, 57, 15);
		
		Label lblPurchasePrice = new Label(shlSeasonalItems, SWT.NONE);
		lblPurchasePrice.setText("Purchase Price");
		lblPurchasePrice.setBounds(418, 45, 82, 15);
		
		Label lblOrderCost = new Label(shlSeasonalItems, SWT.NONE);
		lblOrderCost.setText("Order Cost");
		lblOrderCost.setBounds(350, 45, 57, 15);
		
		Label lblQuantity = new Label(shlSeasonalItems, SWT.NONE);
		lblQuantity.setText("Quantity");
		lblQuantity.setBounds(285, 45, 48, 15);
		
		Label lblCategory = new Label(shlSeasonalItems, SWT.NONE);
		lblCategory.setText("Category");
		lblCategory.setBounds(220, 45, 48, 15);
		
		Label lblId = new Label(shlSeasonalItems, SWT.NONE);
		lblId.setText("ID");
		lblId.setBounds(189, 45, 25, 15);

	}
	
	//Populate clearance items into this window
		public int PopulateSeasonalInventory(Composite inner, ArrayList<ProductItem> productList, Color lightRed, Color lightGreen) {
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
				id.setAlignment(SWT.CENTER);
				id.setText(String.valueOf(product.getProductID()));
				id.setBounds(170, index * 20 + 5, 30, 15);
				
				Label cat = new Label(inner, SWT.None);
				cat.setText(product.getCategory());
				cat.setBounds(210, index * 20 + 5, 60, 15);
				
				Label numInStock = new Label(inner, SWT.None);
				numInStock.setAlignment(SWT.CENTER);
				numInStock.setText(String.valueOf(product.getStockNum()));
				numInStock.setBounds(271, index * 20 + 5, 50, 15);
				
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
					id.setBackground(lightRed);
					numInStock.setBackground(lightRed);
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
