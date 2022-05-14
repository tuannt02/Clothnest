package nhom7.clothnest.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ChatRoom {
    @PropertyName("is_admin_read")
    public boolean isAdminRead;

    @PropertyName("is_admin_sent")
    public boolean isAdminSent;

    @PropertyName("recent_msg")
    public String recentMsg;

    @ServerTimestamp
    public Date time;

    public DocumentReference userRef;

    public ChatRoom() {
    }

    public ChatRoom(boolean isAdminRead, String recentMsg, DocumentReference userRef) {
        this.isAdminRead = isAdminRead;
        this.recentMsg = recentMsg;
        this.userRef = userRef;
    }

    @Exclude
    public String chatRoomID;

    @Exclude
    public String avatar, name;
}
