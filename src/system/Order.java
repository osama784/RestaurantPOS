package system;

import java.util.*;
import java.io.Serializable;
import java.util.HashMap;

public class Order implements Serializable {

    public static enum OrderType {
        ON_TABLE("on Table"),
        DELIVERY("Delivery"),
        TAKE_AWAY("Take Away");

        private final String displayName;

        OrderType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public static enum OrderStatus {
        PROCCESSING("Proccessing"),
        DELIVERING("Delivering"),
        DONE("Done"),
        CANCELED("Canceled");

        private final String displayName;

        OrderStatus(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public static enum PayingType {
        CREDIT_CARD("Credit Card"),
        CASH("Cash"),
        FREE("Free");

        private final String displayName;

        PayingType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private HashMap<Product, Integer> products = new HashMap();
    private int ID;
    private String customer = "";
    private Date orderDate;
    private OrderType type = null;
    private OrderStatus status = null;
    private PayingType payingType = null;

    // Getters & Setters.
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomer() {
        return this.customer;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderType getType() {
        return type;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public HashMap getProducts() {
        return this.products;
    }

    public void setPayingType(PayingType payingType) {
        this.payingType = payingType;
    }

    public PayingType getPayingType() {
        return this.payingType;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date date) {
        this.orderDate = date;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public double getTotalPrice() {
        double sum = 0;
        Iterator it = this.products.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            sum += (((Product) entry.getKey()).getPrice()) * (int) entry.getValue();
        }
        return sum;
    }

    // Methods
    public void increaseQuantity(Product product) {
        if (!this.products.containsKey(product)) {
            this.products.put(product, 1);
            return;
        }
        int oldQuantity = (int) this.products.get(product);

        this.products.put(product, oldQuantity + 1);
    }

    public void decreaseQuantity(Product product) {
        if (!this.products.containsKey(product)) {
            return;
        }
        int oldQuantity = (int) this.products.get(product);

        if (oldQuantity == 1) {
            this.products.remove(product);
            return;
        }
        this.products.put(product, oldQuantity - 1);
    }

    public boolean isValid() {

        if (this.products.isEmpty() || this.customer.isEmpty()) {
            return false;
        }
        return true;
    }

}
