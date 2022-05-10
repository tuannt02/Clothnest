package nhom7.clothnest.models;

import java.util.ArrayList;

public class Transaction_Detail {

    private String idDetail;
    private String nameDetail;
    private double priceDetail;
    private int imageListDetail;;
    private  int quantilyDetail;

    public Transaction_Detail()
    {

    }
    public Transaction_Detail(String idDetail, String nameDetail, double priceDetail, int imageListDetail, int quantilyDetail) {
        this.idDetail = idDetail;
        this.nameDetail = nameDetail;
        this.priceDetail = priceDetail;
        this.imageListDetail = imageListDetail;
        this.quantilyDetail = quantilyDetail;
    }
    public String getIdDetail() {
        return idDetail;
    }

    public String getNameDetail() {
        return nameDetail;
    }

    public double getPriceDetail() {
        return priceDetail;
    }

    public int getImageListDetail() {
        return imageListDetail;
    }

    public int getQuantilyDetail() {
        return quantilyDetail;
    }


}
