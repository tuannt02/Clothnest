package nhom7.clothnest.models;

public class SizeClass {
    public static final String COLLECTION_NAME = "sizes";

    private String name;
    private String short_name;

    public SizeClass(String name, String short_name) {
        this.name = name;
        this.short_name = short_name;
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

    @Override
    public String toString() {
        return "SizeClass{" +
                "name='" + name + '\'' +
                ", short_name='" + short_name + '\'' +
                '}';
    }
}
