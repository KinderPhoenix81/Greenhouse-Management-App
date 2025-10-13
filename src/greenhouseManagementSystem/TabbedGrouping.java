package greenhouseManagementSystem;

//Imports
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.custom.CTabFolder;

import org.eclipse.swt.graphics.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.custom.ScrolledComposite;

public class TabbedGrouping {
	//Fields for the main window
	protected Shell shlGreenhouseManagementApplication;
	private Color lightGreen;
	private Color lightRed;
	private Color lightYellow;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TabbedGrouping window = new TabbedGrouping();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		
		//Set colors
		lightGreen = new Color(display, 144, 238, 144);
		lightRed = new Color(display, 255, 102, 102);
		lightYellow = new Color(display, 255, 255, 153);
		
		createContents();
		shlGreenhouseManagementApplication.open();
		shlGreenhouseManagementApplication.layout();
		while (!shlGreenhouseManagementApplication.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		//Add a management object to reference for populating data
		Management manage = new Management();
		
		//Shell info
		shlGreenhouseManagementApplication = new Shell();
		shlGreenhouseManagementApplication.setMinimumSize(new Point(700, 450));
		shlGreenhouseManagementApplication.setSize(450, 300);
		shlGreenhouseManagementApplication.setText("Greenhouse Management Application");
		
		//Start creating content for each tab in the tab folder
		CTabFolder tabFolder = new CTabFolder(shlGreenhouseManagementApplication, SWT.BORDER);
		tabFolder.setBounds(0, 0, 684, 411);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		//Employess tab content
		CTabItem tbtmEmployees = new CTabItem(tabFolder, SWT.NONE);
		tbtmEmployees.setText("Employees");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmEmployees.setControl(composite);
		
		//Headers for each employee
		Label lblWelcomeToThe = new Label(composite, SWT.NONE);
		lblWelcomeToThe.setBounds(10, 10, 275, 15);
		lblWelcomeToThe.setText("Welcome to the Employee Management Interface");
		
		Label lblLetsSeeWhos = new Label(composite, SWT.NONE);
		lblLetsSeeWhos.setBounds(10, 52, 166, 15);
		lblLetsSeeWhos.setText("Let's see who's working today...");
		
		Label lblWorkStatus = new Label(composite, SWT.NONE);
		lblWorkStatus.setBounds(225, 52, 63, 15);
		lblWorkStatus.setText("Work Status");
		
		Label lblPosition = new Label(composite, SWT.NONE);
		lblPosition.setBounds(349, 52, 55, 15);
		lblPosition.setText("Position");
		
		Label lblOpening = new Label(composite, SWT.NONE);
		lblOpening.setBounds(514, 52, 55, 15);
		lblOpening.setText("Opening?");
		
		Label lblClosing = new Label(composite, SWT.NONE);
		lblClosing.setBounds(580, 52, 55, 15);
		lblClosing.setText("Closing?");
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 73, 662, 2);
		
		//View employees button
		Button btnViewEmployees = new Button(composite, SWT.NONE);
		btnViewEmployees.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//opens panel to view employees
				ViewEmployeesDialog dialog = new ViewEmployeesDialog(shlGreenhouseManagementApplication, 0, manage.getFullEmployeeList());
				dialog.open(lightRed, lightGreen);
			}
		});
		btnViewEmployees.setBounds(10, 349, 105, 25);
		btnViewEmployees.setText("View Employees");
		
		//Add employee button
		Button btnAddEmployee = new Button(composite, SWT.NONE);
		btnAddEmployee.setBounds(151, 349, 89, 25);
		btnAddEmployee.setText("Add Employee");
		
		//Edit employee button
		Button btnEditEmployees = new Button(composite, SWT.NONE);
		btnEditEmployees.setBounds(277, 349, 105, 25);
		btnEditEmployees.setText("Edit Employee");
		
		//Remove employee button
		Button btnRemoveEmployee = new Button(composite, SWT.NONE);
		btnRemoveEmployee.setBounds(417, 349, 111, 25);
		btnRemoveEmployee.setText("Remove Employee");
		
		//Close button
		Button btnClose = new Button(composite, SWT.NONE);
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//Close program
				System.exit(0);
			}
		});
		btnClose.setText("Close");
		btnClose.setBounds(617, 349, 55, 25);
		
		//Create a scrolled composite container
		ScrolledComposite onDutyEmployee_ScrollComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		onDutyEmployee_ScrollComposite.setBounds(10, 73, 662, 270);
		onDutyEmployee_ScrollComposite.setExpandHorizontal(true);
		
		//Create the inner composite to hold actual content
		Composite InnerContent_onDutyEmployees = new Composite(onDutyEmployee_ScrollComposite, SWT.NONE);
		InnerContent_onDutyEmployees.setLayout(null);
		InnerContent_onDutyEmployees.setBounds(10, 73, 662, 270);
		
		//Match the content for the inner and outer composites
		onDutyEmployee_ScrollComposite.setContent(InnerContent_onDutyEmployees);
		onDutyEmployee_ScrollComposite.setMinSize(InnerContent_onDutyEmployees.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate the scrolled composite with on duty employee info & update the size of the container
		int onDutyHeight = PopulateOnDutyEmployeesForm(InnerContent_onDutyEmployees, manage.getOnDutyList());
		InnerContent_onDutyEmployees.setSize(662, onDutyHeight);
		
		//Inventory tab content
		CTabItem tbtmInventory = new CTabItem(tabFolder, SWT.NONE);
		tbtmInventory.setText("Inventory");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmInventory.setControl(composite_1);
		
		//Close button
		Button btnClose_1 = new Button(composite_1, SWT.NONE);
		btnClose_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//Close
				System.exit(0);
			}
		});
		btnClose_1.setText("Close");
		btnClose_1.setBounds(617, 349, 55, 25);
		
		Label lblWelcomeToThe_1 = new Label(composite_1, SWT.NONE);
		lblWelcomeToThe_1.setText("Welcome to the Inventory Management Interface");
		lblWelcomeToThe_1.setBounds(10, 10, 262, 15);
		
		Label lblCurrentInventory = new Label(composite_1, SWT.NONE);
		lblCurrentInventory.setBounds(10, 50, 93, 15);
		lblCurrentInventory.setText("Product Name");
		
		Label label_5 = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_5.setBounds(10, 71, 662, 2);
		
		Label label_6 = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_6.setBounds(10, 341, 662, 2);
		
		//View clearance items button
		Button btnViewClearanceItems = new Button(composite_1, SWT.NONE);
		btnViewClearanceItems.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//opens pane to view all clearance items
				ViewClearanceInventoryDialog dialog = new ViewClearanceInventoryDialog(shlGreenhouseManagementApplication, 0, manage.getClearanceInventory());
				dialog.open(lightRed, lightGreen);
			}
		});
		btnViewClearanceItems.setText("View Clearance Items");
		btnViewClearanceItems.setBounds(177, 349, 124, 25);
		
		//View seasonal items button
		Button btnViewSeasonalItems = new Button(composite_1, SWT.NONE);
		btnViewSeasonalItems.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//opens pane to view all seasonal items
				ViewSeasonalInventoryDialog dialog = new ViewSeasonalInventoryDialog(shlGreenhouseManagementApplication, 0, manage.getSeasonalInventory());
				dialog.open(lightRed, lightGreen);
			}
		});
		btnViewSeasonalItems.setText("View Seasonal Items");
		btnViewSeasonalItems.setBounds(346, 349, 124, 25);
		
		//Modify inventory button
		Button btnModifyInventory = new Button(composite_1, SWT.NONE);
		btnModifyInventory.setText("Manage Inventory");
		btnModifyInventory.setBounds(20, 349, 112, 25);
		
		//Outer composite for holding inner composite
		ScrolledComposite currentInventory_outerComposite = new ScrolledComposite(composite_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		currentInventory_outerComposite.setBounds(10, 71, 662, 264);
		currentInventory_outerComposite.setExpandHorizontal(true);
		
		//Inner composite to hold content
		Composite currentInventory_innerComposite = new Composite(currentInventory_outerComposite, SWT.NONE);
		currentInventory_innerComposite.setLayout(null);
		currentInventory_innerComposite.setBounds(10, 71, 662, 264);
		
		//Match contents of composites
		currentInventory_outerComposite.setContent(currentInventory_innerComposite);
		currentInventory_outerComposite.setMinSize(currentInventory_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate the composite and set size
		int currentInventoryHeight = PopulateCurrentInventoryForm(currentInventory_innerComposite, manage.getCurrentInventory());
		currentInventory_innerComposite.setSize(662, currentInventoryHeight);
		
		//Headers for each product
		Label lblId = new Label(composite_1, SWT.NONE);
		lblId.setText("ID");
		lblId.setBounds(221, 50, 23, 15);
		
		Label lblCategory = new Label(composite_1, SWT.NONE);
		lblCategory.setText("Category");
		lblCategory.setBounds(250, 50, 55, 15);
		
		Label lblQuantityInStock = new Label(composite_1, SWT.NONE);
		lblQuantityInStock.setText("Quantity In Stock");
		lblQuantityInStock.setBounds(346, 50, 93, 15);
		
		Label lblPerishable = new Label(composite_1, SWT.NONE);
		lblPerishable.setText("Perishable?");
		lblPerishable.setBounds(515, 50, 66, 15);
		
		Label lblClearance = new Label(composite_1, SWT.NONE);
		lblClearance.setText("Clearance?");
		lblClearance.setBounds(582, 50, 66, 15);
		
		Label lblPrice = new Label(composite_1, SWT.NONE);
		lblPrice.setText("Price");
		lblPrice.setBounds(462, 50, 50, 15);
		
		//Orders tab content
		CTabItem tbtmOrders = new CTabItem(tabFolder, SWT.NONE);
		tbtmOrders.setText("Orders");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmOrders.setControl(composite_2);
		
		//Close button
		Button btnClose_2 = new Button(composite_2, SWT.NONE);
		btnClose_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
			//close
			System.exit(0);
			}
		});
		btnClose_2.setText("Close");
		btnClose_2.setBounds(617, 349, 55, 25);
		
		//Context labels for the window
		Label lblWelcomeToThe_2 = new Label(composite_2, SWT.NONE);
		lblWelcomeToThe_2.setText("Welcome to the Orders Inbox");
		lblWelcomeToThe_2.setBounds(10, 10, 262, 15);
		
		Label lblCurrentOrders = new Label(composite_2, SWT.NONE);
		lblCurrentOrders.setBounds(10, 50, 89, 15);
		lblCurrentOrders.setText("Incoming Orders");
		
		Label lblPreviousOrders = new Label(composite_2, SWT.NONE);
		lblPreviousOrders.setText("Received Orders");
		lblPreviousOrders.setBounds(345, 50, 89, 15);
		
		Label label_7 = new Label(composite_2, SWT.SEPARATOR | SWT.VERTICAL);
		label_7.setBounds(337, 50, 2, 324);
		
		Label label_8 = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_8.setBounds(10, 71, 662, 2);
		
		//Receive order button
		Button btnReceiveOrder = new Button(composite_2, SWT.NONE);
		btnReceiveOrder.setBounds(10, 349, 101, 25);
		btnReceiveOrder.setText("Receive an Order");
		
		//Place order button
		Button btnPlaceAnOrder = new Button(composite_2, SWT.NONE);
		btnPlaceAnOrder.setText("Place an Order");
		btnPlaceAnOrder.setBounds(345, 349, 89, 25);
		
		//Outer composite to hold inner composite (incoming orders)
		ScrolledComposite incomingOrders_outerComposite = new ScrolledComposite(composite_2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		incomingOrders_outerComposite.setBounds(10, 71, 325, 272);
		incomingOrders_outerComposite.setExpandHorizontal(true);
		
		//Inner composite to hold content (incoming orders)
		Composite incomingOrders_innerComposite = new Composite(incomingOrders_outerComposite, SWT.NONE);
		incomingOrders_innerComposite.setLayout(null);
		incomingOrders_innerComposite.setBounds(10, 71, 325, 272);
		
		//Match contents of composites (incoming orders)
		incomingOrders_outerComposite.setContent(incomingOrders_innerComposite);
		incomingOrders_outerComposite.setMinSize(incomingOrders_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate the scrolled composite with content (incoming orders)
		int incomingOrderHeight = PopulateOrdersForm(incomingOrders_innerComposite, manage.getOutgoingOrders());
		incomingOrders_innerComposite.setSize(325, incomingOrderHeight);
		
		//Outer composite to hold inner composite (received orders)
		ScrolledComposite receivedOrders_outerComposite = new ScrolledComposite(composite_2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		receivedOrders_outerComposite.setBounds(337, 72, 335, 271);
		receivedOrders_outerComposite.setExpandHorizontal(true);

		//Inner composite to hold content (received orders)
		Composite receivedOrders_innerComposite = new Composite(receivedOrders_outerComposite, SWT.NONE);
		receivedOrders_innerComposite.setLayout(null);
		receivedOrders_innerComposite.setBounds(337, 72, 335, 271);
		
		//Match contents of composites (received orders)
		receivedOrders_outerComposite.setContent(receivedOrders_innerComposite);
		receivedOrders_outerComposite.setMinSize(receivedOrders_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate the scrolled composite with received orders
		int receivedOrderHeight = PopulateOrdersForm(receivedOrders_innerComposite, manage.getReceivedOrders());
		receivedOrders_innerComposite.setSize(335, receivedOrderHeight);
		
		//Invoices tab content
		CTabItem tbtmInvoices = new CTabItem(tabFolder, SWT.NONE);
		tbtmInvoices.setText("Invoices");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmInvoices.setControl(composite_3);
		
		//Close button
		Button btnClose_3 = new Button(composite_3, SWT.NONE);
		btnClose_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
			//close
			System.exit(0);
			}
		});
		btnClose_3.setText("Close");
		btnClose_3.setBounds(617, 349, 55, 25);
		
		//Context labels for the window
		Label lblWelcomeToThe_4 = new Label(composite_3, SWT.NONE);
		lblWelcomeToThe_4.setText("Welcome to the Invoices Inbox");
		lblWelcomeToThe_4.setBounds(10, 10, 262, 15);
		
		Label lblCurrentInvoices = new Label(composite_3, SWT.NONE);
		lblCurrentInvoices.setBounds(10, 50, 86, 15);
		lblCurrentInvoices.setText("Unpaid Invoices");
		
		Label lblPreviousInvoices = new Label(composite_3, SWT.NONE);
		lblPreviousInvoices.setText("Paid Invoices");
		lblPreviousInvoices.setBounds(342, 50, 86, 15);
		
		Label label_9 = new Label(composite_3, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_9.setBounds(10, 71, 662, 2);
		
		Label label_10 = new Label(composite_3, SWT.SEPARATOR | SWT.VERTICAL);
		label_10.setBounds(334, 50, 2, 324);
		
		//Pay invoice button
		Button btnPayInvoice = new Button(composite_3, SWT.NONE);
		btnPayInvoice.setBounds(10, 349, 75, 25);
		btnPayInvoice.setText("Pay Invoice");
		
		//Create a scroll composite container for unpaid invoices
		ScrolledComposite unpaidInvoices_outerComposite = new ScrolledComposite(composite_3, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		unpaidInvoices_outerComposite.setBounds(10, 71, 326, 265);
		unpaidInvoices_outerComposite.setExpandHorizontal(true);

		//Create an inner composite container for actual content
		Composite unpaidInvoices_innerComposite = new Composite(unpaidInvoices_outerComposite, SWT.NONE);
		unpaidInvoices_innerComposite.setLayout(null);
		unpaidInvoices_innerComposite.setBounds(10, 71, 326, 265);
		
		//Setting the content of the outer container
		unpaidInvoices_outerComposite.setContent(unpaidInvoices_innerComposite);
		unpaidInvoices_outerComposite.setMinSize(unpaidInvoices_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate the inner container with actual content
		int unpaidInvoiceHeight = PopulateInvoicesForm(unpaidInvoices_innerComposite, manage.getUnpaidInvoices(), true);
		unpaidInvoices_innerComposite.setSize(326, unpaidInvoiceHeight);
		
		//Create a scroll composite container for paid invoices
		ScrolledComposite paidInvoices_outerComposite = new ScrolledComposite(composite_3, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		paidInvoices_outerComposite.setBounds(334, 71, 338, 265);
		paidInvoices_outerComposite.setExpandHorizontal(true);

		//Create an inner composite for actual content
		Composite paidInvoices_innerComposite = new Composite(paidInvoices_outerComposite, SWT.NONE);
		paidInvoices_innerComposite.setLayout(null);
		paidInvoices_innerComposite.setBounds(334, 71, 338, 265);
		
		//Setting the content of the outer container
		paidInvoices_outerComposite.setContent(paidInvoices_innerComposite);
		paidInvoices_outerComposite.setMinSize(paidInvoices_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate the inner container with actual content
		int paidInvoiceHeight = PopulateInvoicesForm(paidInvoices_innerComposite, manage.getPaidInvoices(), false);
		paidInvoices_innerComposite.setSize(338, paidInvoiceHeight);
		
		//Event listeners are moved down here, as all content on the window needs to be loaded in order to run some actions
		//Add employee event listener
		btnAddEmployee.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//opens panel to add an employee
				AddEmployeeDialog dialog = new AddEmployeeDialog(shlGreenhouseManagementApplication, 0, manage.getFullEmployeeList());
				dialog.open();
				
				//See if a new employee was added
				if(AddEmployeeDialog.getAdded()) {
					//Perform a visual and backend refresh of all lists
					RefreshEmployeeListings(manage, InnerContent_onDutyEmployees);
				}
			}
		});
		
		//Modify employee event listener
		btnEditEmployees.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//opens panel to edit employee info
				EditEmployeesDialog dialog = new EditEmployeesDialog(shlGreenhouseManagementApplication, 0, manage.getFullEmployeeList());
				dialog.open();
				
				//See if an employee was updated
				if(EditEmployeesDialog.getUpdated()) {
					//Perform a visual and backend refresh of all lists
					RefreshEmployeeListings(manage, InnerContent_onDutyEmployees);
				}
			}
		});
		
		//Delete employee event listener
		btnRemoveEmployee.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//opens panel to remove an employee
				RemoveEmployeeDialog dialog = new RemoveEmployeeDialog(shlGreenhouseManagementApplication, 0, manage.getFullEmployeeList());
				dialog.open();
				
				//See if an employee was deleted
				if(RemoveEmployeeDialog.getDeleted()) {
					//Perform a visual and backend refresh of all lists
					RefreshEmployeeListings(manage, InnerContent_onDutyEmployees);
				}
			}
		});
		
		//Pay invoice event listener
		btnPayInvoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//opens pane to pay off an invoice
				PayInvoiceDialog dialog = new PayInvoiceDialog(shlGreenhouseManagementApplication, 0, manage.getUnpaidInvoices());
				dialog.open();
				
				//See if an invoice was paid
				if(PayInvoiceDialog.getPaid()) {
					//Perform a visual and backend refresh of all lists
					RefreshInvoiceListings(manage, unpaidInvoices_innerComposite,  paidInvoices_innerComposite);
				}
			}
		});
		
		//Receive order event listener
		btnReceiveOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//opens pane to receive an order
				ReceiveOrderDialog dialog = new ReceiveOrderDialog(shlGreenhouseManagementApplication, 0, manage.getOutgoingOrders());
				dialog.open();
				
				//See if an order was received
				if(ReceiveOrderDialog.getReceived()) {
					//Perform a visual and backend refresh of all lists
					RefreshOrderListings(manage, incomingOrders_innerComposite, receivedOrders_innerComposite);
				}
			}
		});
		
		//Modify product event listener
		btnModifyInventory.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//opens pane to manually modify the inventory values
				ManageInventoryDialog dialog = new ManageInventoryDialog(shlGreenhouseManagementApplication, 0, manage.getFullInventory());
				dialog.open();
				
				//See if a product was modified
				if(ManageInventoryDialog.getUpdated()) {
					//Perform a visual and backend refresh of all lists
					RefreshProductListings(manage, currentInventory_innerComposite);
				}
			}
		});
		
		//Place order event listener
		btnPlaceAnOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//opens pane to place an order
				PlaceOrderDialog dialog = new PlaceOrderDialog(shlGreenhouseManagementApplication, 0, manage.getFullInventory(), manage.getAllOrders());
				dialog.open(lightYellow);
				
				//See if an order was placed
				if(PlaceOrderDialog.getOrderPlaced()) {
					//Visual and backend refresh the orders and invoices
					RefreshOrderListings(manage, incomingOrders_innerComposite, receivedOrders_innerComposite);
					RefreshInvoiceListings(manage, unpaidInvoices_innerComposite,  paidInvoices_innerComposite);
				}
			}
		});
	}
	
	//Populate the employees panel with information
	public int PopulateOnDutyEmployeesForm (Composite scrolledComposite, ArrayList<Employee> onDutyList) {
		
		//Create a list of on duty employees to work with
		ArrayList<Employee> onDutyEmployeeList = onDutyList;
		
		//Variable for height
		int height = 0;
		
		//Loop through the on duty employee list and add them dynamically on the scrolled composite
		for(int index = 0; index < onDutyEmployeeList.size(); index++) {
			//Get the employee
			Employee employee = onDutyEmployeeList.get(index);
			
				//Add controls
				Label name = new Label(scrolledComposite, SWT.None);
				name.setText(employee.getName());
				name.setBounds(5, index * 20 + 5, 120, 15);
				
				Label status = new Label(scrolledComposite, SWT.None);
				status.setText(employee.getEmploymentStatus());
				status.setBounds(215, index * 20 + 5, 120, 15);
				
				Label position = new Label(scrolledComposite, SWT.None);
				position.setText(employee.getPosition());
				position.setBounds(338, index * 20 + 5, 130, 15);
				
				Label open = new Label(scrolledComposite, SWT.None);
				open.setAlignment(SWT.CENTER);
				open.setText(capitalizeFirst(employee.getOpen().toString()));
				open.setBounds(495, index * 20 + 5, 60, 20);
				
				Label close = new Label(scrolledComposite, SWT.None);
				close.setAlignment(SWT.CENTER);
				close.setText(capitalizeFirst(employee.getClose().toString()));
				close.setBounds(558, index * 20 + 5, 60, 20);
				
				//Add color to the opener labels
				if(employee.getOpen()) {
					open.setBackground(lightGreen);
				} else {
					open.setBackground(lightRed);
				}
				
				//Add color to the closer labels
				if(employee.getClose()) {
					close.setBackground(lightGreen);
				} else {
					close.setBackground(lightRed);
				}
				
				//Assign height
				height = index * 20 + 20;
		}
		//Debug
		//System.out.println("\nOn Duty Employees Successfully Loaded!\n");
		
		//Return for size calc
		return height;
	}
	
	//Populate the current inventory screen
	public int PopulateCurrentInventoryForm (Composite inner, ArrayList<ProductItem> productList) {
		
		//Create a list for current inventory
		ArrayList<ProductItem> currentInventoryList = productList;
		
		//Variable for height
		int height = 0;
		
		//Loop through each item and add them dynamically
		for(int index = 0; index < currentInventoryList.size(); index++) {
			//Get the product
			ProductItem product = currentInventoryList.get(index);
			
			//Add controls
			Label name = new Label(inner, SWT.None);
			name.setText(product.getName());
			name.setBounds(3, index * 20 + 5, 190, 15);
			
			Label id = new Label(inner, SWT.None);
			id.setAlignment(SWT.CENTER);
			id.setText(String.valueOf(product.getProductID()));
			id.setBounds(202, index * 20 + 5, 30, 15);
			
			Label category = new Label(inner, SWT.None);
			category.setText(product.getCategory());
			category.setBounds(240, index * 20 + 5, 80, 15);
			
			Label quantity = new Label(inner, SWT.None);
			quantity.setText(String.valueOf(product.getStockNum()));
			quantity.setBounds(335, index * 20 + 5, 50, 15);
			
			Label price = new Label(inner, SWT.None);
			price.setText("$" + String.valueOf(product.getPrice()));
			price.setBounds(450, index * 20 + 5, 50, 15);
			
			Label perishable = new Label(inner, SWT.None);
			perishable.setAlignment(SWT.CENTER);
			perishable.setText(capitalizeFirst(product.getPerishable().toString()));
			perishable.setBounds(505, index * 20 + 5, 60, 20);
			
			Label clearance = new Label(inner, SWT.None);
			clearance.setAlignment(SWT.CENTER);
			clearance.setText(capitalizeFirst(product.getClearanceStatus().toString()));
			clearance.setBounds(568, index * 20 + 5, 60, 20);
			
			//Assign colors to perishable labels
			if(product.getPerishable()) {
				perishable.setBackground(lightRed);
			} else {
				perishable.setBackground(lightGreen);
			}
			
			//Assign colors to clearance labels
			if(product.getClearanceStatus()) {
				clearance.setBackground(lightYellow);
				id.setBackground(lightYellow);
			}
			
			//Assign height
			height = index * 20 + 25;
		}
		//Debug
		//System.out.println("Current Inventory Successfully Loaded!\n");
		
		//Return for size calc
		return height;
	}
	
	//Populate the orders lists
	public int PopulateOrdersForm(Composite inner, ArrayList<Order> orderList) {
		
		//Create a list
		ArrayList<Order> orderListing = orderList;
		
		//Variable for container height
		int height = 20;
		
		//Variable for display height in container
		int displayHeight = 25;
		
		//Create headers
		Label id = new Label(inner, SWT.None);
		id.setText("Order ID");
		id.setBounds(5, 5, 75, 15);
		
		Label total = new Label(inner, SWT.None);
		total.setText("Order Total");
		total.setBounds(80, 5, 75, 15);
		
		Label date = new Label(inner, SWT.None);
		date.setText("Order Date");
		date.setBounds(170, 5, 75, 15);
		
		//Loop through each order
		for(int index = 0; index < orderListing.size(); index++) {
			//Create an order object to access
			Order order = orderListing.get(index);
			
			//Add controls
			Label orderID = new Label(inner, SWT.None);
			orderID.setText(String.valueOf(order.getID()));
			orderID.setBounds(5, displayHeight, 25, 15);
			
			Label orderTotal = new Label(inner, SWT.None);
			orderTotal.setText("$" + String.valueOf(order.getTotal()));
			orderTotal.setBounds(80, displayHeight, 70, 15);
			
			Label orderDate = new Label(inner, SWT.None);
			orderDate.setText(String.valueOf(order.getDate()));
			orderDate.setBounds(170, displayHeight, 70, 15);

			//Increment display height for order item headers
			displayHeight += 20;
			
			//Now we create the order items present in each order
			ArrayList<ProductItem> orderProducts = order.getOrderItems();
			
			//Create headers for the product items
			Label idHeader = new Label(inner, SWT.None);
			idHeader.setText("Product ID");
			idHeader.setBounds(15, displayHeight, 60, 15);
			
			Label quantityHeader = new Label(inner, SWT.None);
			quantityHeader.setText("Quantity");
			quantityHeader.setBounds(95, displayHeight, 60, 15);
			
			Label priceHeader = new Label(inner, SWT.None);
			priceHeader.setText("Order Cost");
			priceHeader.setBounds(160, displayHeight, 60, 15);
			
			Label totalPriceHeader = new Label(inner, SWT.None);
			totalPriceHeader.setText("Total Cost");
			totalPriceHeader.setBounds(225, displayHeight, 60, 15);
			
			//Increment display height for order items
			displayHeight += 15;
			
			//Loop through each item in the product order
			for(int indexer = 0; indexer < orderProducts.size(); indexer++) {
				//Create the order item
				ProductItem item = orderProducts.get(indexer);
				
				//Add controls for each item
				Label productID = new Label(inner, SWT.None);
				productID.setText(String.valueOf(item.getProductID()));
				productID.setBounds(15, displayHeight, 25, 15);
				
				Label productQuantity = new Label(inner, SWT.None);
				productQuantity.setText(String.valueOf(item.getStockNum()));
				productQuantity.setBounds(95, displayHeight, 25, 15);
				
				Label productPrice = new Label(inner, SWT.None);
				productPrice.setText("$" + String.valueOf(item.getCost()));
				productPrice.setBounds(160, displayHeight, 45, 15);
				
				Label totalPrice = new Label(inner, SWT.None);
				totalPrice.setText("$" + String.valueOf(item.getStockNum() * item.getCost()));
				totalPrice.setBounds(225, displayHeight, 45, 15);
				
				//Incremenet display height for next item
				displayHeight += 15;
			}
			
			//Increment display height for next order item
			displayHeight += 25;
			
		}
		//Assign height to displayHeight
		height = displayHeight;
		
		//Debug
		//System.out.println("Order List Successfully Loaded!\n");
		
		//Return height
		return height;
	}
	
	//Populate the invoice lists with actual data on the form
	public int PopulateInvoicesForm(Composite inner, ArrayList<Invoice> invoiceList, Boolean unpaidInvoiceComposite) {
		
		//Create the list of invoices
		ArrayList<Invoice> invoiceListing = invoiceList;
		
		//Height variable for container
		int height = 0;
		
		//Create header controls
		Label idHeader = new Label(inner, SWT.None);
		idHeader.setText("Invoice ID");
		idHeader.setBounds(5, 5, 75, 15);
		
		Label totalHeader = new Label(inner, SWT.None);
		totalHeader.setText("Invoice Total");
		totalHeader.setBounds(80, 5, 75, 15);
		
		Label dateHeader = new Label(inner, SWT.None);
		dateHeader.setText("Created Date");
		dateHeader.setBounds(155, 5, 85, 15);
		
		Label dueDateHeader = new Label(inner, SWT.None);
		dueDateHeader.setText("Due Date");
		dueDateHeader.setBounds(245, 5, 85, 15);
		
		//Loop through unpaid invoices
		for(int index = 0; index < invoiceListing.size(); index++) {
			//Create an invoice
			Invoice invoice = invoiceListing.get(index);
			
			//Create controls
			Label id = new Label(inner, SWT.None);
			id.setText(String.valueOf(invoice.getID()));
			id.setBounds(5, index * 20 + 25, 75, 15);
			
			Label total = new Label(inner, SWT.None);
			total.setText("$" + String.valueOf(invoice.getTotal()));
			total.setBounds(80, index * 20 + 25, 75, 15);
			
			Label date = new Label(inner, SWT.None);
			date.setText(String.valueOf(invoice.getDate()));
			date.setBounds(155, index * 20 + 25, 85, 15);
			
			Label dueDate = new Label(inner, SWT.None);
			dueDate.setAlignment(SWT.CENTER);
			dueDate.setText(String.valueOf(invoice.getDueDate()));
			dueDate.setBounds(245, index * 20 + 25, 63, 15);
			
			//If we are working with the unpaid invoice composite, color the due dates
			if(unpaidInvoiceComposite) {
				//Get the current day
				LocalDate currentDate = LocalDate.now();
				
				//Get the due date and parse it as a day
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate invoiceDueDate = LocalDate.parse(dueDate.getText(), formatter);
				
				//Make checks based on the current date
				if(invoiceDueDate.isBefore(currentDate)) {
					//Color background red, invoice is overdue
					dueDate.setBackground(lightRed);
				} else if(invoiceDueDate.equals(currentDate)) {
					//Color background yellow, is due today
					dueDate.setBackground(lightYellow);
				} else {
					//Not due today or overdue, color green
					dueDate.setBackground(lightGreen);
				}
			}
			
			//Assign height
			height = index * 20 + 45;
		}
		//Debug
		//System.out.println("Invoices Successfully Loaded!\n");
		
		//Return height
		return height;
	}
	
	//Refreshes employee lists due to updates, additions, or deletions
	public void RefreshEmployeeListings(Management manage, Composite inner) {
		//Unload the employee lists
		manage.UnloadEmployees(inner);
		
		//Debug
		//System.out.println("Unload employee lists successful!");
		
		//Reload the lists
		manage.InitialLoadEmployees(manage.getFullEmployeeList());
		manage.InitialLoadOnDutyEmployees(manage.getFullEmployeeList());
		
		//Populate the inner composite again
		int onDutyHeight = PopulateOnDutyEmployeesForm(inner, manage.getOnDutyList());
		inner.setSize(662, onDutyHeight);
		
		//Debug
		//System.out.println("Reload employee lists successful!");
		
	}
	
	//Refreshes invoice lists due to updates and additions
	public void RefreshInvoiceListings(Management manage, Composite unpaidInner, Composite paidInner) {
		//Unload the invoice lists
		manage.UnloadInvoices(unpaidInner, paidInner);
		
		//Debug
		//System.out.println("Unload invoice lists successful!");

		//Reload lists
		manage.InitialLoadInvoices(manage.getAllInvoices());
		manage.InitialLoadInvoicePaymentLists(manage.getAllInvoices());
		
		//Populate the unpaid composite again
		int unpaidInvoiceHeight = PopulateInvoicesForm(unpaidInner, manage.getUnpaidInvoices(), true);
		unpaidInner.setSize(338, unpaidInvoiceHeight);
		
		//Populate the paid composite again
		int paidInvoiceHeight = PopulateInvoicesForm(paidInner, manage.getPaidInvoices(), false);
		paidInner.setSize(338, paidInvoiceHeight);
		
		//Debug
		//System.out.println("Reload invoice lists successful!");
	}
	
	//Refreshes order lists due to updates and additions
	public void RefreshOrderListings(Management manage, Composite outgoingInner, Composite receivedInner) {
		//Unload order lists
		manage.UnloadOrders(outgoingInner, receivedInner);
		
		//Debug
		//System.out.println("Unload order lists successful!");
		
		//Reload lists
		manage.InitialLoadOrders(manage.getAllOrders(), manage.getFullInventory());
		manage.InitialLoadOrderStatusLists(manage.getAllOrders());
		
		//Populate the outgoing orders composite again
		int outgoingHeight = PopulateOrdersForm(outgoingInner, manage.getOutgoingOrders());
		outgoingInner.setSize(325, outgoingHeight);
		
		//Populate the received orders composite again
		int receivedHeight = PopulateOrdersForm(receivedInner, manage.getReceivedOrders());
		receivedInner.setSize(325, receivedHeight);
		
		//Debug
		//System.out.println("Reload order lists successful!");
	}
	
	//Refreshes inventory lists due to updates
	public void RefreshProductListings(Management manage, Composite currentInventory) {
		//Unload product lists
		manage.UnloadProducts(currentInventory);
		
		//Debug
		//System.out.println("Unload product lists successful!");
		
		//Reload lists
		manage.InitialLoadInventory(manage.getFullInventory());
		manage.InitialLoadCurrentInventory(manage.getFullInventory());
		manage.InitialLoadSeasonalInventory(manage.getFullInventory());
		manage.InitialLoadClearanceInventory(manage.getFullInventory());
		
		//Populate the outgoing orders composite again
		int currentInventoryHeight = PopulateCurrentInventoryForm(currentInventory, manage.getCurrentInventory());
		currentInventory.setSize(662, currentInventoryHeight);
		
		//Debug
		//System.out.println("Reload product lists successful!");
	}
	
	//Helper method to capitalize the first letter of strings
	public static String capitalizeFirst(String text) {
		//Check text value
	    if (text == null || text.isEmpty()) {
	    	//Return the text if there is none present
	    	return text;
	    }
	    //Return the modified text
	    return text.substring(0, 1).toUpperCase() + text.substring(1);
	}
}
