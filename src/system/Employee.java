package system;

public class Employee {
    private String name ;
    private Category category;
    private double salary;
    private String password;

    public Employee(String name, String category, double salary, String password) {
        this.name = name;
        this.category = new Category(category);
        this.salary = salary;
        this.password = password;
    }
    
    public Employee(String line) {
        String[] productInfo = line.split(",");
        this.name = productInfo[0].trim();
        this.category = new Category( productInfo[1].trim());
        this.salary = Double.parseDouble(productInfo[2].trim());
        this.password = productInfo[3].trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
