package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Transaction;
import nhom7.clothnest.models.User;
import nhom7.clothnest.util.customizeComponent.CustomDialog;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class Admin_StatisticActivity extends AppCompatActivity {

    ImageView btnClose;
    Button navViewDetail;
    TextView txtRevenue;
    TextView txtTotalTransaction;
    TextView txtTransactionFinished;
    TextView txtTransactionInProgress;
    TextView txtTransactionCanceled;
    TextView txtTotalUser;
    TextView txtTotalProduct;
    CustomProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_statistic);

        //Init UI
        initUi();

        // Set on click
        setOnClick();

        // Set view
        statistics();
    }

    private void initUi()   {
        btnClose = findViewById(R.id.admin_statistic_btn_close);
        navViewDetail = findViewById(R.id.admin_statistic_btn_view_detail);
        txtRevenue = findViewById(R.id.admin_statistic_txt_revenue);
        txtTotalTransaction = findViewById(R.id.admin_statistic_transaction);
        txtTransactionFinished = findViewById(R.id.admin_statistic_txt_quantity_finished);
        txtTransactionInProgress = findViewById(R.id.admin_statistic_txt_quantity_in_progress);
        txtTransactionCanceled = findViewById(R.id.admin_statistic_txt_quantity_cancel);
        txtTotalUser = findViewById(R.id.admin_statistic_txt_quantity_users);
        txtTotalProduct = findViewById(R.id.admin_statistic_txt_quantity_products);
        progressBar = new CustomProgressBar(Admin_StatisticActivity.this);
    }

    private void setOnClick()   {

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        navViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_StatisticActivity.this, Admin_StatisticsDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void statistics()   {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        progressBar.show();
        db.collection(User.COLLECTION_NAME)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int count = 0;
                        for(DocumentSnapshot document : queryDocumentSnapshots) {
                            count++;
                        }
                        txtTotalUser.setText(Integer.toString(count));
                    }
                });

        db.collection("products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int count = 0;
                        for(DocumentSnapshot document : queryDocumentSnapshots) {
                            count++;
                        }
                        txtTotalProduct.setText(Integer.toString(count));
                    }
                });


        db.collection("products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int count = 0;
                        for(DocumentSnapshot document : queryDocumentSnapshots) {
                            count++;
                        }
                        txtTotalProduct.setText(Integer.toString(count));
                    }
                });

        db.collection(Transaction.COLLECTION_TRANSACTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int count = 0;
                        double revenue = 0;
                        int qtyFinished = 0, qtyInprogress = 0, qtyCanceled = 0;
                        for(DocumentSnapshot document : queryDocumentSnapshots) {
                            String status = document.getString("status");
                            count++;
                            if(status.equals("Finished"))   {
                                double revenueItem = document.getDouble("total");
                                revenue+=revenueItem;
                                qtyFinished++;
                            }

                            if(status.equals("In Progress"))   {
                                qtyInprogress++;
                            }

                            if(status.equals("Canceled"))   {
                                qtyCanceled++;
                            }
                        }

                        txtRevenue.setText("$ " + revenue);
                        txtTotalTransaction.setText(Integer.toString(count));
                        txtTransactionFinished.setText(Integer.toString(qtyFinished));
                        txtTransactionInProgress.setText(Integer.toString(qtyInprogress));
                        txtTransactionCanceled.setText(Integer.toString(qtyCanceled));
                        progressBar.dismiss();

                    }
                });
    }
}