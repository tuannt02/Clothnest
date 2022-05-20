package nhom7.clothnest.models;

import com.google.firebase.firestore.DocumentReference;

public class ColorClass {

    public static final String COLLECTION_NAME = "colors";

    private String name;
    private String hex;
    private boolean isSelected;
    private DocumentReference colorRef;

    public DocumentReference getColorRef() {
        return colorRef;
    }

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setColorRef(DocumentReference colorRef) {
        this.colorRef = colorRef;
    }

    public ColorClass() {
    }

    public ColorClass(String name, String hex) {
        this.name = name;
        this.hex = hex;
    }

    public ColorClass(String name, String hex, boolean isSelected) {
        this.name = name;
        this.hex = hex;
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return "ColorClass{" +
                "name='" + name + '\'' +
                ", hex='" + hex + '\'' +
                '}';
    }


}
