package nhom7.clothnest.models;

import android.app.ProgressDialog;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Stock {
    public static final String COLLECTION_NAME = "stocks";
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
