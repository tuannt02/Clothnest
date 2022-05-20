package nhom7.clothnest.models;

import android.provider.ContactsContract;

public class Image {

    public static final String COLLECTION_BANNERS="banners";

    private String  image;

    public String getImage() {
        return image;
    }

    public Image(String image) {
        this.image = image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Image()
    {

    }
}
