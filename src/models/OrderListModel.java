package models;

public class OrderListModel {
	private int servingId;
	private String thumbnail;
	private String servingName;
	private double itemPrice;
	private double totalPrice;
	private String note;
	private int quantity;
	private String size = "";
	private String sugar = "";
	private String ice = "";
	
	public OrderListModel(int servingId, String thumbnail, String servingName, double itemPrice, double totalPrice,
							String note, int quantity, String size, String sugar, String ice) {
		this.setServingId(servingId);
		this.setThumbnail(thumbnail);
		this.setServingName(servingName);
		this.setItemPrice(itemPrice);
		this.setTotalPrice(totalPrice);
		this.setNote(note);
		this.setQuantity(quantity);
		this.setSize(size);
		this.setSugar(sugar);
		this.setIce(ice);
	}
	
	
	
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getServingName() {
		return servingName;
	}

	public void setServingName(String servingName) {
		this.servingName = servingName;
	}

	public double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getServingId() {
		return servingId;
	}

	public void setServingId(int servingId) {
		this.servingId = servingId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSugar() {
		return sugar;
	}

	public void setSugar(String sugar) {
		this.sugar = sugar;
	}

	public String getIce() {
		return ice;
	}

	public void setIce(String ice) {
		this.ice = ice;
	}
	
	
}
