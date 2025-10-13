package greenhouseManagementSystem;

import java.util.ArrayList;

//Imports
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

public class ViewEmployeesDialog extends Dialog {
	//Fields for this dialog class
	protected Object result;
	protected Shell shlViewEmployees;
	private ArrayList<Employee> employees;


	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ViewEmployeesDialog(Shell parent, int style, ArrayList<Employee> employees) {
		super(parent, style);
		setText("SWT Dialog");
		this.employees = employees;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(Color lightRed, Color lightGreen) {
		createContents(lightRed, lightGreen);
		shlViewEmployees.open();
		shlViewEmployees.layout();
		Display display = getParent().getDisplay();
		
		while (!shlViewEmployees.isDisposed()) {
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
		shlViewEmployees = new Shell(getParent(), SWT.SHELL_TRIM);
		shlViewEmployees.setMinimumSize(new Point(750, 450));
		shlViewEmployees.setSize(990, 450);
		shlViewEmployees.setText("View Employees");
		
		//Context label for dialog
		Label lblViewingCurrentEmployees = new Label(shlViewEmployees, SWT.NONE);
		lblViewingCurrentEmployees.setBounds(10, 10, 145, 15);
		lblViewingCurrentEmployees.setText("Viewing Current Employees");
		
		//Exit button
		Button btnExit = new Button(shlViewEmployees, SWT.NONE);
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlViewEmployees.dispose();
			}
		});

		btnExit.setBounds(895, 10, 75, 25);
		btnExit.setText("Exit");
		
		//Outer composite to hold inner composite
		ScrolledComposite allEmployee_outerComposite = new ScrolledComposite(shlViewEmployees, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		allEmployee_outerComposite.setBounds(10, 79, 960, 322);
		allEmployee_outerComposite.setExpandHorizontal(true);

		//Inner composite for info
		Composite allEmployee_innerComposite = new Composite(allEmployee_outerComposite, SWT.NONE);
		allEmployee_innerComposite.setLayout(null);
		allEmployee_innerComposite.setBounds(10, 79, 848, 322);
		
		//Matching the content of the inner and outer composites
		allEmployee_outerComposite.setContent(allEmployee_innerComposite);
		allEmployee_outerComposite.setMinSize(allEmployee_innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//Populating the inner composite with employees
		int allEmployeeHeight = PopulateAllEmployees(allEmployee_innerComposite, employees, lightRed, lightGreen);
		allEmployee_innerComposite.setSize(848, allEmployeeHeight);
		
		//Headers for each employee field
		Label lblName = new Label(shlViewEmployees, SWT.NONE);
		lblName.setBounds(10, 58, 55, 15);
		lblName.setText("Name");
		
		Label lblId = new Label(shlViewEmployees, SWT.NONE);
		lblId.setBounds(110, 58, 23, 15);
		lblId.setText("ID");
		
		Label lblPhone = new Label(shlViewEmployees, SWT.NONE);
		lblPhone.setText("Phone");
		lblPhone.setBounds(137, 58, 55, 15);
		
		Label lblEmail = new Label(shlViewEmployees, SWT.NONE);
		lblEmail.setBounds(206, 58, 55, 15);
		lblEmail.setText("Email");
		
		Label lblDateOfBirth = new Label(shlViewEmployees, SWT.NONE);
		lblDateOfBirth.setBounds(415, 58, 75, 15);
		lblDateOfBirth.setText("Date of Birth");
		
		Label lblPosition = new Label(shlViewEmployees, SWT.NONE);
		lblPosition.setText("Position");
		lblPosition.setBounds(496, 58, 75, 15);
		
		Label lblEmploymentStatus = new Label(shlViewEmployees, SWT.NONE);
		lblEmploymentStatus.setText("Employment Status");
		lblEmploymentStatus.setBounds(634, 58, 103, 15);
		
		Label lblWage = new Label(shlViewEmployees, SWT.NONE);
		lblWage.setText("Wage");
		lblWage.setBounds(751, 58, 43, 15);
		
		Label lblOpener = new Label(shlViewEmployees, SWT.NONE);
		lblOpener.setText("Opener?");
		lblOpener.setBounds(803, 58, 43, 15);
		
		Label lblCloser = new Label(shlViewEmployees, SWT.NONE);
		lblCloser.setText("Closer?");
		lblCloser.setBounds(859, 58, 43, 15);
		
		Label lblOnDuty = new Label(shlViewEmployees, SWT.NONE);
		lblOnDuty.setText("On Duty?");
		lblOnDuty.setBounds(909, 58, 55, 15);

	}
	
	//Populate the view all employees window
	public int PopulateAllEmployees(Composite inner, ArrayList<Employee> employeeList, Color lightRed, Color lightGreen) {
		//Create a list of all employees
		ArrayList<Employee> employeeListing = employeeList;
		
		//Create a height variable 
		int height = 0;
		
		//Loop through list of employees
		for(int index = 0; index < employeeListing.size(); index++ ) {
			//Create an employee
			Employee employee = employeeListing.get(index);
			
			//Create controls
			Label name = new Label(inner, SWT.None);
			name.setText(employee.getName());
			name.setBounds(3, index * 20 + 5, 95, 15);
			
			Label id = new Label(inner, SWT.None);
			id.setText(String.valueOf(employee.getID()));
			id.setBounds(100, index * 20 + 5, 20, 15);
			
			Label phone = new Label(inner, SWT.None);
			phone.setText(String.valueOf(employee.getPhone()));
			phone.setBounds(120, index * 20 + 5, 60, 15);
			
			Label email = new Label(inner, SWT.None);
			email.setText(employee.getEmail());
			email.setBounds(195, index * 20 + 5, 200, 15);
			
			Label dob = new Label(inner, SWT.None);
			dob.setText(String.valueOf(employee.getDOB()));
			dob.setBounds(403, index * 20 + 5, 60, 15);
			
			Label position = new Label(inner, SWT.None);
			position.setText(employee.getPosition());
			position.setBounds(482, index * 20 + 5, 140, 15);
			
			Label employmentStatus = new Label(inner, SWT.None);
			employmentStatus.setText(employee.getEmploymentStatus());
			employmentStatus.setBounds(628, index * 20 + 5, 100, 15);
			
			Label wage = new Label(inner, SWT.None);
			wage.setText("$" + String.valueOf(employee.getWage()));
			wage.setBounds(740, index * 20 + 5, 45, 15);
			
			Label open = new Label(inner, SWT.None);
			open.setAlignment(SWT.CENTER);
			open.setText(TabbedGrouping.capitalizeFirst(employee.getOpen().toString()));
			open.setBounds(789, index * 20 + 5, 48, 20);
			
			Label close = new Label(inner, SWT.None);
			close.setAlignment(SWT.CENTER);
			close.setText(TabbedGrouping.capitalizeFirst(employee.getClose().toString()));
			close.setBounds(841, index * 20 + 5, 48, 20);
			
			Label onDuty = new Label(inner, SWT.None);
			onDuty.setAlignment(SWT.CENTER);
			onDuty.setText(TabbedGrouping.capitalizeFirst(employee.getOnDuty().toString()));
			onDuty.setBounds(894, index * 20 + 5, 48, 20);

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
			
			//Add color to onDuty labels
			if(employee.getOnDuty()) {
				onDuty.setBackground(lightGreen);
			} else {
				onDuty.setBackground(lightRed);
			}
			
			//Update height
			height = index * 20 + 5;
		}
		
		//Return height
		return height + 25;
	}
}
