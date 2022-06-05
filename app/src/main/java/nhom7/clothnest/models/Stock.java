package nhom7.clothnest.models;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

import nhom7.clothnest.adapters.StockImageUpdateAdapter;

public class Stock {
    public static final String COLLECTION_NAME = "stocks";
    private String stockID;
    private String sizeID;
    private String colorID;
    private String sizeName, colorName;
    private int quantity;
    private String img;
    private ArrayList<Uri> imageList;
    private ArrayList<String> downloadUrls;

    public Stock() {
        imageList = new ArrayList<>();
    }

    public Stock(String sizeID, String colorID, int quantity, ArrayList<Uri> imageList) {
        this.sizeID = sizeID;
        this.colorID = colorID;
        this.quantity = quantity;
        this.imageList = imageList;
    }

    public Stock(String sizeID, String colorID, String sizeName, String colorName, int quantity, ArrayList<String> downloadUrls) {
        this.sizeID = sizeID;
        this.colorID = colorID;
        this.sizeName = sizeName;
        this.colorName = colorName;
        this.quantity = quantity;
        this.downloadUrls = downloadUrls;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public String getImg() {
        return img;
    }

    public String getSizeID() {
        return sizeID;
    }

    public void setSizeID(String sizeID) {
        this.sizeID = sizeID;
    }

    public String getColorID() {
        return colorID;
    }

    public void setColorID(String colorID) {
        this.colorID = colorID;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<Uri> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<Uri> imageList) {
        this.imageList = imageList;
    }

    public ArrayList<String> getDownloadUrls() {
        return downloadUrls;
    }

    public void setDownloadUrls(ArrayList<String> downloadUrls) {
        this.downloadUrls = downloadUrls;
    }

//    public ArrayList<Bitmap> getBitmapList() {
//        return bitmapList;
//    }

//    public void setBitmapList(ArrayList<Bitmap> bitmapList) {
//        this.bitmapList = bitmapList;
//    }

//    public Bitmap getBitmap(int i){
//        return  bitmapList.get(i);
//    }

//    public void addBitMapToList(Bitmap bitmap){
//        if(bitmap == null) {
//            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);        }
//        this.bitmapList.add(bitmap);
//    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "sizeID='" + sizeID + '\'' +
                ", colorID='" + colorID + '\'' +
                ", sizeName='" + sizeName + '\'' +
                ", colorName='" + colorName + '\'' +
                ", quantity=" + quantity +
                ", img='" + img + '\'' +
                ", imageList=" + imageList +
                '}';
    }
}
