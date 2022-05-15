package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.Admin_ChatActivity;
import nhom7.clothnest.adapters.CustomChatListAdapter;
import nhom7.clothnest.models.ChatRoom;

public class ChatListFragment extends Fragment {
    private ListView lvChatList;
    private List<ChatRoom> chatRoomList;
    private CustomChatListAdapter adapter;
    private TextView btnClear;
    private TextInputEditText etSearch;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatListRef = db.collection("chat");
    private ListenerRegistration chatListListener;

    private static final String TAG = "ChatListFragment";

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        // init views
        lvChatList = view.findViewById(R.id.listview_chatList);
        btnClear = view.findViewById(R.id.textview_clear);
        etSearch = view.findViewById(R.id.edittext_search);


        // setup adapter
        chatRoomList = new ArrayList<>();
        adapter = new CustomChatListAdapter(getContext(), R.layout.chat_room, chatRoomList);
        lvChatList.setAdapter(adapter);

        // setup listview click
        lvChatList.setOnItemClickListener(lvClickListener);

        // setup clear click
        btnClear.setOnClickListener(clearListener);

        // setup on etSearch text change
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private AdapterView.OnItemClickListener lvClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getContext(), Admin_ChatActivity.class);
            intent.putExtra("chatRoomID", chatRoomList.get(i).chatRoomID);
            intent.putExtra("avatar", chatRoomList.get(i).avatar);
            intent.putExtra("name", chatRoomList.get(i).name);
            startActivity(intent);
        }
    };

    private View.OnClickListener clearListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            etSearch.getText().clear();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        etSearch.setText("");
        chatListListener = chatListRef
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Error while loading chat list!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error while loading chat list: " + error.toString());
                    return;
                }

                chatRoomList.clear();

                List<Task> tasks = new ArrayList<>();

                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    ChatRoom chatRoom = documentSnapshot.toObject(ChatRoom.class);
                    chatRoom.chatRoomID = documentSnapshot.getId();
                    chatRoomList.add(chatRoom);
                    tasks.add(chatRoom.userRef.get());
                }

                Task<List<DocumentSnapshot>> allTasks = Tasks.whenAllSuccess(tasks);

                allTasks.addOnSuccessListener(new OnSuccessListener<List<DocumentSnapshot>>() {
                    @Override
                    public void onSuccess(List<DocumentSnapshot> documentSnapshots) {
                        for (int i = 0; i < documentSnapshots.size(); i++) {
                            DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                            chatRoomList.get(i).avatar = documentSnapshot.getString("img");
                            chatRoomList.get(i).name = documentSnapshot.getString("name");
                        }

                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error while loading users!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Error while loading users: " + error.toString());
                    }
                });
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        chatListListener.remove();
    }
}