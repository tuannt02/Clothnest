package nhom7.clothnest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.ProductDetailActivity;
import nhom7.clothnest.adapters.CommentAdapter;
import nhom7.clothnest.models.Comment;
import nhom7.clothnest.models.User;

public class CommentFragment extends Fragment {
    public static final String RELOAD_CODE = CommentFragment.class.getName();
    View mView;
    ListView listView;
    ArrayList<Comment> commentArrayList;
    CommentAdapter commentAdapter;
    Button btnWriteReview;
    public static WriteCommentFragment writeCommentFragment = new WriteCommentFragment();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_comment, container, false);

        reference();

        getEvents();

        getComment();

        return mView;
    }

    private void getEvents() {
        // setup listview to make it scrollable
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mView.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        mView.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                view.onTouchEvent(motionEvent);
                return true;
            }
        });

        btnWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailActivity.tvDescription.setVisibility(View.INVISIBLE);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.productDetail_frame, writeCommentFragment);
                transaction.addToBackStack(RELOAD_CODE);
                transaction.commit();
            }
        });
    }

    private void reference() {
        listView = mView.findViewById(R.id.list_Comment);
        btnWriteReview = mView.findViewById(R.id.commentFragment_WriteComment);
    }

    private void getComment() {
        commentArrayList = new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), R.layout.comment_item, commentArrayList);
        listView.setAdapter(commentAdapter);

        FirebaseFirestore.getInstance().collection("products").document(ProductDetailActivity.productID).collection("reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
                                listView.requestLayout();
                                listView.getLayoutParams().height = 0;
                            }
                            for (QueryDocumentSnapshot review_Ref : task.getResult()) {
                                DocumentReference review = review_Ref.getDocumentReference("review_ref");
                                review.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot review_Doc) {
                                        review_Doc.getDocumentReference("user")
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot user_Doc) {
                                                        User user = user_Doc.toObject(User.class);
                                                        Comment comment = new Comment(
                                                                user.getEMAIL(),
                                                                user.getIMG(),
                                                                review_Doc.getString("time"),
                                                                (int) Math.round(review_Doc.getDouble("star_number")),
                                                                review_Doc.getString("type"),
                                                                review_Doc.getString("review")
                                                        );
                                                        commentArrayList.add(comment);
                                                        commentAdapter.notifyDataSetChanged();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    }
                });

    }
}