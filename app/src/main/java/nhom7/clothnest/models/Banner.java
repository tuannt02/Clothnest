package nhom7.clothnest.models;

public class Banner {
    public static final String COLLECTION_NAME = "banners";

    private String docID;
    private String img;


    public Banner() {

    }

    public Banner(String docID, String img) {
        this.docID = docID;
        this.img = img;
    }

    public String getDocID() {
        return docID;
    }

    public String getImg() {
        return img;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "docID='" + docID + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
