package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.ChatRoom;

public class CustomChatListAdapter extends ArrayAdapter<ChatRoom> {
    Context context;
    List<ChatRoom> chatRoomList;
    List<ChatRoom> originChatRoomList;

    public CustomChatListAdapter(@NonNull Context context, int resource, @NonNull List<ChatRoom> objects) {
        super(context, resource, objects);
        this.context = context;
        chatRoomList = objects;
        originChatRoomList = objects;
    }

    @Override
    public int getCount() {
        return chatRoomList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.chat_room, viewGroup, false);

        ChatRoom chatRoom = chatRoomList.get(i);

        ImageView ivAvatar;
        CardView ivReadDot;
        TextView tvName, tvMessage;

        ivAvatar = view.findViewById(R.id.imageview_avatar);
        tvName = view.findViewById(R.id.textview_name);
        tvMessage = view.findViewById(R.id.textview_message);
        ivReadDot = view.findViewById(R.id.imageview_readDot);

        Picasso.get().load(chatRoom.avatar).into(ivAvatar);
        tvName.setText(chatRoom.name);
        tvMessage.setText(chatRoom.recentMsg);
        if (chatRoom.isAdminRead) {
            tvMessage.setTypeface(ResourcesCompat.getFont(context, R.font.lato));
            ivReadDot.setVisibility(View.GONE);
            tvName.setTypeface(ResourcesCompat.getFont(context, R.font.lato_bold));
        }

        if (chatRoom.isAdminSent) {
            tvMessage.setText("You: " + chatRoom.recentMsg);
        }

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                chatRoomList = (List<ChatRoom>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint.equals("")) {
                    results.count = originChatRoomList.size();
                    results.values = originChatRoomList;
                    return results;
                }

                List<ChatRoom> filteredChatRooms = new ArrayList<ChatRoom>();


                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < originChatRoomList.size(); i++) {
                    String clientName = originChatRoomList.get(i).name;
                    if (clientName.toLowerCase().startsWith(constraint.toString()))  {
                        filteredChatRooms.add(originChatRoomList.get(i));
                    }
                }

                results.count = filteredChatRooms.size();
                results.values = filteredChatRooms;

                return results;
            }
        };

        return filter;
    }
}
