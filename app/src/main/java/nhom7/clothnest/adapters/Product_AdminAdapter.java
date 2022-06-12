package nhom7.clothnest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.Admin_Collections_ProductActivity;
import nhom7.clothnest.activities.Admin_Modify_ProductActivity;
import nhom7.clothnest.activities.Admin_ProductDetailActivity;
import nhom7.clothnest.fragments.Admin_ProductsFragment;
import nhom7.clothnest.models.Product_Admin;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.Stock;

public class Product_AdminAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Product_Admin> productAdminList;
    public static FirebaseFirestore db;

    //Change screen
    private View mView;

    //reference
    private TextView tvProductName;
    private ImageView ivProductImage;
    private TextView tvID;
    private TextView tvCost;
    private TextView tvStock;

    private static int available;

    public Product_AdminAdapter(Context mContext, ArrayList<Product_Admin> productAdminList) {
        this.mContext = mContext;
        this.productAdminList = productAdminList;
    }

    @Override
    public int getCount() {
        return productAdminList.size();
    }

    @Override
    public Object getItem(int i) {
        return productAdminList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = view = layoutInflater.inflate(R.layout.item_admin_productlist, null);

        reference();
        getData(i);
        setEventsClick(i);

        return view;
    }

    private void getData(int i) {
        Product_Admin product = productAdminList.get(i);

        tvProductName.setText(product.getName());

        Glide.with(mContext).load(product.getMainImage()).into(ivProductImage);

        tvID.setText("ID: " + product.getId());

        Double price = Double.parseDouble(String.valueOf(product.getPrice()));
        tvCost.setText("$" + price.toString().replaceAll("\\.?[0-9]*$", ""));

        tvStock.setText("" + product.getStock());
    }

    private void reference() {
        tvProductName = (TextView) mView.findViewById(R.id.item_admin_productList_productName);
        ivProductImage = (ImageView) mView.findViewById(R.id.item_admin_productList_productImage);
        tvID = (TextView) mView.findViewById(R.id.item_admin_productList_productID);
        tvCost = (TextView) mView.findViewById(R.id.item_admin_productList_productPrice);
        tvStock = (TextView) mView.findViewById(R.id.item_admin_productList_productStock);
    }

    private void setEventsClick(int i) {
        //Click on product
        LinearLayout product = mView.findViewById(R.id.item_admin_productList_View);
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoDetail(i);
            }
        });

    }

    private void gotoDetail(int i) {
        Intent intent_AdminProductDetail = new Intent(mContext, Admin_ProductDetailActivity.class);
        intent_AdminProductDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent_AdminProductDetail.putExtra("handle_adminProductDetail", productAdminList.get(i).getId());
        mContext.startActivity(intent_AdminProductDetail);
    }

    public static void getProductAndPushToGridView(ArrayList<Product_Admin> listProduct, ArrayList<Product_Admin> listOriginal, Product_AdminAdapter adapter, TextView tvNumOfProduct, TextView tvNumOfStock) {
        //get data
        db = FirebaseFirestore.getInstance();
        db.collection(Product_Admin.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> productTask) {
                        if (productTask.isSuccessful()) {
                            tvNumOfProduct.setText(productTask.getResult().size() + "");
                            //Duyệt từng product
                            available = 0;

                            for (QueryDocumentSnapshot document : productTask.getResult()) {
                                //product để thêm vào arrayList
//                                Product_Admin productAdmin = new Product_Admin();
//                                //Thêm thumbnail vào arraylist và notifyDataSetChanged
//                                listProduct.add(productAdmin);
//                                listOriginal.add(productAdmin);
//
//                                //tempOject chứa product
//                                Map<String, Object> tempObject = document.getData();
//
//                                //Set id
//                                productAdmin.setId(document.getId());
//                                adapter.notifyDataSetChanged();
//
//                                // Lặp qua từng field của một document
//                                Iterator myVeryOwnIterator = tempObject.keySet().iterator();
//                                while (myVeryOwnIterator.hasNext()) {
//                                    String key = (String) myVeryOwnIterator.next();
//
//                                    //Set name
//                                    if (key.equals("name")) {
//                                        productAdmin.setName((String) tempObject.get(key));
//                                        adapter.notifyDataSetChanged();
//                                    }
//
//                                    //Set price
//                                    if (key.equals("price")) {
//                                        Double price = document.getDouble(key);
//                                        productAdmin.setPrice(price);
//                                        adapter.notifyDataSetChanged();
//                                    }
//
//                                    //set mainImage
//                                    if (key.equals("main_img")) {
//                                        productAdmin.setMainImage((String) tempObject.get(key));
//                                        adapter.notifyDataSetChanged();
//                                    }
//                                }
//
//                                if (Admin_ProductsFragment.dialog.isShowing())
//                                    Admin_ProductsFragment.dialog.dismiss();
//
//                                document.getReference().collection(Stock.COLLECTION_NAME)
//                                        .get()
//                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                if (task.isSuccessful()) {
//                                                    int stock = 0;
//                                                    for (QueryDocumentSnapshot stock_ref : task.getResult()) {
//                                                        stock += (int) Math.round(stock_ref.getDouble("quantity"));
//                                                        if (task.getResult().getDocuments().indexOf(stock_ref) == task.getResult().size() - 1) {
//                                                            productAdmin.setStock(stock);
//                                                            adapter.notifyDataSetChanged();
//
//                                                            if(stock > 0)
//                                                                tvNumOfStock.setText(++available/2 + "");
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        });
                                Product_Admin productAdmin = new Product_Admin();
                                listProduct.add(productAdmin);
                                listOriginal.add(productAdmin);

                                productAdmin.setId(document.getId());
                                adapter.notifyDataSetChanged();


                                productAdmin.setName(document.getString("name"));
                                adapter.notifyDataSetChanged();

                                Double price = document.getDouble("price");
                                productAdmin.setPrice(price);
                                adapter.notifyDataSetChanged();

                                productAdmin.setMainImage((document.getString("main_img")));
                                adapter.notifyDataSetChanged();

                                if (Admin_ProductsFragment.dialog.isShowing())
                                    Admin_ProductsFragment.dialog.dismiss();

                                document.getReference().collection(Stock.COLLECTION_NAME)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int stock = 0;
                                                    for (QueryDocumentSnapshot stock_ref : task.getResult()) {
                                                        stock += (int) Math.round(stock_ref.getDouble("quantity"));

                                                        if (task.getResult().getDocuments().indexOf(stock_ref) == task.getResult().size() - 1) {
                                                            productAdmin.setStock(stock);
                                                            adapter.notifyDataSetChanged();

                                                            if (stock > 0)
                                                                tvNumOfStock.setText(++available + "");
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    public static void getModifyProducts(ArrayList<Product_Admin> listProduct, ArrayList<Product_Admin> originalList, Product_AdminAdapter adapter, String modifyName, TextView tvNumOfProduct, TextView tvNumOfStock) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(modifyName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> productTask) {
                        if (productTask.isSuccessful()) {
                            tvNumOfProduct.setText(productTask.getResult().size() + "");

                            available = 0;

                            for (QueryDocumentSnapshot query : productTask.getResult()) {
                                DocumentReference document_Ref = query.getDocumentReference("product_id");
                                document_Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot document) {
                                        Product_Admin productAdmin = new Product_Admin();
                                        listProduct.add(productAdmin);
                                        originalList.add(productAdmin);

                                        productAdmin.setId(document.getId());
                                        adapter.notifyDataSetChanged();


                                        productAdmin.setName(document.getString("name"));
                                        adapter.notifyDataSetChanged();

                                        Double price = document.getDouble("price");
                                        productAdmin.setPrice(price);
                                        adapter.notifyDataSetChanged();

                                        productAdmin.setMainImage((document.getString("main_img")));
                                        adapter.notifyDataSetChanged();

                                        if (Admin_Modify_ProductActivity.dialog.isShowing())
                                            Admin_Modify_ProductActivity.dialog.dismiss();

                                        document.getReference().collection(Stock.COLLECTION_NAME)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            int stock = 0;
                                                            for (QueryDocumentSnapshot stock_ref : task.getResult()) {
                                                                stock += (int) Math.round(stock_ref.getDouble("quantity"));

                                                                if (task.getResult().getDocuments().indexOf(stock_ref) == task.getResult().size() - 1) {
                                                                    productAdmin.setStock(stock);
                                                                    adapter.notifyDataSetChanged();

                                                                    if (stock > 0)
                                                                        tvNumOfStock.setText(++available + "");
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public static void getProductFromCollection(ArrayList<Product_Admin> listProduct, ArrayList<Product_Admin> listOriginal, Product_AdminAdapter adminAdapter, TextView tvNumOfProduct, TextView tvNumOfStock, String collectionName) {
        String temp = Product_Thumbnail.COLECTION_NAME_COLLECTIONS + '/' + collectionName + '/' + Product_Thumbnail.COLECTION_NAME_PRODUCTS;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(temp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> productTask) {
                        if (productTask.isSuccessful()) {
                            tvNumOfProduct.setText(productTask.getResult().size() + "");

                            available = 0;

                            for (QueryDocumentSnapshot document : productTask.getResult()) {
                                DocumentReference documentReference1 = document.getDocumentReference("product_id");
                                documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Product_Admin productAdmin = new Product_Admin();
                                        listProduct.add(productAdmin);
                                        listOriginal.add(productAdmin);

                                        productAdmin.setId(documentSnapshot.getId());

                                        productAdmin.setName(documentSnapshot.getString("name"));
                                        adminAdapter.notifyDataSetChanged();

                                        productAdmin.setPrice(documentSnapshot.getDouble("price"));
                                        adminAdapter.notifyDataSetChanged();


                                        productAdmin.setMainImage(documentSnapshot.getString("main_img"));
                                        adminAdapter.notifyDataSetChanged();

                                        if (Admin_Collections_ProductActivity.dialog.isShowing())
                                            Admin_Collections_ProductActivity.dialog.dismiss();

                                        documentSnapshot.getReference().collection(Stock.COLLECTION_NAME)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            int stock = 0;
                                                            for (QueryDocumentSnapshot stock_ref : task.getResult()) {
                                                                stock += (int) Math.round(stock_ref.getDouble("quantity"));

                                                                if (task.getResult().getDocuments().indexOf(stock_ref) == task.getResult().size() - 1) {
                                                                    productAdmin.setStock(stock);
                                                                    adminAdapter.notifyDataSetChanged();

                                                                    if(stock > 0)
                                                                        tvNumOfStock.setText(++available + "");

                                                                }
                                                            }
                                                        }
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

