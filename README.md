# BankMange
A command Line Banking Management System java application all created goes to Myself :)
It uses the mySql database to store and reterive the user data. 

This project is open-source you can modify and use this if you want 
but please give me credit :)

You have to install the following setup before running-->
    - JDK 8
    - MySql 5.5
    
You should create a database and table-->
    <code>CREATE DATABASE bankmanage;<br />
           USE bankmanage;<br />
          CREATE TABLE user VALUES<br />
          (accno int not null unique auto_increament, uname varchar(25), dob date, pass varchar(25) not null, mobile bigint not null);</code>
