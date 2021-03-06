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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CartColorAdapter;
import nhom7.clothnest.adapters.CartSizeAdapter;
import nhom7.clothnest.models.CartItem;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.models.SizeClass;
import nhom7.clothnest.models.Stock;
import nhom7.clothnest.models.User;
import nhom7.clothnest.util.customizeComponent.CustomDialog;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;
import nhom7.clothnest.util.customizeComponent.CustomToast;

public class AddToCart extends Dialog implements android.view.View.OnClickListener{
    private ArrayList<ColorClass> colorClassArrayList;
    private ArrayList<SizeClass> sizeClassArrayList;
    private ArrayList<Stock> stockArrayList;
    private ColorClass colorSelected;
    private SizeClass sizeSelected;
    private int qtyChoose = 1;
    private int totalStock = 0;
    private int currentStock = 0;
    private String keyProduct;
    private CartItem cartItem;
    private CartColorAdapter cartColorAdapter;
    private CartSizeAdapter cartSizeAdapter;

    private boolean isColorSelect = false;
    private boolean isSizeSelect = false;
    private int posColorSelected = -1;
    private int posSizeSelected = -1;


    private Button btnAddToCart;
    private ImageButton btnIncrease, btnDecrease;
    private TextView txtQty;
    private TextView txtStock;
    private ImageView mainImg;
    private TextView regularPrice, discountPrice;
    private CustomProgressBar progressBar;


    public AddToCart(@NonNull Context context, String keyProduct) {
        super(context);
        this.keyProduct = keyProduct;

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

//        checkProductIdExists(context);
        show();

        //Get view
        ImageView btnClose = findViewById(R.id.add_to_cart_btn_close);
        GridView gvColor = findViewById(R.id.add_to_cart_gridview_color);
        GridView gvSize = findViewById(R.id.add_to_cart_gridview_size);
        btnAddToCart = findViewById(R.id.add_to_cart_btn_add);
        btnIncrease = findViewById(R.id.add_to_cart_btn_increase);
        btnDecrease = findViewById(R.id.add_to_cart_btn_decrease);
        txtQty = findViewById(R.id.add_to_cart_txt_qty);
        txtStock = findViewById(R.id.add_to_cart_stock);
        cartItem = new CartItem();
        mainImg = findViewById(R.id.add_to_cart_main_img);
        regularPrice = findViewById(R.id.add_to_cart_regular_price);
        discountPrice = findViewById(R.id.add_to_cart_discount_price);
        progressBar = new CustomProgressBar(getContext());

        // Set view
        Glide.with(context).load(cartItem.getImg()).into(mainImg);
        regularPrice.setText("$  " + cartItem.getPrice());
//        discountPrice.setText("$  " + cartItem.getDiscountPrice());
            // set color
        colorClassArrayList = new ArrayList<>();
//        colorClassArrayList = getColor();
        cartColorAdapter = new CartColorAdapter(getContext(), R.layout.cart_color_item, colorClassArrayList);
        gvColor.setAdapter(cartColorAdapter);
            // set size
        sizeClassArrayList = new ArrayList<>();
//        sizeClassArrayList = getSize();
        cartSizeAdapter = new CartSizeAdapter(getContext(), R.layout.cart_size_item, sizeClassArrayList);
        gvSize.setAdapter(cartSizeAdapter);
        stockArrayList = new ArrayList<>();

        getInfoProductWhenClickOnItem();

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
                posColorSelected = i;
                clearIsSelectedColor();
                colorSelected = colorClassArrayList.get(i);
                ColorClass colorClass = colorClassArrayList.get(i);
                colorClass.setSelected(true);
                colorClassArrayList.set(i,colorClass);

                reAssignStock();
                reImg();
                checkAndMoveStatusBtn();

                cartColorAdapter.selectedItem(i);
                cartColorAdapter.notifyDataSetChanged();
            }
        });
        gvSize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isSizeSelect = true;
                posSizeSelected = i;
                clearIsSelectedSize();
                sizeSelected = sizeClassArrayList.get(i);
                SizeClass sizeClass = sizeClassArrayList.get(i);
                sizeClass.setSelected(true);
                sizeClassArrayList.set(i, sizeClass);

                reAssignStock();
                reImg();
                checkAndMoveStatusBtn();

                cartSizeAdapter.selectedItem(i);
                cartSizeAdapter.notifyDataSetChanged();
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartFiretore(context);
            }
        });
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(txtQty.getText().toString().trim());
                qty++;
                txtQty.setText(Integer.toString(qty));
                qtyChoose = qty;
            }
        });
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(txtQty.getText().toString().trim());
                if(qty == 1) return;
                qty--;
                txtQty.setText(Integer.toString(qty));
                qtyChoose = qty;
            }
        });
    }

    private void getInfoProductWhenClickOnItem()    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .document(keyProduct)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        cartItem.setQty(1);
                        cartItem.setImg(documentSnapshot.getString("main_img"));
                        cartItem.setPrice(documentSnapshot.getDouble("price"));
                        cartItem.setDiscount((int)Math.round(documentSnapshot.getDouble("discount")));

                        Glide.with(getContext()).load(cartItem.getImg()).into(mainImg);
                        regularPrice.setText("$  " + cartItem.getPrice());
                        discountPrice.setText(("$  " + cartItem.getDiscountPrice()));

                        colorClassArrayList.clear();
                        sizeClassArrayList.clear();
                        stockArrayList.clear();

                        db.collection("products")
                                .document(keyProduct)
                                .collection(ColorClass.COLLECTION_NAME)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        for(DocumentSnapshot colorListItem : queryDocumentSnapshots)    {
                                            ColorClass colorItem = new ColorClass();
                                            colorItem.setHex(colorListItem.getString("hex"));
                                            colorItem.setName(colorListItem.getString("name"));
                                            colorItem.setColorRef(colorListItem.getReference());

                                            colorClassArrayList.add(colorItem);
                                        }
                                        cartItem.setListColor(colorClassArrayList);
                                        cartColorAdapter.notifyDataSetChanged();
                                    }
                                });

                        db.collection("products")
                                .document(keyProduct)
                                .collection(SizeClass.COLLECTION_NAME)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        for(DocumentSnapshot sizeListItem : queryDocumentSnapshots)    {
                                            SizeClass sizeItem = new SizeClass();
                                            sizeItem.setShort_name(sizeListItem.getString("short_name"));
                                            sizeItem.setName(sizeListItem.getString("name"));
                                            sizeItem.setSizeRef(sizeListItem.getReference());

                                            sizeClassArrayList.add(sizeItem);
                                        }
                                        cartItem.setListSize(sizeClassArrayList);
                                        cartSizeAdapter.notifyDataSetChanged();
                                    }
                                });

                        db.collection("products")
                                .document(keyProduct)
                                .collection(Stock.COLLECTION_NAME)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        for(DocumentSnapshot stockListItem : queryDocumentSnapshots)    {
                                            Stock stockItem = new Stock();

                                            List<String> listImg = (List<String>) stockListItem.get("images");

                                            if(listImg.size() >= 0) {
                                                stockItem.setImg(listImg.get(0));
                                            }
                                            else    {
                                                stockItem.setImg(cartItem.getImg());
                                            }
                                            stockItem.setColorName(stockListItem.getString("color"));
                                            stockItem.setSizeName(stockListItem.getString("size"));
                                            int quantity = (int)Math.round(stockListItem.getDouble("quantity"));
                                            stockItem.setQuantity(quantity);
                                            totalStock += quantity;

                                            stockArrayList.add(stockItem);
                                        }
                                        txtStock.setText(String.valueOf(totalStock));
                                    }
                                });
                    }
                });
    }

    private void addToCartFiretore(Context context)    {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Data
        int quantity = qtyChoose;
        DocumentReference productRef = db.collection("products").document(keyProduct);
        DocumentReference color_selectedRef = colorClassArrayList.get(posColorSelected).getColorRef();
        DocumentReference size_selectedRef = sizeClassArrayList.get(posSizeSelected).getSizeRef();


        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("quantity", quantity);
        data.put("product_id", productRef);
        data.put("color_selected", color_selectedRef);
        data.put("size_selected", size_selectedRef);

        db.collection(User.COLLECTION_NAME)
                .document(userInfo.getUid())
                .collection(CartItem.COLLECTION_NAME)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        dismiss();
                        CustomToast.DisplayToast(context, 1, "Add to cart successfully");
                    }
                });

    }

    private void clearIsSelectedColor()    {
        for(int i=0 ;i<colorClassArrayList.size();i++)  {
            colorClassArrayList.get(i).setSelected(false);
        }
    }

    private void clearIsSelectedSize()  {
        for(int i=0;i< sizeClassArrayList.size();i++)  {
            sizeClassArrayList.get(i).setSelected(false);
        }
    }

    private void checkAndMoveStatusBtn()    {
        if(currentStock == 0)   {
            btnAddToCart.setEnabled(false);
            return;
        }
        if(isSizeSelect && isColorSelect)   {
            btnAddToCart.setEnabled(true);
        }
    }

    private void checkProductIdExists(Context context) {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference productRef = db.collection("products").document(keyProduct);

        db.collection(User.COLLECTION_NAME)
                .document(userInfo.getUid())
                .collection(CartItem.COLLECTION_NAME)
                .whereEqualTo("product_id", productRef)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists())  {
                                CustomDialog customDialog = new CustomDialog(context,
                                        "Notify",
                                        "This product has been added to cart",
                                        1, // hide btn close
                                        new CustomDialog.IClickListenerOnOkBtn() {
                                            @Override
                                            public void onResultOk() {
                                                dismiss();
                                            }
                                        });
                                customDialog.show();
                                return;
                            }

                        }
                        show();
                    }
                });
    }

    private void reAssignStock()    {
        if(isSizeSelect && isColorSelect)   {
            String nameColorSelected = colorSelected.getName();
            String nameSizeSelected = sizeSelected.getShort_name();

            for(Stock stockItem : stockArrayList)   {
                if(stockItem.getColorName().equals(nameColorSelected)
                && stockItem.getSizeName().equals(nameSizeSelected))    {
                    currentStock = stockItem.getQuantity();
                    txtStock.setText(String.valueOf(currentStock));
                    return;
                }
            }

            currentStock = 0;
            txtStock.setText(String.valueOf(currentStock));

        }
    }

    private void reImg()    {
        if(isColorSelect)   {
            String nameColorSelected = colorSelected.getName();

            for(Stock stockItem : stockArrayList)   {
                if(stockItem.getColorName().equals(nameColorSelected))    {
                    String img = stockItem.getImg();
                    Glide.with(getContext()).load(img).error(cartItem.getImg()).into(mainImg);
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}
