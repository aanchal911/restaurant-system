public class MenuItem {
    private int itemId;
    private int categoryId;
    private String categoryName;
    private String name;
    private double price;
    private String description;

    public MenuItem(int itemId, int categoryId, String categoryName, String name, double price, String description) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public int getItemId() { return itemId; }
    public int getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
}