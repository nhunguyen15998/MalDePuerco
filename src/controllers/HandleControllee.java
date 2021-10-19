package controllers;

import java.sql.PreparedStatement;

import db.MySQLJDBC;
import models.OrderDetailModel;

public class HandleControllee {
	public boolean update(OrderDetailModel oModel) {
		try {
			String update = "update oder_details set user_id = ? where id = ?";
			PreparedStatement stm = MySQLJDBC.connection.prepareStatement(update);
			
			stm.setString(1, oModel.getUserCode());
			stm.setString(2, Integer.toString(oModel.getId()));
			
			int rs = stm.executeUpdate(update);
			return (rs>0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
