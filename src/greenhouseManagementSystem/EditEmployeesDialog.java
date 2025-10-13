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
import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;

public class EditEmployeesDialog extends Dialog {

	//Fields for the class
	protected Object result;
	protected Shell shlEditEmployees;
	private ArrayList<Employee> employeeList;
	private static Boolean employeeUpdated;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EditEmployeesDialog(Shell parent, int style, ArrayList<Employee> employeeList) {
		super(parent, style);
		setText("SWT Dialog");
		this.employeeList = employeeList;
		employeeUpdated = false;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlEditEmployees.open();
		shlEditEmployees.layout();
		Display display = getParent().getDisplay();
		while (!shlEditEmployees.isDisposed()) {
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
		shlEditEmployees = new Shell(getParent(), getStyle());
		shlEditEmployees.setMinimumSize(new Point(700, 450));
		shlEditEmployees.setSize(450, 300);
		shlEditEmployees.setText("Edit Employee(s)");
		
		//Exit button
		Button btnExit = new Button(shlEditEmployees, SWT.NONE);
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//close shell
				shlEditEmployees.dispose();
			}
		});
		btnExit.setText("Cancel");
		btnExit.setBounds(599, 10, 75, 25);
		
		//Window prompt label
		Label lblModifyEmployeeInfo = new Label(shlEditEmployees, SWT.NONE);
		lblModifyEmployeeInfo.setText("Modify Employee Info");
		lblModifyEmployeeInfo.setBounds(10, 10, 117, 15);
		
		//Composite to store indiviudal employee information and fields to edit that info
		Composite editInfoComposite = new Composite(shlEditEmployees, SWT.NONE);
		editInfoComposite.setBounds(298, 60, 376, 314);
		
		//Create an outer composite for full employee list
		ScrolledComposite editEmployee_outerComposite = new ScrolledComposite(shlEditEmployees, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		editEmployee_outerComposite.setBounds(10, 31, 282, 370);
		editEmployee_outerComposite.setExpandHorizontal(true);

		//Create an inner composite to store everything
		Composite editEmployee_innerComposite = new Composite(editEmployee_outerComposite, SWT.NONE);
		editEmployee_innerComposite.setLayout(null);
		editEmployee_innerComposite.setBounds(10, 31, 282, 370);
		
		//Set the content of the outer composite to match the inner
		editEmployee_outerComposite.setContent(editEmployee_innerComposite);
		editEmployee_outerComposite.setMinSize(editEmployee_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate the composite with actual data
		int allEmployeeHeight = PopulateAllEmployees(editEmployee_innerComposite, employeeList, editInfoComposite);
		editEmployee_innerComposite.setSize(282, allEmployeeHeight);
		
		//Clarification label for dialog
		Label lblEmployeeInformation = new Label(shlEditEmployees, SWT.NONE);
		lblEmployeeInformation.setText("Employee Information");
		lblEmployeeInformation.setBounds(435, 39, 117, 15);
	}
	
	//Populate the view all employees window
		public int PopulateAllEmployees(Composite inner, ArrayList<Employee> employeeList, Composite editPanel) {
			//Create a list of all employees
			ArrayList<Employee> employeeListing = employeeList;
			
			//Create a height variable 
			int height = 0;
			
			//Create headers
			Label nameHeader = new Label(inner, SWT.None);
			nameHeader.setText("Name");
			nameHeader.setBounds(3, 5, 120, 15);
			
			Label idHeader = new Label(inner, SWT.None);
			idHeader.setText("ID");
			idHeader.setBounds(125, 5, 20, 15);
			
			//Loop through list of employees
			for(int index = 0; index < employeeListing.size(); index++ ) {
				//Create an employee
				Employee employee = employeeListing.get(index);
				
				//Create controls
				Label name = new Label(inner, SWT.None);
				name.setText(employee.getName());
				name.setBounds(3, index * 20 + 35, 120, 15);
				
				Label id = new Label(inner, SWT.None);
				id.setText(String.valueOf(employee.getID()));
				id.setBounds(125, index * 20 + 35, 20, 15);
				
				Button edit = new Button(inner, SWT.None);
				edit.setText("Edit");
				edit.setBounds(200, index * 20 + 35, 60, 15);
				
				//Set data to the button to differentiate them
				edit.setData((employee));

				//Add an event listener for each edit button
				edit.addListener(SWT.Selection, e -> {
					Employee selectedEmployee = (Employee) ((Button) e.widget).getData();
					PopulateEditingPanel(shlEditEmployees, editPanel, selectedEmployee);
				});
				
				//Update height
				height = index * 20 + 35;
			}
			
			//Return height
			return height + 25;
		}
		
		//Method to popualte the edit panel upon clicking edit
		private void PopulateEditingPanel(Shell shell, Composite panel, Employee employee) {
			//Remove the previous content, allowing another employee to be selected and populate data
			for(Control control : panel.getChildren()) {
				control.dispose();
			}
			
			//Labels for data pieces of an employee
			Label lblFullName = new Label(panel, SWT.NONE);
			lblFullName.setText("Full Name");
			lblFullName.setBounds(10, 40, 66, 15);
			
			Label lblPhoneNumber_1 = new Label(panel, SWT.NONE);
			lblPhoneNumber_1.setText("Phone Number*");
			lblPhoneNumber_1.setBounds(10, 67, 102, 15);
			
			Label lblEmail = new Label(panel, SWT.NONE);
			lblEmail.setText("Email");
			lblEmail.setBounds(10, 94, 102, 15);
			
			Label lblEmploymentStatus = new Label(panel, SWT.NONE);
			lblEmploymentStatus.setText("Employment Status");
			lblEmploymentStatus.setBounds(10, 173, 102, 15);
			
			Label lblWage = new Label(panel, SWT.NONE);
			lblWage.setText("Wage");
			lblWage.setBounds(10, 121, 102, 15);
			
			Label lblPosition = new Label(panel, SWT.NONE);
			lblPosition.setText("Position");
			lblPosition.setBounds(10, 202, 102, 15);
			
			//Textboxes for the user to interact with
			Text fullNameText = new Text(panel, SWT.BORDER);
			fullNameText.setText(employee.getName());
			fullNameText.setBounds(162, 37, 102, 21);
			
			Text phoneText = new Text(panel, SWT.BORDER);
			phoneText.setText(employee.getPhone());
			phoneText.setBounds(162, 64, 102, 21);
			
			Text emailText = new Text(panel, SWT.BORDER);
			emailText.setText(employee.getEmail());
			emailText.setBounds(162, 91, 165, 21);
			
			Text wageText = new Text(panel, SWT.BORDER);
			wageText.setText(String.valueOf(employee.getWage()));
			wageText.setBounds(162, 118, 102, 21);
			
			//Combo boxes and labels for the combo boxes for specific employee values
			Label lblOpener = new Label(panel, SWT.NONE);
			lblOpener.setText("Opener?");
			lblOpener.setBounds(10, 231, 55, 15);
			
			Label lblCloser = new Label(panel, SWT.NONE);
			lblCloser.setText("Closer?");
			lblCloser.setBounds(10, 260, 55, 15);
			
			//Combo boxes & populating them with previous info
			Combo openerCombo = new Combo(panel, SWT.READ_ONLY);
			openerCombo.setBounds(162, 228, 100, 23);
			String[] trueFalse = {"true", "false"};
			openerCombo.setItems(trueFalse);
			openerCombo.select(getIndex(trueFalse, employee.getOpen().toString()));
			
			Combo closerCombo = new Combo(panel, SWT.READ_ONLY);
			closerCombo.setBounds(162, 257, 100, 23);
			closerCombo.setItems(trueFalse);
			closerCombo.select(getIndex(trueFalse, employee.getClose().toString()));
			
			Combo statusCombo = new Combo(panel, SWT.READ_ONLY);
			statusCombo.setBounds(162, 170, 150, 23);
			String[] status = {"Orientation", "Part-Time", "Full-Time", "Seasonal", "Temporary" };
			statusCombo.setItems(status);
			statusCombo.select(getIndex(status, employee.getEmploymentStatus()));
			
			Combo positionCombo = new Combo(panel, SWT.READ_ONLY);
			positionCombo.setBounds(162, 199, 150, 23);
			String[] positions = {"Trainee", "Cashier", "Sales Associate", "Nursery Worker", "Nursery Helper", 
					"Seasonal Helper", "Seasonal Assistant", "Delivery Assistant", "Greenhouse Helper", "Greenhouse Technician", 
					"Horticulturist", "Inventory Clerk", "Inventory Clerk Assitant", "Deliver Driver", "Assistant Manager", "Manager"};
			positionCombo.setItems(positions);
			positionCombo.select(getIndex(positions, employee.getPosition()));
			
			//Dialog clarification for user input
			Label lblEnterInA = new Label(panel, SWT.WRAP);
			lblEnterInA.setText("*Enter in a 10 digit format, no breaks");
			lblEnterInA.setBounds(270, 53, 96, 33);
			
			//Submit changes button
			Button btnSubmitChanges = new Button(shlEditEmployees, SWT.NONE);
			btnSubmitChanges.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {	
					//Boolean to hold value for blank fields present
					Boolean blankFieldPresent = false;
					
					//Verify all fields have data
					if(fullNameText.getText().isBlank() || phoneText.getText().isBlank() 
						|| emailText.getText().isBlank() || wageText.getText().isBlank()
						|| positionCombo.getText().isBlank() || statusCombo.getText().isBlank() || openerCombo.getText().isBlank() || closerCombo.getText().isBlank()) {
						
						//Message the user to review data
						MessageBox blankFieldMessage = new MessageBox(shlEditEmployees);
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
								String fullName = fullNameText.getText();
								Long phone = Long.parseLong(phoneText.getText());
								String email = emailText.getText();
								Double wage = Double.parseDouble(wageText.getText());
								
								//Split the name into parts for first and last; use the split method and a max of two splits, this prevents out of bounds exception for strings
								String[] nameParts = fullName.trim().split("\\s+", 2);
								String firstName = nameParts[0];
								String lastName = (nameParts.length > 1) ? nameParts[1] : "";

								
								//Perform some checks
								if(phoneText.getText().length() != 10) {
									//Cause an error
									throw new NumberFormatException("");
								}
								if(wage <= 0) {
									//Cause an error
									throw new NumberFormatException("");
								}
								
								//Connect to the database
								try {
									//Make a connection
									Connection connection = DriverManager.getConnection("jdbc:sqlite:GreenhouseManagement");
									//Make an sql update statement
									String sql = "Update Employee "
											+ "Set FirstName = ?, LastName = ?, Phone = ?, Email = ?, DateOfBirth = ?, "
											+ "EmploymentStatus = ?, OnDuty = ?, Opener = ?, Closer = ?, Wage = ?, Position = ? "
											+ "Where EmployeeID = ?";
									
									//Make a prepared statement
									PreparedStatement inserter = connection.prepareStatement(sql);
									inserter.setString(1, firstName);
									inserter.setString(2, lastName);
									inserter.setString(3, String.valueOf(phone));
									inserter.setString(4, email);
									inserter.setString(5, String.valueOf(employee.getDOB()).replaceAll("-", ""));
									inserter.setString(6, statusCombo.getText());
									inserter.setString(7, employee.getOnDuty().toString());
									inserter.setString(8, openerCombo.getText().toLowerCase());
									inserter.setString(9, closerCombo.getText().toLowerCase());
									inserter.setString(10, String.valueOf(wage));
									inserter.setString(11, positionCombo.getText());
									inserter.setString(12, String.valueOf(employee.getID()));
									
									//Execute update
									inserter.executeUpdate();
									
									//Message to show success
									MessageBox success = new MessageBox(shell);
									success.setText("Success");
									success.setMessage("The employee was successfully updated!");
									success.open();
									
									//Assign the employee updated boolean to true
									employeeUpdated = true;
									
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
			
			//More submit button properties
			btnSubmitChanges.setText("Submit Changes");
			btnSubmitChanges.setBounds(574, 376, 100, 25);
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
		
		//Getter for the employeeUpdated boolean
		public static Boolean getUpdated() {
			return employeeUpdated;
		}
}
