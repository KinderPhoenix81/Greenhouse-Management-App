package greenhouseManagementSystem;

public class ProductItem {
	//Setting fields of a product
	private String productName;
	private String category;
	private int productID;
	private int quantityInStock;
	private double orderCost;
	private double purchasePrice;
	private int orderAmount;
	private Boolean perishable;
	private Boolean inStock;
	private Boolean clearanceItem;
	private Boolean seasonalItem;
	
	//Constructor with input parameters for most fields
	public ProductItem(String productName, String category, int productID, int quantityInStock, double orderCost, double purchasePrice, 
			Boolean perishable, Boolean inStock, Boolean clearanceItem, Boolean seasonalItem) {
		//Assigning values to fields of a product object
		this.productName = productName;
		this.category = category;
		this.productID = productID;
		this.quantityInStock = quantityInStock;
		this.orderCost = orderCost;
		this.purchasePrice = purchasePrice;
		this.inStock = inStock;
		this.perishable = perishable;
		this.clearanceItem = clearanceItem;
		this.seasonalItem = seasonalItem;
	}
	
	//Constructor for a product inserted to existing orders
	public ProductItem(int id, double orderPrice, int quantity) {
		productID = id;
		orderCost = orderPrice;
		quantityInStock = quantity;
	}
	
	//Getters for each field of a product
	public String getName() {
		return this.productName;
	}
	
	public int getProductID() {
		return this.productID;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public int getStockNum() {
		return this.quantityInStock;
	}
	
	public double getPrice() {
		return this.purchasePrice;
	}
	
	public double getCost() {
		return this.orderCost;
	}
	
	public Boolean getInStock() {
		return this.inStock;
	}
	
	public Boolean getPerishable() {
		return this.perishable;
	}
	
	public Boolean getClearanceStatus() {
		return this.clearanceItem;
	}
	
	public Boolean getSeasonalStatus() {
		return this.seasonalItem;
	}
	
	public int getOrderAmount() {
		return this.orderAmount;
	}
	
	//Setter for the order amount
	public void setOrderAmount(int num) {
		orderAmount = num;
	}
}

