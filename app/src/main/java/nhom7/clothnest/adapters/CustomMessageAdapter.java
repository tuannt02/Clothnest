package nhom7.clothnest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.ChatMessage;

public class CustomMessageAdapter extends BaseAdapter {
    private Context context;
    List<ChatMessage> chatMessages;
    private boolean isAdmin;
    private static final String TAG = "CustomMessageAdapter";
    private SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
    private String clientAvatar;

    public CustomMessageAdapter(Context context, List<ChatMessage> messages, boolean isAdmin, String clientAvatar) {
        this.context = context;
        this.chatMessages = messages;
        this.isAdmin = isAdmin;
        this.clientAvatar = clientAvatar;
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
        } else {
            ImageView ivAvatar;
            CardView cvHolder;
            view = inflater.inflate(R.layout.message_received, viewGroup, false);


            ivAvatar = view.findViewById(R.id.imageview_avatar);
            cvHolder = view.findViewById(R.id.cardview_holder);
            if (i > 0 && chatMessages.get(i - 1).isAdmin != isAdmin) {
                cvHolder.setVisibility(View.INVISIBLE);
            } else {
                cvHolder.setVisibility(View.VISIBLE);
                if (clientAvatar != null) {
                    Picasso.get().load(clientAvatar).fit().into(ivAvatar);

                }
            }
        }

        TextView tvMessage = view.findViewById(R.id.textview_message);
        tvMessage.setText(chatMessage.getText());
        TextView tvTime = view.findViewById(R.id.textview_time);
        if (chatMessage.time != null)
            tvTime.setText(formatter.format(chatMessage.time));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTime.setVisibility(tvTime.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        return view;
    }
}
