package nhom7.clothnest;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CommentFragment extends Fragment {
    ListView listView;
    ArrayList<Comment> commentArrayList;
    CommentAdapter commentAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_comment, container, false);

        listView = mView.findViewById(R.id.list_Comment);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Listview clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        // setup listview to make it scrollable
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                switch (action){
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

        GetComment();

        return mView;
    }
    private void GetComment() {
        commentArrayList = new ArrayList<>();
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 4, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful"));
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 3, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful"));
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 2, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful"));
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 1, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful"));
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 2, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful"));
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 4, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful"));

        commentAdapter = new CommentAdapter(getContext(), R.layout.comment_item, commentArrayList);
        listView.setAdapter(commentAdapter);
    }
}