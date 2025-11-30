import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryManagementSystem {
    private static Map<Integer, Product> inventory = new HashMap<>();
    private static final String FILE_NAME = "inventory.dat";

    public static void main(String[] args) {
        loadFromFile();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Inventory Management System ======");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Search Products");
            System.out.println("6. Sort Products");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addProduct(sc);
                case 2 -> viewProducts();
                case 3 -> updateProduct(sc);
                case 4 -> deleteProduct(sc);
                case 5 -> searchProducts(sc);
                case 6 -> sortProducts(sc);
                case 7 -> saveToFile();
                default -> System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 7);
        sc.close();
    }

    // Add Product
    private static void addProduct(Scanner sc) {
        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (inventory.containsKey(id)) {
            System.out.println("Product ID already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Category: ");
        String category = sc.nextLine();
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();

        Product p = new Product(id, name, category, qty, price);
        inventory.put(id, p);
        System.out.println("✅ Product added successfully!");
    }

    // View Products
    private static void viewProducts() {
        if (inventory.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        System.out.println("\n--- Product List ---");
        inventory.values().forEach(System.out::println);
    }

    // Update Product
    private static void updateProduct(Scanner sc) {
        System.out.print("Enter Product ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        Product p = inventory.get(id);
        if (p == null) {
            System.out.println("Product not found!");
            return;
        }

        System.out.print("Enter new Name (" + p.getName() + "): ");
        String name = sc.nextLine();
        System.out.print("Enter new Category (" + p.getCategory() + "): ");
        String category = sc.nextLine();
        System.out.print("Enter new Quantity (" + p.getQuantity() + "): ");
        int qty = sc.nextInt();
        System.out.print("Enter new Price (" + p.getPrice() + "): ");
        double price = sc.nextDouble();

        if (!name.isEmpty()) p.setName(name);
        if (!category.isEmpty()) p.setCategory(category);
        p.setQuantity(qty);
        p.setPrice(price);

        System.out.println("✅ Product updated successfully!");
    }

    // Delete Product
    private static void deleteProduct(Scanner sc) {
        System.out.print("Enter Product ID to delete: ");
        int id = sc.nextInt();
        if (inventory.remove(id) != null)
            System.out.println("✅ Product deleted successfully!");
        else
            System.out.println("Product not found!");
    }

    // Search Products by Name or Category
    private static void searchProducts(Scanner sc) {
        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine().toLowerCase();

        List<Product> results = inventory.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword) ||
                             p.getCategory().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        if (results.isEmpty())
            System.out.println("No matching products found!");
        else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    // Sort Products
    private static void sortProducts(Scanner sc) {
        if (inventory.isEmpty()) {
            System.out.println("No products available to sort.");
            return;
        }

        System.out.println("Sort by:");
        System.out.println("1. Name");
        System.out.println("2. Price (Low to High)");
        System.out.println("3. Quantity (High to Low)");
        System.out.print("Choose: ");
        int opt = sc.nextInt();

        List<Product> sortedList = new ArrayList<>(inventory.values());
        switch (opt) {
            case 1 -> sortedList.sort(Comparator.comparing(Product::getName));
            case 2 -> sortedList.sort(Comparator.comparingDouble(Product::getPrice));
            case 3 -> sortedList.sort(Comparator.comparingInt(Product::getQuantity).reversed());
            default -> {
                System.out.println("Invalid choice!");
                return;
            }
        }

        System.out.println("\n--- Sorted Products ---");
        sortedList.forEach(System.out::println);
    }

    // Save data to file
    private static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            System.out.println("💾 Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // Load data from file
    private static void loadFromFile() {
        File f = new File(FILE_NAME);
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                inventory = (Map<Integer, Product>) ois.readObject();
                System.out.println("📂 Data loaded from file.");
            } catch (Exception e) {
                System.out.println("Error loading file: " + e.getMessage());
            }
        }
    }
}
