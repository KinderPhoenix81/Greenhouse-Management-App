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

public class RemoveEmployeeDialog extends Dialog {
	//Fields of this dialog class
	protected Object result;
	protected Shell shlRemoveEmployee;
	private ArrayList<Employee> employeeList;
	private static Boolean employeeDeleted;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RemoveEmployeeDialog(Shell parent, int style, ArrayList<Employee> employeeList) {
		super(parent, style);
		setText("SWT Dialog");
		this.employeeList = employeeList;
		employeeDeleted = false;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlRemoveEmployee.open();
		shlRemoveEmployee.layout();
		Display display = getParent().getDisplay();
		while (!shlRemoveEmployee.isDisposed()) {
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
		shlRemoveEmployee = new Shell(getParent(), getStyle());
		shlRemoveEmployee.setMinimumSize(new Point(700, 450));
		shlRemoveEmployee.setSize(391, 450);
		shlRemoveEmployee.setText("Remove Employee");
		
		//Cancel button
		Button btnCancel = new Button(shlRemoveEmployee, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//close shell
				shlRemoveEmployee.dispose();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(599, 10, 75, 25);
		
		//Context labels for dialog
		Label lblRemoveEmployee = new Label(shlRemoveEmployee, SWT.NONE);
		lblRemoveEmployee.setText("Remove Employee");
		lblRemoveEmployee.setBounds(10, 10, 117, 15);
		
		Label lblEmployeeInformation = new Label(shlRemoveEmployee, SWT.NONE);
		lblEmployeeInformation.setBounds(433, 36, 117, 15);
		lblEmployeeInformation.setText("Employee Information");
		
		Label lblNoteOnceYou = new Label(shlRemoveEmployee, SWT.WRAP);
		lblNoteOnceYou.setText("Note: Once you perform this action (deletion), you cannot undo it, so please take your time to review the details above.");
		lblNoteOnceYou.setBounds(298, 303, 252, 57);
		
		//Composite to store employee info
		Composite deletionInfoComposite = new Composite(shlRemoveEmployee, SWT.NONE);
		deletionInfoComposite.setBounds(298, 57, 376, 240);
		
		//Create outer composite to store inner composite
		ScrolledComposite deleteEmployee_outerComposite = new ScrolledComposite(shlRemoveEmployee, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		deleteEmployee_outerComposite.setExpandHorizontal(true);
		deleteEmployee_outerComposite.setBounds(10, 31, 282, 370);
		
		//Inner composite to store content
		Composite deleteEmployee_innerComposite = new Composite(deleteEmployee_outerComposite, SWT.NONE);
		deleteEmployee_innerComposite.setLayout(null);
		deleteEmployee_innerComposite.setBounds(10, 31, 282, 370);
		
		//Match the contents of the outer composite to the inner composite
		deleteEmployee_outerComposite.setContent(deleteEmployee_innerComposite);
		deleteEmployee_outerComposite.setMinSize(deleteEmployee_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populate the inner composite
		int deleteEmployeeInt = PopulateAllEmployees(deleteEmployee_innerComposite, employeeList, deletionInfoComposite);
		deleteEmployee_innerComposite.setSize(282, deleteEmployeeInt);
	}
	
	//Populate the view all employees window
			public int PopulateAllEmployees(Composite inner, ArrayList<Employee> employeeList, Composite deletionPanel) {
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
					
					Button remove = new Button(inner, SWT.None);
					remove.setText("Remove");
					remove.setBounds(200, index * 20 + 35, 60, 15);
					
					//Set data to the button to differentiate them
					remove.setData((employee));

					//Add an event listener for each delete button
					remove.addListener(SWT.Selection, e -> {
						Employee selectedEmployee = (Employee) ((Button) e.widget).getData();
						PopulateDeletionPanel(shlRemoveEmployee, deletionPanel, selectedEmployee);
					});
					
					//Update height
					height = index * 20 + 35;
				}
				
				//Return height
				return height + 25;
			}
			
			//Method to popualte the edit panel upon clicking edit
			private void PopulateDeletionPanel(Shell shell, Composite panel, Employee employee) {
				//Remove the previous content, allowing another employee to be selected and populate data
				for(Control control : panel.getChildren()) {
					control.dispose();
				}
				
				//Labels providing info for each employee
				Label lblFullName = new Label(panel, SWT.NONE);
				lblFullName.setText("Full Name");
				lblFullName.setBounds(10, 40, 66, 15);
				
				Label lblPhoneNumber_1 = new Label(panel, SWT.NONE);
				lblPhoneNumber_1.setText("Phone Number");
				lblPhoneNumber_1.setBounds(10, 67, 102, 15);
				
				Label lblEmail = new Label(panel, SWT.NONE);
				lblEmail.setText("Email");
				lblEmail.setBounds(10, 94, 102, 15);
				
				Label lblEmploymentStatus = new Label(panel, SWT.NONE);
				lblEmploymentStatus.setText("Employment Status");
				lblEmploymentStatus.setBounds(10, 120, 102, 15);
				
				Label lblPosition = new Label(panel, SWT.NONE);
				lblPosition.setText("Position");
				lblPosition.setBounds(10, 148, 102, 15);
				
				Label fullNameLabel = new Label(panel, SWT.NONE);
				fullNameLabel.setText(employee.getName());
				fullNameLabel.setBounds(162, 40, 102, 21);
				
				Label phoneLabel = new Label(panel, SWT.NONE);
				phoneLabel.setText(employee.getPhone());
				phoneLabel.setBounds(162, 67, 102, 21);
				
				Label emailLabel = new Label(panel, SWT.NONE);
				emailLabel.setText(employee.getEmail());
				emailLabel.setBounds(162, 94, 165, 21);
				
				Label statusLabel = new Label(panel, SWT.NONE);
				statusLabel.setText(employee.getEmploymentStatus());
				statusLabel.setBounds(162, 120, 150, 23);
				
				Label positionLabel = new Label(panel, SWT.NONE);
				positionLabel.setText(employee.getPosition());
				positionLabel.setBounds(162, 148, 150, 23);
				
				//Confirm deletion button
				Button btnSubmitChanges = new Button(shell, SWT.NONE);
				btnSubmitChanges.setText("Delete Employee");
				btnSubmitChanges.setBounds(574, 376, 100, 25);
				btnSubmitChanges.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {	
						//Connect to the database
						try {
							//Make a connection
							Connection connection = DriverManager.getConnection("jdbc:sqlite:GreenhouseManagement");
							//Make an sql delete statement
							String sql = "Delete From Employee Where EmployeeID = ?";
							
							//Make a prepared statement
							PreparedStatement inserter = connection.prepareStatement(sql);
							inserter.setString(1, String.valueOf(employee.getID()));
							
							//Execute update
							inserter.executeUpdate();
							
							//Message to show success
							MessageBox success = new MessageBox(shell);
							success.setText("Success");
							success.setMessage("The employee was successfully deleted!");
							success.open();
							
							//Assign the employee deleted boolean to true
							employeeDeleted = true;
							
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
		public static Boolean getDeleted() {
			return employeeDeleted;
		}
}

