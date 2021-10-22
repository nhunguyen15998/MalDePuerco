package models;

public class TablesReserModel extends BaseModel {
	public static String table = "tables_reservation";
	public static String[] columns = {"id", "table_id", "reservation_id"};
	int id, tableID,reservationID;
	
	public TablesReserModel(int table_id, int reservation_id) {
		super(table, columns);
		this.setTableID(table_id);
		this.setReservationID(reservation_id);
	}
	public TablesReserModel() {
		super(table, columns);
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTableID() {
		return tableID;
	}
	public void setTableID(int tableID) {
		this.tableID = tableID;
	}
	public int getReservationID() {
		return reservationID;
	}
	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}
	
}
