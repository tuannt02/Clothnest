package nhom7.clothnest.models;

import java.util.ArrayList;

public class Product_Detail {
    public static final String COLLECTION_NAME = "products";

    private String id;
    private String category;
    private String name;
    private double price;
    private int discount;
    private ArrayList<String> imageList;
    private boolean isFavorite;
    private String description;

    public Product_Detail() {
    }

    public Product_Detail(String id, String category, String name, double price, int discount, ArrayList<String> imageList, boolean isFavorite, String description) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.imageList = imageList;
        this.isFavorite = isFavorite;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Product_Detail getProductDetailFromFirestore(String id){
        Product_Detail product = new Product_Detail();

        return product;
    }
}
