package nhom7.clothnest.models;

import java.util.ArrayList;

public class Comment {
    private String name;
    private String avt;
    private String dateTime;
    private int starNumber;
    private String type;
    private String comment;
    private ArrayList<ColorClass> colorList;
    private ArrayList<SizeClass> sizeList;
    private ColorClass colorSelected;
    private SizeClass sizeSelected;

    public Comment() {
        this.colorList = new ArrayList<>();
        this.sizeList = new ArrayList<>();
        this.colorSelected = new ColorClass();
        this.sizeSelected = new SizeClass();
    }

    public Comment(String name, String avt, String dateTime, int starNumber, String type, String comment) {
        this.name = name;
        this.avt = avt;
        this.dateTime = dateTime;
        this.starNumber = starNumber;
        this.type = type;
        this.comment = comment;
        this.colorList = new ArrayList<>();
        this.sizeList = new ArrayList<>();
        this.colorSelected = new ColorClass();
        this.sizeSelected = new SizeClass();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getStarNumber() {
        return starNumber;
    }

    public void setStarNumber(int starNumber) {
        this.starNumber = starNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<ColorClass> getColorList() {
        return colorList;
    }

    public void setColorList(ArrayList<ColorClass> colorList) {
        this.colorList = colorList;
    }

    public ArrayList<SizeClass> getSizeList() {
        return sizeList;
    }

    public void setSizeList(ArrayList<SizeClass> sizeList) {
        this.sizeList = sizeList;
    }

    public int getPosSelectedColor() {
        if(colorList.contains(colorSelected)) {
            int i = colorList.indexOf(colorSelected);
            clearSelectedColor();
            colorList.get(i).setSelected(true);
            return i;
        }
        return 0;
    }

    public int getPosSelectedSize() {
        if(sizeList.contains(sizeSelected)) {
            int i = sizeList.indexOf(sizeSelected);
            clearSelectedSize();
            sizeList.get(i).setSelected(true);
            return i;
        }
        return 0;
    }

    public void clearSelectedColor() {
        for (int i = 0; i < colorList.size(); i++) {
            ColorClass colorItem = colorList.get(i);
            colorItem.setSelected(false);
            colorList.set(i, colorItem);
        }
    }

    public void clearSelectedSize() {
        for (int i = 0; i < sizeList.size(); i++) {
            SizeClass sizeItem = sizeList.get(i);
            sizeItem.setSelected(false);
            sizeList.set(i, sizeItem);
        }
    }

    public ColorClass getColorSelected() {
        return colorSelected;
    }

    public void setColorSelected(ColorClass colorSelected) {
        this.colorSelected = colorSelected;
    }

    public SizeClass getSizeSelected() {
        return sizeSelected;
    }

    public void setSizeSelected(SizeClass sizeSelected) {
        this.sizeSelected = sizeSelected;
    }
}
