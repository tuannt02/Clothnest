package nhom7.clothnest.models;

public class Product_Thumbnail {
    public static final String COLLECTION_NAME = "products";

    private String id;
    private String category;
    private String name;
    private double price;
    private int discount;
    private String mainImage;
    private boolean isFavorite;

    public Product_Thumbnail() {
        isFavorite = false;
    }

    public Product_Thumbnail(String id, String name, double price, int discount, String mainImage, boolean isFavorite) {
        this.id = id;
        this.category = "";
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.mainImage = mainImage;
        this.isFavorite = isFavorite;
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
}
