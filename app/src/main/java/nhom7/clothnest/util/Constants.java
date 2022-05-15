package nhom7.clothnest.util;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class Constants {
    public static final String BASE_URL = "https://fcm.googleapis.com";
    public static final String SERVER_KEY = "AAAA2XnsEwk:APA91bHXZJiVYuvpqO2JWpaMBh_wMNPIPNBs6dwjc_8PKAozATeeIUz2SL_sIqplqJV48PV9lmNb5OGCkdoMDNoLokYetH6NdqIvYNomjaSCu1rJwoiUTNrJlF-ttFRvxHqynpJmJqiC";
    public static final String CONTENT_TYPE = "application/json";

    public static HashMap<String, String> remoteMsgHeaders = null;

    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    "Authorization",
                    "key=" + SERVER_KEY
            );
            remoteMsgHeaders.put(
                    "Content-Type",
                    CONTENT_TYPE
            );
        }
        return remoteMsgHeaders;
    }

    public static String userId;

    public static String getUserId() {
        if (userId == null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return userId;
    }
}

