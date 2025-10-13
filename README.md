## Application Summary

This was my final project for my Java 1 class, where I needed to create a GUI-rich app that featured dynamic data population and database functionality using SQLite.

To run this program as intended, use:
- Eclipse IDE (Version 2025-06)
- Java 21.

*Note:* This project uses WindowBuilder-SWT to make the necessary windows, tabs, controls, and dialogs appear.

### Application Features
For each tab present in the project, users are given several choices as to what they can do.

*Employee Tab*
- View on duty employees
- Create an employee with specified info
- Modify an employee's attributes
- View all employees with comprehensive data of each attribute
- Remove an employee

*Inventory Tab*
- View current inventory
- View clearance & seasonal inventories
- Modify a product's attributes

*Order Tab*
- View all incoming and received orders
- Mark an order as 'received'
- Place an order
    - Comprehensive GUI setup to display calculations based on products ordered

*Invoice Tab*
- View all paid and unpaid invoices
- Mark an invoice as 'paid'

*General Functionality*
- Color coded blocks for specific fields
    - Perishable products
    - Clearance products (Inventory & Placing Order)
    - Employee openers and closers
    - Invoice Due Dates
- Proper data validation for inserting and updating data
- Dynamically loaded data (with refreshing lists upon completing a CRUD transaction)

*Potential Updates*
- Marking an order as received updates the stock count for the items in the order
- Clearance items have an automatic discount applied while being on clearance

### Application Development Tools
- Eclipse IDE: Sole development environment used for Java backend
- WindowBuilder-SWT: Ease of use for creating forms
- SQLite: Database integration & interaction with Eclipse

### Screenshots
<img width="680" height="439" alt="image" src="https://github.com/user-attachments/assets/bbdbc9ef-2819-4d56-ac80-58ee077d16b0" />
<img width="699" height="448" alt="image" src="https://github.com/user-attachments/assets/a29c890d-254a-44d8-94de-5dc06db5dc3f" />
<img width="696" height="450" alt="image" src="https://github.com/user-attachments/assets/047e12a7-b50c-49eb-bdc6-6efddfd54499" />
<img width="697" height="448" alt="image" src="https://github.com/user-attachments/assets/cc52e51e-ce1b-483c-ad16-c82065a5296f" />

