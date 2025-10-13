package greenhouseManagementSystem;

import java.time.LocalDate;

public class Employee {
	//Setting fields of the employee
	private String name;
	private String phone;
	private String email;
	private int employeeID;
	private LocalDate dateOfBirth;
	private String employmentStatus;
	private String position;
	private double wage;
	private Boolean onDuty;
	private Boolean opener;
	private Boolean closer;
	
	//Employee constructor with input parameters for all fields, will all be required fields upon initialization
	public Employee(String name, String phone, String email, int employeeID, LocalDate dateOfBirth, String employmentStatus, 
			String position, double wage, Boolean opener, Boolean closer, Boolean onDuty) {
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.employeeID = employeeID;
		this.dateOfBirth = dateOfBirth;
		this.employmentStatus = employmentStatus;
		this.position = position;
		this.wage = wage;
		this.opener = opener;
		this.closer = closer;
		this.onDuty = onDuty;
	}

	//Getters for the employee fields
	public String getName() {
		return name;
	}
	
	public int getID() {
		return employeeID;
	}
	
	public LocalDate getDOB() {
		return dateOfBirth;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public double getWage() {
		return wage;
	}
	
	public String getEmploymentStatus() {
		return employmentStatus;
	}
	
	public String getPosition() {
		return position;
	}
	
	public Boolean getOpen() {
		return opener;
	}
	
	public Boolean getClose() {
		return closer;
	}
	
	public Boolean getOnDuty() {
		return this.onDuty;
	}
}

