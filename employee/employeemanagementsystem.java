import java.io.*;
import java.util.*;

public class EmployeeManagementSystem {
    private static List<Employee> employees = new ArrayList<>();
    private static final String FILE_NAME = "employees.dat";

    public static void main(String[] args) {
        loadFromFile();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Employee Management System ===");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch(choice) {
                case 1 -> addEmployee(sc);
                case 2 -> viewEmployees();
                case 3 -> updateEmployee(sc);
                case 4 -> deleteEmployee(sc);
                case 5 -> saveToFile();
                default -> System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 5);

        sc.close();
    }

    // Add Employee
    private static void addEmployee(Scanner sc) {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Department: ");
        String dept = sc.nextLine();
        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();

        Employee emp = new Employee(id, name, dept, salary);
        employees.add(emp);
        System.out.println("Employee added successfully!");
    }

    // View Employees
    private static void viewEmployees() {
        if(employees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            System.out.println("\nEmployee List:");
            for(Employee emp : employees) {
                System.out.println(emp);
            }
        }
    }

    // Update Employee
    private static void updateEmployee(Scanner sc) {
        System.out.print("Enter Employee ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        boolean found = false;

        for(Employee emp : employees) {
            if(emp.getId() == id) {
                found = true;
                System.out.print("Enter new Name (" + emp.getName() + "): ");
                String name = sc.nextLine();
                System.out.print("Enter new Department (" + emp.getDepartment() + "): ");
                String dept = sc.nextLine();
                System.out.print("Enter new Salary (" + emp.getSalary() + "): ");
                double salary = sc.nextDouble();

                emp.setName(name.isEmpty() ? emp.getName() : name);
                emp.setDepartment(dept.isEmpty() ? emp.getDepartment() : dept);
                emp.setSalary(salary);
                System.out.println("Employee updated successfully!");
                break;
            }
        }

        if(!found) System.out.println("Employee with ID " + id + " not found!");
    }

    // Delete Employee
    private static void deleteEmployee(Scanner sc) {
        System.out.print("Enter Employee ID to delete: ");
        int id = sc.nextInt();
        boolean removed = employees.removeIf(emp -> emp.getId() == id);

        if(removed) System.out.println("Employee deleted successfully!");
        else System.out.println("Employee with ID " + id + " not found!");
    }

    // Save to File
    private static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(employees);
            System.out.println("Data saved to file successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load from File
    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        if(file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                employees = (List<Employee>) ois.readObject();
                System.out.println("Data loaded from file.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading data: " + e.getMessage());
            }
        }
    }
}
