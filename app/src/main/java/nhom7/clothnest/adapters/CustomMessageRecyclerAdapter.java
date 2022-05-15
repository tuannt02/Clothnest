package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.ChatMessage;
import nhom7.clothnest.models.ChatRoom;

public class CustomMessageRecyclerAdapter extends RecyclerView.Adapter<CustomMessageRecyclerAdapter.DataViewHolder> {
    private Context context;
    private List<ChatMessage> messages;
    private boolean isAdmin;
    private SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
    private String clientAvatar;
    private static final String TAG = "CustomMessageRecyclerAd";

    public CustomMessageRecyclerAdapter(Context context, List<ChatMessage> messages, boolean isAdmin, String clientAvatar) {
        this.context = context;
        this.messages = messages;
        this.isAdmin = isAdmin;
        this.clientAvatar = clientAvatar;
    }


    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == 1) {
            view = inflater.inflate(R.layout.message_sent, parent, false);
        } else {
            view = inflater.inflate(R.layout.message_received, parent, false);
        }
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        String time = formatter.format(message.time);
        String text = message.getText();

        holder.tvMessage.setText(text);
        holder.tvTime.setText(time);

        if (clientAvatar != null && message.isAdmin != isAdmin) {
            if (position > 0 && messages.get(position - 1).isAdmin != isAdmin) {
                holder.cvHolder.setVisibility(View.INVISIBLE);
            } else {
                holder.cvHolder.setVisibility(View.VISIBLE);
                Picasso.get().load(clientAvatar)
                        .fit()
                        .into(holder.ivAvatar);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).isAdmin == isAdmin) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage, tvTime;
        private CardView cvHolder;
        private ImageView ivAvatar;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.textview_message);
            tvTime = itemView.findViewById(R.id.textview_time);
            cvHolder = itemView.findViewById(R.id.cardview_holder);
            ivAvatar = itemView.findViewById(R.id.imageview_avatar);
        }
    }

}
