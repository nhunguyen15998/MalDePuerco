package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import javafx.application.Platform;

import controllers.NotificationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HandleNotifications {
	private static String clientId = Helpers.randomString(20);
	private final static int serverPort = 3457;
	private final static String ip = "3.15.8.122";
	
	private static DataInputStream dis;
	private static DataOutputStream dos;
	
	public static HandleNotifications inst = null;
	
	public static String currentRole = "CUSTOMER";
	public static String currentTable  = "1";
	public static String currentUser = "0123";
	
	//customer
	public static final String CUSTOMER_ORDER_CONFIRMED = "CUSTOMER_ORDER_CONFIRMED";
	//Cooking - ready - serving - served - canceled
	public static final String CUSTOMER_DISH_COOKING = "CUSTOMER_DISH_COOKING";
	public static final String CUSTOMER_DISH_READY = "CUSTOMER_DISH_READY";
	public static final String CUSTOMER_DISH_SERVING = "CUSTOMER_DISH_SERVING";
	public static final String CUSTOMER_DISH_SERVED = "CUSTOMER_DISH_SERVED";
	public static final String CUSTOMER_DISH_CANCELED = "CUSTOMER_DISH_CANCELED";
	public static final String CUSTOMER_CASH_PAYMENT_SUCESS = "CUSTOMER_CASH_PAYMENT_SUCESS";

	//server
	public static final String SERVER_NEW_ORDER = "SERVER_NEW_ORDER";
	//Cooking - ready - canceled
	public static final String SERVER_DISH_COOKING = "SERVER_DISH_COOKING";
	public static final String SERVER_DISH_READY = "SERVER_DISH_READY";
	public static final String SERVER_DISH_CANCELED = "SERVER_DISH_CANCELED";
	
	public static final String SERVER_CASH_PAYMENT_REQUEST = "SERVER_CASH_PAYMENT_REQUEST";
	public static final String SERVER_ONLINE_PAYMENT_SUCCESS = "SERVER_CASH_PAYMENT_SUCCESS";
	
	public static final String SERVER_HELP_REQUEST = "SERVER_HELP_REQUEST";

	//chef
	public static final String CHEF_NEW_DISH = "CHEF_NEW_DISH";
	//serving - served
	public static final String CHEF_DISH_SERVING = "SERVER_DISH_SERVING";
	public static final String CHEF_DISH_SERVED = "CHEF_DISH_SERVED";

	
	public static HandleNotifications getInstance() {
		if(HandleNotifications.inst == null) {
			HandleNotifications.inst = new HandleNotifications();
		}
		return HandleNotifications.inst;
	}
	
	public HandleNotifications(){
		Socket sc;
		try {
			sc = new Socket(ip, serverPort);
			dis = new DataInputStream(sc.getInputStream());
			dos = new DataOutputStream(sc.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sendMessage(String message) {
		try {
			dos.writeUTF(message);
		} catch (IOException e) {
			e.getStackTrace();
		}
	}
	
	public void handleReceivedMessage() throws UnknownHostException, IOException{
		
		Thread readMessage = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						String msg = dis.readUTF();
						System.out.println(msg);
						// process message
						// role#type#table#message#logged
						String[] msgContents = msg.split("#");
						if(msgContents.length == 5 ) {
							String role = msgContents[0];
							String type = msgContents[1];
							String table = msgContents[2];
							String message = msgContents[3];
							String logged = msgContents[4];
							System.out.println(role+" "+type+" "+table+" "+message+" "+logged);
							FXMLLoader root = new FXMLLoader(getClass().getResource("/views/notification-alert.fxml"));
							// load data customer
							if(currentRole.equals("CUSTOMER") && role.equals("CUSTOMER")){
								if(table.equals(currentTable)) {
									switch(type) {
										case HandleNotifications.CUSTOMER_ORDER_CONFIRMED:
											handleMessage(root, role, type, table, message, logged);
											break;
										case HandleNotifications.CUSTOMER_DISH_COOKING:
										case HandleNotifications.CUSTOMER_DISH_READY:
										case HandleNotifications.CUSTOMER_DISH_SERVING:
										case HandleNotifications.CUSTOMER_DISH_SERVED:
										case HandleNotifications.CUSTOMER_DISH_CANCELED:
											//load list order
											//handleMessage(root, role, type, table, message, logged);
											break;
										case HandleNotifications.CUSTOMER_CASH_PAYMENT_SUCESS:
											handleMessage(root, role, type, table, message, logged);
											// load lai man create order
											break;
									}
								}
							}
		
							// load data server
							if(currentRole.equals("SERVER") && role.equals("SERVER")){
								if(currentUser.equals(logged)) {
									
								}
							}
							
							// load data chef
							if(currentRole.equals("CHEF") && role.equals("CHEF")){
								if(currentUser.equals(logged)) {
									
								}
							}
						}
						
						
					} catch (IOException ie) {
						System.out.println("qwerty");
						// TODO: handle exception
					}
				}
			}
		});
		
		readMessage.start();
		
	}
	
	public static void handleMessage(FXMLLoader root, String role, String type, String table, String message, String user) {
		try {
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					AnchorPane apNotiAlert;
					Rectangle2D screenBounds = Screen.getPrimary().getBounds();
					try {
						apNotiAlert = root.load();

						//controller
						NotificationController controller = root.getController();
						controller.processMessages(role, type, table, message, user);
						
						Scene scene = new Scene(apNotiAlert, 322, 334);
						Stage stage = new Stage();
						stage.initStyle(StageStyle.TRANSPARENT);
						scene.setFill(Color.TRANSPARENT);
						stage.setX((screenBounds.getWidth() - 322)/2);
						stage.setY((screenBounds.getHeight() - 334)/2);
						stage.initStyle(StageStyle.UNDECORATED);
						stage.setScene(scene);
						stage.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
