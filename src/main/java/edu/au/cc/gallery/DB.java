package edu.au.cc.gallery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DB {

   private static final String dbURL = "jdbc:postgresql://demo-database-1.ck8cgnljdrsw.us-west-2.rds.amazonaws.com/image_gallery";

   private Connection connection;
   
   private String getPassword() {
      try(BufferedReader br = new BufferedReader(new FileReader("/home/ec2-user/.sql-passwd"))) {
         String result = br.readLine();
         return result;
      } catch (IOException ex) {
         System.out.println("Error opening password file. Make sure .sql-passwd exists and contains your sql password.");
         System.exit(1);
      }
      return null;
   } 

   public void connect() throws SQLException {
      try {
         Class.forName("org.postgresql.Driver");
         connection = DriverManager.getConnection(dbURL, "image_gallery", getPassword());
      } catch (ClassNotFoundException ex) {
         ex.printStackTrace();
         System.exit(1);
      }
   }

   public ResultSet execute(String query) throws SQLException {
      PreparedStatement stmt = connection.prepareStatement(query);
      ResultSet rs = stmt.executeQuery();
      return rs;
   }

   public void execute(String query, String[] values) throws SQLException {
      PreparedStatement stmt = connection.prepareStatement(query);
      for (int i=0; i < values.length; i++) {
          stmt.setString(i+1, values[i]);
      } 
      stmt.execute();
   }

   public void close() throws SQLException {
      connection.close();
      System.out.println("Database closed");
   }

  /* public static void demo() throws Exception {
      DB db = new DB();
      db.connect();
      db.execute("update users set password=? where username=?", new String[] {"monkey", "fred"});
      ResultSet rs = db.execute("select username, password, full_name from users");
      while (rs.next()) System.out.println(rs.getString(1)+"'"+rs.getString(2)+"'"+rs.getString(3));
      rs.close();
      db.close();
  }*/

  public void listUsers() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("select * from users;");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3));
        }
        rs.close();
    }

  public void addUser(String username, String password, String fullName) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("insert into users (username, password, full_name) values (?, ?, ?);");
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setString(3, fullName);
        stmt.execute();
        System.out.println("User " + username + " added");
    }

  public void editUser(String editUserName, String editPassword, String editFullName) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("update users set password=?, full_name=? where username=?;");
        stmt.setString(1, editPassword);
        stmt.setString(2, editFullName);
        stmt.setString(3, editUserName);
        stmt.execute();
        System.out.println("User " + editUserName + " password and full name updated.");
    }

  public void deleteUser(String deleteName) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("delete from users where username=?;");
        stmt.setString(1, deleteName);
        stmt.execute();
        System.out.println("User " + deleteName + " deleted from database");
    }

  public boolean userExists(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("select * from users where username=?");
        stmt.setString(1, query);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }
}
