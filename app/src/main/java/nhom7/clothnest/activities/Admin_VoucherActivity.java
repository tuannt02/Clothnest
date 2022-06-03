package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.VoucherAdapter;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Voucher;
import nhom7.clothnest.notifications.NetworkChangeReceiver;
import nhom7.clothnest.util.customizeComponent.CustomDialog;
import nhom7.clothnest.util.customizeComponent.CustomDialogAdminAdd;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;
import nhom7.clothnest.util.customizeComponent.CustomToast;

public class Admin_VoucherActivity extends AppCompatActivity {

    TextView btnAdd;
    TextView btnClose;
    GridView gridViewVoucher;
    ArrayList<Voucher> voucherArrayList;
    VoucherAdapter voucherAdapter;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_voucher);

        // Init ui
        initUi();


        // Set on click listener
        setOnClickListener();

        voucherArrayList = new ArrayList<>();
        voucherAdapter = new VoucherAdapter(Admin_VoucherActivity.this, R.layout.voucher_item, voucherArrayList);
        gridViewVoucher.setAdapter(voucherAdapter);


        getVoucherAndShowOnGridview();

        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void initUi()   {
        btnAdd = findViewById(R.id.admin_voucher_btn_add);
        btnClose = findViewById(R.id.admin_voucher_btn_close);
        gridViewVoucher = findViewById(R.id.admin_voucher_gridview);
        voucherArrayList = new ArrayList<>();
    }

    private void setOnClickListener()   {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogAdminAdd customDialogAdminAdd = new CustomDialogAdminAdd(Admin_VoucherActivity.this,
                        "Add voucher",
                        2,
                        "Code",
                        "Discount",
                        "",
                        -1,
                        new CustomDialogAdminAdd.IClickListenerOnSaveBtn() {
                            @Override
                            public void onSubmit(String txtInput1, String txtInput2) {
                                voucherArrayList.clear();
                                addVoucherToFirestore(txtInput1, txtInput2);
                                getVoucherAndShowOnGridview();
                            }
                        });

                customDialogAdminAdd.show();
            }
        });

        gridViewVoucher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> list = getListOptionMenu();
                Voucher voucherItem = voucherArrayList.get(i);
                String docID = voucherItem.getDocID();
                String txtCode = voucherItem.getCode();
                int txtDisc = voucherItem.getDiscount();

                CustomOptionMenu orderOptionMenu = new CustomOptionMenu(Admin_VoucherActivity.this, new CustomOptionMenu.IClickListenerOnItemListview() {
                    @Override
                    public void onClickItem(int pos) {
                        if(pos == 0)    { // Update voucher
                            CustomDialogAdminAdd customDialogAdminAdd = new CustomDialogAdminAdd(Admin_VoucherActivity.this,
                                    "Update voucher",
                                    2,
                                    "Code",
                                    "Discount",
                                    txtCode,
                                    txtDisc,
                                    new CustomDialogAdminAdd.IClickListenerOnSaveBtn() {
                                        @Override
                                        public void onSubmit(String txtInput1, String txtInput2) {
                                            updateVoucherToFirestore(docID, txtInput1, Integer.valueOf(txtInput2));
                                            voucherArrayList.clear();
                                            getVoucherAndShowOnGridview();
                                        }
                                    });

                            customDialogAdminAdd.show();
                        }

                        if(pos == 1)    { // Remove voucher
                            CustomDialog comfirmRemove = new CustomDialog(Admin_VoucherActivity.this,
                                    "Confirm remove",
                                    "Are you sure you want to delete this Voucher",
                                    2,
                                    new CustomDialog.IClickListenerOnOkBtn() {
                                        @Override
                                        public void onResultOk() {
                                            // Call api firestore
                                            removeVoucherItemFromFirestore(docID);
                                            voucherArrayList.clear();
                                            getVoucherAndShowOnGridview();
                                        }
                                    });

                            comfirmRemove.show();
                        }

                    }
                },list, "OPTION", getListImg());
                orderOptionMenu.show();
            }
        });
    }


    private void getVoucherAndShowOnGridview()  {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy tất cả voucher
        db.collection(Voucher.COLLECTION_NAME)
                .orderBy("discount", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Lặp qua từng document
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Voucher voucherItem = new Voucher();
                                Map<String, Object> tempObject;
                                tempObject = document.getData();

                                String docID = document.getId();
                                voucherItem.setDocID(docID);

                                // Lặp qua từng field của một document
                                Iterator myVeryOwnIterator = tempObject.keySet().iterator();
                                while(myVeryOwnIterator.hasNext()) {
                                    String key=(String)myVeryOwnIterator.next();

                                    if(key.equals("code"))   {
                                        String name = (String)tempObject.get(key);
                                        voucherItem.setCode(name);
                                    }



                                    if(key.equals("discount"))   {
                                        int type =Integer.valueOf(tempObject.get(key).toString());
                                        voucherItem.setDiscount(type);
                                    }


                                }

                                voucherArrayList.add(voucherItem);
                                voucherAdapter.notifyDataSetChanged();

                            }
//                            customProgressBar.dismiss();
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void addVoucherToFirestore(String code, String discount)    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        int disc = Integer.valueOf(discount);
        Map<String, Object> data = new HashMap<>();
        data.put("code", code);
        data.put("discount", disc);

        db.collection(Voucher.COLLECTION_NAME)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        CustomToast.DisplayToast(Admin_VoucherActivity.this, 1, "Add voucher successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void removeVoucherItemFromFirestore(String docID)   {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Voucher.COLLECTION_NAME).document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CustomToast.DisplayToast(Admin_VoucherActivity.this, 4, "Remove successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CustomToast.DisplayToast(Admin_VoucherActivity.this, 2, "Remove failure");
                    }
                });
    }

    private void updateVoucherToFirestore(String docID, String code, int discount)  {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
        data.put("code", code);
        data.put("discount", discount);

        db.collection(Voucher.COLLECTION_NAME).document(docID)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        CustomToast.DisplayToast(Admin_VoucherActivity.this, 4, "Update voucher successfully");
                    }
                });
    }

    private ArrayList<String> getListOptionMenu()   {
        ArrayList<String> list = new ArrayList<>();
        list.add("Update Voucher");
        list.add("Remove Voucher");

        return list;
    }

    private int[] getListImg()  {
        int[] listImg = {R.drawable.ic_update,
                R.drawable.trash};

        return listImg;
    }
}