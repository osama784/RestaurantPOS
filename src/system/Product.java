package system;

import java.io.Serializable;

public class Product implements Serializable{
    String name;
    String ingredients;
    Category category;
    boolean active;
    double price;
    
    public Product() {
        
    }
    public Product(String name, String ingredients, Category category, double price, boolean isActive) {
        this.name = name;
        this.ingredients = ingredients;
        this.category = category;
        this.price = price;
        this.active = isActive;
    }    
    
    public Product(String line) {
        String[] productInfo = line.split(",");
        this.name = productInfo[0].trim();
        this.price = Double.parseDouble(productInfo[1].trim());
        this.category = new Category( productInfo[2].trim());
        this.ingredients = productInfo[3].trim();
        this.active = Boolean.parseBoolean(productInfo[4].trim());
    }
    
    
    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isActive() {
        return active;
    }

    public double getPrice() {
        return price;
    }

   
    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setCategory(String category) {
        this.category.setName(category);
    }

    public void setIsActive(boolean isActive) {
        this.active = isActive;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    
    
    
}
