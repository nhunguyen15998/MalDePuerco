package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class PermissionModel extends BaseModel{


	private static String table = "permissions";
	private static String[] columns = {"id", "code", "name", "created_at", "status"};
	
	private int id;
	private String code;
	private String name;
	private int status;
	
	//status
	public static final int PERMISSION_ACTIVATED = 1;
	public static final int PERMISSION_DEACTIVATED = 0;
	public static DataMapping isActivated = DataMapping.getInstance(PERMISSION_ACTIVATED, "Activated");
	public static DataMapping isDeactivated = DataMapping.getInstance(PERMISSION_DEACTIVATED, "Deactivated");
	
	public static boolean isShown = false;
	
	public PermissionModel() {
		super(table, columns);
	}
	
	public PermissionModel(String permissionCode, String name) {
		super(table,columns);
		this.setPermissionCode(permissionCode);
		this.setName(name);
	}
	
	//get list
	public ResultSet getPermissionList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"permissions.id as permission_id", "roles.code", "permissions.name as permission_name"};
						
			//per vs user type per
			ArrayList<CompareOperator> pUserTypePermissionCondition = new ArrayList<CompareOperator>();
			pUserTypePermissionCondition.add(CompareOperator.getInstance("role_permissions.permission_id", "=", "permissions.id"));

			//user type per vs user type
			ArrayList<CompareOperator> userTypePermissionUserTypeCondition = new ArrayList<CompareOperator>();
			userTypePermissionUserTypeCondition.add(CompareOperator.getInstance("roles.id", "=", "role_permissions.role_id "));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "role_permissions", pUserTypePermissionCondition));
			joins.add(JoinCondition.getInstance("join", "roles", userTypePermissionUserTypeCondition));
						
			return this.getData(selects, conditions, joins);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//get data by id
	public ResultSet getByUserId(int id) {
		try {
			String[] selects = {"permissions.id as permission_id", "permissions.code", "permissions.name as permission_name"};
			
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("users.id", "=", String.valueOf(id)));
			
			//per vs user type per
			ArrayList<CompareOperator> pUserTypePermissionCondition = new ArrayList<CompareOperator>();
			pUserTypePermissionCondition.add(CompareOperator.getInstance("role_permissions.permission_id", "=", "permissions.id"));

			//user type per vs user type
			ArrayList<CompareOperator> userTypePermissionUserTypeCondition = new ArrayList<CompareOperator>();
			userTypePermissionUserTypeCondition.add(CompareOperator.getInstance("roles.id", "=", "role_permissions.role_id "));
						
			//user type vs user
			ArrayList<CompareOperator> userTypeUserCondition = new ArrayList<CompareOperator>();
			userTypeUserCondition.add(CompareOperator.getInstance("users.role_id", "=", "roles.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add( JoinCondition.getInstance("join", "role_permissions", pUserTypePermissionCondition));
			joins.add(JoinCondition.getInstance("join", "roles", userTypePermissionUserTypeCondition));
			joins.add(JoinCondition.getInstance("join", "users", userTypeUserCondition));
			
			
			return this.getData(selects, conditions, joins);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	//get & set
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPermissionCode() {
		return code;
	}

	public void setPermissionCode(String permissionCode) {
		this.code = permissionCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
