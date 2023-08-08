import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException; // Add this import

public class EmployeeManagementApp {
    private static List<Employee> employees = new ArrayList<>();
    private static final String FILE_NAME = "employees.xlsx";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadEmployees();

        while (true) {
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Search Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Save and Exit");
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
                    searchEmployee();
                    break;
                case 5:
                    deleteEmployee();
                    break;
                case 6:
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

private static void searchEmployee() {
        if (employees.isEmpty()) {
            System.out.println("No employees to search.");
            return;
        }
        System.out.print("Enter Employee ID to search: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        for (Employee employee : employees) {
            if (employee.getId() == id) {
                System.out.println("Employee found:");
                System.out.println(employee);
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
        System.out.print("Enter Employee ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Iterator<Employee> iterator = employees.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            if (employee.getId() == id) {
                iterator.remove();
                found = true;
                System.out.println("Employee deleted successfully!");
                break;
            }
        }

        if (!found) {
            System.out.println("Employee not found.");
        }
    }

 private static void loadEmployees() {
    FileInputStream fis = null;
    Workbook workbook = null;

    try {
        System.out.print("Enter Excel Sheet Name for Loading: ");
        String sheetName = scanner.nextLine();

        fis = new FileInputStream(FILE_NAME);
        workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheet(sheetName); // Use the provided sheet name

        if (sheet == null) {
            System.out.println("Sheet not found: " + sheetName);
            return;
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Employee employee = new Employee();
            employee.setId((int) row.getCell(0).getNumericCellValue());
            employee.setName(row.getCell(1).getStringCellValue());
            employee.setDesignation(row.getCell(2).getStringCellValue());
            employee.setSalary(row.getCell(3).getNumericCellValue());
            employees.add(employee);
        }

        System.out.println("Loaded " + employees.size() + " employees from sheet: " + sheetName);
    } catch (IOException e) {
        System.out.println("No employee data found. Starting with an empty list.");
    } catch (InvalidFormatException e) {
        System.out.println("Invalid format exception: " + e.getMessage());
    } finally {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                System.out.println("Error closing FileInputStream: " + e.getMessage());
            }
        }
        if (workbook != null && workbook instanceof Closeable) {
            try {
                ((Closeable) workbook).close();
            } catch (IOException e) {
                System.out.println("Error closing Workbook: " + e.getMessage());
            }
        }
    }
}
    private static void saveEmployees() {
    Workbook workbook = new XSSFWorkbook();
    System.out.print("Enter Excel Sheet Name for Saving: ");
    String sheetName = scanner.nextLine();

    Sheet sheet = workbook.createSheet(sheetName); // Use the provided sheet name

    // Create header row
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("ID");
    headerRow.createCell(1).setCellValue("Name");
    headerRow.createCell(2).setCellValue("Designation");
    headerRow.createCell(3).setCellValue("Salary");

    int rowNum = 1;
    for (Employee employee : employees) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(employee.getId());
        row.createCell(1).setCellValue(employee.getName());
        row.createCell(2).setCellValue(employee.getDesignation());
        row.createCell(3).setCellValue(employee.getSalary());
    }

    // Construct the file name using the user-provided sheet name
    String fileName = sheetName + ".xlsx";

    try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
        workbook.write(fileOut);
        System.out.println("Employee data saved to Excel file: " + fileName);
    } catch (IOException e) {
        System.out.println("Error saving employee data: " + e.getMessage());
    }
 }
}