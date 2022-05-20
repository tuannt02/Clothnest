package nhom7.clothnest.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.BannerAdapter;
import nhom7.clothnest.models.Banner;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Voucher;
import nhom7.clothnest.util.OpenGallery;
import nhom7.clothnest.util.customizeComponent.CustomDialog;
import nhom7.clothnest.util.customizeComponent.CustomDialogAdminAdd;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;
import nhom7.clothnest.util.customizeComponent.CustomToast;

public class Admin_BannersActivity extends AppCompatActivity {
    public static final int REQ_CODE_READ_EXTERNAL_STORAGE = 10;
    private Uri mUri;
    private int action = -1;
    private String docIDtemp = "";
    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if(intent == null)  return;
                        mUri = intent.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);

                            // Set img from gallery to bitmap var
                            if(action == 2) {
                                addBannerItemToFirestore(bitmap);
                            }

                            if(action == 1) {
                                updateBannerToFirestore(docIDtemp, bitmap);
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    private TextView btnClose;
    private ListView lvBanner;
    private ArrayList<Banner> bannerArrayList;
    private BannerAdapter bannerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_banners);

        //init UI
        initUi();

        // set on click
        setOnClick();

        bannerAdapter = new BannerAdapter(Admin_BannersActivity.this, R.layout.banner_item, bannerArrayList);

        lvBanner.setAdapter(bannerAdapter);

        getBannersAndShowOnListview();
    }

    private void initUi()   {
        btnClose = findViewById(R.id.admin_banner_btn_close);
        lvBanner = findViewById(R.id.admin_banner_listview);
        bannerArrayList = new ArrayList<>();
    }

    private void setOnClick()   {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lvBanner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> list = getListOptionMenu();
                Banner bannerItem = bannerArrayList.get(i);
                String docID = bannerItem.getDocID();
                String img = bannerItem.getImg();

                if(i == bannerArrayList.size() - 1) {
                    action = 2;
                    OpenGallery.onClickOpenGallery(Admin_BannersActivity.this, mActivityResultLauncher);

                    return;
                }

                CustomOptionMenu orderOptionMenu = new CustomOptionMenu(Admin_BannersActivity.this, new CustomOptionMenu.IClickListenerOnItemListview() {
                    @Override
                    public void onClickItem(int pos) {
                        if(pos == 0)    { // Update banner
                            action = 1;
                            OpenGallery.onClickOpenGallery(Admin_BannersActivity.this, mActivityResultLauncher);
                            docIDtemp = docID;

                        }

                        if(pos == 1)    { // Remove banner
                            CustomDialog comfirmRemove = new CustomDialog(Admin_BannersActivity.this,
                                    "Confirm remove",
                                    "Are you sure you want to delete this Banner",
                                    2,
                                    new CustomDialog.IClickListenerOnOkBtn() {
                                        @Override
                                        public void onResultOk() {
                                            // Call api firestore
                                            removeBannerItemFromFirestore(docID);
                                            bannerArrayList.clear();
                                            getBannersAndShowOnListview();
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

    private void getBannersAndShowOnListview()  {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy tất cả banner
        db.collection(Banner.COLLECTION_NAME)
                .orderBy("date_add", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Lặp qua từng document
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Banner bannerItem = new Banner();
                                Map<String, Object> tempObject;
                                tempObject = document.getData();

                                String docID = document.getId();
                                bannerItem.setDocID(docID);

                                // Lặp qua từng field của một document
                                Iterator myVeryOwnIterator = tempObject.keySet().iterator();
                                while(myVeryOwnIterator.hasNext()) {
                                    String key=(String)myVeryOwnIterator.next();

                                    if(key.equals("img"))   {
                                        String name = (String)tempObject.get(key);
                                        bannerItem.setImg(name);
                                    }


                                }

                                bannerArrayList.add(bannerItem);
                                bannerAdapter.notifyDataSetChanged();

                            }

                            bannerArrayList.add(new Banner("", "https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/utils%2Fadd_banner.png?alt=media&token=76b3e902-6b8e-4977-9d37-3adbf76d9896"));
                            bannerAdapter.notifyDataSetChanged();
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void removeBannerItemFromFirestore(String docID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Banner.COLLECTION_NAME).document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CustomToast.DisplayToast(Admin_BannersActivity.this, 4, "Remove successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CustomToast.DisplayToast(Admin_BannersActivity.this, 2, "Remove failure");
                    }
                });
    }

    private void addBannerItemToFirestore(Bitmap bitmap)   {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("date_add", new Timestamp(new Date()));
        data.put("img", "");

        db.collection(Banner.COLLECTION_NAME)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference bannerRef = storageRef.child("banners/" + documentReference.getId());

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = bannerRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                System.out.println(exception.toString());
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                        new OnCompleteListener<Uri>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                String fileLink = task.getResult().toString();
                                                //next work with URL
                                                System.out.println(fileLink);

                                                // Update one field, creating the document if it does not already exist.
                                                Map<String, Object> data = new HashMap<>();
                                                data.put("img", fileLink);

                                                db.collection(Banner.COLLECTION_NAME).document(documentReference.getId())
                                                        .set(data, SetOptions.merge())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                bannerArrayList.clear();
                                                                bannerAdapter.notifyDataSetChanged();
                                                                getBannersAndShowOnListview();
                                                            }
                                                        });

                                            }
                                        });
                            }
                        });
                        CustomToast.DisplayToast(Admin_BannersActivity.this, 1, "Add voucher successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    private void updateBannerToFirestore(String docID, Bitmap bitmap)    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference bannerRef = storageRef.child("banners/" + docID);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = bannerRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println(exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                        new OnCompleteListener<Uri>() {

                            // Mỗi Url là duy nhất cho một lần upload ảnh. Vậy nên mỗi khi đổi
                            // ảnh mới phải đổi luôn cái token. Gán token mới này vào firestore
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String fileLink = task.getResult().toString();
                                //next work with URL
                                System.out.println(fileLink);

                                // Update one field, creating the document if it does not already exist.
                                Map<String, Object> data = new HashMap<>();
                                data.put("img", fileLink);

                                db.collection(Banner.COLLECTION_NAME).document(docID)
                                        .set(data, SetOptions.merge())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                bannerArrayList.clear();
                                                getBannersAndShowOnListview();
                                            }
                                        });

                            }
                        });
                CustomToast.DisplayToast(Admin_BannersActivity.this, 1, "Upload successfully");
            }
        });
    }


    private ArrayList<String> getListOptionMenu()    {
        ArrayList<String> list = new ArrayList<>();
        list.add("Update Banner");
        list.add("Remove Banner");

        return list;
    }

    private int[] getListImg()  {
        int[] listImg = {R.drawable.ic_update,
                R.drawable.trash};

        return listImg;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_CODE_READ_EXTERNAL_STORAGE)   {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                OpenGallery.openGallery(mActivityResultLauncher);
            }
            else    {
                Toast.makeText(this, "You denied, please allow the app to access the gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }
}