package nhom7.clothnest.models;

public class SizeClass {
    public static final String COLLECTION_NAME = "sizes";

    private String name;
    private String short_name;
    private boolean isSelected;


    public SizeClass() {
    }

    public SizeClass(String name, String short_name) {
        this.name = name;
        this.short_name = short_name;
        isSelected = false;
    }

    public String getName() {
        return name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "SizeClass{" +
                "name='" + name + '\'' +
                ", short_name='" + short_name + '\'' +
                '}';
    }
}
