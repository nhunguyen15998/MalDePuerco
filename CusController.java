/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myfx.demofx1.controller;

import com.myfx.demofx1.dao.DBConnection;
import com.myfx.demofx1.model.Customer;
import com.myfx.demofx1.model.Validations;
import com.myfx.demofx1.model.getDataModify;
import com.myfx.demofx1.resources.StringName;
import com.myfx.demofx1.util.MessageDialogHelper;
import com.myfx.demofx1.util.sharedMethod;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class CusController implements Initializable {
  ObservableList<String> listGender =  FXCollections.observableArrayList("Male","Female","Other");
  ObservableList<String> listFind =  FXCollections.observableArrayList("Name","Customer Code","Identity Card", "Email", "Phone","Firm name");
  ObservableList<String> listFirm =  FXCollections.observableArrayList("No Firm");

    @FXML private AnchorPane userMgrPane;
    @FXML private TableView<Customer> tblCusMgr;
    @FXML private TableColumn<Customer, Integer> ID;
    @FXML private TableColumn<Customer, Integer> no;
    @FXML private TableColumn<Customer, String> cus_code;
    @FXML private TableColumn<Customer, String> name;
    @FXML private TableColumn<Customer, String> gender;
    @FXML private TableColumn<Customer, String> birth;
    @FXML private TableColumn<Customer, String> identity_card;
    @FXML private TableColumn<Customer, String> email;
    @FXML private TableColumn<Customer, String> phone;
    @FXML private TableColumn<Customer, String> address;
    @FXML  private TableColumn<Customer, String> firmName;
    @FXML private TableColumn<Customer, String> status;
    @FXML private ImageView btnBack;
    @FXML private TextField tfName;
    @FXML private TextField tfIdentityCard;
    @FXML private ComboBox<String> cbGender;
    @FXML private DatePicker dkBirth;
    @FXML private ComboBox<String> cbFirm;
    @FXML private TextField tfEmail;
    @FXML private TextField tfPhone;
    @FXML private Button btnAdd;
    @FXML private Button btnReset;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private ImageView logout;
    @FXML private CheckBox chkActive;
    @FXML private Label lblNameValid;
    @FXML private Label lblGenderValid;
    @FXML private Label lblBirthValid;
    @FXML private Label lblICValid;
    @FXML private Label lblEmailValid;
    @FXML private Label lblPhoneValid;
    @FXML private Label lblFirmEmpty;
    @FXML private Label lblNameEmpty;
    @FXML private Label lblEmailEmpty;
    @FXML private Label lblPhoneEmpty;
    @FXML  private Label lblICEmpty;
    @FXML private Button btnRefreshSearch;
    @FXML private ComboBox<String> cbFind;
    @FXML private TextField tfFind;
    @FXML private ImageView btnFind;
    @FXML private TextField tfAddress;
    @FXML private Label lblAddressEmpty;
    public static int back = 0;
  ObservableList<Customer> listCus;
    @FXML
    private Label lblICDup;
    @FXML
    private Label lblEmailDup;
    @FXML
    private Label lblPhoneDup;
    @FXML
    private Label lblBirthCheck;
    @FXML
    private Label lblPhoneLength;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         ID.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
        no.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("no"));
        cus_code.setCellValueFactory(new PropertyValueFactory<Customer,String>("customer_code"));
        name.setCellValueFactory(new PropertyValueFactory<Customer,String>("name"));
        gender.setCellValueFactory(new PropertyValueFactory<Customer,String>("gender"));
        birth.setCellValueFactory(new PropertyValueFactory<Customer,String>("birth"));
        identity_card.setCellValueFactory(new PropertyValueFactory<Customer,String>("identity_card"));
        email.setCellValueFactory(new PropertyValueFactory<Customer,String>("email"));
        phone.setCellValueFactory(new PropertyValueFactory<Customer,String>("phone"));
        firmName.setCellValueFactory(new PropertyValueFactory<Customer,String>("firmName"));
        address.setCellValueFactory(new PropertyValueFactory<Customer,String>("address"));
        status.setCellValueFactory(new PropertyValueFactory<Customer,String>("status"));
        cbGender.setItems(listGender);
        cbFind.setItems(listFind);
        loadCbFirmName();
        cbFirm.setItems(listFirm);
        showTblCus();
        dkBirth.setValue(LocalDate.of(2006, 01, 01));
         btnUpdate.setDisable(true);
         btnDelete.setDisable(true);
        getRowSelected();
        refreshSearch();
    }    
 private void userPermission(){
        
        if(LoginController.userType.equals("Staff")){
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
           
            
        }
    }    
    private void loadCbFirmName() {
          Connection conn = DBConnection.Instance().getConn();
       try{
           String sql = "select name,status from firms where status = 1";
           PreparedStatement pstmt = conn.prepareStatement(sql);
           ResultSet rs = pstmt.executeQuery();
           while(rs.next()){
               listFirm.add(rs.getString("name"));
           }
           pstmt.close();
           rs.close();
       }catch(SQLException ex){
           
       }
    }
    @FXML
    private void getRowSelected() {
        resetInput();
      btnAdd.setDisable(true);
      btnUpdate.setDisable(false);
      btnDelete.setDisable(false);
    
       int index= tblCusMgr.getSelectionModel().getSelectedIndex();
       if(index <= -1){
           return;
       }
       
        Customer c = new Customer();
        c = listCus.get(index);
        int status =0;
        if(chkActive.isSelected()){
             status = 1;
         }
               tfName.setText(c.getName());
               tfIdentityCard.setText(c.getIdentity_card());
               tfEmail.setText(c.getEmail());
               tfPhone.setText(c.getPhone());
               cbGender.setValue(c.getGender());
               tfAddress.setText(c.getAddress());
               chkActive.setSelected(false);
               if(c.getStatus().equals("actived")){
                 chkActive.setSelected(true);
                }
       Connection conn = DBConnection.Instance().getConn();
       try{
           String sql = "select c.*, (select name from firms where c.firm_id=firms.id) as firmName, DAY(birth) as dayB,"
                   + "MONTH(birth) as monthB, YEAR(birth) as yearB from customers c where c.id = ?";
           PreparedStatement ps = conn.prepareStatement(sql);
           ps.setInt(1,c.getId());
           ResultSet rs = ps.executeQuery();
           if(rs.next()){
                dkBirth.setValue(LocalDate.of(rs.getInt("yearB"), rs.getInt("monthB"), rs.getInt("dayB")));
                 cbFirm.setValue(rs.getString("firmName"));
            }
           
       }catch(SQLException ex){
           ex.printStackTrace();
       }
      
    userPermission();
    }

    @FXML
    private void backPane() {
        if(back==0){
          FXMLLoader loader = new FXMLLoader(CusController.this.getClass().getResource(StringName.MAINMGR_PAGE));
                                  try{
                                      AnchorPane managerPane = loader.load();
                                      
                                      Stage substage=new Stage();
                                      substage.initModality(Modality.WINDOW_MODAL);
                                      substage.setScene(new Scene(managerPane,962,703));
                                      substage.show();  
                                      btnBack.getScene().getWindow().hide();
                                  }catch(IOException ex){
                                      ex.printStackTrace();
                                  } 
        }else{
            back=0;
            btnBack.getScene().getWindow().hide();
        }
                                  
    }

    @FXML
    private void addCus() throws ParseException {
            boolean nameIsNull = Validations.textFieldIsNull(tfName, lblNameEmpty, "Name cannot be empty!");
            boolean icIsNull = Validations.textFieldIsNull(tfIdentityCard, lblICEmpty, "Identity card cannot be empty!");
            boolean emailIsNull = Validations.textFieldIsNull(tfEmail, lblEmailEmpty, "Email cannot be empty!");
            boolean phoneIsNull = Validations.textFieldIsNull(tfPhone, lblPhoneEmpty, "Phone cannot be empty!");
            boolean addressIsNull = Validations. textFieldIsNull(tfAddress, lblAddressEmpty, "Address cannot be empty!");
            boolean nameIsAlpha = Validations.textAlpha(tfName, lblNameValid, "Do not be have numberic!");
            boolean genderSelected = Validations.selectedCbb(cbGender, lblGenderValid, "You need to choose gender!");
            boolean birthSelected= Validations.datePicker(dkBirth, lblBirthValid, "Yout need to choose date");
            boolean icIsNumber = Validations.textNumberic(tfIdentityCard, lblICValid, "Do not have alphabet!");
            boolean isEmail = Validations.emailFormat(tfEmail, lblEmailValid, "Need to correct email format!");
            boolean isPhone = Validations.textNumberic(tfPhone, lblPhoneValid, "Do not have aphabet!");
            boolean phoneDup =  sharedMethod.checkDup("phone", "customers", " ",tfPhone.getText(), lblPhoneDup, "Phone number already exists");
            boolean emailDup = sharedMethod.checkDup("email", "customers"," ", tfEmail.getText(), lblEmailDup, "Email already exists");
            boolean icDup = sharedMethod.checkDup("identity_card", "customers"," ", tfIdentityCard.getText(), lblICDup, "Identity card number already exists");
            boolean checkBirth= Validations.checkBirth(dkBirth,15, lblBirthCheck, "Must be over 15 years old");
            boolean phoneLength = Validations.dataLength(tfPhone, lblPhoneLength, "Please enter the correct phone number", 11);
            if(nameIsNull==true)
              lblNameValid.setText("");
            
            if(icIsNull==true){
                lblICValid.setText("");
            } else if(icDup==false){
                lblICValid.setText("");
            }else lblICDup.setText("");
                
            if(emailIsNull){
                lblEmailValid.setText("");
                lblEmailDup.setText("");
            }
            if(phoneIsNull==true){
                lblPhoneValid.setText("");
                lblPhoneLength.setText("");
                lblPhoneDup.setText("");
            }else if(isPhone==false){
                lblPhoneLength.setText("");
                lblPhoneDup.setText("");
            }else if(phoneDup==false){
                lblPhoneValid.setText("");
                lblPhoneLength.setText("");
            }else{
                lblPhoneDup.setText("");
            }
          System.out.println("com.myfx.demofx1.controller.CusController.addCus() "+emailDup);
            if(emailDup==false){
                lblEmailValid.setText("");
            }else{
                lblEmailDup.setText("");
            }
            if(!checkBirth)lblBirthValid.setText("");
            else lblBirthCheck.setText("");
            if(nameIsAlpha&&genderSelected&&birthSelected&&icIsNumber&&
                isEmail&&isPhone&&!addressIsNull&&phoneDup&&emailDup&&checkBirth&&icDup&&phoneLength){
                  int status =0;
                if(chkActive.isSelected()){
                     status = 1;
                 }
                if(cbFirm.getSelectionModel().getSelectedItem()==null||cbFirm.getSelectionModel().getSelectedItem().equals("No Firm")){
                     String columnName="name,customer_code,gender,birth,identity_card, email, phone,address,firm_id,status";
                String valueInsert="'"+tfName.getText()+"','"+ sharedMethod.randomCode("CUS0")+"','"+cbGender.getSelectionModel().getSelectedItem()+"','"+
                                dkBirth.getValue().toString()+"','"+tfIdentityCard.getText()+"','"+tfEmail.getText()+"','"+
                                tfPhone.getText()+"','"+tfAddress.getText()+"',"+0+","+status;
            sharedMethod.insertOj("customers", columnName, valueInsert);
                }else{
                    String columnName="name,customer_code,gender,birth,identity_card, email, phone,address,firm_id,status";
                String valueInsert="'"+tfName.getText()+"','"+ sharedMethod.randomCode("CUS0")+"','"+cbGender.getSelectionModel().getSelectedItem()+"','"+
                                dkBirth.getValue().toString()+"','"+tfIdentityCard.getText()+"','"+tfEmail.getText()+"','"+
                                tfPhone.getText()+"','"+tfAddress.getText()+"',"+getIDFromFirmName(cbFirm.getSelectionModel().getSelectedItem())+","+status;
                     sharedMethod.insertOj("customers", columnName, valueInsert);
                }
                lblNameEmpty.setText("");
                lblICEmpty.setText("");
                lblEmailEmpty.setText("");
                lblPhoneEmpty.setText("");
                lblGenderValid.setText("");
                lblBirthValid.setText("");
               lblPhoneLength.setText("");
                lblPhoneDup.setText("");
                lblEmailDup.setText("");
                lblICDup.setText("");
                lblBirthCheck.setText("");
            showTblCus();
            resetInput();
             
               
            }
            
    }

    @FXML
    private void resetInput() {
        tfName.clear();
        cbGender.getSelectionModel().clearSelection();
        cbGender.setValue(null);
        cbFirm.getSelectionModel().clearSelection();
        cbFirm.setValue(null);
        dkBirth.getEditor().clear();
        tfIdentityCard.clear();
        tfEmail.clear();
        tfPhone.clear();
        tfAddress.clear();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnAdd.setDisable(false);
        chkActive.setSelected(false);
        lblNameEmpty.setText("");
           lblICEmpty.setText("");
           lblEmailEmpty.setText("");
           lblPhoneEmpty.setText("");
           lblGenderValid.setText("");
           lblAddressEmpty.setText("");
             lblPhoneDup.setText("");
            lblEmailDup.setText("");
            lblICDup.setText("");
            lblBirthCheck.setText("");
    }

    @FXML
    private void updateCus() throws ParseException {
        int index= tblCusMgr.getSelectionModel().getSelectedIndex();
            if(index <= -1){
                return;
            }
            boolean nameIsNull = Validations.textFieldIsNull(tfName, lblNameEmpty, "Name cannot be empty!");
            boolean icIsNull = Validations.textFieldIsNull(tfIdentityCard, lblICEmpty, "Identity card cannot be empty!");
            boolean emailIsNull = Validations.textFieldIsNull(tfEmail, lblEmailEmpty, "Email cannot be empty!");
            boolean phoneIsNull = Validations.textFieldIsNull(tfPhone, lblPhoneEmpty, "Phone cannot be empty!");
            boolean addressIsNull = Validations. textFieldIsNull(tfAddress, lblAddressEmpty, "Address cannot be empty!");
            boolean nameIsAlpha = Validations.textAlpha(tfName, lblNameValid, "Do not be have numberic!");
            boolean genderSelected = Validations.selectedCbb(cbGender, lblGenderValid, "You need to choose gender!");
            boolean birthSelected= Validations.datePicker(dkBirth, lblBirthValid, "Yout need to choose date");
            boolean icIsNumber = Validations.textNumberic(tfIdentityCard, lblICValid, "Do not have alphabet!");
            boolean isEmail = Validations.emailFormat(tfEmail, lblEmailValid, "Need to correct email format!");
            boolean isPhone = Validations.textNumberic(tfPhone, lblPhoneValid, "Do not have aphabet!");
            boolean phoneDup =  sharedMethod.checkDup("phone", "customers", " ",tfPhone.getText(), lblPhoneDup, "Phone number already exists");
            boolean emailDup = sharedMethod.checkDup("email", "customers"," ", tfEmail.getText(), lblEmailDup, "Email already exists");
            boolean icDup = sharedMethod.checkDup("identity_card", "customers"," ", tfIdentityCard.getText(), lblICDup, "Identity card number already exists");
            boolean checkBirth= Validations.checkBirth(dkBirth,15, lblBirthCheck, "Must be over 17 years old");
            boolean phoneLength = Validations.dataLength(tfPhone, lblPhoneLength, "Please enter the correct phone number", 11);
            if(nameIsNull==true)
              lblNameValid.setText("");
            
            if(icIsNull==true){
                lblICValid.setText("");
            } else if(icDup==false){
                lblICValid.setText("");
            }else lblICDup.setText("");
                
            if(emailIsNull){
                lblEmailValid.setText("");
                lblEmailDup.setText("");
            }
            if(phoneIsNull==true){
                lblPhoneValid.setText("");
                lblPhoneLength.setText("");
                lblPhoneDup.setText("");
            }else if(isPhone==false){
                lblPhoneLength.setText("");
                lblPhoneDup.setText("");
            }else if(phoneDup==false){
                lblPhoneValid.setText("");
                lblPhoneLength.setText("");
            }else{
                lblPhoneDup.setText("");
            }
          
            if(!emailDup){
                lblEmailValid.setText("");
            }else{
                lblEmailDup.setText("");
            }
            if(!checkBirth)lblBirthValid.setText("");
            else lblBirthCheck.setText("");
            if(nameIsAlpha&&genderSelected&&birthSelected&&icIsNumber&&
                isEmail&&isPhone&&!addressIsNull&&phoneDup&&emailDup&&checkBirth&&icDup&&phoneLength){
                Customer c = new Customer();
                c=listCus.get(index);
                if(cbFirm.getSelectionModel().getSelectedItem()==null){
                    updateSupport("0",c.getId());
                }else{
                    updateSupport(String.valueOf(getIDFromFirmName(cbFirm.getSelectionModel().getSelectedItem())),c.getId());
                }
                }
    }
    private void updateSupport(String input, int id){
           int status=0;
                if(chkActive.isSelected()){
                     status = 1;
                 }
                
         String value = "name ='"+tfName.getText()+"',gender ='"+cbGender.getSelectionModel().getSelectedItem()+"',birth ='"+dkBirth.getValue().toString()+"',"
                   + "identity_card='"+tfIdentityCard.getText()+"',address='"+tfAddress.getText()+"',email='"+tfEmail.getText()+"', phone='"+tfPhone.getText()+
                   "', firm_id="+input+", status="+status;
            sharedMethod.updateOj("customers", value, id);
            lblNameEmpty.setText("");
            lblICEmpty.setText("");
            lblEmailEmpty.setText("");
            lblPhoneEmpty.setText("");
            lblGenderValid.setText("");
            lblBirthValid.setText("");
           lblPhoneLength.setText("");
            lblPhoneDup.setText("");
            lblEmailDup.setText("");
            lblICDup.setText("");
            lblBirthCheck.setText("");
            showTblCus();
            resetInput();
         
    }

    @FXML
    private void deleteCus() {
          int index= tblCusMgr.getSelectionModel().getSelectedIndex();
            if(index <= -1){
                return;
            }
            Customer c = new Customer();
            c = listCus.get(index);
            
            sharedMethod.deleteOj("customers", c.getId());
             showTblCus();
             refreshSearch();
    }


    @FXML
    private void logoutAction() {
          int option = JOptionPane.showConfirmDialog(null, "Logout?");
            System.out.println("option : " + option);
            
            if(option == 0) {
            FXMLLoader loader = new FXMLLoader(CusController.this.getClass().getResource(StringName.LOGIN_PAGE));
                                  try{
                                      AnchorPane managerPane = loader.load();
                                      LoginController constrol = loader.getController();
                                      
                                      Stage substage=new Stage();
                                      substage.initModality(Modality.WINDOW_MODAL);
                                      substage.initStyle(StageStyle.UNDECORATED);
                                      substage.setScene(new Scene(managerPane,848,502));
                                      substage.show();  
                                      
                                      MainMgrController.tab="inv";
                                     logout.getScene().getWindow().hide();
                                  }catch(IOException ex){
                                      ex.printStackTrace();
                                  } 
            }
    }

    @FXML
    private void refreshSearch() {
        tfFind.clear();
        cbFind.getSelectionModel().clearSelection();
        showTblCus();
        resetInput();
    }

    @FXML
    private void findAction() {
          if(cbFind.getSelectionModel().getSelectedItem()==null){
            MessageDialogHelper.showErrorDialog(null, "You need to choose", "unachievable");
        }
        else if(!(cbFind.getSelectionModel().getSelectedItem()==null)){
         String input = tfFind.getText();
         String ob = cbFind.getSelectionModel().getSelectedItem();
         String oj= "c.identity_card";
         if(ob.equals("Name")){
             oj = "c.name";
         }
         if(ob.equals("Email")){
             oj = "c.email";
         }
         if(ob.equals("Phone")){
             oj = "c.phone";
         }
         if(ob.equals("Customer Code")){
             oj = "c.customer_code";
         }
         if(ob.equals("Firm name")){
             oj = "(select name from firms where c.firm_id=firms.id)";
         }
            listCus = getDataModify.getAllCus("where "+oj+" like "+"'%"+input+"%'");
            tblCusMgr.setItems(listCus);
        } else{
             showTblCus();
        }
        if(tfFind.equals(null)){
            showTblCus();
        }
       
        
    }

    private void showTblCus() {
        listCus=getDataModify.getAllCus("");
        tblCusMgr.setItems(listCus);
    }
    private  int getIDFromFirmName(String fname){
         int i =0;
         Connection conn = DBConnection.Instance().getConn();
       try{
           String sql = "select id from firms where name ='"+fname+"'";
           PreparedStatement pstmt = conn.prepareStatement(sql);
           System.out.println("firm id:"+sql);
           ResultSet rs = pstmt.executeQuery();
           while(rs.next()){
              i=rs.getInt("id");
           }
           pstmt.close();
           rs.close();
       }catch(SQLException ex){
           
       }
          return i;
    }
    
}
