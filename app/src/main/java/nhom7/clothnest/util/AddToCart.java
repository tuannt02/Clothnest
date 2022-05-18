package nhom7.clothnest.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CartColorAdapter;
import nhom7.clothnest.adapters.CartSizeAdapter;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.SizeClass;

public class AddToCart extends Dialog implements android.view.View.OnClickListener{
    private ArrayList<ColorClass> colorClassArrayList;
    private ArrayList<SizeClass> sizeClassArrayList;
    private boolean isColorSelect = false;
    private boolean isSizeSelect = false;
    private Button btnAddToCart;
    private ImageButton btnIncrease, btnDecrease;
    private TextView txtQty;

    public AddToCart(@NonNull Context context) {
        super(context);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_to_cart_dialog);
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        if(window == null)  {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);

        //Get view
        ImageView btnClose = findViewById(R.id.add_to_cart_btn_close);
        GridView gvColor = findViewById(R.id.add_to_cart_gridview_color);
        GridView gvSize = findViewById(R.id.add_to_cart_gridview_size);
        btnAddToCart = findViewById(R.id.add_to_cart_btn_add);
        btnIncrease = findViewById(R.id.add_to_cart_btn_increase);
        btnDecrease = findViewById(R.id.add_to_cart_btn_decrease);
        txtQty = findViewById(R.id.add_to_cart_txt_qty);

        // Set view
            // set color
        colorClassArrayList = new ArrayList<>();
        colorClassArrayList = getColor();
        CartColorAdapter cartColorAdapter = new CartColorAdapter(getContext(), R.layout.cart_color_item, colorClassArrayList);
        gvColor.setAdapter(cartColorAdapter);
            // set size
        sizeClassArrayList = new ArrayList<>();
        sizeClassArrayList = getSize();
        CartSizeAdapter cartSizeAdapter = new CartSizeAdapter(getContext(), R.layout.cart_size_item, sizeClassArrayList);
        gvSize.setAdapter(cartSizeAdapter);


        // Set on click
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        gvColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isColorSelect = true;
                checkAndMoveStatusBtn();
                clearIsSelectedColor();
                ColorClass colorClass = colorClassArrayList.get(i);
                colorClass.setSelected(true);
                colorClassArrayList.set(i,colorClass);
                cartColorAdapter.selectedItem(i);
                cartColorAdapter.notifyDataSetChanged();
            }
        });
        gvSize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isSizeSelect = true;
                checkAndMoveStatusBtn();
                clearIsSelectedSize();
                SizeClass sizeClass = sizeClassArrayList.get(i);
                sizeClass.setSelected(true);
                sizeClassArrayList.set(i, sizeClass);
                cartSizeAdapter.selectedItem(i);
                cartSizeAdapter.notifyDataSetChanged();
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Say hello");
            }
        });
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(txtQty.getText().toString().trim());
                qty++;
                txtQty.setText(Integer.toString(qty));
            }
        });
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(txtQty.getText().toString().trim());
                if(qty == 1) return;
                qty--;
                txtQty.setText(Integer.toString(qty));
            }
        });
    }

    private ArrayList<ColorClass> getColor()    {
        ArrayList<ColorClass> colorClassArrayList = new ArrayList<>();
        colorClassArrayList.add(new ColorClass("Pink","#f542d7", false));
        colorClassArrayList.add(new ColorClass("Black","#0a0008",false));
        colorClassArrayList.add(new ColorClass("Blue","#2232bf",false));
        colorClassArrayList.add(new ColorClass("Gray","#a3a4a8",false));
        colorClassArrayList.add(new ColorClass("Hồng","#f542d7",false));

        return colorClassArrayList;
    }

    private ArrayList<SizeClass> getSize()  {
        ArrayList<SizeClass> sizeClassArrayList = new ArrayList<>();
        sizeClassArrayList.add(new SizeClass("Size L", "L"));
        sizeClassArrayList.add(new SizeClass("Size M", "M"));
        sizeClassArrayList.add(new SizeClass("Size S", "S"));
        sizeClassArrayList.add(new SizeClass("Size XL", "XL"));
        sizeClassArrayList.add(new SizeClass("Size XXL", "XXL"));
        sizeClassArrayList.add(new SizeClass("Size SM", "SM"));
        sizeClassArrayList.add(new SizeClass("Size XXL", "XXL"));
        sizeClassArrayList.add(new SizeClass("Size XXXL", "XXXL"));

        return sizeClassArrayList;
    }

    private void clearIsSelectedColor()    {
        for(int i=0 ;i<colorClassArrayList.size();i++)  {
            colorClassArrayList.get(i).setSelected(false);
        }
    }

    private void clearIsSelectedSize()  {
        for(int i=0;i< colorClassArrayList.size();i++)  {
            sizeClassArrayList.get(i).setSelected(false);
        }
    }

    private void checkAndMoveStatusBtn()    {
        if(isSizeSelect && isColorSelect)   {
            btnAddToCart.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
