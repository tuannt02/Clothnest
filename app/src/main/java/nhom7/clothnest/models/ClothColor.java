package nhom7.clothnest.models;

import com.google.firebase.firestore.Exclude;

public class ClothColor {
    @Exclude
    public String colorId;

    public String hex, name;
}
