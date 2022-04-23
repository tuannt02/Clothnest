package nhom7.clothnest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    Context mContext;
    private int mLayout;
    private List<Comment> commentList;

    public CommentAdapter(Context mContext, int mLayout, List<Comment> commentList) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.comment_item, null);

        TextView name = view.findViewById(R.id.comment_UserName);
        ImageView avt = view.findViewById(R.id.comment_Avata);
        TextView dateTime = view.findViewById(R.id.commnet_DateTime);
        ImageView star1 = view.findViewById(R.id.comment_StarNumber1);
        ImageView star2 = view.findViewById(R.id.comment_StarNumber2);
        ImageView star3 = view.findViewById(R.id.comment_StarNumber3);
        ImageView star4 = view.findViewById(R.id.comment_StarNumber4);
        ImageView star5 = view.findViewById(R.id.comment_StarNumber5);
        TextView type = view.findViewById(R.id.comment_ProductType);
        TextView comment = view.findViewById(R.id.comment_ProductComment);

        name.setText(commentList.get(i).getName());
        avt.setImageResource(commentList.get(i).getAvt());
        dateTime.setText(commentList.get(i).getDateTime());
        switch (commentList.get(i).getStarNumber()) {
            case 5:
                star5.setImageResource(R.drawable.ic_star);
            case 4:
                star4.setImageResource(R.drawable.ic_star);
            case 3:
                star3.setImageResource(R.drawable.ic_star);
            case 2:
                star2.setImageResource(R.drawable.ic_star);
            default:
                star1.setImageResource(R.drawable.ic_star);
        }
        type.setText(commentList.get(i).getType());
        comment.setText(commentList.get(i).getComment());

        return view;
    }

}
