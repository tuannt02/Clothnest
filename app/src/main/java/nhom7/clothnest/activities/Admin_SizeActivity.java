package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CustomSizeAdapter;
import nhom7.clothnest.interfaces.ActivityConstants;
import nhom7.clothnest.models.Size;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class Admin_SizeActivity extends AppCompatActivity {
    private ArrayList<Size> sizes;
    private CustomSizeAdapter adapter;
    private ListView lvSize;
    private CustomProgressBar customProgressBar;
    private TextView btnCancel;
    private TextView tvTitle;
    private TextView btnAdd;
    private Dialog dialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference sizeRef = db.collection("sizes");
    private ListenerRegistration listenerRegistration;

    // search
    private EditText etSearch;
    private TextView btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size);

        // init views
        etSearch = findViewById(R.id.edittext_search);
        btnClear = findViewById(R.id.textview_clear);
        tvTitle = findViewById(R.id.textview_title);
        btnCancel = findViewById(R.id.textview_cancel);
        btnAdd = findViewById(R.id.textview_add);

        // set initial data
        etSearch.setText("");

        // Setup list view size
        lvSize = findViewById(R.id.listview_size);
        sizes = new ArrayList<>();
        adapter = new CustomSizeAdapter(this, R.layout.item_size, sizes);
        lvSize.setAdapter(adapter);

        // custom progress bar
        customProgressBar = new CustomProgressBar(this);

        // setup search on change event
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

        // Clear button event
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.getText().clear();
            }
        });

        // Cancel button event
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        handleExtra();
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveSizeData();
    }

    private void retrieveSizeData() {
        customProgressBar.show();

        listenerRegistration = sizeRef
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(Admin_SizeActivity.this, "Error while loading", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        sizes.clear();

                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            Size size = documentSnapshot.toObject(Size.class);
                            size.sizeId = documentSnapshot.getId();
                            sizes.add(size);
                        }

                        adapter.notifyDataSetChanged();
                        customProgressBar.dismiss();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listenerRegistration != null)
            listenerRegistration.remove();
    }

    private void handleExtra() {
        Intent intent = getIntent();
        int activityType = intent.getIntExtra("activity_type", -1);

        switch (activityType) {
            case ActivityConstants.VIEW_SIZE:
                handleViewSize();
                break;
            case ActivityConstants.CHOOSE_SIZE:
                handleChooseSize();
                break;
            default:
                break;
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewDialog(1, null);
            }
        });
    }

    private void handleViewSize() {
        tvTitle.setText("Size");

        lvSize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Size size = sizes.get(i);
                createNewDialog(2, size);
            }
        });
    }

    private void handleChooseSize() {
        tvTitle.setText("Choose Size");

        lvSize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("selected_size", sizes.get(i).sizeId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void createNewDialog(int dialogType, @Nullable Size size) {
        dialog = new Dialog(Admin_SizeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_size_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitle = dialog.findViewById(R.id.textview_title);
        TextInputEditText etName = dialog.findViewById(R.id.edittext_name);
        TextInputEditText etShortName = dialog.findViewById(R.id.edittext_shortName);
        TextInputEditText etType = dialog.findViewById(R.id.edittext_type);
        TextView btnSave = dialog.findViewById(R.id.textview_save);

        switch (dialogType) {
            case 1:
                tvTitle.setText("Add Size");
                break;
            case 2:
                tvTitle.setText("Edit Size");
                etName.setText(size.name);
                etShortName.setText(size.shortName);
                etType.setText(size.type);
                break;
            default:
                break;
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.length() != 0 && etShortName.length() != 0 && etType.length() != 0) {
                    String name = etName.getText().toString();
                    String shortName = etShortName.getText().toString();
                    String type = etType.getText().toString();
                    handleSizeDialogResult(name, shortName, type, dialogType, size == null ? null : size.sizeId);

                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }


    private void handleSizeDialogResult(String name, String shortName, String type, int dialogType, @Nullable String sizeId) {
        Size size = new Size();
        size.name = name;
        size.shortName = shortName;
        size.type = type;

        switch (dialogType) {
            case 1: // add
                sizeRef.document().set(size)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //retrieveSizeData();
                                Toast.makeText(Admin_SizeActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Admin_SizeActivity.this, "Added failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case 2: // update
                sizeRef.document(sizeId).set(size)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Admin_SizeActivity.this, "Edited successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Admin_SizeActivity.this, "Edited failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }
}