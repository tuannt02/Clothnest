package nhom7.clothnest.models;

import java.util.ArrayList;

public class Province {
    private String name;
    private int code;
    private ArrayList<District> districts;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public ArrayList<District> getDistricts() {
        return districts;
    }
}
