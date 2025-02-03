package GUI;

import system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import static java.lang.Integer.MAX_VALUE;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

public class GUISystem {
    
    private JFrame frame;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private CardLayout cardLayoutSttuf;
    private static JDialog Dialog;

    
    private final Color mainColor = new Color(5, 25, 55);
    private final ImageIcon appIcon = new ImageIcon("img.png");
    private HashMap<String, String> nameWithPassword;

    private JTable CategoryEmploy;
    private JTable categoriesTable;
    private JTable productsTable;
    private JTable tableEmployee;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox gender;
    
    private JButton[] navButtons;

    private static Vector<Vector<String>> orderTableRows;
    private static JTable orderTable;

    public GUISystem() {
//        new Restaurant();
        init();
        frame.setVisible(true);

    }

    private void init() {
        
        
        
        frame = new JFrame("POS System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1366, 768);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.setIconImage(appIcon.getImage());

        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
        navigationPanel.setBackground(mainColor);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        navigationPanel.setBounds(0, 0, 200, 900);
        Image scaledImage = appIcon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        navigationPanel.add(imageLabel, BorderLayout.CENTER);
        navigationPanel.add(Box.createVerticalStrut(20));

        String[] buttonLabels = {"     Home   ", "Categories", "  Products ", "  Kitchen   ", "   Reports  ", "    Staff      ", "    Order    ", "  Settings  "};
        int m = buttonLabels.length;
        navButtons = new JButton[m];
        for (int i = 0; i < m; i++) {
            navButtons[i] = new JButton(buttonLabels[i]);
            navButtons[i].setFont(new Font("Arial", Font.BOLD, 28));
            navButtons[i].setBackground(mainColor);
            navButtons[i].setForeground(Color.WHITE);
            navButtons[i].setFocusPainted(false);
            navButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            navButtons[i].setMargin(new Insets(10, 20, 10, 20));
            navButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            navButtons[0].setBackground(Color.WHITE);
            navButtons[0].setForeground(mainColor);

            final int INDEX = i;
            navButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent evt) {
                    navButtons[INDEX].setForeground(mainColor);
                    navButtons[INDEX].setBackground(Color.WHITE);

                    for (int j = 0; j < buttonLabels.length; j++) {
                        if (j != INDEX) {
                            navButtons[j].setBackground(mainColor);
                            navButtons[j].setForeground(Color.WHITE);
                        }
                    }
                }
            });

            navigationPanel.add(navButtons[i]);

            if (i < buttonLabels.length - 1) {
                navigationPanel.add(Box.createVerticalStrut(15));
            }
        }

        frame.add(navigationPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(homeFunction(), "Home");
        contentPanel.add(categoriesFunction(), "Categories");
        contentPanel.add(productsFunction(), "Products");
        contentPanel.add(kitchenFunction(), "Kitchen");
        contentPanel.add(reportsFunction(), "Reports");  
        contentPanel.add(orderFunction(), "Order");
        contentPanel.add(settingsFunction(), "Settings");

        contentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        frame.add(contentPanel, BorderLayout.CENTER);

        navButtons[0].addActionListener(e -> {
            homeFunction();
            showPanel("Home");
        });

        navButtons[1].addActionListener(e -> {
            categoriesFunction();
            showPanel("Categories");
        });

        navButtons[2].addActionListener(e -> {
            productsFunction();
            showPanel("Products");
        });

        navButtons[3].addActionListener(e -> {
            kitchenFunction();
            showPanel("Kitchen");
        });

        navButtons[4].addActionListener(e -> {
            reportsFunction();
            showPanel("Reports");
        });

        navButtons[5].addActionListener(e -> {
            staffFunction();
            showPanel("Staff");
            
        });

        navButtons[6].addActionListener(e -> {
            orderFunction();
            showPanel("Order");
        });

        navButtons[7].addActionListener(e -> {
            settingsFunction();
            showPanel("Settings");
        });

    }     

    private void logInSystem() {
        nameWithPassword = new HashMap<>();
        try (Scanner scanner = new Scanner(new File("database\\staff.txt"))) {
            while (scanner.hasNextLine()) {
                String[] columns = scanner.nextLine().split(",");
                nameWithPassword.put(columns[0].trim().toLowerCase(), columns[3].trim());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        String userName = usernameField.getText().strip().toLowerCase();
        final String password = new String(passwordField.getPassword());
        if ("admin".equals(userName) && Restaurant.adminPassword.equals(password)) {
            usersStatus.admin.setValue(true);
            usersStatus.user.setValue(false);
            usersStatus.out.setValue(false);
            JOptionPane.showMessageDialog(null, "Logged in successfully \n Now you are an \"ADMIN\" ", "Log in System", JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText("");
            passwordField.setText("");

        } else if (nameWithPassword.containsKey(userName) && password.equals(nameWithPassword.get(userName))) {   
            usersStatus.admin.setValue(false);
            usersStatus.user.setValue(true);
            usersStatus.out.setValue(false);
            JOptionPane.showMessageDialog(null, "Logged in successfully \n Now you are a \"GENERAL USER\" ", "Log in System", JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText("");
            passwordField.setText("");

        } else if ("".equals(userName)) {
            JOptionPane.showMessageDialog(null, "Username field is empty", "Log in System", JOptionPane.WARNING_MESSAGE);
        } else if ("".equals(password)) {
            JOptionPane.showMessageDialog(null, "Password field is empty", "Log in System", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "unvalid information", "Log in System", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void showPanel(String name) {
        cardLayout.show(contentPanel, name);
    }

    private JPanel showImage() {
        JPanel pn = new JPanel(new BorderLayout());
        pn.add(new JLabel(new ImageIcon(appIcon.getImage())), BorderLayout.CENTER);
        return pn;
    }

    private static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private JPanel homeFunction() {

        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBorder(BorderFactory.createEmptyBorder(120, 410, 140, 410));
        homePanel.setBackground(new Color(200, 200, 200));

        JPanel panel = new JPanel(null);
        panel.setBorder(BorderFactory.createTitledBorder(" LOG IN "));
        panel.setForeground(Color.white);
        panel.setBounds(50, 30, 290, 300);
        panel.setBackground(mainColor);

        JLabel label = new JLabel("You can log in as an ADMIN or a USER");
        JLabel label2 = new JLabel("Username");
        JLabel label4 = new JLabel("Password");
        JLabel label5 = new JLabel("Gender");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        gender = new JComboBox(new String[]{"Male", "Female"});
        JButton logIn = new JButton("Log in");
        JButton logUot = new JButton("Log out");

        label2.setBounds(40, 50, 130, 35);
        usernameField.setBounds(130, 50, 180, 35);

        label4.setBounds(40, 110, 110, 35);
        passwordField.setBounds(130, 110, 180, 35);

        label5.setBounds(40, 170, 110, 25);
        gender.setBounds(130, 170, 180, 35);

        logIn.setBounds(60, 250, 220, 45);
        logUot.setBounds(60, 320, 220, 45);

        label.setBounds(60, 400, 220, 45);

        logIn.setBackground(mainColor);

        logIn.setForeground(Color.WHITE);
        label2.setForeground(Color.WHITE);
        label4.setForeground(Color.WHITE);
        label5.setForeground(Color.WHITE);
        label.setForeground(Color.WHITE);

        panel.add(label2);
        panel.add(label4);
        panel.add(label5);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(gender);
        panel.add(logIn);
        panel.add(logUot);
        panel.add(label);

        homePanel.add(panel, BorderLayout.CENTER);

        logIn.addActionListener(e -> {
            logInSystem();
        });

        logUot.addActionListener(e -> {
            if (!usersStatus.out.isValue()) {
                JOptionPane.showMessageDialog(null, "Logged out successfully ", "Log out System", JOptionPane.INFORMATION_MESSAGE);
            }
            if (usersStatus.out.isValue()) {
                JOptionPane.showMessageDialog(null, "You have not logged in yet ", "Log out System", JOptionPane.INFORMATION_MESSAGE);
            }

            usersStatus.admin.setValue(false);
            usersStatus.user.setValue(false);
            usersStatus.out.setValue(true);
        });

        contentPanel.add(homePanel, "Home");
        return homePanel;
    }

    private JPanel categoriesFunction() {
        JPanel categoriesPanel = new JPanel();
        categoriesPanel.setLayout(null);

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleleteButton = new JButton("Delete");

        addButton.setForeground(Color.WHITE);
        editButton.setForeground(Color.WHITE);
        deleleteButton.setForeground(Color.WHITE);

        addButton.setBounds(55, 20, 100, 50);
        editButton.setBounds(205, 20, 100, 50);
        deleleteButton.setBounds(355, 20, 110, 50);

        addButton.setFont(new Font("Arial", Font.BOLD, 22));
        editButton.setFont(new Font("Arial", Font.BOLD, 22));
        deleleteButton.setFont(new Font("Arial", Font.BOLD, 22));

        addButton.setBackground(mainColor);
        editButton.setBackground(mainColor);
        deleleteButton.setBackground(mainColor);

        categoriesPanel.add(addButton);
        categoriesPanel.add(editButton);
        categoriesPanel.add(deleleteButton);

        
        Vector<String> columnNames = new Vector<>();
        columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add(" Categories Name ");

        categoriesTable = new JTable(Restaurant.rowCategoryData, columnNames);
        categoriesTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JScrollPane scrollPane = new JScrollPane(categoriesTable);
        categoriesTable.setFont(new Font("Arial", 0, 20));
        categoriesTable.setRowHeight(40);
        categoriesTable.setCellSelectionEnabled(false);

        scrollPane.setBounds(50, 95, 1000, 630);

        categoriesPanel.add(scrollPane);

        JTableHeader header = categoriesTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBackground(mainColor);
        header.setForeground(Color.white);

        addButton.addActionListener(e -> {
            addCategoryButton();
        });
        editButton.addActionListener(e -> {
            editCategoryButton();
        });
        deleleteButton.addActionListener(e -> {
            deleteCategoryButton();
        });

        contentPanel.add((usersStatus.out.isValue())? showImage() : categoriesPanel, "Categories");

        return categoriesPanel;
    }

    private JPanel productsFunction() {
        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(null);

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleleteButton = new JButton("Delete");

        addButton.setForeground(Color.WHITE);
        editButton.setForeground(Color.WHITE);
        deleleteButton.setForeground(Color.WHITE);

        addButton.setBounds(55, 20, 100, 50);
        editButton.setBounds(205, 20, 100, 50);
        deleleteButton.setBounds(355, 20, 110, 50);

        addButton.setFont(new Font("Arial", Font.BOLD, 22));
        editButton.setFont(new Font("Arial", Font.BOLD, 22));
        deleleteButton.setFont(new Font("Arial", Font.BOLD, 22));

        addButton.setBackground(mainColor);
        editButton.setBackground(mainColor);
        deleleteButton.setBackground(mainColor);

        productsPanel.add(addButton);
        productsPanel.add(editButton);
        productsPanel.add(deleleteButton);

        
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add(" Product Name ");
        columnNames.add(" Price");
        columnNames.add(" Category ");
        columnNames.add(" IsActive ");

        productsTable = new JTable(Restaurant.rowProductData, columnNames);
        productsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JScrollPane scrollPane = new JScrollPane(productsTable);
        productsTable.setFont(new Font("Arial", 0, 20));
        productsTable.setRowHeight(30);
        scrollPane.setBounds(50, 95, 1000, 630);

        productsPanel.add(scrollPane);

        JTableHeader header = productsTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBackground(mainColor);
        header.setForeground(Color.white);

        addButton.addActionListener(e -> {
            addproductButton();
        });
        editButton.addActionListener(e -> {
            editproductButton();
        });
        deleleteButton.addActionListener(e -> {
            deleteproductButton();
        });

       contentPanel.add((usersStatus.out.isValue()) ? showImage() : productsPanel, "Products");
        return productsPanel;
    }

    private JPanel kitchenFunction() {
        JPanel kitchenPanel = new JPanel(null);

        JPanel ordersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 50));
        ordersPanel.setPreferredSize(new Dimension(850, Restaurant.kitchenOrders.size() / 3 * 300));
        ordersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPanel = new JScrollPane(ordersPanel);
        scrollPanel.setBounds(0, 0, 1150, 720);
        

        JPanel orderPanel;

        JTextArea orderID;
        JTextArea orderCustomer;
        JTextArea orderProducts;

        JPanel footer;
        JButton payCash;
        JButton creditCard;
        JButton cancel;

        for (Order order : Restaurant.kitchenOrders) {
            orderPanel = new JPanel();
            orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
            orderPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            orderPanel.setPreferredSize(new Dimension(300, 200));

            orderID = new JTextArea("  #" + order.getID());
            orderID.setFont(new Font("Arial", Font.BOLD, 20));
            orderID.setBorder(new EmptyBorder(10, 0, 10, 0));

            orderCustomer = new JTextArea("  - Cusomter: " + order.getCustomer());
            orderCustomer.setLineWrap(true);

            orderProducts = new JTextArea();
            String products = "  - Products: \n";
            Iterator iterator = order.getProducts().entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Product product = (Product) entry.getKey();
                products += ("       - " + entry.getValue() + " * " + product.getName());
                products += "\n";
            }

            orderProducts.setText(products);
            orderProducts.setLineWrap(true);
            orderProducts.setWrapStyleWord(false);

            footer = new JPanel();
            footer.setLayout(new GridBagLayout());
            footer.setBackground(Color.white);

            
            payCash = new JButton("Pay cash");
            payCash.setBackground(mainColor);
            payCash.setForeground(Color.WHITE);
            footer.add(payCash);

            creditCard = new JButton("Credit Card");
            creditCard.setBackground(mainColor);
            creditCard.setForeground(Color.WHITE);
            footer.add(creditCard);
            
            cancel = new JButton("Cancel");
            cancel.setBackground(mainColor);
            cancel.setForeground(Color.WHITE);
            footer.add(cancel);
            
            

            payCash.addActionListener(e -> {
                Restaurant.finishingOrder(Order.PayingType.CASH, Order.OrderStatus.DONE, order);
                Restaurant.removeFromKitchen(order);
                JOptionPane.showMessageDialog(null, "Order Done Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                final int ORDER_INDEX = 6;
            
                navButtons[ORDER_INDEX].setForeground(mainColor);
                navButtons[ORDER_INDEX].setBackground(Color.WHITE);
                for (int j = 0; j < navButtons.length; j++) {
                    if (j != ORDER_INDEX) {
                        navButtons[j].setBackground(mainColor);
                        navButtons[j].setForeground(Color.WHITE);
                    }
                }
                orderFunction();
                showPanel("Order");
            });
            
            creditCard.addActionListener(e -> {
                Restaurant.finishingOrder(Order.PayingType.CREDIT_CARD, Order.OrderStatus.DONE, order);
                Restaurant.removeFromKitchen(order);
                JOptionPane.showMessageDialog(null, "Order Done Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                final int ORDER_INDEX = 6;
            
                navButtons[ORDER_INDEX].setForeground(mainColor);
                navButtons[ORDER_INDEX].setBackground(Color.WHITE);
                for (int j = 0; j < navButtons.length; j++) {
                    if (j != ORDER_INDEX) {
                        navButtons[j].setBackground(mainColor);
                        navButtons[j].setForeground(Color.WHITE);
                    }
                }
                orderFunction();
                showPanel("Order");
            });
            
            cancel.addActionListener(e -> {
                Restaurant.finishingOrder(null, Order.OrderStatus.CANCELED,order);
                Restaurant.removeFromKitchen(order);
                JOptionPane.showMessageDialog(null, "Order Canceled.", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                final int ORDER_INDEX = 6;
            
                navButtons[ORDER_INDEX].setForeground(mainColor);
                navButtons[ORDER_INDEX].setBackground(Color.WHITE);
                for (int j = 0; j < navButtons.length; j++) {
                    if (j != ORDER_INDEX) {
                        navButtons[j].setBackground(mainColor);
                        navButtons[j].setForeground(Color.WHITE);
                    }
                }
                orderFunction();
                showPanel("Order");
            });

            orderPanel.add(orderID);
            orderPanel.add(orderCustomer);
            orderPanel.add(orderProducts);
            orderPanel.add(footer);

            ordersPanel.add(orderPanel);
        }
        if (Restaurant.kitchenOrders.isEmpty()) {
            JLabel empty = new JLabel("Kitchen is Empty.");
            empty.setFont(new Font("Arial", 0, 24));
            ordersPanel.add(empty);
        }        
        kitchenPanel.add(scrollPanel);

       contentPanel.add((usersStatus.out.isValue()) ? showImage() : kitchenPanel, "Kitchen");
        return kitchenPanel;

    }

    private JPanel reportsFunction() {
        JPanel reportsPanel = new JPanel(null);
        reportsPanel.setBackground(Color.lightGray);
        
        JPanel main = new JPanel(new FlowLayout(FlowLayout.CENTER,0,25));
        main.setBounds(180, 50, 800, 620); 
        main.setBackground(mainColor);
        
        JButton allOrders = new JButton("All orders");
        allOrders.setBackground(Color.lightGray);
        allOrders.setForeground(mainColor);
        allOrders.setFont(new Font("Arial", Font.BOLD, 37));
        allOrders.setPreferredSize(new Dimension(550, 70));
        main.add(allOrders);
        
        JButton todayOrders = new JButton("All orders for today");
        todayOrders.setBackground(Color.lightGray);
        todayOrders.setForeground(mainColor);
        todayOrders.setFont(new Font("Arial", Font.BOLD, 37));
        todayOrders.setPreferredSize(new Dimension(550, 70));
        main.add(todayOrders);
        
        JButton allCustomers = new JButton("All customers");
        allCustomers.setBackground(Color.lightGray);
        allCustomers.setForeground(mainColor);
        allCustomers.setFont(new Font("Arial", Font.BOLD, 37));
        allCustomers.setPreferredSize(new Dimension(550, 70));
        main.add(allCustomers);
        
        JButton permanentCustomer = new JButton("Permanent customer");
        permanentCustomer.setBackground(Color.lightGray);
        permanentCustomer.setForeground(mainColor);
        permanentCustomer.setFont(new Font("Arial", Font.BOLD, 37));
        permanentCustomer.setPreferredSize(new Dimension(550, 70));
        main.add(permanentCustomer);
        
        JButton requestedMeal = new JButton("The most requested meal");
        requestedMeal.setBackground(Color.lightGray);
        requestedMeal.setForeground(mainColor);
        requestedMeal.setFont(new Font("Arial", Font.BOLD, 37));
        requestedMeal.setPreferredSize(new Dimension(550, 70));
        main.add(requestedMeal);
        
        JButton monthlyProfits = new JButton("Monthly profits");
        monthlyProfits.setBackground(Color.lightGray);
        monthlyProfits.setForeground(mainColor);
        monthlyProfits.setFont(new Font("Arial", Font.BOLD, 37));
        monthlyProfits.setPreferredSize(new Dimension(550, 70));
        main.add(monthlyProfits);
        
        
       
        JScrollPane ordersReportPanel = new JScrollPane(getOrdersReportsTable());
        ordersReportPanel.setBounds(0, 0, 600, 300);
        
        JScrollPane todayOrdersReportPanel = new JScrollPane(getTodayOrdersReportsTable());
        todayOrdersReportPanel.setBounds(0, 0, 600, 300);
        
        JScrollPane customersReportPanel = new JScrollPane(getAllCustomersTable());
        customersReportPanel.setBounds(0, 0, 600, 300);
        
        JScrollPane PermanentCustomerReportPanel = new JScrollPane(getPermanentCustomerTable());
        PermanentCustomerReportPanel.setBounds(0, 0, 600, 300);
        
        JScrollPane mostRequestMealPanel = new JScrollPane(getMostRequestedMealTable());
        mostRequestMealPanel.setBounds(0, 0, 600, 300);
        
        JScrollPane monthlyProfitsPanel = new JScrollPane(getMonthlyProfitsTable());      
        monthlyProfitsPanel.setBounds(0, 0, 600, 300);
        
        
        
        
        allOrders.addActionListener(e -> {
             Dialog = new JDialog(new JFrame(), "All Orders", true);
             Dialog.setBounds(430, 300, 600, 300);
             Dialog.add(ordersReportPanel);
             Dialog.setVisible(true);
        });
        
        todayOrders.addActionListener(e -> {
             Dialog = new JDialog(new JFrame(), "All orders for today", true);
             Dialog.setBounds(430, 300, 600, 300);
             Dialog.add(todayOrdersReportPanel);
             Dialog.setVisible(true);
        });
        
        allCustomers.addActionListener(e -> {
             Dialog = new JDialog(new JFrame(), "All Customers", true);
             Dialog.setBounds(430, 300, 600, 300);
             Dialog.add(customersReportPanel);
             Dialog.setVisible(true);
        });
        
        permanentCustomer.addActionListener(e -> {
             Dialog = new JDialog(new JFrame(), "Permanent Customer", true);
             Dialog.setBounds(430, 300, 600, 300);
             Dialog.add(PermanentCustomerReportPanel);
             Dialog.setVisible(true);
        });
        
        requestedMeal.addActionListener(e -> {
             Dialog = new JDialog(new JFrame(), "The most requested meal", true);
             Dialog.setBounds(430, 300, 600, 300);
             Dialog.add(mostRequestMealPanel);
             Dialog.setVisible(true);
        });
        
        monthlyProfits.addActionListener(e -> {
             Dialog = new JDialog(new JFrame(), "Monthly Profits", true);
             Dialog.setBounds(430, 300, 600, 300);
             Dialog.add(monthlyProfitsPanel);
             Dialog.setVisible(true);
        });
        
        reportsPanel.add(main);
        contentPanel.add((usersStatus.admin.isValue()) ? reportsPanel :  showImage(), "Reports");
        return reportsPanel;
    }

    private JPanel staffFunction() {
        JPanel staffPanel = new JPanel(null);

        JButton categories = new JButton("Categories");
        categories.setBounds(380, 10, 180, 60);
        categories.setBackground(mainColor);
        categories.setForeground(Color.WHITE);
        categories.setFont(new Font("Arial", Font.BOLD, 20));
        staffPanel.add(categories);

        JButton employees = new JButton("Employees");
        employees.setBounds(570, 10, 180, 60);
        employees.setBackground(mainColor);
        employees.setForeground(Color.WHITE);
        employees.setFont(new Font("Arial", Font.BOLD, 20));
        staffPanel.add(employees);

        cardLayoutSttuf = new CardLayout();
        JPanel all = new JPanel(cardLayoutSttuf);
        all.setBounds(80, 100, 1000, 650);

        JPanel employeesPanel = new JPanel(null);      
        employeesPanel.setBackground(mainColor);
        employeesPanel.setBounds(0, 0, 500, 500);

        JButton addButton = new JButton(" Add ");
        JButton editButton = new JButton(" Edit ");
        JButton deleleteButton = new JButton("Delete");

        addButton.setForeground(Color.BLACK);
        editButton.setForeground(Color.BLACK);
        deleleteButton.setForeground(Color.BLACK);

        addButton.setForeground(Color.BLACK);
        editButton.setForeground(Color.BLACK);
        deleleteButton.setForeground(Color.BLACK);

        addButton.setBounds(255, 5, 160, 30);
        editButton.setBounds(420, 5, 160, 30);
        deleleteButton.setBounds(585, 5, 160, 30);

        addButton.setFont(new Font("Arial", Font.BOLD, 22));
        editButton.setFont(new Font("Arial", Font.BOLD, 22));
        deleleteButton.setFont(new Font("Arial", Font.BOLD, 22));

        addButton.setBackground(Color.WHITE);
        editButton.setBackground(Color.WHITE);
        deleleteButton.setBackground(Color.WHITE);

        employeesPanel.add(addButton);
        employeesPanel.add(editButton);
        employeesPanel.add(deleleteButton);

        Vector<String> staffEmployeeTabalHead = new Vector<>();
        staffEmployeeTabalHead.add("SN#");
        staffEmployeeTabalHead.add(" Name ");
        staffEmployeeTabalHead.add(" category");
        staffEmployeeTabalHead.add(" salary");
        staffEmployeeTabalHead.add(" Password");

        tableEmployee = new JTable(Restaurant.staffEmployeeInformation, staffEmployeeTabalHead);
        JScrollPane scrollPane = new JScrollPane(tableEmployee);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        tableEmployee.setFont(new Font("Arial", 0, 20));
        tableEmployee.setRowHeight(30);
        scrollPane.setBounds(50, 95, 900, 630);
        tableEmployee.setBackground(Color.orange);

        employeesPanel.add(scrollPane);

        JTableHeader header = tableEmployee.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBackground(mainColor);
        header.setForeground(Color.white);

        addButton.addActionListener(e -> {
            addEmployeeButton();
        });
        editButton.addActionListener(e -> {
            editEmployeeButton();
        });
        deleleteButton.addActionListener(e -> {
            deleteEmployeeButton();
        });

        
        JPanel categoriesPanel = new JPanel(null);      
        categoriesPanel.setBackground(mainColor);
        categoriesPanel.setBounds(0, 0, 500, 500);

        JButton addButton1 = new JButton(" Add ");
        JButton editButton1 = new JButton(" Edit ");
        JButton deleleteButton1 = new JButton("Delete");

        addButton1.setForeground(Color.BLACK);
        editButton1.setForeground(Color.BLACK);
        deleleteButton1.setForeground(Color.BLACK);

        addButton1.setBounds(255, 5, 160, 30);
        editButton1.setBounds(420, 5, 160, 30);
        deleleteButton1.setBounds(585, 5, 160, 30);

        addButton1.setFont(new Font("Arial", Font.BOLD, 22));
        editButton1.setFont(new Font("Arial", Font.BOLD, 22));
        deleleteButton1.setFont(new Font("Arial", Font.BOLD, 22));

        addButton1.setBackground(Color.WHITE);
        editButton1.setBackground(Color.WHITE);
        deleleteButton1.setBackground(Color.WHITE);

        categoriesPanel.add(addButton1);
        categoriesPanel.add(editButton1);
        categoriesPanel.add(deleleteButton1);

        Vector<String> staffCategoriesTabalHead = new Vector<>();
        staffCategoriesTabalHead.add("SN#");
        staffCategoriesTabalHead.add(" Name ");

        CategoryEmploy = new JTable(Restaurant.staffCategoriesInformation, staffCategoriesTabalHead);
        JScrollPane scrollPane1 = new JScrollPane(CategoryEmploy);
        scrollPane1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        CategoryEmploy.setFont(new Font("Arial", 0, 20));
        CategoryEmploy.setRowHeight(30);
        scrollPane1.setBounds(50, 95, 900, 630);
        CategoryEmploy.setSize(300, 200);
        CategoryEmploy.setBackground(Color.orange);

        JTableHeader header1 = CategoryEmploy.getTableHeader();
        header1.setFont(new Font("Arial", Font.BOLD, 24));
        header1.setBackground(mainColor);
        header1.setForeground(Color.white);

        categoriesPanel.add(scrollPane1);

        addButton1.addActionListener(e -> {
            addCategoryEmployeeButton();
        });
        editButton1.addActionListener(e -> {
            editCategoryEmployeeButton();
        });
        deleleteButton1.addActionListener(e -> {
            deleteCategoryEmployeeButton();
        });

        all.add(categoriesPanel, "categories");
        all.add(employeesPanel, "employees");
        staffPanel.add(all);

        categories.addActionListener(e -> {
            cardLayoutSttuf.show(all, "categories");
        });

        employees.addActionListener(e -> {
            cardLayoutSttuf.show(all, "employees");
        });

        contentPanel.add((usersStatus.admin.isValue()) ? staffPanel : showImage(), "Staff");

        return staffPanel;
    }

    private JPanel orderFunction() {

        JPanel orderPanel = new JPanel(null);
        orderPanel.setBackground(mainColor);

        JPanel contantPanel = new JPanel(null);
        contantPanel.setBounds(0, 0, 850, MAX_VALUE);
        orderPanel.add(contantPanel);

        Restaurant.currentOrder = new Order();
        
        JButton Deliver = new JButton("Delivery");
        Deliver.setBounds(20, 0, 120, 50);
        Deliver.setBackground(mainColor);
        Deliver.setForeground(Color.WHITE);
        Deliver.setEnabled(false);
        contantPanel.add(Deliver);

        JButton OnTable = new JButton("On Table");
        OnTable.setBounds(150, 0, 120, 50);
        OnTable.setBackground(mainColor);
        OnTable.setForeground(Color.WHITE);
        OnTable.setEnabled(false);
        contantPanel.add(OnTable);

        JButton takeAway = new JButton(" Take Away ");
        takeAway.setBounds(280, 0, 120, 50);
        takeAway.setBackground(mainColor);
        takeAway.setForeground(Color.WHITE);
        takeAway.setEnabled(false);
        contantPanel.add(takeAway);

        JButton print = new JButton("Print");
        print.setBounds(410, 0, 120, 50);
        print.setBackground(mainColor);
        print.setForeground(Color.WHITE);
        print.setEnabled(false);
        contantPanel.add(print);
        
        JLabel customerLabel = new JLabel("Cusomter: ");
        customerLabel.setBounds(540, 10, 100, 50);
        customerLabel.setBackground(mainColor);
        customerLabel.setForeground(Color.WHITE);
        contantPanel.add(customerLabel);
        
        JTextField customer = new JTextField();
        customer.setBounds(620, 20, 200, 30);
        contantPanel.add(customer);
       
        
        takeAway.addActionListener(e -> {
            Restaurant.currentOrder.setType(Order.OrderType.TAKE_AWAY);
        });

        JPanel billPanel = new JPanel(null);
        billPanel.setBounds(855, 0, 350, MAX_VALUE);
        billPanel.setBackground(new Color(100, 100, 100));
        orderPanel.add(billPanel);

        Vector<String> orderTableHeader = new Vector<>();
        orderTableHeader.add("Product");
        orderTableHeader.add("Quantity");
        orderTableHeader.add("Price");

        orderTableRows = new Vector<>();
        if (Restaurant.currentOrder.getProducts().isEmpty()) {
            Iterator iterator = Restaurant.currentOrder.getProducts().entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Product product = (Product) entry.getKey();
                int Quantity = (int) entry.getValue();
                Vector<String> newProduct = new Vector();

                newProduct.add(product.getName());
                newProduct.add(Integer.toString(Quantity));
                newProduct.add(Double.toString(product.getPrice()));

                orderTableRows.add(newProduct);
            }
        }

        orderTable = new JTable(orderTableRows, orderTableHeader);
        orderTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        orderTable.setFont(new Font("Arial", 0, 20));
        orderTable.setRowHeight(40);
        orderTable.setCellSelectionEnabled(false);

        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBounds(2,10 , 300, 400);

        billPanel.add(scrollPane);

        JTableHeader header = orderTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBackground(mainColor);
        header.setForeground(Color.white);

        JButton cash = new JButton("Pay cash");
        cash.setBounds(20, 550, 220, 40);
        cash.setBackground(mainColor);
        cash.setForeground(Color.WHITE);
        cash.setEnabled(false);
        billPanel.add(cash);

        JButton creditCard = new JButton("Credit Card");
        creditCard.setBounds(20, 600, 220, 40);
        creditCard.setBackground(mainColor);
        creditCard.setForeground(Color.WHITE);
        creditCard.setEnabled(false);
        billPanel.add(creditCard);

        JButton free = new JButton("For Free");
        free.setBounds(20, 650, 220, 40);
        free.setBackground(mainColor);
        free.setForeground(Color.WHITE);
        free.setEnabled(false);
        billPanel.add(free);
        
        JButton clear = new JButton("Clear");
        clear.setBounds(20, 500, 220 , 40);
        clear.setBackground(Color.white);
        clear.setForeground(mainColor);
        billPanel.add(clear);
        
        clear.addActionListener(e-> {
            this.clearOrderTable();
            customer.setText("");
            Restaurant.currentOrder = new Order();
            
            free.setEnabled(false);
            creditCard.setEnabled(false);
            cash.setEnabled(false);
            
            Deliver.setEnabled(false);
            OnTable.setEnabled(false);
            takeAway.setEnabled(false);
            print.setEnabled(false);
        });
        
        Deliver.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Your order being proccessed in the kitchen", "Success", JOptionPane.INFORMATION_MESSAGE);
            Restaurant.currentOrder.setType(Order.OrderType.DELIVERY);
            Restaurant.addOrderToKitchen(Restaurant.currentOrder);
            this.clearOrderTable();
            customer.setText("");
            Restaurant.currentOrder = new Order();
            
            free.setEnabled(false);
            creditCard.setEnabled(false);
            cash.setEnabled(false);
            
            Deliver.setEnabled(false);
            OnTable.setEnabled(false);
            takeAway.setEnabled(false);
            print.setEnabled(false);
        });
        
        OnTable.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Your order being proccessed in the kitchen", "Success", JOptionPane.INFORMATION_MESSAGE);
            Restaurant.currentOrder.setType(Order.OrderType.ON_TABLE);
            Restaurant.addOrderToKitchen(Restaurant.currentOrder);
            this.clearOrderTable();
            customer.setText("");
            Restaurant.currentOrder = new Order();
            
            free.setEnabled(false);
            creditCard.setEnabled(false);
            cash.setEnabled(false);
            
            Deliver.setEnabled(false);
            OnTable.setEnabled(false);
            takeAway.setEnabled(false);
            print.setEnabled(false);
        }); 
        
        takeAway.addActionListener(e -> {
            cash.setEnabled(true);
            creditCard.setEnabled(true);
            free.setEnabled(true);
        });


        cash.addActionListener(e -> {
            Restaurant.finishingOrder(Order.PayingType.CASH, Order.OrderStatus.DONE,Restaurant.currentOrder);
            clearOrderTable();
            
            customer.setText("");
            Restaurant.currentOrder = new Order();
            
            free.setEnabled(false);
            creditCard.setEnabled(false);
            cash.setEnabled(false);
            
            Deliver.setEnabled(false);
            OnTable.setEnabled(false);
            takeAway.setEnabled(false);
            print.setEnabled(false);
        });
        
        creditCard.addActionListener(e -> {
            Restaurant.finishingOrder(Order.PayingType.CREDIT_CARD, Order.OrderStatus.DONE, Restaurant.currentOrder);
            clearOrderTable();
            
            customer.setText("");
            Restaurant.currentOrder = new Order();
            
            free.setEnabled(false);
            creditCard.setEnabled(false);
            cash.setEnabled(false);
            
            Deliver.setEnabled(false);
            OnTable.setEnabled(false);
            takeAway.setEnabled(false);
            print.setEnabled(false);
        });
        
        free.addActionListener(e -> {
            Restaurant.finishingOrder(Order.PayingType.FREE, Order.OrderStatus.DONE, Restaurant.currentOrder);
            clearOrderTable();
            
            customer.setText("");
            Restaurant.currentOrder = new Order();
            
            free.setEnabled(false);
            creditCard.setEnabled(false);
            cash.setEnabled(false);
            
            Deliver.setEnabled(false);
            OnTable.setEnabled(false);
            takeAway.setEnabled(false);
            print.setEnabled(false);
        });
        
        JTextPane price = new JTextPane();
        price.setText("Price: 00.0$");
        price.setFont(new Font("Arial", Font.BOLD, 20));
        price.setBounds(20, 420, 220, 40);
        price.setForeground(Color.white);
        price.setBackground(mainColor);
        billPanel.add(price);
        
        customer.addKeyListener(new KeyListener(){
                @Override
                public void keyPressed(KeyEvent e) {
                    
                }

                @Override
                public void keyTyped(KeyEvent e) {
                    
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    String customerName = customer.getText().trim();
                    if (customerName.isEmpty()) {
                        print.setEnabled(false);
                        takeAway.setEnabled(false);
                        OnTable.setEnabled(false);
                        Deliver.setEnabled(false);
                        
                        cash.setEnabled(false);
                        free.setEnabled(false);
                        creditCard.setEnabled(false);                        
                    }
                    Restaurant.currentOrder.setCustomer(customerName);
                    
                    if (Restaurant.currentOrder.isValid()) {
                        print.setEnabled(true);
                        takeAway.setEnabled(true);
                        OnTable.setEnabled(true);
                        Deliver.setEnabled(true);

                    }
                }
        });
    
        JPanel ProductsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        ProductsPanel.setPreferredSize(new Dimension(800, Restaurant.products.size() / 4 * 300));
        
        JScrollPane ProductsScrollPanel = new JScrollPane(ProductsPanel);
        ProductsScrollPanel.setBounds(0, 60, 850, 650);
        JPanel productElement;
        JTextArea productName;
        JTextArea productIngredients;
        JTextArea productPrice;
        JButton plus;
        JButton minus;

        JPanel footer;

        int numProducts = 0;
        for (Product product : Restaurant.products) {

            if (!product.isActive()) {
                continue;
            }
            numProducts++;
            productElement = new JPanel();
            productElement.setLayout(new BoxLayout(productElement, BoxLayout.Y_AXIS));
            productElement.setPreferredSize(new Dimension(200, 200));
            productElement.setBackground(Color.WHITE);

            productName = new JTextArea("   " + (String) product.getName());
            productName.setForeground(Color.BLUE);
            productName.setFont(new Font("Arial", 0, 20));
            productName.setBorder(new EmptyBorder(10, 5, 10, 0));
            

            productIngredients = new JTextArea("Ingredients: " + product.getIngredients());
            productIngredients.setLineWrap(true);
            productIngredients.setWrapStyleWord(false);
            productIngredients.setFont(new Font("Arial", 0, 18));
            productIngredients.setBorder(new EmptyBorder(5, 5, 0, 0));

           
            productPrice = new JTextArea("Price: " + Double.toString(product.getPrice()));
            productPrice.setFont(new Font("Arial", 0, 18));
            productPrice.setBorder(new EmptyBorder(5, 5, 10, 0));

            productElement.add(productName);
            productElement.add(productPrice);
            productElement.add(productIngredients);
            productElement.add(Box.createVerticalStrut(15));
            productElement.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            plus = new JButton("+");
            minus = new JButton("-");

            footer = new JPanel();
            footer.setBackground(Color.white);
            footer.add(minus);
            footer.add(plus);

            plus.setFont(new Font("Arial", 0, 18));
            minus.setFont(new Font("Arial", 0, 18));
            plus.setBackground(Color.black);
            minus.setBackground(Color.black);

            plus.setForeground(Color.white);
            minus.setForeground(Color.white);

            plus.addActionListener(e -> {
                addProductToOrderTable(product, orderTable, orderTableRows);
                price.setText("Price: " + Restaurant.currentOrder.getTotalPrice() + "$");
                
                if (Restaurant.currentOrder.isValid()) {
                    print.setEnabled(true);
                    takeAway.setEnabled(true);
                    OnTable.setEnabled(true);
                    Deliver.setEnabled(true);
                }
                
            });

            minus.addActionListener(e -> {
                removeProductFromOrderTable(product, orderTable, orderTableRows);
                price.setText("Price: " + Restaurant.currentOrder.getTotalPrice() + "$");
                if (!Restaurant.currentOrder.isValid()) {
                    
                    print.setEnabled(false);
                    takeAway.setEnabled(false);
                    OnTable.setEnabled(false);
                    Deliver.setEnabled(false);
                    
                    cash.setEnabled(false);
                    creditCard.setEnabled(false);
                    free.setEnabled(false);
                }
            });

            productElement.add(footer);
            productElement.setBorder(new EmptyBorder(10, 10, 10, 10));
            ProductsPanel.add(productElement);

        }
        contantPanel.add(ProductsScrollPanel);

       contentPanel.add((usersStatus.out.isValue()) ? showImage() : orderPanel, "Order");
        return orderPanel;

    }

    private JPanel settingsFunction() {
        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 35));
        settingsPanel.setBackground(Color.lightGray);

        JButton using = new JButton("How  to  use  the  application");
        using.setBackground(mainColor);
        using.setForeground(Color.white);
        using.setFont(new Font("Arial", Font.BOLD, 44));
        using.setPreferredSize(new Dimension(1170, 90));
        settingsPanel.add(using);

        using.addActionListener(e -> {
            Dialog = new JDialog(new JFrame(), "How  to  use  the  application", true);
            Dialog.setBounds(230, 100, 850, 500);
            Dialog.setLayout(new FlowLayout(FlowLayout.LEADING));
            
            JLabel cont = new JLabel(WayOfUsing.str);
            cont.setFont(new Font("Arial", Font.BOLD, 20));
           
            Dialog.add(cont);
            Dialog.setVisible(true);
        });

        JPanel storeDetails = new JPanel(null);
        storeDetails.setPreferredSize(new Dimension(1000, 350));
        storeDetails.setBackground(Color.lightGray);
        storeDetails.setBorder(BorderFactory.createLineBorder(mainColor, 5));

        JLabel st = new JLabel(" RESTAURANT DETAILS");
        st.setBackground(Color.GREEN);
        st.setForeground(mainColor);
        st.setBorder(BorderFactory.createLineBorder(mainColor, 10));
        st.setFont(new Font("Arial", Font.BOLD, 20));
        st.setBounds(350, 0, 255, 70);
        storeDetails.add(st);

        JLabel name = new JLabel("Restaurant name : ");
        name.setForeground(mainColor);
        name.setFont(new Font("Arial", Font.BOLD, 20));
        name.setBounds(10, 100, 200, 70);
        storeDetails.add(name);

        JLabel address = new JLabel("Restaurant address : ");
        address.setForeground(mainColor);
        address.setFont(new Font("Arial", Font.BOLD, 20));
        address.setBounds(10, 150, 230, 70);
        storeDetails.add(address);

        JLabel phone = new JLabel("Restaurant phone : ");
        phone.setForeground(mainColor);
        phone.setFont(new Font("Arial", Font.BOLD, 20));
        phone.setBounds(10, 200, 200, 70);
        storeDetails.add(phone);

        JLabel email = new JLabel("Restaurant email : ");
        email.setForeground(mainColor);
        email.setFont(new Font("Arial", Font.BOLD, 20));
        email.setBounds(10, 250, 200, 70);
        storeDetails.add(email);

        JLabel name0 = new JLabel();
        name0.setForeground(Color.BLACK);
        name0.setFont(new Font("Arial", Font.BOLD, 20));
        name0.setBounds(280, 100, 200, 70);
        storeDetails.add(name0);

        JLabel address0 = new JLabel();
        address0.setForeground(Color.BLACK);
        address0.setFont(new Font("Arial", Font.BOLD, 20));
        address0.setBounds(280, 150, 230, 70);
        storeDetails.add(address0);

        JLabel phone0 = new JLabel();
        phone0.setForeground(Color.BLACK);
        phone0.setFont(new Font("Arial", Font.BOLD, 20));
        phone0.setBounds(280, 200, 200, 70);
        storeDetails.add(phone0);

        JLabel email0 = new JLabel();
        email0.setForeground(Color.BLACK);
        email0.setFont(new Font("Arial", Font.BOLD, 20));
        email0.setBounds(280, 250, 300, 70);
        storeDetails.add(email0);

        JButton set = new JButton("Set Details");
        set.setBackground(mainColor);
        set.setForeground(Color.lightGray);
        set.setFont(new Font("Arial", Font.BOLD, 24));
        set.setBounds(780, 250, 200, 70);
        storeDetails.add(set);

        name0.setText(Restaurant.details[0]);
        address0.setText(Restaurant.details[1]);
        phone0.setText(Restaurant.details[2]);
        email0.setText(Restaurant.details[3]);

        set.addActionListener(e -> {

            Dialog = new JDialog(new JFrame(), "Set Details", true);
            Dialog.setLayout(null);

            JLabel name1 = new JLabel("Enter the Restaurant name : ");
            name1.setBounds(40, 0, 200, 50);
            Dialog.add(name1);

            JTextField newP = new JTextField();
            newP.setBounds(40, 40, 300, 30);
            Dialog.add(newP);

            JLabel productPrice = new JLabel("Enter the Restaurant address : ");
            productPrice.setBounds(40, 80, 200, 50);
            Dialog.add(productPrice);

            JTextField ad = new JTextField();
            ad.setBounds(40, 120, 300, 30);
            Dialog.add(ad);

            JLabel ph = new JLabel("Enter the Restaurant phone : ");
            ph.setBounds(40, 160, 200, 50);
            Dialog.add(ph);

            JTextField cont = new JTextField();
            cont.setBounds(40, 200, 300, 30);
            Dialog.add(cont);

            JLabel em = new JLabel("Enter the Restaurant email : ");
            em.setBounds(40, 240, 200, 50);
            Dialog.add(em);

            JTextField con = new JTextField();
            con.setBounds(40, 280, 300, 30);
            Dialog.add(con);

            JButton saveButtn = new JButton("Save");
            saveButtn.setBounds(280, 320, 80, 30);
            Dialog.add(saveButtn);

            saveButtn.addActionListener(ee -> {
                String n = newP.getText().trim();
                String a = ad.getText().trim();
                String p = cont.getText().trim();
                String eee = con.getText().trim();
                if ("".equals(n) || "".equals(a) || "".equals(p) || "".equals(eee)) {
                    JOptionPane.showMessageDialog(null, "There is missing information", "Details System", JOptionPane.WARNING_MESSAGE);
                } else {
                    Restaurant.uploadDetails(n, a, p, eee);
                    Restaurant.loadDetails();
                    JOptionPane.showMessageDialog(null, "Done successfully", "Details System", JOptionPane.INFORMATION_MESSAGE);
                    name0.setText(n);
                    address0.setText(a);
                    phone0.setText(p);
                    email0.setText(eee);
                Dialog.dispose();

                }
            });

            Dialog.setBounds(430, 200, 400, 420);
            Dialog.setVisible(true);
        });
        settingsPanel.add(storeDetails);

        
        JPanel pass = new JPanel(null);
        pass.setPreferredSize(new Dimension(1000, 150));
        pass.setBackground(Color.lightGray);
        pass.setBorder(BorderFactory.createLineBorder(mainColor, 5));

        JButton chPass = new JButton("Change password");
        chPass.setBackground(mainColor);
        chPass.setForeground(Color.lightGray);
        chPass.setFont(new Font("Arial", Font.BOLD, 34));
        chPass.setBounds(10, 30, 350, 80);
        pass.add(chPass);

        JLabel a = new JLabel("old apssword");
        a.setBounds(460, 20, 200, 50);
        pass.add(a);

        JTextField aa = new JTextField();
        aa.setBounds(400, 60, 250, 30);
        pass.add(aa);

        JLabel b = new JLabel("new password");
        b.setBounds(760, 20, 200, 50);
        pass.add(b);

        JTextField bb = new JTextField();
        bb.setBounds(700, 60, 250, 30);
        pass.add(bb);

        chPass.addActionListener(e -> {
            String old = aa.getText().trim();
            String neww = bb.getText().trim();
            if (old.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Old passwrod field is empty", "Security System", JOptionPane.WARNING_MESSAGE);
            } else if (neww.isEmpty()) {
                JOptionPane.showMessageDialog(null, "new password field is empty", "Security System", JOptionPane.WARNING_MESSAGE);
            } else if (old.equals(Restaurant.adminPassword)) {
                Restaurant.uploadpassword(neww);
                Restaurant.loadpassword();
                JOptionPane.showMessageDialog(null, """
                                                Password changed successfully 
                                                 New password is :  """ + neww, "Security System", JOptionPane.INFORMATION_MESSAGE);
                aa.setText("");
                bb.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Old password is wrong ", "Security System", JOptionPane.WARNING_MESSAGE);
            }
        });

        if (usersStatus.admin.isValue()) {
            settingsPanel.add(pass);
        }

        contentPanel.add(settingsPanel, "Settings");

        return settingsPanel;
    }

    private void addCategoryButton() {
        Dialog = new JDialog(new JFrame(), "add new category", true);
        Dialog.setLayout(null);

        JLabel lab2 = new JLabel("Enter the category name : ");
        lab2.setBounds(40, 30, 200, 50);
        Dialog.add(lab2);

        JTextField newcategory = new JTextField();
        newcategory.setBounds(40, 80, 250, 30);
        Dialog.add(newcategory);

        JButton saveButtn = new JButton("Save");
        saveButtn.setBounds(300, 120, 80, 30);
        Dialog.add(saveButtn);
        saveButtn.addActionListener(e -> {
            String temp = newcategory.getText().trim();
            if (temp.isEmpty()) {
                JOptionPane.showMessageDialog(null, "The field is empty ", "Warning Message", JOptionPane.WARNING_MESSAGE);
            } else if (Restaurant.uploadCategory(temp)) {
                JOptionPane.showMessageDialog(null, "Added successfully", "Save", JOptionPane.INFORMATION_MESSAGE);
                Restaurant.loadCategories();
                categoriesTable.revalidate();
                categoriesTable.repaint();
                Dialog.dispose();
            }
        });

        Dialog.setBounds(430, 300, 400, 200);
        Dialog.setVisible(true);
    }

    private void editCategoryButton() {
        Dialog = new JDialog(new JFrame(), "Edit category", true);
        Dialog.setLayout(null);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, Restaurant.rowCategoryData.size(), 1));
        spinner.setBounds(40, 20, 60, 40);
        spinner.setFont(new Font("Arial", Font.BOLD, 20));
        Dialog.add(spinner);

        JLabel lab1 = new JLabel("Enter the category number");
        lab1.setBounds(110, 20, 250, 50);
        Dialog.add(lab1);

        JTextField newcategory = new JTextField();
        newcategory.setBounds(40, 120, 350, 30);
        Dialog.add(newcategory);

        JLabel lab2 = new JLabel("The New category : ");
        lab2.setBounds(40, 80, 200, 50);
        Dialog.add(lab2);

        JButton saveButtn = new JButton("Save");
        saveButtn.setBounds(370, 200, 80, 30);
        Dialog.add(saveButtn);
        saveButtn.addActionListener(e -> {
            String temp = newcategory.getText().strip();
            int num = (int) spinner.getValue();
            if (temp.isEmpty()) {
                JOptionPane.showMessageDialog(null, "The field is empty", "Warning Message", JOptionPane.WARNING_MESSAGE);
            } else {
                Restaurant.edit(num, "database\\categories.txt", temp);
                Restaurant.loadCategories();
                categoriesTable.revalidate();
                categoriesTable.repaint();
                Dialog.dispose();
            }
        });

        Dialog.setBounds(430, 300, 500, 300);
        Dialog.setVisible(true);

    }

    private void deleteCategoryButton() {
        Dialog = new JDialog(new JFrame(), "Delet category", true);
        Dialog.setLayout(null);

        JLabel lab2 = new JLabel("Enter the category number ");
        lab2.setBounds(40, 20, 200, 50);
        Dialog.add(lab2);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, Restaurant.rowCategoryData.size(), 1));
        spinner.setBounds(250, 20, 60, 40);
        spinner.setFont(new Font("Arial", Font.BOLD, 25));
        Dialog.add(spinner);

        JButton delButtn = new JButton("Delete");
        delButtn.setBounds(220, 100, 80, 30);
        Dialog.add(delButtn);
        delButtn.addActionListener(e -> {
            int num = (int) spinner.getValue();
            Restaurant.delete(num, "database\\categories.txt");
            categoriesTable.revalidate();
            categoriesTable.repaint();
            Restaurant.loadCategories();
            Dialog.dispose();
        });

        Dialog.setBounds(460, 300, 350, 200);
        Dialog.setVisible(true);

    }

    private void addproductButton() {
        Dialog = new JDialog(new JFrame(), "Add new product", true);
        Dialog.setLayout(null);

        JLabel name = new JLabel("Enter the product name : ");
        name.setBounds(40, 0, 200, 50);
        Dialog.add(name);

        JTextField newProduct = new JTextField();
        newProduct.setBounds(40, 40, 300, 30);
        Dialog.add(newProduct);

        JLabel productPrice = new JLabel("Enter the product price : ");
        productPrice.setBounds(40, 80, 200, 50);
        Dialog.add(productPrice);

        JTextField price = new JTextField();
        price.setBounds(40, 120, 300, 30);
        Dialog.add(price);

        JLabel productontents = new JLabel("Enter the product contents : ");
        productontents.setBounds(40, 160, 200, 50);
        Dialog.add(productontents);

        JTextField contents = new JTextField();
        contents.setBounds(40, 200, 300, 30);
        Dialog.add(contents);

        JLabel category = new JLabel("Choese category : ");
        category.setBounds(40, 230, 200, 50);
        Dialog.add(category);

        String a[] = new String[Restaurant.rowCategoryData.size()];
        for (int i = 0; i < Restaurant.rowCategoryData.size(); i++) {
            a[i] = (String) Restaurant.rowCategoryData.get(i).get(1);
        }

        JComboBox comboBox = new JComboBox(a);
        comboBox.setBounds(40, 270, 140, 25);
        Dialog.add(comboBox);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(280, 310, 80, 30);
        Dialog.add(saveButton);

        saveButton.addActionListener(e -> {
            String namee = newProduct.getText().trim();
            String content = contents.getText().trim();
            Category categoty = new Category((String) (comboBox.getSelectedItem()));
            boolean test = !(isDouble(price.getText().trim()));

            if (namee.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are missing information", "Warning Message", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (test) {
                JOptionPane.showMessageDialog(null, "\"unvalid price\" ", "Warning Message", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double pricee = Double.parseDouble(price.getText().trim());

            if (Restaurant.uploadProdact(new Product(namee, content, categoty, pricee, true))) {
                productsTable.revalidate();
                productsTable.repaint();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Restaurant.loadProducts();
                Dialog.dispose();
            }
        });

        Dialog.setBounds(430, 200, 400, 400);
        Dialog.setVisible(true);
    }

    private void editproductButton() {
        Dialog = new JDialog(new JFrame(), "Edit product", true);
        Dialog.setLayout(null);

        JLabel lab2 = new JLabel("Enter the product number");
        lab2.setFont(new Font("Arial", Font.BOLD, 17));
        lab2.setBackground(mainColor);
        lab2.setBounds(50, 0, 250, 50);
        Dialog.add(lab2);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, Restaurant.rowProductData.size(), 1));
        spinner.setBounds(275, 0, 60, 40);
        spinner.setFont(new Font("Arial", Font.BOLD, 20));
        Dialog.add(spinner);

        JLabel productName = new JLabel("Enter the product name : ");
        productName.setBounds(40, 60, 200, 50);
        Dialog.add(productName);

        JTextField newProduct = new JTextField();
        newProduct.setBounds(40, 100, 300, 30);
        Dialog.add(newProduct);

        JLabel productPrice = new JLabel("Enter the product price : ");
        productPrice.setBounds(40, 140, 200, 50);
        Dialog.add(productPrice);

        JTextField newPrice = new JTextField();
        newPrice.setBounds(40, 180, 300, 30);
        Dialog.add(newPrice);

        JLabel productontents = new JLabel("Enter the product ingredients : ");
        productontents.setBounds(40, 220, 200, 50);
        Dialog.add(productontents);

        JTextField contents = new JTextField();
        contents.setBounds(40, 260, 300, 30);
        Dialog.add(contents);

        JLabel category = new JLabel("Choese category : ");
        category.setBounds(40, 290, 200, 50);
        Dialog.add(category);

        String a[] = new String[Restaurant.rowCategoryData.size()];
        for (int i = 0; i < Restaurant.rowCategoryData.size(); i++) {
            a[i] = (String) Restaurant.rowCategoryData.get(i).get(1);
        }

        JComboBox comboBox = new JComboBox(a);
        comboBox.setBounds(40, 330, 140, 25);
        Dialog.add(comboBox);

        JButton refresh = new JButton("Refresh");
        refresh.setBounds(280, 370, 80, 30);
        Dialog.add(refresh);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(280, 410, 80, 30);
        Dialog.add(saveButton);

        JRadioButton active = new JRadioButton("Active", true);
        JRadioButton nonActive = new JRadioButton("Not Active");

        ButtonGroup group = new ButtonGroup();
        group.add(active);
        group.add(nonActive);

        refresh.addActionListener(ee -> {
            int n = (int) spinner.getValue() - 1;
            newProduct.setText(Restaurant.products.get(n).getName());
            newPrice.setText(Double.toString(Restaurant.products.get(n).getPrice()));
            contents.setText(Restaurant.products.get(n).getIngredients());
            comboBox.setSelectedItem(Restaurant.products.get(n).getCategory().getName());
            comboBox.revalidate();
        });
        saveButton.addActionListener((ActionEvent e) -> {
            int n = (int) spinner.getValue();
            String mm = newProduct.getText().trim();
            String price = newPrice.getText().trim();
            String ll = contents.getText().trim();
            String hh = (String) comboBox.getSelectedItem();
            
            boolean test = !(isDouble(newPrice.getText().trim()));

            if (mm.isEmpty() || ll.isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are missing information", "Warning Message", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (test) {
                JOptionPane.showMessageDialog(null, "\"unvalid price\" ", "Warning Message", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double nn = Double.parseDouble(price);

            Restaurant.edit(n, "database\\products.txt", mm, ll, new Category(hh), nn, (active.isSelected()) ? true : false);
            Restaurant.loadProducts();
            productsTable.revalidate();
            productsTable.repaint();
            Restaurant.loadProducts();
            Dialog.dispose();
        });

        active.setBounds(40, 380, 80, 50);
        Dialog.add(active);
        nonActive.setBounds(120, 380, 100, 50);
        Dialog.add(nonActive);

        Dialog.setBounds(430, 200, 400, 500);
        Dialog.setVisible(true);

    }

    private void deleteproductButton() {
        Dialog = new JDialog(new JFrame(), "Delet product", true);
        Dialog.setLayout(null);

        JLabel lab2 = new JLabel("Enter the category number ");
        lab2.setBounds(40, 20, 200, 50);
        Dialog.add(lab2);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, Restaurant.rowProductData.size(), 1));
        spinner.setBounds(250, 20, 60, 40);
        spinner.setFont(new Font("Arial", Font.BOLD, 25));
        Dialog.add(spinner);

        JButton delButtn = new JButton("Delete");
        delButtn.setBounds(220, 100, 80, 30);
        Dialog.add(delButtn);
        delButtn.addActionListener(e -> {
            int num = (int) spinner.getValue();
            Restaurant.delete(num, "database\\products.txt");
            productsTable.revalidate();
            productsTable.repaint();
            Restaurant.loadProducts();

            Dialog.dispose();
        });

        Dialog.setBounds(460, 300, 350, 200);
        Dialog.setVisible(true);
    }

    
    private void addCategoryEmployeeButton() {
        Dialog = new JDialog(new JFrame(), "add new category", true);
        Dialog.setLayout(null);

        JLabel lab2 = new JLabel("Enter the category name : ");
        lab2.setBounds(40, 30, 200, 50);
        Dialog.add(lab2);

        JTextField newcategory = new JTextField();
        newcategory.setBounds(40, 80, 250, 30);
        Dialog.add(newcategory);

        JButton saveButtn = new JButton("Save");
        saveButtn.setBounds(300, 120, 80, 30);
        Dialog.add(saveButtn);
        saveButtn.addActionListener(e -> {
            String temp = newcategory.getText().strip();
            if (temp.isEmpty()) {
                JOptionPane.showMessageDialog(null, "The field is empty ", "Warning Message", JOptionPane.WARNING_MESSAGE);
            } else if (Restaurant.uploadStaffCategory(temp)) {
                JOptionPane.showMessageDialog(null, "Added successfully", "Save", JOptionPane.INFORMATION_MESSAGE);
                Restaurant.loadStaffCategories();
                CategoryEmploy.revalidate();
                Dialog.dispose();
            }
        });

        Dialog.setBounds(430, 300, 400, 200);
        Dialog.setVisible(true);

    }

    private void editCategoryEmployeeButton() {
        Dialog = new JDialog(new JFrame(), "Edit category", true);
        Dialog.setLayout(null);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, Restaurant.staffCategoriesInformation.size(), 1));
        spinner.setBounds(40, 20, 60, 40);
        spinner.setFont(new Font("Arial", Font.BOLD, 20));
        Dialog.add(spinner);

        JLabel lab1 = new JLabel("Enter the category number");
        lab1.setBounds(110, 20, 250, 50);
        Dialog.add(lab1);

        JTextField newcategory = new JTextField();
        newcategory.setBounds(40, 120, 350, 30);
        Dialog.add(newcategory);

        JLabel lab2 = new JLabel("The name of new category : ");
        lab2.setBounds(40, 80, 200, 50);
        Dialog.add(lab2);

        JButton saveButtn = new JButton("Save");
        saveButtn.setBounds(370, 200, 80, 30);
        Dialog.add(saveButtn);
        saveButtn.addActionListener(e -> {
            String temp = newcategory.getText().strip();
            int num = (int) spinner.getValue();
            if (temp.isEmpty()) {
                JOptionPane.showMessageDialog(null, "The field is empty", "Warning Message", JOptionPane.WARNING_MESSAGE);
            } else {
                Restaurant.edit(num, "database\\staff_categories.txt", temp);
                Restaurant.loadStaffCategories();
                CategoryEmploy.revalidate();
                CategoryEmploy.repaint();
                Dialog.dispose();
            }
        });

        Dialog.setBounds(430, 300, 500, 300);
        Dialog.setVisible(true);

    }

    private void deleteCategoryEmployeeButton() {
        Dialog = new JDialog(new JFrame(), "Delet category", true);
        Dialog.setLayout(null);

        JLabel lab2 = new JLabel("Enter the category number ");
        lab2.setBounds(40, 20, 200, 50);
        Dialog.add(lab2);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, Restaurant.staffCategoriesInformation.size(), 1));
        spinner.setBounds(250, 20, 60, 40);
        spinner.setFont(new Font("Arial", Font.BOLD, 25));
        Dialog.add(spinner);

        JButton delButtn = new JButton("Delete");
        delButtn.setBounds(220, 100, 80, 30);
        Dialog.add(delButtn);
        delButtn.addActionListener(e -> {
            int num = (int) spinner.getValue();
            Restaurant.delete(num, "database\\staff_categories.txt");
            CategoryEmploy.revalidate();
            CategoryEmploy.repaint();
            Restaurant.loadStaffCategories();

            Dialog.dispose();
        });

        Dialog.setBounds(460, 300, 350, 200);
        Dialog.setVisible(true);

    }

    private void addEmployeeButton() {
        Dialog = new JDialog(new JFrame(), "Add new Employee", true);
        Dialog.setLayout(null);

        JLabel nameee = new JLabel("Enter the Employee name : ");
        nameee.setBounds(40, 0, 200, 50);
        Dialog.add(nameee);

        JTextField name = new JTextField();
        name.setBounds(40, 40, 300, 30);
        Dialog.add(name);

        JLabel salarylab = new JLabel("Enter the Employee salary : ");
        salarylab.setBounds(40, 80, 200, 50);
        Dialog.add(salarylab);

        JTextField salaryy = new JTextField();
        salaryy.setBounds(40, 120, 300, 30);
        Dialog.add(salaryy);

        JLabel passwordd = new JLabel("Enter the Employee password : ");
        passwordd.setBounds(40, 160, 200, 50);
        Dialog.add(passwordd);

        JTextField passwordField = new JTextField();
        passwordField.setBounds(40, 200, 300, 30);
        Dialog.add(passwordField);

        JLabel category = new JLabel("Choese category : ");
        category.setBounds(40, 240, 200, 50);
        Dialog.add(category);

        String a[] = new String[Restaurant.staffCategoriesInformation.size()];
        for (int i = 0; i < Restaurant.staffCategoriesInformation.size(); i++) {
            a[i] = (String) Restaurant.staffCategoriesInformation.get(i).get(1);
        }

        JComboBox comboBox = new JComboBox(a);
        comboBox.setBounds(40, 280, 140, 25);
        Dialog.add(comboBox);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(280, 310, 80, 30);
        Dialog.add(saveButton);
        saveButton.addActionListener(e -> {
            String namee = name.getText().trim();
            String password = passwordField.getText().trim();
            String categoty = (String) (comboBox.getSelectedItem());

            boolean test = !(isDouble(salaryy.getText().trim()));

            if (namee.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are missing information", "Warning Message", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (test) {
                JOptionPane.showMessageDialog(null, "\"unvalid Salary\" ", "Warning Message", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double Salary = Double.parseDouble(salaryy.getText().trim());

            if (Restaurant.uploadstaff(new Employee(namee, categoty, Salary, password))) {
                JOptionPane.showMessageDialog(null, "Added successfully", "Save", JOptionPane.INFORMATION_MESSAGE);
                Restaurant.loadStaff();
                tableEmployee.revalidate();
                tableEmployee.repaint();
                Dialog.dispose();
            }
        });

        Dialog.setBounds(430, 200, 400, 410);
        Dialog.setVisible(true);

    }

    private void editEmployeeButton() {
        Dialog = new JDialog(new JFrame(), "Edit Employee information", true);
        Dialog.setLayout(null);

        JLabel lab2 = new JLabel("Enter the Employee number");
        lab2.setFont(new Font("Arial", Font.BOLD, 17));
        lab2.setBackground(mainColor);
        lab2.setBounds(50, 0, 250, 50);
        Dialog.add(lab2);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, Restaurant.staffEmployeeInformation.size(), 1));
        spinner.setBounds(275, 0, 60, 40);
        spinner.setFont(new Font("Arial", Font.BOLD, 20));
        Dialog.add(spinner);

        JLabel employeeName = new JLabel("Enter the Employee name : ");
        employeeName.setBounds(40, 60, 200, 50);
        Dialog.add(employeeName);

        JTextField newEmployee = new JTextField();
        newEmployee.setBounds(40, 100, 300, 30);
        Dialog.add(newEmployee);

        JLabel salaryLabel = new JLabel("Enter the Employee salary : ");
        salaryLabel.setBounds(40, 140, 200, 50);
        Dialog.add(salaryLabel);

        JTextField salary = new JTextField();
        salary.setBounds(40, 180, 300, 30);
        Dialog.add(salary);

        JLabel passwordd = new JLabel("Enter the Employee password : ");
        passwordd.setBounds(40, 220, 200, 50);
        Dialog.add(passwordd);

        JTextField passwordField = new JTextField();
        passwordField.setBounds(40, 260, 300, 30);
        Dialog.add(passwordField);

        JLabel category = new JLabel("Choese category : ");
        category.setBounds(40, 300, 200, 50);
        Dialog.add(category);

        String a[] = new String[Restaurant.staffCategoriesInformation.size()];
        for (int i = 0; i < Restaurant.staffCategoriesInformation.size(); i++) {
            a[i] = (String) Restaurant.staffCategoriesInformation.get(i).get(1);
        }

        JComboBox comboBox = new JComboBox(a);
        comboBox.setBounds(40, 340, 140, 25);
        Dialog.add(comboBox);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(280, 365, 80, 30);
        Dialog.add(saveButton);

        JButton refresh = new JButton("Refresh");
        refresh.setBounds(280, 325, 80, 30);
        Dialog.add(refresh);

        refresh.addActionListener(e -> {
            int n = (int) spinner.getValue() - 1;
            newEmployee.setText((String) Restaurant.staffEmployeeInformation.get(n).get(1));
            comboBox.setSelectedItem((String) Restaurant.staffEmployeeInformation.get(n).get(2));
            salary.setText(Double.toString((Double) Restaurant.staffEmployeeInformation.get(n).get(3)));
            passwordField.setText((String) Restaurant.staffEmployeeInformation.get(n).get(4));
            comboBox.revalidate();
        });

        saveButton.addActionListener(e -> {
            int n = (int) spinner.getValue();
            String name = newEmployee.getText().trim();
            String pass = passwordField.getText().trim();
            
            boolean test = !(isDouble(salary.getText().trim()));

            if (name.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are missing information", "Warning Message", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (test) {
                JOptionPane.showMessageDialog(null, "\"unvalid Salary\" ", "Warning Message", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double s = Double.parseDouble(salary.getText().trim());
            
            
            String ccat = (String) comboBox.getSelectedItem();
            Restaurant.edit(n, "database\\staff.txt", name, pass, new Category(ccat), s, null);
            Restaurant.loadStaff();
            tableEmployee.revalidate();
            tableEmployee.repaint();
            Restaurant.loadStaff();
            tableEmployee.revalidate();
            tableEmployee.repaint();
            Dialog.dispose();
        });

        Dialog.setBounds(430, 200, 400, 450);
        Dialog.setVisible(true);

    }

    private void deleteEmployeeButton() {
        Dialog = new JDialog(new JFrame(), "Delet category", true);
        Dialog.setLayout(null);

        JLabel lab2 = new JLabel("Enter the Employee number ");
        lab2.setBounds(40, 20, 200, 50);
        Dialog.add(lab2);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, Restaurant.staffEmployeeInformation.size(), 1));
        spinner.setBounds(250, 20, 60, 40);
        spinner.setFont(new Font("Arial", Font.BOLD, 25));
        Dialog.add(spinner);

        JButton delButtn = new JButton("Delete");
        delButtn.setBounds(220, 100, 80, 30);
        Dialog.add(delButtn);
        delButtn.addActionListener(e -> {
            int num = (int) spinner.getValue();
            Restaurant.delete(num, "database\\staff.txt");
            tableEmployee.revalidate();
            tableEmployee.repaint();
            Restaurant.loadStaff();

            Dialog.dispose();
        });

        Dialog.setBounds(460, 300, 350, 200);
        Dialog.setVisible(true);

    }

    
    private void addProductToOrderTable(Product product, JTable orderTable, Vector orderTableRows) {
        Restaurant.currentOrder.increaseQuantity(product);
        Iterator iterator = orderTableRows.iterator();
        while (iterator.hasNext()) {
            Vector currentProduct = (Vector) iterator.next();
            if (currentProduct.contains(product.getName())) {
                int oldQuantity = Integer.parseInt((String) currentProduct.get(1));

                int newQuantity = oldQuantity + 1;
                currentProduct.set(1, Integer.toString(newQuantity));

                double newPrice = product.getPrice() * newQuantity;
                currentProduct.set(2, Double.toString(newPrice));

                
                orderTable.revalidate();
                orderTable.repaint();
                return;
            }
        }

        Vector<String> newProduct = new Vector<String>();
        newProduct.add(product.getName());
        newProduct.add("1");
        newProduct.add(Double.toString(product.getPrice()));

        orderTableRows.add(newProduct);
        orderTable.revalidate();
        orderTable.repaint();

    }

    private void removeProductFromOrderTable(Product product, JTable orderTable, Vector orderTableRows) {
        Restaurant.currentOrder.decreaseQuantity(product);
        
        Iterator iterator = orderTableRows.iterator();
        while (iterator.hasNext()) {
            Vector currentProduct = (Vector) iterator.next();
            if (currentProduct.contains(product.getName())) {
                double oldQuantity = Double.parseDouble((String) currentProduct.get(1));

                if (oldQuantity == 1) {
                    orderTableRows.remove(currentProduct);
                    orderTable.revalidate();
                    orderTable.repaint();

                    return;
                }

                int newQuantity = Integer.parseInt((String) currentProduct.get(1)) - 1;
                currentProduct.set(1, Integer.toString(newQuantity));

                double newPrice = product.getPrice() * newQuantity;
                currentProduct.set(2, Double.toString(newPrice));

                orderTable.revalidate();
                orderTable.repaint();
                return;
            }
        }

    }

    private void clearOrderTable() {
        Vector<String> orderTableHeader = new Vector<>();
        orderTableHeader.add("Product");
        orderTableHeader.add("Quantity");
        orderTableHeader.add("Price");

        orderTableRows.clear();
        orderTable.revalidate();
        orderTable.repaint();
    }

    private JTable getOrdersReportsTable() {
        Vector<String> ordersReportTableHeader = new Vector();
        ordersReportTableHeader.add("Customer");
        ordersReportTableHeader.add("Price");
        ordersReportTableHeader.add("Status");
        ordersReportTableHeader.add("Paying Type");
        
        Vector<Vector<String>> ordersReportTableRows = new Vector();
        Vector<String> orderInfo;
        Iterator ordersIterator = Restaurant.orders.iterator();
        while (ordersIterator.hasNext()) {
            Order order = (Order)ordersIterator.next();
            if (order == null) {
                continue;
            }
            orderInfo = new Vector();
            orderInfo.add(order.getCustomer());
            orderInfo.add(Double.toString(order.getTotalPrice()));
            orderInfo.add(order.getStatus().toString());
            if (order.getPayingType() == null) {
                orderInfo.add("x");
            } else {
                orderInfo.add(order.getPayingType().toString());
            }
            
            ordersReportTableRows.add(orderInfo);
        }
        
        JTable ordersReportsTable = new JTable(ordersReportTableRows, ordersReportTableHeader);
        ordersReportsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ordersReportsTable.setFont(new Font("Arial", 0, 20));
        ordersReportsTable.setRowHeight(40);
        ordersReportsTable.setCellSelectionEnabled(false);
        
        JTableHeader header = ordersReportsTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBackground(mainColor);
        header.setForeground(Color.white);
        
        return ordersReportsTable;
    }

    private JTable getTodayOrdersReportsTable() {
        Vector<String> ordersReportTableHeader = new Vector();
        ordersReportTableHeader.add("Customer");
        ordersReportTableHeader.add("Price");
        ordersReportTableHeader.add("Status");
        ordersReportTableHeader.add("Paying Type");
        
        Vector<Vector<String>> ordersReportTableRows = new Vector();
        Vector<String> orderInfo;
        Iterator ordersIterator = Restaurant.orders.iterator();
        Date today = new Date();
        while (ordersIterator.hasNext()) {
            Order order = (Order)ordersIterator.next();
            if (today.compareTo(order.getOrderDate()) == 0) {
                continue;
            }
            if (order == null) {
                continue;
            }
            orderInfo = new Vector();
            orderInfo.add(order.getCustomer());
            orderInfo.add(Double.toString(order.getTotalPrice()));
            orderInfo.add(order.getStatus().toString());
            if (order.getPayingType() == null) {
                orderInfo.add("x");
            } else {
                orderInfo.add(order.getPayingType().toString());
            }
            
            ordersReportTableRows.add(orderInfo);
        }
        
        JTable ordersReportsTable = new JTable(ordersReportTableRows, ordersReportTableHeader);
        ordersReportsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ordersReportsTable.setFont(new Font("Arial", 0, 20));
        ordersReportsTable.setRowHeight(40);
        ordersReportsTable.setCellSelectionEnabled(false);
        
        JTableHeader header = ordersReportsTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBackground(mainColor);
        header.setForeground(Color.white);
        
        return ordersReportsTable;
    }    
    

    private JTable getAllCustomersTable() {
        Vector<String> customersReportTableHeader = new Vector();
        customersReportTableHeader.add("Customer");
        
        
        Vector<Vector<String>> customersReportTableRows = new Vector();
        Vector<String> orderInfo;
        Iterator ordersIterator = Restaurant.orders.iterator();

        while (ordersIterator.hasNext()) {
            Order order = (Order)ordersIterator.next();
            if (order == null) {
                continue;
            }
            orderInfo = new Vector();
            orderInfo.add(order.getCustomer());
            
            customersReportTableRows.add(orderInfo);
        }
        
        JTable cusomtersReportsTable = new JTable(customersReportTableRows, customersReportTableHeader);
        cusomtersReportsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        cusomtersReportsTable.setFont(new Font("Arial", 0, 20));
        cusomtersReportsTable.setRowHeight(40);
        cusomtersReportsTable.setCellSelectionEnabled(false);
        
        JTableHeader header = cusomtersReportsTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBackground(mainColor);
        header.setForeground(Color.white);
        
        return cusomtersReportsTable;
    }
    
    private JTable getPermanentCustomerTable() {
        Vector<String> PermanentCustomerReportTableHeader = new Vector();
        PermanentCustomerReportTableHeader.add("Customer");
        
        
        Vector<Vector<String>> PermanentCustomersReportTableRows = new Vector();
        Iterator ordersIterator = Restaurant.orders.iterator();
        
        HashMap<String, Integer> customers = new HashMap();
        

        while (ordersIterator.hasNext()) {
            Order order = (Order)ordersIterator.next();
            if (order == null) {
                continue;
            }
            if (customers.containsKey(order.getCustomer())) {
                int count = (int)customers.get(order.getCustomer());
                customers.put(order.getCustomer(), count + 1);
            } else {
                customers.put(order.getCustomer(), 1);
            }
        }
        
        Iterator customersIterator = customers.entrySet().iterator();
        int max = 0;
        String customerName = "";
        while (customersIterator.hasNext()) {
            Map.Entry entry = (Map.Entry)customersIterator.next();
            if ((int)entry.getValue() > max) {
                max = (int)entry.getValue();
                customerName = (String)entry.getKey();
            }
        }
        Vector<String> name = new Vector();
        name.add(customerName);
        PermanentCustomersReportTableRows.add(name);
        
        JTable PermanentCusomtersReportsTable = new JTable(PermanentCustomersReportTableRows, PermanentCustomerReportTableHeader);
        PermanentCusomtersReportsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        PermanentCusomtersReportsTable.setFont(new Font("Arial", 0, 20));
        PermanentCusomtersReportsTable.setRowHeight(40);
        PermanentCusomtersReportsTable.setCellSelectionEnabled(false);
        
        JTableHeader header = PermanentCusomtersReportsTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBackground(mainColor);
        header.setForeground(Color.white);
        
        return PermanentCusomtersReportsTable;
    }
    private JTable getMostRequestedMealTable() {
        Vector<String> mostRequestedMealTableHeader = new Vector();
        mostRequestedMealTableHeader.add("Customer");
        
        
        Vector<Vector<String>> mostRequestedMealTableRows = new Vector();
        Iterator ordersIterator = Restaurant.orders.iterator();
        
        HashMap<Product, Integer> meals = new HashMap();

        int count = 0;
        while (ordersIterator.hasNext()) {
            HashMap<Product, Integer> currentProducts = ((Order)ordersIterator.next()).getProducts();
            Iterator productsIterator = currentProducts.entrySet().iterator();
            while (productsIterator.hasNext()) {
                Map.Entry entry = (Map.Entry)productsIterator.next();
                if (meals.containsKey(entry.getKey())) {
                    count = (int)entry.getValue();
                    meals.put((Product)entry.getKey(), count + 1);
                } else {
                    meals.put((Product)entry.getKey(), 1);
                }
            }
        }
        
        int max = 0;
        Product product = new Product();
        Iterator mealsIterator = meals.entrySet().iterator();
        while (mealsIterator.hasNext()) {
            Map.Entry entry = (Map.Entry)mealsIterator.next();
            if ((int)entry.getValue() > max) {
                max = (int)entry.getValue();
                product = (Product)entry.getKey();
            }
        }
        
        Vector<String> productName = new Vector();
        productName.add(product.getName());
        mostRequestedMealTableRows.add(productName);
        
        JTable mostRequestedMealTable = new JTable(mostRequestedMealTableRows, mostRequestedMealTableHeader);
        mostRequestedMealTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        mostRequestedMealTable.setFont(new Font("Arial", 0, 20));
        mostRequestedMealTable.setRowHeight(40);
        mostRequestedMealTable.setCellSelectionEnabled(false);
        
        JTableHeader header = mostRequestedMealTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBackground(mainColor);
        header.setForeground(Color.white);
        
        return mostRequestedMealTable;
    }
    
    private JTable getMonthlyProfitsTable() {
        double sum = 0;
        boolean exist = false;
        Iterator ordersIterator = Restaurant.orders.iterator();
        Vector<Vector<String>> monthlyProfitsRows = new Vector();
        Vector<String> monthlyProfits;
        
        while (ordersIterator.hasNext()) {
            sum = 0;
            exist = false;
            
            Order order = (Order)ordersIterator.next();
            Date date = order.getOrderDate();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = localDate.getMonthValue();
            for (Vector row: monthlyProfitsRows) {
                if (Integer.parseInt((String)row.get(0)) == month) {
                    exist = true;
                    sum = Double.parseDouble((String)row.get(1)) + order.getTotalPrice();
                    row.remove(1);
                    row.insertElementAt(Double.toString(sum), 1);
                }
            }
            if (!exist) {
                monthlyProfits = new Vector();
                monthlyProfits.add(Integer.toString(month));
                monthlyProfits.add(Double.toString(sum));
                monthlyProfitsRows.add(monthlyProfits);
                
            }
            
        }
        Vector<String> monthlyProfitsHeader = new Vector();
        
        monthlyProfitsHeader.add("Month");
        monthlyProfitsHeader.add("Profits");
        
                
        JTable monthlyProfitsTable = new JTable(monthlyProfitsRows, monthlyProfitsHeader);
        monthlyProfitsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        monthlyProfitsTable.setFont(new Font("Arial", 0, 20));
        monthlyProfitsTable.setRowHeight(40);
        monthlyProfitsTable.setCellSelectionEnabled(false);
        
        JTableHeader header = monthlyProfitsTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBackground(mainColor);
        header.setForeground(Color.white);
        
        return monthlyProfitsTable;
        
        
    }
}