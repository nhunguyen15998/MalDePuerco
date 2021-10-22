package models;

public class ChefItemModel {
	private String oderCode, servingName, quantity, userCode, createdAt, size;
	private int id, status;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getOderCode() {
		return oderCode;
	}
	public void setOderCode(String oderCode) {
		this.oderCode = oderCode;
	}
	
	public String getServingName() {
		return servingName;
	}
	public void setServingName(String servingName) {
		this.servingName = servingName;
	}
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
