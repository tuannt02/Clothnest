package nhom7.clothnest.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CartItem implements Serializable {

    public static final String COLLECTION_NAME = "cart";

    private String keyProduct;
    private String key;
    private String img;
    private int qty;
    private String name;
    private Double price;
    private int discount;
    private double discountPrice;
    private ArrayList<ColorClass> listColor = new ArrayList<>();
    private ArrayList<SizeClass> listSize = new ArrayList<>();
    private ColorClass colorSelected = new ColorClass();
    private SizeClass sizeSelected = new SizeClass();

    public CartItem()   {

    }

    public CartItem(String keyProduct, String key, String img, int qty, String name, Double price, int discount, double discountPrice, ArrayList<ColorClass> listColor, ArrayList<SizeClass> listSize, ColorClass colorSelected, SizeClass sizeSelected) {
        this.keyProduct = keyProduct;
        this.key = key;
        this.img = img;
        this.qty = qty;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.discountPrice = discountPrice;
        this.listColor = listColor;
        this.listSize = listSize;
        this.colorSelected = colorSelected;
        this.sizeSelected = sizeSelected;
    }

    public ColorClass getColorSelected() {
        return colorSelected;
    }

    public SizeClass getSizeSelected() {
        return sizeSelected;
    }

    public ArrayList<ColorClass> getListColor() {
        return listColor;
    }

    public ArrayList<SizeClass> getListSize() {
        return listSize;
    }

    public String getKeyProduct() {
        return keyProduct;
    }

    public int getDiscountPrice() {
        return (int)(price*(100-discount)/100)*qty;
    }

    public int getDiscount() {
        return discount;
    }

    public String getKey() {
        return key;
    }

    public String getImg() {
        return img;
    }

    public int getQty() {
        return qty;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void setKeyProduct(String keyProduct) {
        this.keyProduct = keyProduct;
    }

    public void setListColor(ArrayList<ColorClass> listColor) {
        this.listColor = listColor;
    }

    public void setListSize(ArrayList<SizeClass> listSize) {
        this.listSize = listSize;
    }

    public void setColorSelected(ColorClass colorSelected) {
        this.colorSelected = colorSelected;
    }

    public void setSizeSelected(SizeClass sizeSelected) {
        this.sizeSelected = sizeSelected;
    }

    public int getPosSelectedColor()   {
        for(int i=0;i<listColor.size();i++) {
            ColorClass colorItem = listColor.get(i);

            if(colorItem.getHex().equals(this.colorSelected.getHex()))   {
                clearSelectedColor();
                colorItem.setSelected(true);
                return i;
            }

        }

        return 0;
    }

    public int getPosSelectedSize()   {
        for(int i=0;i<listSize.size();i++) {
            SizeClass sizeItem = listSize.get(i);

            if(sizeItem.getName().equals(this.sizeSelected.getName()))   {
                clearSelectedSize();
                sizeItem.setSelected(true);
                return i;
            }

        }

        return 0;
    }

    public void clearSelectedColor()    {
        for(int i=0;i<listColor.size();i++) {
            ColorClass colorItem = listColor.get(i);
            colorItem.setSelected(false);
            listColor.set(i,colorItem);
        }
    }

    public void clearSelectedSize()    {
        for(int i=0;i<listSize.size();i++) {
            SizeClass sizeItem = listSize.get(i);
            sizeItem.setSelected(false);
            listSize.set(i,sizeItem);
        }
    }

    public void incrementQuantity() {
        this.qty++;
    }

    public void decreaseQuantity()  {
        if(qty == 1) return;
        this.qty--;
    }
}
