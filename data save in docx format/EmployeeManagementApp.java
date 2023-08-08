import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagementApp {
    private static List<Employee> employees = new ArrayList<>();
    private static final String FILE_NAME = "employees.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadEmployees();

        while (true) {
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Save and Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewEmployees();
                    break;
                case 3:
                    updateEmployee();
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    saveEmployees();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

   private static void addEmployee() {
    Employee employee = new Employee();
    System.out.print("Enter Employee ID: ");
    employee.setId(scanner.nextInt());
    scanner.nextLine();  // Consume newline
    System.out.print("Enter Employee Name: ");
    employee.setName(scanner.nextLine());
    System.out.print("Enter Employee Designation: ");
    employee.setDesignation(scanner.nextLine());
    System.out.print("Enter Employee Salary: ");
    employee.setSalary(scanner.nextDouble());
    scanner.nextLine();  // Consume newline
    employees.add(employee);
    System.out.println("Employee added successfully!");
}

 private static void viewEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees to display.");
            return;
        }
        System.out.println("Employee List:");
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }


private static void updateEmployee() {
    if (employees.isEmpty()) {
        System.out.println("No employees to update.");
        return;
    }
    System.out.print("Enter the Employee ID to update: ");
    int id = scanner.nextInt();
    scanner.nextLine();  // Consume newline

    for (Employee employee : employees) {
        if (employee.getId() == id) {
            System.out.print("Enter new Employee Name: ");
            employee.setName(scanner.nextLine());
            System.out.print("Enter new Employee Designation: ");
            employee.setDesignation(scanner.nextLine());
            System.out.print("Enter new Employee Salary: ");
            employee.setSalary(scanner.nextDouble());
            scanner.nextLine();  // Consume newline
            System.out.println("Employee updated successfully!");
            return;
        }
    }
    System.out.println("Employee not found.");
}
   private static void deleteEmployee() {
        if (employees.isEmpty()) {
            System.out.println("No employees to delete.");
            return;
        }
        System.out.print("Enter the Employee ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Employee employeeToRemove = null;
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                employeeToRemove = employee;
                break;
            }
        }

        if (employeeToRemove != null) {
            employees.remove(employeeToRemove);
            System.out.println("Employee deleted successfully!");
        } else {
            System.out.println("Employee not found.");
        }
    }

    private static void loadEmployees() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            employees = (List<Employee>) ois.readObject();
            System.out.println("Loaded " + employees.size() + " employees from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No employee data found. Starting with an empty list.");
        }
    }

    private static void saveEmployees() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Employee employee : employees) {
                writer.println(employee);
                writer.println(); // Add an empty line between employee records
            }
            System.out.println("Saved " + employees.size() + " employees to file.");
        } catch (IOException e) {
            System.out.println("Error saving employee data: " + e.getMessage());
        }
    }
}
