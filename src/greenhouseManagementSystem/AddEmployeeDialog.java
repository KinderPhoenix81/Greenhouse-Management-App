package greenhouseManagementSystem;

//Import statements
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

public class AddEmployeeDialog extends Dialog {

	//Fields for the class
	protected Object result;
	protected Shell shlAddEmployee;
	private ArrayList<Employee> employeeList;
	private Text lastNameText;
	private Text phoneText;
	private Text emailText;
	private Text dateOfBirthText;
	private Text wageText;
	private Text firstNameText;
	private static Boolean employeeAdded;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddEmployeeDialog(Shell parent, int style, ArrayList<Employee> employeeList) {
		super(parent, style);
		setText("SWT Dialog");
		employeeList = this.employeeList;
		employeeAdded = false;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlAddEmployee.open();
		shlAddEmployee.layout();
		Display display = getParent().getDisplay();
		while (!shlAddEmployee.isDisposed()) {
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
		shlAddEmployee = new Shell(getParent(), getStyle());
		shlAddEmployee.setMinimumSize(new Point(550, 450));
		shlAddEmployee.setSize(450, 300);
		shlAddEmployee.setText("Add Employee");
		
		//Cancel button
		Button btnCancel = new Button(shlAddEmployee, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//close shell
				shlAddEmployee.dispose();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(10, 376, 75, 25);
		
		//Label controls
		Label lblAddAnEmployee = new Label(shlAddEmployee, SWT.NONE);
		lblAddAnEmployee.setText("Add an Employee");
		lblAddAnEmployee.setBounds(10, 10, 102, 15);
		
		Label lblLastName = new Label(shlAddEmployee, SWT.NONE);
		lblLastName.setBounds(122, 93, 66, 15);
		lblLastName.setText("Last Name");
		
		Label lblFirstName = new Label(shlAddEmployee, SWT.NONE);
		lblFirstName.setText("First Name");
		lblFirstName.setBounds(122, 66, 66, 15);
		
		Label lblPhoneNumber = new Label(shlAddEmployee, SWT.NONE);
		lblPhoneNumber.setText("Phone Number*");
		lblPhoneNumber.setBounds(122, 120, 102, 15);
		
		Label lblEmail = new Label(shlAddEmployee, SWT.NONE);
		lblEmail.setText("Email");
		lblEmail.setBounds(122, 147, 102, 15);
		
		Label lblDateOfBirth = new Label(shlAddEmployee, SWT.NONE);
		lblDateOfBirth.setText("Date of Birth*");
		lblDateOfBirth.setBounds(122, 174, 102, 15);
		
		Label lblEmploymentStatus = new Label(shlAddEmployee, SWT.NONE);
		lblEmploymentStatus.setText("Employment Status");
		lblEmploymentStatus.setBounds(122, 226, 102, 15);
		
		Label lblWage = new Label(shlAddEmployee, SWT.NONE);
		lblWage.setText("Wage");
		lblWage.setBounds(122, 360, 102, 15);
		
		Label lblPosition = new Label(shlAddEmployee, SWT.NONE);
		lblPosition.setText("Position");
		lblPosition.setBounds(122, 255, 102, 15);
		
		//Textbox controls
		firstNameText = new Text(shlAddEmployee, SWT.BORDER);
		firstNameText.setBounds(274, 63, 102, 21);
		
		lastNameText = new Text(shlAddEmployee, SWT.BORDER);
		lastNameText.setBounds(274, 90, 102, 21);
		
		phoneText = new Text(shlAddEmployee, SWT.BORDER);
		phoneText.setBounds(274, 117, 102, 21);
		
		emailText = new Text(shlAddEmployee, SWT.BORDER);
		emailText.setBounds(274, 144, 150, 21);
		
		dateOfBirthText = new Text(shlAddEmployee, SWT.BORDER);
		dateOfBirthText.setBounds(274, 171, 102, 21);
		
		wageText = new Text(shlAddEmployee, SWT.BORDER);
		wageText.setBounds(274, 357, 102, 21);
		
		//Combo boxes and labels for those boxes
		Label lblOpener = new Label(shlAddEmployee, SWT.NONE);
		lblOpener.setBounds(122, 284, 55, 15);
		lblOpener.setText("Opener?");
		
		Label lblCloser = new Label(shlAddEmployee, SWT.NONE);
		lblCloser.setText("Closer?");
		lblCloser.setBounds(122, 313, 55, 15);
		
		Combo openerCombo = new Combo(shlAddEmployee, SWT.READ_ONLY);
		openerCombo.setBounds(274, 281, 100, 23);
		openerCombo.add("True");
		openerCombo.add("False");
		
		Combo closerCombo = new Combo(shlAddEmployee, SWT.READ_ONLY);
		closerCombo.setBounds(274, 310, 100, 23);
		closerCombo.add("True");
		closerCombo.add("False");
		
		Combo statusCombo = new Combo(shlAddEmployee, SWT.READ_ONLY);
		statusCombo.setBounds(274, 223, 150, 23);
		statusCombo.add("Orientation");
		statusCombo.add("Part-Time");
		statusCombo.add("Full-Time");
		statusCombo.add("Seasonal");
		statusCombo.add("Temporary");
		
		Combo positionCombo = new Combo(shlAddEmployee, SWT.READ_ONLY);
		positionCombo.setBounds(274, 252, 150, 23);
		positionCombo.add("Trainee");
		positionCombo.add("Cashier");
		positionCombo.add("Sales Associate");
		positionCombo.add("Nursery Worker");
		positionCombo.add("Nursery Helper");
		positionCombo.add("Seasonal Helper");
		positionCombo.add("Seasonal Assistant");
		positionCombo.add("Delivery Assistant");
		positionCombo.add("Greenhouse Helper");
		positionCombo.add("Greenhouse Technician");
		positionCombo.add("Horticulturist");
		positionCombo.add("Inventory Clerk");
		positionCombo.add("Inventory Clerk Assitant");
		positionCombo.add("Deliver Driver");
		
		//Creates an employee and adds it to the list
				Button btnSubmit = new Button(shlAddEmployee, SWT.NONE);
				btnSubmit.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						//Boolean to hold value for blank fields present
						Boolean blankFieldPresent = false;
						
						//Verify all fields have data
						if(firstNameText.getText().isBlank() || lastNameText.getText().isBlank() || phoneText.getText().isBlank() 
							|| emailText.getText().isBlank() || dateOfBirthText.getText().isBlank() || wageText.getText().isBlank()
							|| positionCombo.getText().isBlank() || statusCombo.getText().isBlank() || openerCombo.getText().isBlank() || closerCombo.getText().isBlank()) {
							
							//Message the user to review data
							MessageBox blankFieldMessage = new MessageBox(shlAddEmployee);
							blankFieldMessage.setText("Warning");
							blankFieldMessage.setMessage("One or more of your fields are blank, please try again.");
							blankFieldMessage.open();
							
							//Update boolean
							blankFieldPresent = true;
						}
						
						//If the text boxes are not blank, run the try and catch
						if(!blankFieldPresent) {
							
							//Validate data
							try {
								//Retrieve and parse input
								String firstName = firstNameText.getText();
								String lastName = lastNameText.getText();
								Long phone = Long.parseLong(phoneText.getText());
								String email = emailText.getText();
								int dob = Integer.parseInt(dateOfBirthText.getText());
								Double wage = Double.parseDouble(wageText.getText());
								
								//Perform some checks
								if(phoneText.getText().length() != 10) {
									//Cause an error
									throw new NumberFormatException("");
								}
								if(dateOfBirthText.getText().length() != 8) {
									//Cause an error
									throw new NumberFormatException("");
								}
								if(wage <= 0) {
									//Cause an error
									throw new NumberFormatException("");
								}
								
								//Check to see if the date can be parsed-- This code is only here to throw an error if an improper date is added
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								
								@SuppressWarnings("unused")
								LocalDate validDOB = LocalDate.parse(String.valueOf(dob), formatter);
								
								//Connect to the database & add information to the table
								try {
									//Make a connection
									Connection connection = DriverManager.getConnection("jdbc:sqlite:GreenhouseManagement");
									//Make an sql insert statement
									String sql = "Insert Into Employee(FirstName, LastName, Phone, Email, DateOfBirth, EmploymentStatus, OnDuty, Opener, Closer, Wage, Position) "
											+ "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
									
									//Make a prepared statement and insert validated user data
									PreparedStatement inserter = connection.prepareStatement(sql);
									inserter.setString(1, firstName);
									inserter.setString(2, lastName);
									inserter.setString(3, String.valueOf(phone));
									inserter.setString(4, email);
									inserter.setString(5, String.valueOf(dob));
									inserter.setString(6, statusCombo.getText());
									inserter.setString(7, "false");
									inserter.setString(8, openerCombo.getText().toLowerCase());
									inserter.setString(9, closerCombo.getText().toLowerCase());
									inserter.setString(10, String.valueOf(wage));
									inserter.setString(11, positionCombo.getText());
									
									//Execute update
									inserter.executeUpdate();
									
									//Message to show success
									MessageBox success = new MessageBox(shlAddEmployee);
									success.setText("Success");
									success.setMessage("The employee was successfully created!");
									success.open();
									
									//Assign the employee added boolean
									employeeAdded = true;
									
									//Close out of window
									shlAddEmployee.dispose();
									
								} catch (SQLException sqle) {
									//Print out error
									sqle.printStackTrace();
									
									//Catch error
									MessageBox sqlError = new MessageBox(shlAddEmployee);
									sqlError.setText("Error");
									sqlError.setMessage("It seems that we ran into an issue on our end with the database, please try again later.");
									sqlError.open();
								}

							} catch (NumberFormatException nfe) {
								//Print error
								nfe.printStackTrace();
								
								//Catch error
								MessageBox parseError = new MessageBox(shlAddEmployee);
								parseError.setText("Error");
								parseError.setMessage("One of the values entered was not of correct typing or incorrect length, causing an issue, please double check, and re-enter your data.");
								parseError.open();
								
							} catch (DateTimeParseException dtpe) {
								//Print error
								dtpe.printStackTrace();
								
								//Catch error
								MessageBox parseError = new MessageBox(shlAddEmployee);
								parseError.setText("Error");
								parseError.setMessage("Your date value was of incorrect formatting, please review it and re-enter your data.");
								parseError.open();
								
								//Clear field
								dateOfBirthText.setText("");
							}
						}
					}
				});
				
				//Submit button
				btnSubmit.setText("Submit");
				btnSubmit.setBounds(449, 376, 75, 25);
				
				//Labels for input context
				Label lblEnterInA = new Label(shlAddEmployee, SWT.WRAP);
				lblEnterInA.setBounds(382, 105, 142, 30);
				lblEnterInA.setText("*Enter in a 10 digit format, no breaks");
				
				Label lblenterInA = new Label(shlAddEmployee, SWT.WRAP);
				lblenterInA.setText("*Enter in a 8 digit format, no breaks; (yyyymmdd)");
				lblenterInA.setBounds(382, 174, 142, 30);
				
				//Set a list for tab order
				Control[] tabOrder = {firstNameText, lastNameText, phoneText, emailText, dateOfBirthText, statusCombo, positionCombo, openerCombo, closerCombo, wageText, btnSubmit, btnCancel};
				shlAddEmployee.setTabList(tabOrder);
				
	}
	
	//Getter for boolean value
	public static Boolean getAdded() {
		return employeeAdded;
	}
	
}
