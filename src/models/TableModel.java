package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;

public class TableModel extends BaseModel{
	private static String table = "tables";
	private static String[] columns = {"id, code, name, seats, created_at, status, is_set"};
	private static TableModel tableModel;
	public static String tablet_code;
	private int id;
	private int sequence;
	private String code;
	private String name;
	private int seats;
	private String createdAt;
	private int status;
	private int is_set;
	
	//status

	public static final int TABLE_EMPTY = 0;
	public static final int TABLE_SERVING = 1;
	public static final int TABLE_WAITING = 2; 
	public static final int TABLE_UPCOMING = 3;
	public static DataMapping isEmpty = DataMapping.getInstance(TABLE_EMPTY, "Empty");
	public static DataMapping isServing = DataMapping.getInstance(TABLE_SERVING, "Serving");
	public static DataMapping isWaiting = DataMapping.getInstance(TABLE_WAITING, "Waiting");
	public static DataMapping isUpcoming = DataMapping.getInstance(TABLE_UPCOMING, "Upcoming");
	// is set on tablet
	public static final int YES = 1; 
	public static final int NO = 0;  
	public static DataMapping isSet = DataMapping.getInstance(TABLE_EMPTY, "Yes");
	public static DataMapping isNotSet = DataMapping.getInstance(TABLE_SERVING, "No");
	//constructor
	public TableModel() {
		super(table, columns);
		if(tableModel != null) {
			tableModel = new TableModel();
		}
	}
 
	//get
		public ResultSet getTableList(ArrayList<CompareOperator> conditions) {
			try {	
				return this.getData(columns, conditions, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	public static TableModel getInstance( int id, int sequence, String code, String name, int seats,
			String createdAt, int status, int is_set) {
		if(tableModel ==  null) {
			TableModel item = new TableModel();
			item.id = id;
			item.sequence = sequence;
			item.code = code;
			item.name = name;
			item.seats = seats;
			item.createdAt = createdAt;
			item.status = status;
			item.is_set = is_set;
			return item;
		}
		return tableModel;
		
	}
	//get data by id
		public ResultSet getTableById(int id) {
			try {		
				ArrayList<CompareOperator> joinRole = new ArrayList<CompareOperator>();
				joinRole.add(CompareOperator.getInstance("id", " = ", String.valueOf(id)));
				
				return this.getData(columns, joinRole, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public int createTable(ArrayList<DataMapping> data) {
			try {
				return this.create(data);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		
		//update
		public boolean updateTableById(int id, ArrayList<DataMapping> data) {
			try {
				
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
				return this.update(data, conditions);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		//delete
		public boolean deleteTableById(int id) {
			try {
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
				
				return this.delete(conditions);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public int getSequence() {
			return sequence;
		}

		public void setSequence(int sequence) {
			this.sequence = sequence;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getSeats() {
			return seats;
		}

		public void setSeats(int seats) {
			this.seats = seats;
		}

		public String getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getIs_set() {
			return is_set;
		}

		public void setIs_set(int is_set) {
			this.is_set = is_set;
		}
		
		//get && set
		
		
		
}
