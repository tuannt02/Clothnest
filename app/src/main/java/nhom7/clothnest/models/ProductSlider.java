package nhom7.clothnest.models;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.ProductSliderAdapter;
import nhom7.clothnest.models.Product;

public class ProductSlider {
    private RecyclerView recyclerView;
    private ArrayList<Product> products;
    private int dotsNum;
    private final String htmlDot = "&#8226";
    private LinearLayout container;
    private LinearLayout linearLayout;
    private Context mContext;

    public ProductSlider(Context context, LinearLayout container, ArrayList<Product> products) {
        mContext = context;
        this.container = container;
        this.products = products;
        dotsNum = products.size() / 2;
    }

    public void createProductSlider() {
        implementRecyclerView();
        createDotsIndicator();
    }

    private void implementRecyclerView() {
        // create new recycler view
        recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // add recycler view to container
        container.addView(recyclerView);

        // Initialize ProductSliderAdapter
        ProductSliderAdapter adapter = new ProductSliderAdapter(mContext, products);
        recyclerView.setAdapter(adapter);
    }

    private void createDotsIndicator() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        // create layout container
        linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setDividerPadding(4);
        linearLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

        // add dots to container
        for (int i = 0; i < dotsNum; i++) {
            TextView dot = new TextView(mContext);
            dot.setText(HtmlCompat.fromHtml(htmlDot, HtmlCompat.FROM_HTML_MODE_LEGACY));
            dot.setTextSize(40);
            dot.setTextColor(Color.parseColor("#B6B6B6"));
            linearLayout.addView(dot);
        }

        // add dots indicator to
        container.addView(linearLayout);

        // Change the indicator when user scroll the slider
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisibleIndex = linearLayoutManager.findLastVisibleItemPosition();
                    // Since 1 slide contains two products, we need to divide it before passing
                    changeDotsIndicator(lastVisibleIndex / 2);
                }
            });
        }
    }

    private void changeDotsIndicator(int index) {
        TextView prevDot = null, currDot, nextDot = null;

        // init dot
        if (index > 0)
            prevDot = (TextView) linearLayout.getChildAt(index - 1);

        if (index < dotsNum - 1)
            nextDot = (TextView) linearLayout.getChildAt(index + 1);

        currDot = (TextView) linearLayout.getChildAt(index);

        // change dot background
        if (prevDot != null) {
            prevDot.setTextColor(Color.parseColor("#B6B6B6"));
        }

        if (nextDot != null) {
            nextDot.setTextColor(Color.parseColor("#B6B6B6"));
        }

        if (currDot != null)
            currDot.setTextColor(ContextCompat.getColor(mContext, R.color.black));
    }
}
