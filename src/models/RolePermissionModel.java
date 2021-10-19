package models;

import java.util.ArrayList;

import utils.CompareOperator;

public class RolePermissionModel extends BaseModel{
	

	public static String table = "role_permissions";
	public static String[] columns = {"id", "role_id", "permission_id"};
	
	private int id;
	private int roleId;
	private int permissionId;
	
	public RolePermissionModel(int userTypeId, int permissionId) {
		super(table, columns);
		this.setUserTypeId(userTypeId);
		this.setPermissionId(permissionId);
	}

	
	public RolePermissionModel() {
		super(table, columns);
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserTypeId() {
		return roleId;
	}

	public void setUserTypeId(int userTypeId) {
		this.roleId = userTypeId;
	}

	public int getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

}
