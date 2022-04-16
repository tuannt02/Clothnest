package nhom7.clothnest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AddressBookActivity extends AppCompatActivity {
    private ImageView btnBack;
    private MaterialButton btnAddNewAddress;
    private ListView lvAddressBook;
    private ArrayList<Address> addresses = null;
    private CustomAddressAdapter adapter;

    private int currIndex = -1;

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

        // Thiet lap LocalBroadcastReceiver
        setupBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    private void setupBtnAddNewAddress() {
        btnAddNewAddress = findViewById(R.id.button_addNewAddress_address);
        btnAddNewAddress.setOnClickListener(btnAddNewAddressListener);
    }

    private void setupListViewAddressBook() {
        addresses = new ArrayList<>();

        lvAddressBook = findViewById(R.id.listview_addressBook);
        addresses.add(new Address("Hoang Dinh Anh Tuan", "Ho Chi Minh", "Thu Duc", "Linh Trung", "KTX Khu A", "0849167234"));
        Log.d("setupAddressBook", "Add address successfully");
        adapter = new CustomAddressAdapter(addresses, getApplicationContext());
        Log.d("setupAddressBook", "Create custom adapter instance successfully");
        lvAddressBook.setAdapter(adapter);
        Log.d("setupAddressBook", "Setup successfully");
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
                        addresses.add(address);
                        adapter.notifyDataSetChanged();
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
                                addresses.remove(currIndex);
                                break;
                            case 1:
                                Address address = (Address) intent.getExtras().getSerializable("new-address");
                                addresses.set(currIndex, address);
                                break;
                            default:
                                break;
                        }

                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );
}