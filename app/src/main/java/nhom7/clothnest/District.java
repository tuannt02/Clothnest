package nhom7.clothnest;

import java.util.ArrayList;

public class District {
    private String name;
    private int code;
    private ArrayList<Ward> wards;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public ArrayList<Ward> getWards() {
        return wards;
    }
}
