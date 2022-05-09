package nhom7.clothnest.models;

public class CategoryItem {
    public static final String COLLECTION_NAME = "categories";

    public String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int icon;

    public CategoryItem(String categoryName, int icon) {
        this.categoryName = categoryName;
        this.icon = icon;
    }
}
