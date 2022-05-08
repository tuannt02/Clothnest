package nhom7.clothnest.models;

public class ColorClass {

    public static final String COLLECTION_NAME = "colors";

    private String name;
    private String hex;


    public String getName() {
        return name;
    }

    public String getHex() {
        return hex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public ColorClass() {
    }

    public ColorClass(String name, String hex) {
        this.name = name;
        this.hex = hex;
    }

    @Override
    public String toString() {
        return "ColorClass{" +
                "name='" + name + '\'' +
                ", hex='" + hex + '\'' +
                '}';
    }


}
