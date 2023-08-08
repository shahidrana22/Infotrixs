import java.io.*;
import java.util.List;

public class ReadSerializedEmployees {
    public static void main(String[] args) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("employees.dat"))) {
            List<Employee> employees = (List<Employee>) ois.readObject();
            
            System.out.println("Employee Data:");
            for (Employee employee : employees) {
                System.out.println(employee);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading employee data: " + e.getMessage());
        }
    }
}
