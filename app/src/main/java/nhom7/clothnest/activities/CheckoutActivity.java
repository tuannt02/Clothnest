package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.interfaces.ActivityConstants;
import nhom7.clothnest.models.Address;
import nhom7.clothnest.models.CartItem;
import nhom7.clothnest.models.CheckoutTransaction;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.PurchaseItem;
import nhom7.clothnest.models.SizeClass;
import nhom7.clothnest.util.Constants;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class CheckoutActivity extends AppCompatActivity {
    CustomProgressBar progressBar;
    ListView lv;
    ImageView btnBack;
    ArrayList<CartItem> cartItems;
    CheckoutTransaction transaction;
    ArrayList<DocumentReference> colors;
    ArrayList<DocumentReference> sizes;
    ArrayList<DocumentReference> products;

    CustomCheckoutItemAdapter adapter;
    RelativeLayout btnChooseAddress, btnChoosePaymentMethod, btnEnterVoucher;
    boolean isAddressChosen = false;
    TextView tvName, tvPhone, tvDetailWard, tvDistrictProvince;
    TextView tvPrice, tvDiscountPrice;
    TextView tvPaymentMethod, tvTotal;
    TextView tvVoucher, tvDiscount;
    LinearLayout llDiscount;
    MaterialButton btnPay;

    private static final String TAG = "CheckoutActivity";

    // Store value
    Long discountInMoney;
    Long deliveryFee;
    String addressId;

    // Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference userRef;
    CollectionReference cartRef;
    CollectionReference voucherRef;
    CollectionReference transactionRef;

    public final int CHOOSE_ADDRESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //Init UI
        initUi();
        setupClickOnListener();
        setupChooseAddressClick();
        setupChoosePaymentMethodClick();
        setupEnterVoucherClick();
        setupPayClick();

        // Init other variables
        deliveryFee = 5l;
        discountInMoney = 0l;

        // Get reference
        userRef = db.collection("users").document(Constants.getUserId());
        cartRef = userRef.collection("cart");
        voucherRef = db.collection("vouchers");
        transactionRef = db.collection("transactions");

        adapter = new CustomCheckoutItemAdapter(this, R.layout.checkout_item, cartItems);
        lv.setAdapter(adapter);

        getCheckoutItems();
    }

    private void getCheckoutItems() {
        // Declare an Array List of Task here. This List contains Task that get some more detail in each checkout item
        ArrayList<Task> checkoutTasks = new ArrayList<>();
        progressBar.show();

        cartRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        products.clear();
                        sizes.clear();
                        colors.clear();

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            CartItem cartItem = new CartItem();

                            cartItem.setKey(documentSnapshot.getId());
                            cartItem.setQty(Integer.parseInt(documentSnapshot.getLong("quantity").toString()));

                            ArrayList<Task> tasks = new ArrayList<>();

                            DocumentReference productRef = documentSnapshot.getDocumentReference("product_id");
                            DocumentReference colorRef = documentSnapshot.getDocumentReference("color_selected");
                            DocumentReference sizeRef = documentSnapshot.getDocumentReference("size_selected");

                            products.add(productRef);
                            colors.add(colorRef);
                            sizes.add(sizeRef);

                            Task task1 = productRef.get();
                            Task task2 = colorRef.get();
                            Task task3 = sizeRef.get();

                            tasks.add(task1);
                            tasks.add(task2);
                            tasks.add(task3);

                            Task<List<DocumentSnapshot>> allTasks = Tasks.whenAllSuccess(tasks);
                            checkoutTasks.add(allTasks.addOnSuccessListener(new OnSuccessListener<List<DocumentSnapshot>>() {
                                @Override
                                public void onSuccess(List<DocumentSnapshot> documentSnapshots) {
                                    DocumentSnapshot product = documentSnapshots.get(0);
                                    DocumentSnapshot color = documentSnapshots.get(1);
                                    DocumentSnapshot size = documentSnapshots.get(2);

                                    // Get Product data
                                    cartItem.setImg(product.getString("main_img"));
                                    cartItem.setName(product.getString("name"));
                                    cartItem.setPrice(product.getDouble("price"));
                                    cartItem.setDiscount(Integer.parseInt(product.getLong("discount").toString()));

                                    // Get Color data
                                    ColorClass colorItem = new ColorClass();
                                    colorItem.setName(color.getString("name"));
                                    colorItem.setHex(color.getString("hex"));
                                    cartItem.setColorSelected(colorItem);

                                    // Get Size data
                                    SizeClass sizeItem = new SizeClass();
                                    sizeItem.setName(size.getString("name"));
                                    cartItem.setSizeSelected(sizeItem);

                                    sizeItem.setShort_name(size.getString("short_name"));
                                }
                            }));

                            cartItems.add(cartItem);
                        }

                        Tasks.whenAllSuccess(checkoutTasks)
                                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                    @Override
                                    public void onSuccess(List<Object> objects) {
                                        adapter.notifyDataSetChanged();
                                        progressBar.dismiss();
                                        tvPrice.setText("$" + calculatePrice(false).toString());
                                        tvDiscountPrice.setText("$" + calculatePrice(true).toString());

                                        updateTotal();
                                    }
                                });

                    }
                });
    }

    private Long calculatePrice(boolean isDiscount) {
        Long result = 0l;

        for (CartItem item : cartItems) {
            result += isDiscount ? item.getDiscountPrice() : item.getTotalPrice();
        }

        return result;
    }

    private void initUi() {
        lv = findViewById(R.id.listview_checkout);
        btnBack = findViewById(R.id.btnBackCheckout);
        cartItems = new ArrayList<>();
        transaction = new CheckoutTransaction();
        colors = new ArrayList<>();
        sizes = new ArrayList<>();
        products = new ArrayList<>();

        btnChooseAddress = findViewById(R.id.relativelayout_choose_address);
        btnChoosePaymentMethod = findViewById(R.id.relativelayout_choose_payment_method);

        tvName = findViewById(R.id.checkout_deli_addr_txt_name);
        tvPhone = findViewById(R.id.checkout_deli_addr_txt_phone);
        tvDetailWard = findViewById(R.id.checkout_deli_addr_txt_detail_ward);
        tvDistrictProvince = findViewById(R.id.checkout_deli_addr_txt_district_province);
        tvPrice = findViewById(R.id.checkout_total_txt_regular_price);
        tvDiscountPrice = findViewById(R.id.checkout_total_txt_discount_price);

        // Assign Payment method textview and set default value for it
        tvPaymentMethod = findViewById(R.id.textview_payment_method);
        tvPaymentMethod.setText("COD");

        // Discount
        llDiscount = findViewById(R.id.linearlayout_discount);
        tvDiscount = findViewById(R.id.textview_discount);
        tvTotal = findViewById(R.id.textview_total);

        btnEnterVoucher = findViewById(R.id.relativelayout_enter_voucher);

        // Assign Voucher textview and set default value (SAMPLE)
        tvVoucher = findViewById(R.id.textview_voucher);

        btnPay = findViewById(R.id.button_pay);

        progressBar = new CustomProgressBar(this);
    }

    private void setupClickOnListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setupChooseAddressClick() {
        btnChooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, AddressBookActivity.class)
                        .putExtra("activity_type", ActivityConstants.CHOOSE_ADDRESSES);
                startActivityForResult(intent, CHOOSE_ADDRESS);
            }
        });
    }

    private void setupChoosePaymentMethodClick() {
        btnChoosePaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentMethodDialog();
            }
        });
    }

    private void showPaymentMethodDialog() {
        // Create a new dialog
        Dialog dialog = createNewDialog(R.layout.payment_method_dialog, null);

        // Init UI Views
        TextView btnSelect = dialog.findViewById(R.id.textview_select);
        RadioGroup rgPaymentMethod = dialog.findViewById(R.id.radiogroup_payment_method);

        // Get previous value of payment method
        String previousMethod = tvPaymentMethod.getText().toString();
        int previousRadioButtonId;
        switch (previousMethod) {
            case "COD":
                previousRadioButtonId = R.id.radiobutton_cod;
                break;
            case "MOMO":
                previousRadioButtonId = R.id.radiobutton_momo;
                break;
            default:
                previousRadioButtonId = R.id.radiobutton_paypal;
        }
        rgPaymentMethod.check(previousRadioButtonId);

        // Setup OnClick Listener for button select
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paymentMethod = "";

                // Get Id of checked RadioButton
                switch (rgPaymentMethod.getCheckedRadioButtonId()) {
                    case R.id.radiobutton_cod:
                        paymentMethod = "COD";
                        break;
                    case R.id.radiobutton_momo:
                        paymentMethod = "MOMO";
                        break;
                    case R.id.radiobutton_paypal:
                        paymentMethod = "PAYPAL";
                        break;
                }

                changePaymentMethod(paymentMethod);
                dialog.dismiss();
            }
        });

        // Show dialog
        dialog.show();
    }

    private void changePaymentMethod(String method) {
        // Return if payment method is invalid
        if (method.isEmpty()) {
            Toast.makeText(this, "Payment method is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }

        tvPaymentMethod.setText(method);
    }

    private void setupEnterVoucherClick() {
        btnEnterVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterVoucherDialog();
            }
        });
    }

    private void showEnterVoucherDialog() {
        Dialog dialog = createNewDialog(R.layout.enter_voucher_dialog, null);

        // Init UI Views
        TextInputEditText etCode = dialog.findViewById(R.id.edittext_code);
        TextView btnApply = dialog.findViewById(R.id.textview_apply);
        TextView btnCancel = dialog.findViewById(R.id.textview_cancel);

        String prevCode = tvVoucher.getText().toString();
        if (!prevCode.isEmpty()) {
            etCode.setText(prevCode);
        }

        // Setup button apply
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate voucher
                String code = etCode.getText().toString();
                Long discount = voucherValidation(code);

                // If code is invalid, set error
                if (discount == -1) {
                    etCode.setError("Invalid Voucher");
                } else {
                    etCode.setError(null);
                    tvVoucher.setText(code);
                    dialog.dismiss();

                    Long subtotal = calculatePrice(true);
                    discountInMoney = (long) Math.ceil(discount * subtotal / 100.0);
                    tvDiscount.setText("$" + discountInMoney);
                    llDiscount.setVisibility(View.VISIBLE);

                    updateTotal();
                }
            }
        });

        // Setup cancel click
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvVoucher.setText("");
                tvDiscount.setText("");
                llDiscount.setVisibility(View.GONE);
                discountInMoney = 0l;

                updateTotal();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private Long voucherValidation(String code) {
        Long discount = -1l;

        Task getVouchers = voucherRef.get();
        while (!getVouchers.isComplete());

        QuerySnapshot result = (QuerySnapshot) getVouchers.getResult();
        for (DocumentSnapshot documentSnapshot: result.getDocuments()) {
            if (documentSnapshot.getString("code").equals(code)) {
                discount = documentSnapshot.getLong("discount");
                break;
            }
        }

        return discount;
    }

    private Dialog createNewDialog(int resource, @Nullable String msg) {
        // Alert if resource is not provided
        if (resource == -1) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Payed failed!")
                    .setMessage(msg)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(true)
                    .create();
            return dialog;
        }

        // Create a new dialog
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(resource);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private Long updateTotal() {
        Long total = calculatePrice(true);
        total += deliveryFee;
        total -= discountInMoney;
        tvTotal.setText("$" + total);

        return total;
    }

    private void setupPayClick() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems.size() == 0) {
                    createNewDialog(-1, "You have not added anything to your cart yet!").show();
                    return;
                }

                if (isAddressChosen == false) {
                    createNewDialog(-1, "You must chooose address!").show();
                    return;
                }

                // Create new transaction
                DocumentReference newTransaction = transactionRef.document();
                CheckoutTransaction checkoutTransaction = new CheckoutTransaction();
                checkoutTransaction.setAddressRef(userRef.collection("addr").document(addressId));
                checkoutTransaction.setUserRef(userRef);
                checkoutTransaction.setDeliveryFee(deliveryFee);
                checkoutTransaction.setStatus("In Progress");
                checkoutTransaction.setOrderDate(getDate());
                checkoutTransaction.setDiscount(discountInMoney);
                checkoutTransaction.setTotal(updateTotal());

                Task createTransaction = newTransaction.set(checkoutTransaction);
                while (!createTransaction.isComplete());

                // Add item to transaction
                ArrayList<Task> addTransactionItemsTask = new ArrayList<>();

                CollectionReference transactionItemList = newTransaction.collection("transactionItemList");
                for (int i = 0; i < cartItems.size(); i++) {
                    CartItem item = cartItems.get(i);

                    Double salePrice = (double) (item.getDiscountPrice() / item.getQty());

                    PurchaseItem transactionItem = new PurchaseItem(
                            item.getQty(),
                            products.get(i),
                            colors.get(i),
                            sizes.get(i),
                            item.getPrice(),
                            salePrice
                    );

                    addTransactionItemsTask.add(transactionItemList.document().set(transactionItem));
                }

                Tasks.whenAllSuccess(addTransactionItemsTask)
                        .addOnCompleteListener(new OnCompleteListener<List<Object>>() {
                            @Override
                            public void onComplete(@NonNull Task<List<Object>> task) {
                                // Show Order success dialog
                                Dialog dialog = createNewDialog(R.layout.order_received_dialog, null);
                                dialog.show();

                                // Remove cart items
                                cartRef.get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                                                    cartRef.document(documentSnapshot.getId()).delete();
                                                }
                                            }
                                        });

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                                        intent.putExtra("fragment_type", ActivityConstants.PROFILE_FRAGMENT);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 3000);
                            }
                        });
            }
        });
    }

    private String getDate() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle address choosing from Address Book
        if (requestCode == CHOOSE_ADDRESS) {
            if (resultCode == RESULT_OK) {
                addressId = data.getStringExtra("selected_address");
                if (addressId != null)
                    userRef.collection("addr")
                            .document(addressId)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Address address = documentSnapshot.toObject(Address.class);
                                    tvName.setText(address.fullName + " | ");
                                    tvPhone.setText(address.phoneNumber);
                                    tvDetailWard.setText(address.detail + ", " + address.getWard());
                                    tvDistrictProvince.setText(address.getDistrict() + ", " + address.getProvince());
                                    isAddressChosen = true;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CheckoutActivity.this, "Failed while loading address", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onFailure: " + e);
                                }
                            });
            }
        }
    }
}