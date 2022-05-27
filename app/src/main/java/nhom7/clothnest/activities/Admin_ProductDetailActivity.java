package nhom7.clothnest.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.StockAdapter;
import nhom7.clothnest.adapters.StockImageAdapter;
import nhom7.clothnest.interfaces.ActivityConstants;
import nhom7.clothnest.models.CategoryItem;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.Product_Admin;
import nhom7.clothnest.models.Product_Detail;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.SizeClass;
import nhom7.clothnest.models.Stock;

public class Admin_ProductDetailActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SIZE = 0x2902;
    private static final int REQUEST_CODE_COLOR = 0x2903;
    private static final int REQUEST_CODE_IMAGE = 0x2904;
    private static final int REQUEST_CODE_IMAGE_MAIN = 0x2905;

    NestedScrollView nestedScrollView;
    Product_Detail productDetail;

    String colorID;
    String sizeID;

    ArrayList<String> categoryList;
    ArrayList<String> categoryIdList;
    ArrayList<Stock> stockList;

    ArrayAdapter<String> categoryAdapter;
    StockAdapter stockAdapter;

    TextView tvCancel, tvSave;
    EditText etName, etPrice, etDiscount, etDescription;
    Button btnAdd, btnClear, btnRemove;
    EditText etSize, etColor, etQuantity;
    ListView lvStock;
    GridView gvImages;
    AutoCompleteTextView autoCpTv_category;
    ImageView ivMainImage;

    //Select image
    ArrayList<Uri> uriList;
    StockImageAdapter stockImageAdapter;
    Uri imageUri, mainImageUri;

    ProgressDialog pd;
    FirebaseFirestore db;
    DocumentReference docRef_product;
    String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);

        reference();

        handleExtra();

        createStock();
        getCategory();
        getEvent();
    }

    private void handleExtra() {
        String key = getIntent().getStringExtra("handle_adminProductDetail");
        switch (key) {
            case "add":
                btnRemove.setVisibility(View.GONE);
                nestedScrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;

            default:
                productID = key;
                getAdminProductDetail(productID);
        }
    }

    private void getAdminProductDetail(String productID) {
        pd = new ProgressDialog(Admin_ProductDetailActivity.this);
        pd.setMessage("Loading...");
        pd.show();

        getProductDetail(productID);
    }


    public void getProductDetail(String productID) {
        productDetail = new Product_Detail();

        DocumentReference product_docRef = FirebaseFirestore.getInstance().collection(Product_Admin.COLLECTION_NAME).document(productID);
        product_docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                productDetail.setId(productID);
                productDetail.setName(doc.getString("name"));
                etName.setText(productDetail.getName());

                DocumentReference category_docRef = (DocumentReference) doc.get("category");
                category_docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        productDetail.setCategory(documentSnapshot.getString("name"));
                        autoCpTv_category.setText(productDetail.getCategory());
                    }
                });
                productDetail.setPrice(Double.valueOf(doc.getDouble("price")));
                etPrice.setText(productDetail.getPrice() + "");

                productDetail.setDiscount((int) Math.round(doc.getDouble("discount")));
                etDiscount.setText(productDetail.getDiscount() + "");

                productDetail.setDescription(doc.getString("desc"));
                etDescription.setText(productDetail.getDescription());

                productDetail.setMainImage(doc.getString("main_img"));
                Glide.with(getApplicationContext()).load(productDetail.getMainImage()).into(ivMainImage);

                getStockListFromFireStore(productID);

                if(pd.isShowing())
                    pd.dismiss();
            }
        });
    }

    public void getStockListFromFireStore(String productID) {
        DocumentReference product_docRef = FirebaseFirestore.getInstance().collection(Product_Admin.COLLECTION_NAME).document(productID);
        product_docRef.collection(Stock.COLLECTION_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    createStock();

                    for (QueryDocumentSnapshot stock_doc : task.getResult()) {
                        Stock stock = new Stock();
                        stockList.add(stock);
                        addStockToList(stock, stock_doc);
                    }

                    productDetail.setStockList(stockList);
                }
            }
        });
    }

    private void addStockToList(Stock stock, QueryDocumentSnapshot stock_doc){

        stock.setSizeName(stock_doc.getString("size"));
        stock.setColorName(stock_doc.getString("color"));
        int quantity = (int) Math.round(stock_doc.getDouble("quantity"));
        stock.setQuantity(quantity);

        stock.setColorID("nKavecMrOQqbCicJg96b");
        stock.setSizeID("KCzGpGvEptHpIl1ZctvX");

        db.collection(ColorClass.COLLECTION_NAME).whereEqualTo("name", stock.getColorName())
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot result : task.getResult()) {
                                stock.setColorID(result.getId());
                                stockAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });


        db.collection(SizeClass.COLLECTION_NAME).whereEqualTo("short_name", stock.getSizeName())
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot result : task.getResult()) {
                                stock.setSizeID(result.getId());
                                stockAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storage_ref = storage.getReference().child(Product_Detail.COLLECTION_NAME).child(productID);
        storage_ref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                ArrayList<Uri> uris = new ArrayList<>();
                stock.setImageList(uris);
                for (StorageReference fileRef : listResult.getItems()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uris.add(uri);
                            stockAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

    }

    private void getEvent() {
        etSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sizeIntent = new Intent(getApplicationContext(), Admin_SizeActivity.class);
                sizeIntent.putExtra("activity_type", ActivityConstants.CHOOSE_SIZE);
                startActivityForResult(sizeIntent, REQUEST_CODE_SIZE);
            }
        });

        etColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sizeIntent = new Intent(getApplicationContext(), Admin_ColorActivity.class);
                sizeIntent.putExtra("activity_type", ActivityConstants.CHOOSE_COLOR);
                startActivityForResult(sizeIntent, REQUEST_CODE_COLOR);
            }
        });

        ivMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(REQUEST_CODE_IMAGE_MAIN);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSize.getText().toString().isEmpty() || etColor.getText().toString().isEmpty() || etQuantity.getText().toString().isEmpty() || uriList.size() == 1) {
                    Toast.makeText(Admin_ProductDetailActivity.this, "Lack of information", Toast.LENGTH_SHORT).show();
                } else {
                    int quantity = 0;
                    try {
                        quantity = Integer.parseInt(etQuantity.getText().toString());

                        uriList.remove(0);

                        Stock currentStoct = new Stock(sizeID, colorID, quantity, uriList);

                        currentStoct.setSizeName(etSize.getText().toString());
                        currentStoct.setColorName(etColor.getText().toString());

                        stockList.add(currentStoct);
                        stockAdapter.notifyDataSetChanged();

                        etQuantity.setText("");

                        getStockImageList();
                    } catch (NumberFormatException e) {
                        Toast.makeText(Admin_ProductDetailActivity.this, "Quantity is an integer", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSize.setText("");
                etColor.setText("");
                etQuantity.setText("");
            }
        });

        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    selectImage(REQUEST_CODE_IMAGE);
                } else {
                    uriList.remove(i);
                    stockImageAdapter.notifyDataSetChanged();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String category = autoCpTv_category.getText().toString();
                String price = etPrice.getText().toString();
                String discount = etDiscount.getText().toString();
                String description = etDescription.getText().toString();

                boolean b1 = name.isEmpty();
                boolean b2 = category.isEmpty();
                boolean b3 = price.isEmpty();
                boolean b4 = discount.isEmpty();
                boolean b5 = description.isEmpty();
                boolean b6 = stockList.size() == 0;
                boolean b7 = ivMainImage.getDrawable() == getResources().getDrawable(R.drawable.add_image);

                if (b1 || b2 || b3 || b4 || b5 || b6 || b7) {
                    Toast.makeText(Admin_ProductDetailActivity.this, "Lack of information", Toast.LENGTH_SHORT).show();
                } else {
                    pd = new ProgressDialog(Admin_ProductDetailActivity.this);
                    pd.setMessage("Saving...");
                    pd.show();

                    //create database
                    db = FirebaseFirestore.getInstance();

                    //get data
                    //get docRef of category
                    int indexId = categoryList.indexOf(category);
                    String categoryId = categoryIdList.get(indexId);
                    DocumentReference docRef_category = db.document(CategoryItem.COLLECTION_NAME + '/' + categoryId);
                    //get double value of price
                    Double price_Double = 0.0;
                    try {
                        price_Double = Double.valueOf(price);
                    } catch (NumberFormatException e) {
                        Toast.makeText(Admin_ProductDetailActivity.this, "Price is an double", Toast.LENGTH_SHORT).show();
                        if (pd.isShowing())
                            pd.dismiss();
                        return;
                    }
                    //get int value of discount
                    int discount_Int = 0;
                    try {
                        discount_Int = Integer.parseInt(discount);
                    } catch (NumberFormatException e) {
                        Toast.makeText(Admin_ProductDetailActivity.this, "Discount is an integer", Toast.LENGTH_SHORT).show();
                        if (pd.isShowing())
                            pd.dismiss();
                        return;
                    }
                    //get current datetime
                    Timestamp now = new Timestamp(new Date());

                    Map<String, Object> product = new HashMap<>();
//                    product.put("category", category_DocRef);
                    product.put("category", docRef_category);
                    product.put("date_add", now);
                    product.put("desc", description);
                    product.put("discount", discount_Int);
                    product.put("name", name);
                    product.put("price", price_Double);

                    db.collection(Product_Thumbnail.COLLECTION_NAME).add(product)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    docRef_product = documentReference;
                                    addMainImage();
                                    addStocksToProduct();
                                    addCollectionColors();
                                    addCollectionSizes();

                                    if (pd.isShowing()) {
                                        clearInfo();
                                        pd.dismiss();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void addMainImage() {
        //Đặt tên file ảnh
        String filename = docRef_product.getId() + "_MainImage";

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(Product_Thumbnail.COLLECTION_NAME + '/' + docRef_product.getId() + '/' + filename);
        storageReference
                .putFile(mainImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUri) {
                                        Map<String, Object> product = new HashMap<>();
                                        product.put("main_img", downloadUri.toString());
                                        docRef_product.update(product);
                                    }
                                });
                    }
                });
    }

    private void addStocksToProduct() {
        //push Stock
        for (Stock stock : stockList) {
            Map<String, Object> stockMap = new HashMap<>();

            stockMap.put("color", stock.getColorName());
            stockMap.put("size", stock.getSizeName());
            stockMap.put("quantity", stock.getQuantity());
            stockMap.put("images", new ArrayList<String>());

            docRef_product.collection(Stock.COLLECTION_NAME)
                    .add(stockMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference stockRef) {
                            addImagesToStock(stockRef, stock.getImageList());
                        }
                    });
        }
    }

    private void addImagesToStock(DocumentReference docRef_currentStock, ArrayList<Uri> uris) {
        for (Uri uri : uris) {
            //Đặt tên file ảnh
            String filename = docRef_currentStock.getId() + '_' + uris.indexOf(uri);

            //Lấy tham chiếu đến ảnh
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(Product_Thumbnail.COLLECTION_NAME + '/' + docRef_product.getId() + '/' + filename);
            storageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUri) {
                                            docRef_currentStock.update("images", FieldValue.arrayUnion(downloadUri.toString()));
                                        }
                                    });
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIZE) {
            if (resultCode == Activity.RESULT_OK) {
                sizeID = data.getStringExtra("selected_size");
                getSize(sizeID);
            }
        } else if (requestCode == REQUEST_CODE_COLOR) {
            if (resultCode == Activity.RESULT_OK) {
                colorID = data.getStringExtra("selected_color");
                getColor(colorID);
            }
        } else if (requestCode == REQUEST_CODE_IMAGE) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                uriList.add(imageUri);
                stockImageAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_CODE_IMAGE_MAIN && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                mainImageUri = data.getData();
                ivMainImage.setImageURI(mainImageUri);
            }
        }
    }

    private void getColor(String colorID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(ColorClass.COLLECTION_NAME + '/' + colorID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        etColor.setText(documentSnapshot.getString("name"));
                    }
                });
    }

    private void getSize(String sizeID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(SizeClass.COLLECTION_NAME + '/' + sizeID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        etSize.setText(documentSnapshot.getString("short_name"));
                    }
                });
    }

    private void getCategory() {
        categoryList = new ArrayList<>();
        categoryIdList = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<String>(this, R.layout.item_admin_productdetail_category, categoryList);
        autoCpTv_category.setAdapter(categoryAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(CategoryItem.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String category = document.getString("name");
                                categoryList.add(category);
                                categoryIdList.add(document.getId());
                                categoryAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void reference() {
        db = FirebaseFirestore.getInstance();

        nestedScrollView = findViewById(R.id.admin_productDetail_nestedSV);
        autoCpTv_category = findViewById(R.id.admin_productDetail_ddCategory);
        etName = findViewById(R.id.admin_productDetail_etName);
        etPrice = findViewById(R.id.admin_productDetail_etPrice);
        etDiscount = findViewById(R.id.admin_productDetail_etDiscount);
        etDescription = findViewById(R.id.admin_productDetail_etDescription);
        tvSave = findViewById(R.id.admin_productDetail_tvSave);
        tvCancel = findViewById(R.id.admin_productDetail_tvCancel);
        btnAdd = findViewById(R.id.admin_productDetail_btnAdd);
        btnClear = findViewById(R.id.admin_productDetail_btnClear);
        btnRemove = findViewById(R.id.admin_productDetail_btnRemove);
        etSize = findViewById(R.id.admin_productDetail_etSize);
        etColor = findViewById(R.id.admin_productDetail_etColor);
        etQuantity = findViewById(R.id.admin_productDetail_etQuantity);
        lvStock = findViewById(R.id.admin_productDetail_lvStock);
        gvImages = findViewById(R.id.admin_productDetail_gvImage);
        ivMainImage = findViewById(R.id.admin_productDetail_ivMainImage);
    }

    private void createStock() {
        stockList = new ArrayList<>();
        stockAdapter = new StockAdapter(this, stockList, new StockAdapter.ClickListener() {
            @Override
            public void removeItem(int position) {
                stockList.remove(position);
                stockAdapter.notifyDataSetChanged();
            }
        });
        lvStock.setAdapter(stockAdapter);

        getStockImageList();
    }

    private void selectImage(int request_code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, request_code);
    }

    private void getStockImageList() {
        uriList = new ArrayList<>();
        stockImageAdapter = new StockImageAdapter(Admin_ProductDetailActivity.this, uriList);
        gvImages.setAdapter(stockImageAdapter);

        String uriPath = "android.resource://" + getPackageName() + "/" + R.drawable.add_image;
        Uri uri = Uri.parse(uriPath);
        uriList.add(uri);
        stockImageAdapter.notifyDataSetChanged();
    }

    private void clearInfo() {
        etName.setText("");
        autoCpTv_category.setText("");
        etPrice.setText("");
        etDiscount.setText("");
        etDescription.setText("");
        ivMainImage.setImageResource(R.drawable.add_image);
        etSize.setText("");
        etColor.setText("");
        etQuantity.setText("");
        stockList.clear();
        stockAdapter.notifyDataSetChanged();
    }

    private void addCollectionColors() {
        ArrayList<String> colorsOfProduct = new ArrayList<>();
        for (Stock stock : stockList) {
            if (!colorsOfProduct.contains(stock.getColorID()))
                colorsOfProduct.add(stock.getColorID());
        }
        for (String colorID : colorsOfProduct) {
            Map<String, Object> colorMap = new HashMap<>();

            db.collection(ColorClass.COLLECTION_NAME).document(colorID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot docS) {
                            colorMap.put("hex", docS.get("hex"));
                            colorMap.put("name", docS.get("name"));

                            docRef_product.collection(ColorClass.COLLECTION_NAME).add(colorMap);
                        }
                    });
        }
    }

    private void addCollectionSizes() {
        ArrayList<String> sizesOfProduct = new ArrayList<>();
        for (Stock stock : stockList) {
            if (!sizesOfProduct.contains(stock.getSizeID()))
                sizesOfProduct.add(stock.getSizeID());
        }
        for (String sizeID : sizesOfProduct) {
            Map<String, Object> sizeMap = new HashMap<>();

            db.collection(SizeClass.COLLECTION_NAME).document(sizeID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot docS) {
                            sizeMap.put("name", docS.get("name"));
                            sizeMap.put("short_name", docS.get("short_name"));
                            sizeMap.put("type", docS.get("type"));

                            docRef_product.collection(SizeClass.COLLECTION_NAME).add(sizeMap);
                        }
                    });
        }
    }
}