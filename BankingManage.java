/*
Credit - Harsh Tripathi
Date = 23/11/2020
Developed by Harsh Tripathi
---------------------------------
A command Line Banking Management System java application all created goes to Myself :)
It uses the mySql database to store and reterive the user data. 
**
This project is open-source you can modify and use this if you want 
but before using this please give me credit :)
**

This is under development
*/

import java.sql.*;
import java.util.Scanner;

public class BankingManage {
    private static int balance = 0;
    private static int previous;
    private static Statement stmt;
    private static String acc;
    private static Connection con;
    private static final Scanner sc = new Scanner(System.in);
    
    BankingManage(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection(  
"jdbc:mysql://localhost:3306/bankmanage","root","admin");
            stmt=con.createStatement();
        }
        catch(Exception e){
            System.out.println("**Failed to connect**\nError: "+e);
        }
    }
    
    private static void userPanel() throws SQLException{
        System.out.println("----------USER PANEL----------");
        System.out.println("\n\n1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Show Previous transaction");
        System.out.println("4. Check Balance");
        System.out.println("5. Exit");
        
        System.out.println("Enter your choice: ");
            int ch = sc.nextInt();
            switch(ch){
                case 1:
                    System.out.println("Enter Amount: ");
                    int amount_d = sc.nextInt();
                    deposite(amount_d);
                    break;
                case 2:
                    System.out.println("Enter Amount: ");
                    int amount_w = sc.nextInt();
                    withdraw(amount_w);
                    break;
                case 3:
                    if(previous > 0){
                        System.out.println("Previous deposited of "+previous);
                    }
                    else if(previous<0){
                        System.out.println("Previous withdrawed of "+Math.abs(previous));
                    }
                    userContinue();
                    break;
                case 4:
                    System.out.println("Your Current Balance is "+balance);
                    userContinue();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid Input");
            }
    }
    private static void userContinue() throws SQLException{
        System.out.println("Do you want to continue(y/n)");
        String c = sc.next();
        
        if(c.equals("y")) {
            userPanel();
        }
    }
    private static void deposite(int amount) throws SQLException{
        if(amount < 500){
            System.out.println("You Cannot Depoite amount less than 500");
        }
        else if(amount > 500){
            balance += amount;
            System.out.println("Amout "+amount+" deposited...");
            previous = amount;
            
            stmt.executeUpdate("update user set bal="+String.valueOf(balance)+" where accno="+acc);
            
        }
        userContinue();
    }
    private static void withdraw(int amount) throws SQLException{
        if(amount>25000){
            System.out.println("You cannot withdraw amount more than 25000");
        }
        else if(amount<25000 && amount>0){
            balance -= amount;
            System.out.println("Amout "+amount+" withdrawed...");
            previous = -amount;
            
            stmt.executeUpdate("update user set bal="+String.valueOf(balance)+" where accno="+acc);
        }
        userContinue();
    }
    private static void userLogin() throws SQLException{
        String e_pass;
        
        System.out.println("Enter Account no.: ");
        acc = sc.next();
        System.out.println("Enter Paasword: ");
        e_pass = sc.next();
        
        ResultSet s = stmt.executeQuery("select accno,pass,bal from user where accno="+acc);
        String accno;
        accno = "";
        String pass;
        pass = "";
        while(s.next()){
            accno = s.getString("accno");
            pass = s.getString("pass");
            balance = Integer.parseInt(s.getString("bal"));
        }
        if(pass.equals(e_pass) && accno.equals(acc)){
            userPanel();
        }
        else{
            System.out.println("\nInvalid AccountNumber/Password");
        }
    }
    private static void adminLogin() throws SQLException{
        System.out.println("------------Admin login--------------");
        String pass = "admin123";
        
        System.out.println("\n\nEnter password: ");
        String e_pass = sc.next();
        if(e_pass.equals(pass)){
            adminPanel();
        }
        else{
             System.out.println("\nWrong Password...");
        }
    }
    private static void adminContinue() throws SQLException{
        System.out.println("Do you want to continue(y/n)");
        String c = sc.next();
        
        if(c.equals("y")) {
            adminPanel();
        }
    }
    private static void adminPanel() throws SQLException{
        System.out.println("--------WELCOME--------\n\n");
        System.out.println("1. Add User");
        System.out.println("2. Remove User");
        System.out.println("3. List of all user");
        System.out.println("4. Exit");
        
        System.out.println("Enter your choice: ");
        int ch = sc.nextInt();
        switch(ch){
            case 1:
                String ano;
                String name;
                String pass;
                String dob;
                String bal;
                
                System.out.println("Enter Account number: ");
                ano = sc.next();
                System.out.println("Enter your name: ");
                name = sc.next();
                System.out.println("Enter password: ");
                pass = sc.next();
                System.out.println("Enter your Date of Birth: ");
                dob = sc.next();
                System.out.println("Enter Balance: ");
                bal = sc.next();
                
                PreparedStatement pst = con.prepareStatement("insert into user values(?,?,?,?,?)");
                
                pst.setString(1, ano);
                pst.setString(2, name);
                pst.setString(3, pass);
                pst.setString(4, dob);
                pst.setString(5, bal);
                
                pst.executeUpdate();
                System.out.println("......User Added......");
                adminContinue();
                break;
                
            case 2:
                System.out.println("Enter your account number: ");
                String acno = sc.next();
                stmt.executeUpdate("delete from user where accno="+acno);
                System.out.println("......Deleted User.....");
                adminContinue();
                break;
                
            case 3:
                ResultSet rs = stmt.executeQuery("select accno,uname,dob,bal from user");
                System.out.println("Acno. Name    DOB    Balance");
                while(rs.next()){
                    System.out.print(" "+rs.getInt("accno"));
                    System.out.print("   "+rs.getString("uname"));
                    System.out.print(" "+rs.getDate("dob"));
                    System.out.print(" "+rs.getInt("bal"));
                    System.out.println("");
                }
                adminContinue();
                break;
            
            case 4:
                return;
            default:
                System.out.println("Invalid Input");
        } 
    }
    public static void main(String[] args) throws SQLException{
        BankingManage obj = new BankingManage();
        while(true){
            System.out.println("-------Welcome--------");
            System.out.println("\n\n1. User Login");
            System.out.println("2. Admin Login");
            System.out.println("Who you are(Enter 3 for exit): ");
                String ch = sc.next();
                //here we are taking the choice in string because we don't know what user will enter
                //extracting the first char and checking is it's ascii value is b/w 48-57(in decimal[0-9])
                //this logic will help the program to not terminate if we give any input
                char charc = ch.charAt(0);
                int ascii = (int) charc;
                if(ascii>=48 && ascii<=57){
                    int c = Integer.parseInt(ch);
                    switch(c){
                        case 1:
                            userLogin();
                            break;
                        case 2:
                            adminLogin();
                            break;
                        case 3:
                            return;
                        default:
                            System.out.println("Invalid Input");
                    }
                }
                else{
                    System.out.println("Please Enter a valid Input");
                }
        } 
        }
        
    }
