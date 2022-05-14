package nhom7.clothnest.models;

import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ChatMessage {
    @PropertyName("is_admin")
    public boolean isAdmin;

    private String text;

    @ServerTimestamp
    public Date time;

    public ChatMessage() {}

    public ChatMessage(boolean isAdmin, String text) {
        this.isAdmin = isAdmin;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
