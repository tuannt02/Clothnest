package nhom7.clothnest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.ChatMessage;

public class CustomMessageAdapter extends BaseAdapter {
    private Context context;
    List<ChatMessage> chatMessages;
    private boolean isAdmin;
    private static final String TAG = "CustomMessageAdapter";

    public CustomMessageAdapter(Context context, List<ChatMessage> messages, boolean isAdmin) {
        this.context = context;
        this.chatMessages = messages;
        this.isAdmin = isAdmin;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int i) {
        return chatMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessage chatMessage = chatMessages.get(i);
        LayoutInflater inflater = LayoutInflater.from(context);

        Log.d(TAG, chatMessage.getText() + " -- " + chatMessage.isAdmin + " -- " + isAdmin);


        if (chatMessage.isAdmin == isAdmin) {
            view = inflater.inflate(R.layout.message_sent, viewGroup, false);

            TextView tvMessage = view.findViewById(R.id.textview_message);
            tvMessage.setText(chatMessage.getText());
            ;
        } else {
            view = inflater.inflate(R.layout.message_received, viewGroup, false);

            TextView tvMessage = view.findViewById(R.id.textview_message);
            tvMessage.setText(chatMessage.getText());
        }

        return view;
    }
}
