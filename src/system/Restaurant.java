package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Date;

public class Restaurant implements Runnable{
        

    
    public static String adminPassword ;

    static public ArrayList<Product> products = new ArrayList();
    static public HashSet<Order> orders = new HashSet();
    
    public static Vector<Vector<Object>> rowCategoryData = new Vector<>();
    public static Vector<Vector<Object>> rowProductData = new Vector<>();

    public static Vector<Vector<Object>> staffEmployeeInformation = new Vector<>();
    public static Vector<Vector<Object>> staffCategoriesInformation = new Vector<>();

    static public HashSet<Order> kitchenOrders = new HashSet();
    static public int numberOfOrders = 0;
    
    public static Order currentOrder = null;

    public Restaurant() {
        
    }
    @Override
    public void run() {
        Restaurant.load();
    }
    
    
    public static void load() {
        loadCategories();
        loadProducts();
        loadStaff();
        loadStaffCategories();
        loadpassword();
        loadDetails();
        
        loadKitchen();
        loadOrders();
    }
    
    public static void finishingOrder(Order.PayingType payingType, Order.OrderStatus status, Order order) {
        order.setOrderDate(new Date());
        order.setPayingType(payingType);
        order.setStatus(status);
        Restaurant.orders.add(order);
        Restaurant.uploadOrders();
        
        Restaurant.currentOrder = null;
    }
    
    public static void addOrderToKitchen(Order order) {
        order.setID(++numberOfOrders);
        Restaurant.kitchenOrders.add(order);
        Restaurant.uploadKithcen();
        
        Restaurant.currentOrder = null;
    }
    
    public static void removeFromKitchen(Order order) {
        Restaurant.kitchenOrders.remove(order);
        Restaurant.uploadKithcen();
    }
    
        
    
    // kitchen orders
    public static void loadKitchen() {
        File f = new File("database\\kitchenOrders.ser");
        if (f.length() == 0) {
            return;
        }
        try {
            FileInputStream fis=new FileInputStream("database\\kitchenOrders.ser");
            ObjectInputStream kitchenOrdersFile =new ObjectInputStream(fis);
            
            kitchenOrders = (HashSet<Order>)kitchenOrdersFile.readObject();
            numberOfOrders = orders.size() + kitchenOrders.size();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void uploadKithcen() {
        try {
            FileOutputStream fop=new FileOutputStream("database\\kitchenOrders.ser");
            ObjectOutputStream kitchenOrdersFile = new ObjectOutputStream(fop);
            
            kitchenOrdersFile.writeObject(kitchenOrders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // orders
    public static void uploadOrders() {
        try {
            FileOutputStream fop=new FileOutputStream("database\\orders.ser");
            ObjectOutputStream ordersFile = new ObjectOutputStream(fop);
            
            ordersFile.writeObject(orders);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void loadOrders() {
        File f = new File("database\\orders.ser");
        if (f.length() == 0) {
            return;
        }
        try {
            FileInputStream fis=new FileInputStream("database\\orders.ser");
            ObjectInputStream ordersFile =new ObjectInputStream(fis);

            orders = (HashSet<Order>)ordersFile.readObject();
            numberOfOrders = orders.size() + kitchenOrders.size();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    // categories
    public static void loadCategories() {
        try {
            File categoriesFile = new File("database\\categories.txt");
            Scanner inFile = new Scanner(categoriesFile);
            rowCategoryData.removeAllElements();
            int i = 1;
            Vector<Object> Categoryrow;
            while (inFile.hasNext()) {
                Categoryrow = new Vector<>();
                Categoryrow.add(i++);
                Categoryrow.add(inFile.nextLine().trim());
                rowCategoryData.add(Categoryrow);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean uploadCategory(String name) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("database\\categories.txt", true))) {
            writer.println(name);
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    // products
    public static boolean uploadProdact(Product product) {
        try (PrintWriter writer
                = new PrintWriter(
                        new FileWriter("database\\products.txt", true))) {
            String s = String.format("%s ,%1.2f ,%s ,%s, %b",
                    product.getName(),
                    product.getPrice(),
                    product.getCategory().getName(),
                    product.getIngredients(),
                    product.isActive());

            writer.println(s);
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    public static void loadProducts() {
        try {
            File productsFile = new File("database\\products.txt");
            Scanner inFile = new Scanner(productsFile);
            rowProductData.removeAllElements();
            products.clear();
            int j = 1;
            Vector<Object> Productrow;
            while (inFile.hasNext()) {
                String get = inFile.nextLine();
                Product temp = new Product(get);
                Productrow = new Vector<>();
                Productrow.add(j++);
                Productrow.add(temp.getName());
                Productrow.add(temp.getPrice());
                Productrow.add(temp.getCategory().getName());
                Productrow.add(((temp.isActive()) ? "Active" : "not Active"));
                rowProductData.add(Productrow);
                products.add(new Product(get));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // staff categories
    public static void loadStaffCategories() {
        try {
            File categoriesFile = new File("database\\staff_categories.txt");
            Scanner inFile = new Scanner(categoriesFile);
            staffCategoriesInformation.removeAllElements();
            int i = 1;
            Vector<Object> Categoryrow;
            while (inFile.hasNext()) {
                Categoryrow = new Vector<>();
                Categoryrow.add(i++);
                Categoryrow.add(inFile.nextLine().trim());
                staffCategoriesInformation.add(Categoryrow);

            }
        } catch (IOException e) {
            e.printStackTrace();
            }
    }
    public static boolean uploadStaffCategory(String name) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("database\\staff_categories.txt", true))) {
            writer.println(name);
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    // staff
    public static boolean uploadstaff(Employee employee) {
        try (PrintWriter writer
                = new PrintWriter(
                        new FileWriter("database\\staff.txt", true))) {
            String s = String.format("%s ,%s ,%1.2f ,%s ",
                    employee.getName(),
                    employee.getCategory().getName(),
                    employee.getSalary(),
                    employee.getPassword());

            writer.println(s);
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    public static void loadStaff() {
        try {
            File file = new File("database\\staff.txt");
            Scanner inFile = new Scanner(file);
            staffEmployeeInformation.removeAllElements();
            int j = 1;
            Vector<Object> employeerow;
            while (inFile.hasNext()) {
                String get = inFile.nextLine();
                Employee temp = new Employee(get);
                employeerow = new Vector<>();
                employeerow.add(j++);
                employeerow.add(temp.getName());
                employeerow.add(temp.getCategory().getName());
                employeerow.add(temp.getSalary());
                employeerow.add(temp.getPassword());
                staffEmployeeInformation.add(employeerow);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static void loadpassword(){
       try {
            File le = new File("database\\password.txt");
            Scanner inFile = new Scanner(le);
            if(inFile.hasNext())
            adminPassword = inFile.nextLine().trim();
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    public static void uploadpassword(String newpass){
        try (PrintWriter writer
                = new PrintWriter(
                        new FileWriter("database\\password.txt"))) {
            writer.write(newpass);
            writer.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
    }
    
    
    public static String[] details; 
    public static void loadDetails(){
        try {
            File le = new File("database\\details.txt");
            Scanner inFile = new Scanner(le);
            String line ="";
            if(inFile.hasNext())
                line = inFile.nextLine().trim();
                details= line.split(",");
                
        } catch (IOException e) {
            e.printStackTrace();
           
        }
    }
    public static void uploadDetails(String name ,String adderss ,String phone ,String email){
        try (PrintWriter writer
                = new PrintWriter(
                        new FileWriter("database\\details.txt" ))) {
            writer.write(name+","+adderss+","+phone+","+email);
            writer.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }   
    
    
    public static boolean delete(int n, String path) {
        try {
            Stack<String> lines = new Stack<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                int m = 0;
                while ((line = reader.readLine()) != null) {
                    m++;
                    if (n != m) {
                        lines.add(line);
                    }
                }
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
                for (String line : lines) {
                    writer.println(line);
                }
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public static boolean edit(int n, String path , String newValue) {
        try {
            Stack<String> lines = new Stack<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                int m = 0;
                while ((line = reader.readLine()) != null) {
                    m++;
                    if (n != m) {
                        lines.add(line);
                    }else{
                        lines.add(newValue);
                    }
                }
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
                for (String line : lines) {
                    writer.println(line);
                }
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public static void edit(int n, 
            String path, 
            String firstString, 
            String secundString, 
            Category categori, 
            Double doubl, 
            Boolean bool)
    {
        try {
            ArrayList<String> lines;
            lines = (ArrayList<String>) Files.readAllLines(Paths.get(path));
            String product= firstString+","+doubl+","+categori.getName()+","+secundString+","+bool;
            String employee = firstString+","+categori.getName()+","+doubl+","+secundString;
            String newContent = (bool==null)?employee:product;
            lines.set(n-1, newContent);
            Files.write(Paths.get(path), lines);
           
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    
}
