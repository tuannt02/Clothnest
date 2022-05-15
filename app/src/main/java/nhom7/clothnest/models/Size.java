package nhom7.clothnest.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

public class Size {
    @Exclude
    public String sizeId;

    public String name;

    @PropertyName("short_name")
    public String shortName;

    public String type;
}
