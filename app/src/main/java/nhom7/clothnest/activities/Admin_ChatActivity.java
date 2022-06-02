package nhom7.clothnest.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
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
import nhom7.clothnest.adapters.CustomMessageRecyclerAdapter;
import nhom7.clothnest.models.ChatMessage;
import nhom7.clothnest.notifications.NetworkChangeReceiver;

public class Admin_ChatActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messagesRef;
    private DocumentReference chatRef;
    private ListenerRegistration messagesListener;
    private static final String TAG = "ChatActivity";
    //private CustomMessageRecyclerAdapter adapter;
    private CustomMessageAdapter adapter;
    private List<ChatMessage> chatMessages;

    private ListView lvMessages;
    private ImageView btnSend, btnClose, ivAvatar;
    private TextInputEditText etMessage;
    private TextView tvName;

    private Boolean isAdminAccessed, isRoomCreated = true;
    private String chatRoomID, clientName, clientAvatar;

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get chat room id from chat list fragments
        getChatRoomInfo();

        // init firestore references
        if (isRoomCreated) {
            messagesRef = db.collection("chat").document(chatRoomID).collection("messages");
            chatRef = db.collection("chat").document(chatRoomID);
        }

        // init view
        lvMessages = findViewById(R.id.recycler_view_messages);
        btnSend = findViewById(R.id.imageview_send);
        etMessage = findViewById(R.id.edittext_message);
        btnClose = findViewById(R.id.imageview_close);
        tvName = findViewById(R.id.textview_name);
        ivAvatar = findViewById(R.id.imageview_avatar);

        // set data for view (admin side)
        if (isAdminAccessed) {
            tvName.setText(clientName);
            Picasso.get().load(clientAvatar).into(ivAvatar);
        }

        // setup listview
        chatMessages = new ArrayList<>();
        adapter = new CustomMessageAdapter(getApplicationContext(), chatMessages, isAdminAccessed, clientAvatar);
        lvMessages.setAdapter(adapter);

        // setup onclick listener
        btnSend.setOnClickListener(sendListener);
        btnClose.setOnClickListener(closeListener);

        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void getChatRoomInfo() {
        isAdminAccessed = getIntent().getBooleanExtra("isAdminAccessed", true);
        chatRoomID = getIntent().getStringExtra("chatRoomID");

        if (isAdminAccessed) {
            clientAvatar = getIntent().getStringExtra("avatar");
            clientName = getIntent().getStringExtra("name");
        } else {
            if (chatRoomID == null) {
                isRoomCreated = false;
            }
        }
    }

    private View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (etMessage.length() == 0) {
                return;
            }

            ChatMessage chatMessage = new ChatMessage(isAdminAccessed, etMessage.getText().toString());

            String title = clientName == null ? "Admin" : clientName;

            // If client never chats, a new chat room will be created
            Map<String, Object> data = new HashMap<>();
            if (!isRoomCreated) {
                String currUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference currUserRef = db.collection("users").document(currUserUid);

                // Create new chat room data
                data.put("userRef", currUserRef);
                data.put("recent_msg", chatMessage.getText());
                data.put("time", FieldValue.serverTimestamp());
                data.put("is_admin_read", isAdminAccessed);
                data.put("is_client_read", !isAdminAccessed);
                data.put("is_admin_sent", isAdminAccessed);

                db.collection("chat").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        chatRoomID = documentReference.getId();

                        // init references
                        messagesRef = db.collection("chat").document(chatRoomID).collection("messages");
                        chatRef = db.collection("chat").document(chatRoomID);

                        // create new listener
                        messagesListener = messagesRef
                                .orderBy("time")
                                .addSnapshotListener(messagesEventListener);
                        isRoomCreated = true;

                        // Send message
                        etMessage.getText().clear();
                        messagesRef.add(chatMessage);
                    }
                });
            } else {
                data.put("recent_msg", chatMessage.getText());
                data.put("time", FieldValue.serverTimestamp());
                data.put("is_admin_read", isAdminAccessed);
                data.put("is_client_read", !isAdminAccessed);
                data.put("is_admin_sent", isAdminAccessed);
                chatRef.update(data);
                etMessage.getText().clear();
                messagesRef.add(chatMessage);
            }
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
        if (!isRoomCreated) {
            return;
        }
        messagesListener = messagesRef
                .orderBy("time")
                .addSnapshotListener(messagesEventListener);

    }

    private EventListener<QuerySnapshot> messagesEventListener = new EventListener<QuerySnapshot>() {
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
            if (isAdminAccessed)
                chatRef.update("is_admin_read", true);
            if (!isAdminAccessed)
                chatRef.update("is_client_read", true);
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        if (isRoomCreated && messagesListener != null) {
            messagesListener.remove();
        }
    }

}