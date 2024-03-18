package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Connection connection;
    public static void main(String[] args) {
        //Generates connection object to psql database
        String url = "jdbc:postgresql://localhost:5432/Student";
        String user = "postgres";
        String password = "password";//very secure password

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
        }
        catch(Exception e){System.out.println(e);}

        //Starts main menu operation
        int choice = -1;
        while (choice != 5){
            choice = menu();
            Scanner input = new Scanner(System.in);
            switch(choice){
                case(1):
                    System.out.println("getAllStudents Selected");
                    getAllStudents();
                    break;
                case(2):
                    System.out.println("addStudent Selected");
                    System.out.println("Please input the new students first name");
                    String first_name = input.next();
                    System.out.println("Please input the new students last name");
                    String last_name = input.next();
                    System.out.println("Please input the new students email");
                    String email = input.next();
                    System.out.println("Please input the new students enrollment date in yyyy-mm-dd format");
                    String enrollment_date = input.next();
                    addStudent(first_name,last_name,email,enrollment_date);
                    break;
                case(3):
                    System.out.println("updateStudentEmail Selected");
                    System.out.println("Please input the students id to be updated");
                    int update_id = input.nextInt();
                    System.out.println("Please input the students new email");
                    String update_email = input.next();
                    updateStudentEmail(update_id,update_email);
                    break;
                case(4):
                    System.out.println("deleteStudent Selected");
                    System.out.println("Please input the student id to be deleted");
                    int delete_id = input.nextInt();
                    deleteStudent(delete_id);
                    break;
                default:
                    System.out.println("Please Input a valid integer between 1 and 5");
                    break;
            }
        }
        System.out.println("\n --Quitting Application--");
    }

    //Generates Main Menu Interface
    private static int menu(){
        System.out.println("\n --MAIN MENU OPTIONS--");
        System.out.println("1: getAllStudents");
        System.out.println("2: addStudent");
        System.out.println("3: updateStudentEmail");
        System.out.println("4: deleteStudent");
        System.out.println("5: Quit Application");
        Scanner input = new Scanner(System.in);
        System.out.print(": ");
        try{
            int number = input.nextInt();
            return(number);
        }
        catch(Exception e){}
        return(-1);
    }
    //gets all students and prints them
    private static void getAllStudents(){
        try{
            if (connection != null){
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT * FROM students");
                ResultSet resultSet = statement.getResultSet();
                while(resultSet.next()){
                    System.out.print(resultSet.getString("student_id")+"\t");
                    System.out.print(resultSet.getString("first_name")+"\t");
                    System.out.print(resultSet.getString("last_name")+"\t");
                    System.out.print(resultSet.getString("email")+"\t");
                    System.out.print(resultSet.getString("enrollment_date")+"\n");
                }
            }
        }
        catch(Exception e){System.out.println(e.toString());}
    }
    //adds new student to students table
    private static void addStudent(String first_name,String last_name,String email,String enrollment_date){
        java.sql.Date date = Date.valueOf(enrollment_date);
        if (connection != null){
            //decided to use prepared statement instead of regular statement so my brain doesn't explode
            String query = "INSERT INTO Students (first_name,last_name,email,enrollment_date)"+"Values (?,?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, first_name);
                statement.setString(2, last_name);
                statement.setString(3, email);
                statement.setDate(4, date);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A student was successfully added");
                } else {
                    System.out.println("Operation Failed");
                }
            }
            catch(Exception e){System.out.println(e);}
        }
    }
    //updates student email by id
    private static void updateStudentEmail(int student_id, String new_email){
        if (connection!=null){
            try{
                Statement statement = connection.createStatement();
                statement.executeUpdate("UPDATE Students set email = '"+new_email+"' WHERE student_id = "+student_id);
            }
            catch(Exception e){System.out.println(e);}
        }
    }
    //deletes student by id
    private static void deleteStudent(int student_id){
        try{
            if (connection != null){
                Statement statement = connection.createStatement();
                statement.executeUpdate("DELETE  FROM students WHERE student_id ="+student_id);
            }
        }
        catch(Exception e){System.out.println(e.toString());}
    }
}