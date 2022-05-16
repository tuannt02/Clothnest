package nhom7.clothnest.models;

import java.util.ArrayList;

public class Transaction_Detail {

    public static final String COLLECTION_TRANSACTIONDETAIL = "transactionItemList";
    private String idDetail;
    private String nameDetail;
    private double priceDetail;
    private String imageListDetail;
    private int quantilyDetail;
    private String sizeDetail;
    private String colorDetail;


    public Transaction_Detail() {

    }

    public Transaction_Detail(String idDetail, String nameDetail, double priceDetail, String imageListDetail, int quantilyDetail, String sizeDetail, String colorDetail) {
        this.idDetail = idDetail;
        this.nameDetail = nameDetail;
        this.priceDetail = priceDetail;
        this.imageListDetail = imageListDetail;
        this.quantilyDetail = quantilyDetail;
        this.sizeDetail = sizeDetail;
        this.colorDetail = colorDetail;
    }

    public String getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(String idDetail) {
        this.idDetail = idDetail;
    }

    public void setNameDetail(String nameDetail) {
        this.nameDetail = nameDetail;
    }

    public void setPriceDetail(double priceDetail) {
        this.priceDetail = priceDetail;
    }

    public void setImageListDetail(String imageListDetail) {
        this.imageListDetail = imageListDetail;
    }

    public void setQuantilyDetail(int quantilyDetail) {
        this.quantilyDetail = quantilyDetail;
    }

    public String getSizeDetail() {
        return sizeDetail;
    }

    public void setSizeDetail(String sizeDetail) {
        this.sizeDetail = sizeDetail;
    }

    public String getColorDetail() {
        return colorDetail;
    }

    public void setColorDetail(String colorDetail) {
        this.colorDetail = colorDetail;
    }

    public String getNameDetail() {
        return nameDetail;
    }

    public double getPriceDetail() {
        return priceDetail;
    }

    public String getImageListDetail() {
        return imageListDetail;
    }

    public int getQuantilyDetail() {
        return quantilyDetail;
    }


}
