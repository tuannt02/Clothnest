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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CustomColorAdapter;
import nhom7.clothnest.interfaces.ActivityConstants;
import nhom7.clothnest.models.ClothColor;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class Admin_ColorActivity extends AppCompatActivity {
    private ArrayList<ClothColor> colors;
    private CustomColorAdapter adapter;
    private ListView lvColor;
    private CustomProgressBar customProgressBar;
    private TextView btnCancel;
    private TextView tvTitle;
    private TextView btnAdd;
    private static final String TAG = "Admin_ColorActivity";

    private Dialog dialog;
    private static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("\\p{XDigit}+");

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colorRef = db.collection("colors");
    private ListenerRegistration listenerRegistration;

    // search
    private EditText etSearch;
    private TextView btnClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        // init views
        etSearch = findViewById(R.id.edittext_search);
        btnClear = findViewById(R.id.textview_clear);
        tvTitle = findViewById(R.id.textview_title);
        btnCancel = findViewById(R.id.textview_cancel);
        btnAdd = findViewById(R.id.textview_add);

        // set initial data
        etSearch.setText("");

        // custom progress bar
        customProgressBar = new CustomProgressBar(this);

        // Setup list view color
        lvColor = findViewById(R.id.listview_color);
        colors = new ArrayList<>();
        adapter = new CustomColorAdapter(this, R.layout.item_size, colors);
        lvColor.setAdapter(adapter);

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

        listenerRegistration = colorRef
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(Admin_ColorActivity.this, "Error while loading", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        colors.clear();

                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            ClothColor color = documentSnapshot.toObject(ClothColor.class);
                            color.colorId = documentSnapshot.getId();
                            colors.add(color);
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
            case ActivityConstants.VIEW_COLOR:
                handleViewColor();
                break;
            case ActivityConstants.CHOOSE_COLOR:
                handleChooseColor();
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

    private void handleViewColor() {
        tvTitle.setText("Color");

        lvColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClothColor size = colors.get(i);
                createNewDialog(2, size);
            }
        });
    }

    private void handleChooseColor() {
        tvTitle.setText("Choose Color");

        lvColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("selected_color", colors.get(i).colorId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void createNewDialog(int dialogType, @Nullable ClothColor color) {
        dialog = new Dialog(Admin_ColorActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_color_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitle = dialog.findViewById(R.id.textview_title);
        TextInputEditText etName = dialog.findViewById(R.id.edittext_name);
        TextInputEditText etColor = dialog.findViewById(R.id.edittext_color);
        LinearLayout llColor = dialog.findViewById(R.id.linearLayout_color);
        TextView btnSave = dialog.findViewById(R.id.textview_save);

        etColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 7 && charSequence.charAt(0) == '#') {
                    if (isHexadecimal(charSequence.toString().substring(1))) {
                        llColor.setBackgroundColor(Color.parseColor(charSequence.toString()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        switch (dialogType) {
            case 1:
                tvTitle.setText("Add Color");
                break;
            case 2:
                tvTitle.setText("Edit Color");
                etName.setText(color.name);
                etColor.setText(color.hex);
                break;
            default:
                break;
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.length() != 0 && etColor.length() != 0) {
                    String name = etName.getText().toString();
                    String hex = etColor.getText().toString();
                    handleSizeDialogResult(name, hex, dialogType, color == null ? null : color.colorId);

                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    private void handleSizeDialogResult(String name, String hex, int dialogType, @Nullable String colorId) {
        ClothColor color = new ClothColor();
        color.hex = hex;
        color.name = name;

        switch (dialogType) {
            case 1: // add
                colorRef.document().set(color)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //retrieveSizeData();
                                Toast.makeText(Admin_ColorActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Admin_ColorActivity.this, "Added failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case 2: // update
                colorRef.document(colorId).set(color)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Admin_ColorActivity.this, "Edited successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Admin_ColorActivity.this, "Edited failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }

    private boolean isHexadecimal(String input) {
        final Matcher matcher = HEXADECIMAL_PATTERN.matcher(input);
        return matcher.matches();
    }
}