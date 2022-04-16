package nhom7.clothnest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
        GetComment();

        return mView;
    }
    private void GetComment() {
        commentArrayList = new ArrayList<>();
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 4, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful, beautiful"));
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 4, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful"));
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 4, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful"));
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 4, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful"));
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 4, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful"));
        commentArrayList.add(new Comment("Phan Thanh Tu", R.drawable.avt_1, "15.04.2022 - 15:30", 4, "Type: white - NAM S", "Good product, beautiful, beautiful, beautiful"));

        commentAdapter = new CommentAdapter(getContext(), R.layout.comment_item, commentArrayList);
        listView.setAdapter(commentAdapter);
    }
}