package greenhouseManagementSystem;

//Imports
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class PayInvoiceDialog extends Dialog {

	//Fields of the dialog class
	protected Object result;
	protected Shell shlPayInvoice;
	private ArrayList<Invoice> unpaidInvoices;
	private static Boolean paidInvoice;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PayInvoiceDialog(Shell parent, int style, ArrayList<Invoice> unpaidInvoices) {
		super(parent, style);
		setText("SWT Dialog");
		this.unpaidInvoices = unpaidInvoices;
		paidInvoice = false;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlPayInvoice.open();
		shlPayInvoice.layout();
		Display display = getParent().getDisplay();
		while (!shlPayInvoice.isDisposed()) {
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
		shlPayInvoice = new Shell(getParent(), getStyle());
		shlPayInvoice.setMinimumSize(new Point(700, 450));
		shlPayInvoice.setSize(450, 300);
		shlPayInvoice.setText("Pay Invoice");
		
		//Cancel button
		Button btnCancel = new Button(shlPayInvoice, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//Close dialog window
				shlPayInvoice.dispose();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(599, 10, 75, 25);
		
		//Context labels for the dialog
		Label lblPayAnInvoice = new Label(shlPayInvoice, SWT.NONE);
		lblPayAnInvoice.setText("Pay an Invoice");
		lblPayAnInvoice.setBounds(10, 10, 117, 15);
		
		Label lblInvoiceDetails = new Label(shlPayInvoice, SWT.NONE);
		lblInvoiceDetails.setAlignment(SWT.CENTER);
		lblInvoiceDetails.setText("Invoice Details");
		lblInvoiceDetails.setBounds(435, 39, 117, 15);
		
		//Composite to store individual invoice information
		Composite invoiceInfoComposite = new Composite(shlPayInvoice, SWT.NONE);
		invoiceInfoComposite.setBounds(298, 60, 376, 240);
		
		//Outer composite to contain inner composite and content
		ScrolledComposite payInvoice_outerComposite = new ScrolledComposite(shlPayInvoice, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		payInvoice_outerComposite.setExpandHorizontal(true);
		payInvoice_outerComposite.setBounds(10, 31, 282, 370);
		
		//Inner composite to store content
		Composite payInvoice_innerComposite = new Composite(payInvoice_outerComposite, SWT.NONE);
		payInvoice_innerComposite.setLayout(null);
		payInvoice_innerComposite.setBounds(10, 31, 282, 370);
		
		//Setting the content of the outer composite to match the content of the inner composite
		payInvoice_outerComposite.setContent(payInvoice_innerComposite);
		payInvoice_outerComposite.setMinSize(payInvoice_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate inner content
		int unpaidInvoicesInt = PopulateUnpaidInvoices(payInvoice_innerComposite, unpaidInvoices, invoiceInfoComposite);
		payInvoice_innerComposite.setSize(282, unpaidInvoicesInt);
	}
	
			//Populate the unpaid invoices window
			public int PopulateUnpaidInvoices(Composite inner, ArrayList<Invoice> invoiceList, Composite infoPanel) {
				//Create a list of all invoices
				ArrayList<Invoice> invoiceListing = invoiceList;
				
				//Create a height variable 
				int height = 0;
				
				//Create headers
				Label idHeader = new Label(inner, SWT.None);
				idHeader.setText("Invoice ID");
				idHeader.setBounds(3, 5, 80, 15);
				
				Label totalHeader = new Label(inner, SWT.None);
				totalHeader.setText("Invoice Total");
				totalHeader.setBounds(100, 5, 80, 15);
				
				//Loop through list of invoices
				for(int index = 0; index < invoiceListing.size(); index++ ) {
					//Create an invoice
					Invoice invoice = invoiceListing.get(index);
					
					//Create controls
					Label id = new Label(inner, SWT.None);
					id.setText(String.valueOf(invoice.getID()));
					id.setBounds(3, index * 35 + 35, 80, 15);
					
					Label total = new Label(inner, SWT.None);
					total.setText("$" + String.valueOf(invoice.getTotal()));
					total.setBounds(100, index * 35 + 35, 65, 15);
					
					Button pay = new Button(inner, SWT.None);
					pay.setText("Pay");
					pay.setBounds(200, index * 35 + 35, 60, 20);
					
					//Set data to the button to differentiate them
					pay.setData((invoice));

					//Add an event listener for each payment button
					pay.addListener(SWT.Selection, e -> {
						Invoice selectedInvoice = (Invoice) ((Button) e.widget).getData();
						PopulateInfoPanel(shlPayInvoice, infoPanel, selectedInvoice);
					});
					
					//Update height
					height = index * 20 + 35;
				}
				
				//Return height
				return height + 100;
			}
			
			//Method to populate info panel
			private void PopulateInfoPanel(Shell shell, Composite panel, Invoice invoice) {
				//Remove the previous content, allowing another invoice to be selected and populate data
				for(Control control : panel.getChildren()) {
					control.dispose();
				}
				
				//Labels for invoice information
				Label lblId = new Label(panel, SWT.NONE);
				lblId.setText("Invoice ID");
				lblId.setBounds(10, 40, 66, 15);
				
				Label lblTotal = new Label(panel, SWT.NONE);
				lblTotal.setText("Invoice Total");
				lblTotal.setBounds(10, 67, 102, 15);
				
				Label lblDate = new Label(panel, SWT.NONE);
				lblDate.setText("Date");
				lblDate.setBounds(10, 94, 102, 15);
				
				Label lblDueDate = new Label(panel, SWT.NONE);
				lblDueDate.setText("Due Date");
				lblDueDate.setBounds(10, 120, 102, 15);
				
				Label idLabel = new Label(panel, SWT.NONE);
				idLabel.setText(String.valueOf(invoice.getID()));
				idLabel.setBounds(162, 40, 102, 21);
				
				Label totalLabel = new Label(panel, SWT.NONE);
				totalLabel.setText("$" + String.valueOf(invoice.getTotal()));
				totalLabel.setBounds(162, 67, 102, 21);
				
				Label dateLabel = new Label(panel, SWT.NONE);
				dateLabel.setText(String.valueOf(invoice.getDate()));
				dateLabel.setBounds(162, 94, 165, 21);
				
				Label dueDateLabel = new Label(panel, SWT.NONE);
				dueDateLabel.setText(String.valueOf(invoice.getDueDate()));
				dueDateLabel.setBounds(162, 120, 150, 23);
				
				//Pay invoice button
				Button btnPayInvoice = new Button(shell, SWT.NONE);
				btnPayInvoice.setText("Pay Invoice");
				btnPayInvoice.setBounds(574, 376, 100, 25);
				btnPayInvoice.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {	
						//Connect to the database
						try {
							//Make a connection
							Connection connection = DriverManager.getConnection("jdbc:sqlite:GreenhouseManagement");
							//Make an sql update statement
							String sql = "Update Invoice Set Paid = 'true' Where InvoiceID = ?";
							
							//Make a prepared statement
							PreparedStatement inserter = connection.prepareStatement(sql);
							inserter.setString(1, String.valueOf(invoice.getID()));
							
							//Execute update
							inserter.executeUpdate();
							
							//Message to show success
							MessageBox success = new MessageBox(shell);
							success.setText("Success");
							success.setMessage("The invoice was successfully paid!");
							success.open();
							
							//Assign the invoice paid boolean
							paidInvoice = true;
							
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
							}
						});
					}
			
	//Getter for boolean value of employee deleted
	public static Boolean getPaid() {
		return paidInvoice;
	}
}
