package nhom7.clothnest.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.Admin_ProductDetailActivity;
import nhom7.clothnest.activities.ProductDetailActivity;
import nhom7.clothnest.adapters.ColorAdapter;
import nhom7.clothnest.adapters.SizeAdapter;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.Comment;
import nhom7.clothnest.models.SizeClass;
import nhom7.clothnest.models.User;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;
import nhom7.clothnest.util.customizeComponent.CustomToast;

public class WriteCommentFragment extends Fragment {
    //View
    private View mView;
    private ImageView ivAvt;
    private TextView tvUserName;
    private Spinner cbxColor, cbxSize;
    private ArrayList<ImageView> starNumberList;
    private EditText etWriteReview;
    private Button btnPost;
    private CustomProgressBar dialog;
    private Comment comment;
    FirebaseFirestore db;
    DocumentReference user_Ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_write_comment, container, false);
        comment = new Comment();
        db = FirebaseFirestore.getInstance();
        reference();

        getEvents();

        return mView;
    }

    private void getData() {
        dialog = new CustomProgressBar(getContext());
        setStarsColor(5);

        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        user_Ref = FirebaseFirestore.getInstance().collection(User.COLLECTION_NAME).document(userInfo.getUid());
        user_Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                Glide.with(WriteCommentFragment.this).load(user.getIMG()).error(R.drawable.ic_avatar_default).into(ivAvt);
                comment.setAvt(user.getIMG());

                tvUserName.setText(user.getNAME());
                comment.setName(user.getNAME());
            }
        });

        getSpinnerData();
    }

    private void getEvents() {
        for (ImageView star : starNumberList) {
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setStarsColor(starNumberList.indexOf(star) + 1);
                }
            });
        }

        btnPost.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                dialog.show();

                Map<String, Object> commentMap = new HashMap<>();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime local = LocalDateTime.now();
                String selectedColor = ((ColorClass)cbxColor.getSelectedItem()).getName();
                String selectedSize = ((SizeClass)cbxSize.getSelectedItem()).getName();

                commentMap.put("user", user_Ref);
                commentMap.put("star_number", comment.getStarNumber());
                commentMap.put("type", selectedColor + " - " + selectedSize);
                commentMap.put("review", etWriteReview.getText().toString());
                commentMap.put("time", formatter.format(local));

                db.collection("reviews").add(commentMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Map<String, Object> review = new HashMap<>();
                                review.put("review_ref", documentReference);
                                db.collection("products").document(ProductDetailActivity.productID).collection("reviews")
                                        .document(documentReference.getId())
                                        .set(review)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                if(dialog.isShowing())
                                                    dialog.dismiss();
                                                CustomToast.DisplayToast(getContext(), 1, "write review successfully");
                                            }
                                        });
                            }
                        });
            }
        });
    }

    private void setStarsColor(int i) {
        for (int j = 0; j < starNumberList.size(); j++)
            if (j < i)
                starNumberList.get(j).setImageResource(R.drawable.ic_star);
            else
                starNumberList.get(j).setImageResource(R.drawable.ic_unstar);
        comment.setStarNumber(i);
    }

    private void reference() {
        ivAvt = mView.findViewById(R.id.writeComment_Avata);
        tvUserName = mView.findViewById(R.id.writeComment_UserName);
        cbxColor = mView.findViewById(R.id.writeComment_spnColor);
        cbxSize = mView.findViewById(R.id.writeComment_spnSize);

        starNumberList = new ArrayList<>();
        starNumberList.add(mView.findViewById(R.id.writeComment_StarNumber1));
        starNumberList.add(mView.findViewById(R.id.writeComment_StarNumber2));
        starNumberList.add(mView.findViewById(R.id.writeComment_StarNumber3));
        starNumberList.add(mView.findViewById(R.id.writeComment_StarNumber4));
        starNumberList.add(mView.findViewById(R.id.writeComment_StarNumber5));

        etWriteReview = mView.findViewById(R.id.writeComment_etWriteReview);
        btnPost = mView.findViewById(R.id.writeComment_btnPost);

        getData();
    }

    private void getSpinnerData() {
        dialog.show();
        DocumentReference product_Ref = FirebaseFirestore.getInstance().collection("products").document(ProductDetailActivity.productID);

        Task<QuerySnapshot> getColor = product_Ref.collection("colors").get();
        Task<QuerySnapshot> getSize = product_Ref.collection("sizes").get();
        Task<List<QuerySnapshot>> listTask = Tasks.whenAllSuccess(getColor, getSize);

        listTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                ArrayList<ColorClass> colorList = new ArrayList<>();
                ArrayList<SizeClass> sizeList = new ArrayList<>();

                for (DocumentSnapshot color_Doc : querySnapshots.get(0).getDocuments()) {
                    ColorClass color = new ColorClass();
                    color.setHex(color_Doc.getString("hex"));
                    color.setName(color_Doc.getString("name"));
                    colorList.add(color);

                    if (querySnapshots.get(0).getDocuments().indexOf(color_Doc) == querySnapshots.get(0).size() - 1) {
                        comment.setColorList(colorList);
                        ColorAdapter colorAdapter = new ColorAdapter(getContext(), R.layout.color_item_selected, colorList);
                        cbxColor.setAdapter(colorAdapter);
                        cbxColor.setSelection(comment.getPosSelectedColor());
                    }
                }

                for (DocumentSnapshot size_Doc : querySnapshots.get(1).getDocuments()) {
                    SizeClass size = new SizeClass();
                    size.setShort_name(size_Doc.getString("short_name"));
                    size.setName(size_Doc.getString("name"));
                    sizeList.add(size);

                    if (querySnapshots.get(1).getDocuments().indexOf(size_Doc) == querySnapshots.get(0).size() - 1) {
                        comment.setSizeList(sizeList);
                        SizeAdapter sizeAdapter = new SizeAdapter(getContext(), R.layout.size_item_selected, sizeList);
                        cbxSize.setAdapter(sizeAdapter);
                        cbxSize.setSelection(comment.getPosSelectedSize());
                    }
                }

                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }
}