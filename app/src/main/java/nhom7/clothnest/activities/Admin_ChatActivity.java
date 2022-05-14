package nhom7.clothnest.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CustomMessageAdapter;
import nhom7.clothnest.models.ChatMessage;

public class Admin_ChatActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messagesRef;
    private DocumentReference chatRef;
    private ListenerRegistration messagesListener;
    private static final String TAG = "ChatActivity";
    private CustomMessageAdapter adapter;
    private List<ChatMessage> chatMessages;

    private ListView lvMessages;
    private ImageView btnSend, btnClose, ivAvatar;
    private TextInputEditText etMessage;
    private TextView tvName;

    private Boolean isAdminAccessed;
    private String chatRoomID, clientName, clientAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get chat room id from chat list fragments
        getChatRoomInfo();

        // init firestore references
        messagesRef = db.collection("chat").document(chatRoomID).collection("messages");
        chatRef = db.collection("chat").document(chatRoomID);

        // init view
        lvMessages = findViewById(R.id.listview_messages);
        btnSend = findViewById(R.id.imageview_send);
        etMessage = findViewById(R.id.edittext_message);
        btnClose = findViewById(R.id.imageview_close);
        tvName = findViewById(R.id.textview_name);
        ivAvatar = findViewById(R.id.imageview_avatar);

        // set data for view
        tvName.setText(clientName);
        Picasso.get().load(clientAvatar).into(ivAvatar);

        // setup listview
        chatMessages = new ArrayList<>();
        adapter = new CustomMessageAdapter(getApplicationContext(), chatMessages, true);
        lvMessages.setAdapter(adapter);

        // setup onclick listener
        btnSend.setOnClickListener(sendListener);
        btnClose.setOnClickListener(closeListener);
    }

    private void getChatRoomInfo() {
        chatRoomID = getIntent().getStringExtra("chatRoomID");
        clientAvatar = getIntent().getStringExtra("avatar");
        clientName = getIntent().getStringExtra("name");
    }

    private View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (etMessage.length() == 0) {
                return;
            }

            ChatMessage chatMessage = new ChatMessage(true, etMessage.getText().toString());
            messagesRef.add(chatMessage);

            Map<String, Object> data = new HashMap<>();
            data.put("recent_msg", chatMessage.getText());
            data.put("time", FieldValue.serverTimestamp());
            data.put("is_admin_read", true);
            data.put("is_admin_sent", true);
            chatRef.update(data);

            etMessage.getText().clear();
        }
    };

    private View.OnClickListener closeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        messagesListener = messagesRef
                .orderBy("time")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(Admin_ChatActivity.this, "Error while loading messages!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, error.toString());
                            return;
                        }

                        chatMessages.clear();

                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            ChatMessage message = documentSnapshot.toObject(ChatMessage.class);
                            chatMessages.add(message);
                        }

                        adapter.notifyDataSetChanged();

                        // Message read
                        chatRef.update("is_admin_read", true);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        messagesListener.remove();
    }
}