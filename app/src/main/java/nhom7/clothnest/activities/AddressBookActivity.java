package nhom7.clothnest.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.interfaces.ActivityConstants;
import nhom7.clothnest.models.Address;
import nhom7.clothnest.adapters.CustomAddressAdapter;
import nhom7.clothnest.R;
import nhom7.clothnest.notifications.NetworkChangeReceiver;
import nhom7.clothnest.util.Constants;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class AddressBookActivity extends AppCompatActivity {
    private ImageView btnBack;
    private MaterialButton btnAddNewAddress;
    private ListView lvAddressBook;
    private ArrayList<Address> addresses = null;
    private CustomAddressAdapter adapter;

    private CustomProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference addrRef = db.collection("users").document(Constants.getUserId()).collection("addr");

    private int currIndex = -1;
    private static final String TAG = "AddressBookActivity";

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        // Thiet lap button back
        btnBack = findViewById(R.id.imageview_back_addressBook);
        btnBack.setOnClickListener(btnBackListener);

        // Thiet lap button add new address
        setupBtnAddNewAddress();

        // Thiet lap list view address book
        setupListViewAddressBook();

        // Xac dinh activity duoc mo duoi hanh dong nao
        specifyOnCreateBehavior();

        // Thiet lap LocalBroadcastReceiver
        setupBroadcastReceiver();

        progressBar = new CustomProgressBar(this);
        progressBar.show();

        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveAddresses();
    }

    private void retrieveAddresses() {
        addresses.clear();
        addrRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()) {
                            Address address = snapshot.toObject(Address.class);
                            address.addressId = snapshot.getId();
                            addresses.add(address);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.dismiss();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(broadcastReceiver);
    }

    private void setupBtnAddNewAddress() {
        btnAddNewAddress = findViewById(R.id.button_addNewAddress_address);
        btnAddNewAddress.setOnClickListener(btnAddNewAddressListener);
    }

    private void setupListViewAddressBook() {
        addresses = new ArrayList<>();

        lvAddressBook = findViewById(R.id.listview_addressBook);

        adapter = new CustomAddressAdapter(addresses, getApplicationContext());
        lvAddressBook.setAdapter(adapter);
    }

    private View.OnClickListener btnAddNewAddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
            intent.putExtra("calling-activity", ActivityConstants.ADD_ADDRESS);
            activityResultLauncher_addNewAddress.launch(intent);
        }
    };

    private View.OnClickListener btnBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AddressBookActivity.super.onBackPressed();
        }
    };

    // alternative start activity for result
    private ActivityResultLauncher<Intent> activityResultLauncher_addNewAddress = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Address address = (Address) result.getData().getExtras().getSerializable("data");
                        addNewAddress(address);
                    }
                }
            }
    );

    private void setupBroadcastReceiver() {
        IntentFilter filter = new IntentFilter("edit-address");
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, filter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Address address = (Address) intent.getExtras().getSerializable("address");
            currIndex = intent.getIntExtra("index", -1);

            Intent intent_editAddress = new Intent(getApplicationContext(), AddAddressActivity.class);

            if (address == null) {
                Log.d("addressBookActivity", "Address received from CustomAddressAdapter is null ");
            }

            intent_editAddress.putExtra("address", address);
            intent_editAddress.putExtra("calling-activity", ActivityConstants.EDIT_ADDRESS);
            launcher_editAddress.launch(intent_editAddress);
        }
    };

    private ActivityResultLauncher<Intent> launcher_editAddress = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("addressBookActivity", "Received result successfully with ResultCode: " + result.getResultCode());
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        int actionCode = intent.getIntExtra("action-code", -1);

                        // Escape the function if currIndex == -1;
                        if (currIndex == -1)
                            return;

                        switch (actionCode) {
                            case 0:
                                String addressId = intent.getStringExtra("address-id");
                                removeAddress(addressId);
                                break;
                            case 1:
                                Address address = (Address) intent.getExtras().getSerializable("new-address");
                                addNewAddress(address);
                                break;
                            default:
                                break;
                        }

                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );

    private void addNewAddress(Address address) {
        String addressId = address.addressId;
        DocumentReference newAddress = addressId == null ? addrRef.document() : addrRef.document(addressId);
        newAddress.set(address)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        retrieveAddresses();
                    }
                });
    }

    private void removeAddress(String addressId) {
        addrRef.document(addressId).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        retrieveAddresses();
                    }
                });
    }

    private void specifyOnCreateBehavior() {
        Intent intent = getIntent();
        int type = intent.getIntExtra("activity_type", -1);
        if (type == ActivityConstants.CHOOSE_ADDRESSES) {
            lvAddressBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent result = new Intent().putExtra("selected_address", addresses.get(position).addressId);
                    setResult(RESULT_OK, result);
                    finish();
                }
            });
        }
    }
}