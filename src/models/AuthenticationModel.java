package models;

import java.util.ArrayList;

public class AuthenticationModel {
	public static int id;
	public static String name;
	public static String roleName;
	public static ArrayList<PermissionModel> permissions;
	
	public static boolean hasPermission(String permissionCode) {
		for(PermissionModel permission : permissions) {
			if(permission.getPermissionCode().equals(permissionCode)) {
				return true;
			}
		}
		return false;
	}
	
	
	
}
