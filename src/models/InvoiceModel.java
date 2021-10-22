package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class InvoiceModel extends BaseModel {
	private static String table = "invoices";
	private static String[] columns = {"id, code, order_id, payment_status, created_at"};
	
	private int id;
	private int sequence;
	private String invoiceCode;
	private String orderCode;
	private double totalOrder;
	private String paymentMethod;
	private int paymentStatus;
	private String createdAt;

	//status
	public static final int PAID = 1;
	public static final int UNPAID = 0;

	public static DataMapping isPaid = DataMapping.getInstance(PAID, "Paid"); 
	public static DataMapping isUnpaid = DataMapping.getInstance(UNPAID, "Unpaid"); 

	public InvoiceModel() {
		super(table, columns);	
	}
	
	public InvoiceModel(int id, int sequence,  String invoiceCode, String orderCode, double totalOrder,
						String paymentMethod, int paymentStatus, String createdAt) {
		super(table, columns);	
		this.setId(id);
		this.setSequence(sequence);
		this.setInvoiceCode(invoiceCode);
		this.setOrderCode(orderCode);
		this.setTotalOrder(totalOrder);
		this.setPaymentMethod(paymentMethod);
		this.setPaymentStatus(paymentStatus);
		this.setCreatedAt(createdAt);		
	}
	
	//get data - orders - payment_method 
	public ResultSet getInvoiceList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"invoices.*, orders.code, orders.id, orders.total_amount, payment_method.name"};
			//orders
			ArrayList<CompareOperator> orderCondition = new ArrayList<CompareOperator>();
			orderCondition.add(CompareOperator.getInstance("invoices.order_id", "=", "orders.id"));
			//payment_method
			ArrayList<CompareOperator> paymentMethodCondition = new ArrayList<CompareOperator>();
			paymentMethodCondition.add(CompareOperator.getInstance("payment_method.id", "=", "orders.payment_method_id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "orders", orderCondition));
			joins.add(JoinCondition.getInstance("left join", "payment_method", paymentMethodCondition));
			return this.getData(selects, conditions, joins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//create
	public int createInvoice(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateInvoice(int id, ArrayList<DataMapping> data) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("invoices.id", "=", String.valueOf(id)));
			return this.update(data, condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	//delete
	public boolean deleteInvoice(int id) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("invoices.id", "=", String.valueOf(id)));
			return this.delete(condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	//get & set
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public double getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(double totalOrder) {
		this.totalOrder = totalOrder;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	

}
