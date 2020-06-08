package edu.au.cc.gallery.tools;

import java.sql.SQLException;
import java.util.*;
import edu.au.cc.gallery.*;

public class UserAdmin {

    boolean closed;
    DB db = new DB();
    Scanner scan = new Scanner(System.in);

    public UserAdmin() {
        closed = false;
    }

    public void userMenu() throws SQLException {
        db.connect();
        while (closed != true) {
            System.out.println("1. List users\n2. Add user\n3. Edit user\n4. Delete user\n5. Quit");
            System.out.println("Enter command: ");
            int choice = scan.nextInt();
            scan.nextLine();
            getCommand(choice);
        }
    }

    public void getCommand(int choice) throws SQLException {
        boolean exists = false;
        switch (choice) {
            case 1:
                db.listUsers();
                break;
            case 2:
                System.out.println("Username: ");
                String username = scan.nextLine();
                exists = db.userExists(username);
                if (exists == true) {
                    System.out.println("Error: user with username " + username + " already exists");
                    break;
                }
                System.out.println("Password: ");
                String password = scan.nextLine();
                System.out.println("Full Name: ");
                String fullName = scan.nextLine();
                db.addUser(username, password, fullName);
                break;
            case 3:
                System.out.println("Username to edit: ");
                String editUserName = scan.nextLine();
                exists = db.userExists(editUserName);
                if (exists == false) {
                    System.out.println("No such user");
                    break;
                }
                System.out.println("New Password: ");
                String editPassword = scan.nextLine();
                System.out.println("New full name: ");
                String editFullName = scan.nextLine();
                db.editUser(editUserName, editPassword, editFullName);
                break;
            case 4:
                System.out.println("Enter username to delete: ");
                String deleteName = scan.nextLine();
                exists = db.userExists(deleteName);
                if (exists == false) {
                    System.out.println("No such user");
                    break;
                }
                System.out.println("Are you sure you want to delete " + deleteName + "? Enter YES or NO: ");
                String answer = scan.nextLine();
                if (answer.toUpperCase().equals("YES")) {
                    db.deleteUser(deleteName);
                    System.out.println("Deleted.");
                } else {
                    System.out.println("Not deleted.");
                }
                break;
            case 5:
                System.out.println("Bye.");
                db.close();
                closed = true;
                break;
            default:
                System.out.println("Please enter a number from 1 to 5");
                this.userMenu();
                break;
        }
    }
}
