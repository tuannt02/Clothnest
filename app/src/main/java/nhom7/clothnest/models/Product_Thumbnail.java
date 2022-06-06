package nhom7.clothnest.models;

import java.util.ArrayList;

public class Product_Thumbnail {
    public static final String COLLECTION_NAME = "products";
    public static final String COLECTION_NAME_ARRIVAL="new_arrivals";
    public static final String COLECTION_NAME_SALES="sales";
    public static final String COLECTION_NAME_COLLECTIONS="collections";
    public static final String COLECTION_NAME_PRODUCTS="products";



    private String id;
    private String category;
    private String name;
    private double price;
    private int discount;
    private String mainImage;
    private boolean isFavorite;
    private ArrayList<String> colorList, sizeList;

    public Product_Thumbnail() {
        isFavorite = false;
        colorList = new ArrayList<>();
        sizeList = new ArrayList<>();
    }

    public Product_Thumbnail(String id, String name, double price, int discount, String mainImage, boolean isFavorite) {
        this.id = id;
        this.category = "";
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.mainImage = mainImage;
        this.isFavorite = isFavorite;
        this.colorList = new ArrayList<>();
        this.sizeList = new ArrayList<>();
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

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public ArrayList<String> getColorList() {
        return colorList;
    }

    public void setColorList(ArrayList<String> colorList) {
        this.colorList = colorList;
    }

    public ArrayList<String> getSizeList() {
        return sizeList;
    }

    public void setSizeList(ArrayList<String> sizeList) {
        this.sizeList = sizeList;
    }
}
