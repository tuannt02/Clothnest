package nhom7.clothnest.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Transaction;
import nhom7.clothnest.util.customizeComponent.CustomDialog;
import nhom7.clothnest.util.customizeComponent.CustomDialogAdminAdd;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class Admin_StatisticsDetailActivity extends AppCompatActivity {

    ImageView btnClose;
    RelativeLayout orderByMonth;
    TextView txtChoose;
    RelativeLayout pickTime;
    TextView txtTime;
    TextView txtError;
    Button btnSearch;
    CustomProgressBar customProgressBar;

    TextView dateFilter;
    TextView txtRevenue;
    TextView txtTotalTransaction;
    TextView txtTransactionFinished;
    TextView txtTransactionInProgress;
    TextView txtTransactionCanceled;


    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat format1 = new SimpleDateFormat("dd / MM / yyyy");
    SimpleDateFormat format2 = new SimpleDateFormat("MM / yyyy");
    SimpleDateFormat format3 = new SimpleDateFormat("yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_statistics_detail);

        // Init view
        initUi();

        // Set on click
        setOnClick();
    }

    private void initUi()   {
        btnClose = findViewById(R.id.admin_statistic_detail_btn_close);
        orderByMonth = findViewById(R.id.admin_statistic_detail_order_by_month);
        txtChoose = findViewById(R.id.admin_statistic_detail_txt_month);
        pickTime = findViewById(R.id.admin_statistic_detail_pick_a_time);
        txtTime = findViewById(R.id.admin_statistic_detail_txt_pick_a_time);
        txtError = findViewById(R.id.admin_statistic_detail_txt_error);
        btnSearch = findViewById(R.id.admin_statistic_detail_btn_seach);
        customProgressBar = new CustomProgressBar(Admin_StatisticsDetailActivity.this);

        dateFilter = findViewById(R.id.admin_statistic_detail_txt_datetime);
        txtRevenue = findViewById(R.id.admin_statistic_detail_txt_revenue);
        txtTotalTransaction = findViewById(R.id.admin_statistic_detail_txt_transaction);
        txtTransactionFinished = findViewById(R.id.admin_statistic_detail_txt_quantity_finished);
        txtTransactionInProgress = findViewById(R.id.admin_statistic_detail_txt_quantity_in_progress);
        txtTransactionCanceled = findViewById(R.id.admin_statistic_detail_txt_quantity_cancel);
    }

    private void setOnClick()   {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        orderByMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = getListOptionMenu();

                CustomOptionMenu orderOptionMenu = new CustomOptionMenu(Admin_StatisticsDetailActivity.this, new CustomOptionMenu.IClickListenerOnItemListview() {
                    @Override
                    public void onClickItem(int pos) {
                        if(pos == 0)    { // Order by Date
                            txtChoose.setText("Date");
                            txtTime.setText("");
                        }

                        if(pos == 1)    { // Order by Month
                            txtChoose.setText("Month");
                            txtTime.setText("");
                        }

                        if(pos == 2)    { // Order by Year
                            txtChoose.setText("Year");
                            txtTime.setText("");
                        }
                    }
                },list, "ORDER BY", null);
                orderOptionMenu.show();
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtChooseFilter = txtChoose.getText().toString();

                if(txtChooseFilter.equals("Date"))  {
                    openPicker(txtChooseFilter);
                }

                if(txtChooseFilter.equals("Month"))  {
                    openDatetimePicker(txtChooseFilter);
                }

                if(txtChooseFilter.equals("Year"))  {
                    openDatetimePicker(txtChooseFilter);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTimeStr = txtTime.getText().toString();

                if(txtTimeStr.equals(""))  {
                    txtError.setText("Picker time must be filled in");
                    return;
                }

                txtError.setText("");
                // Call Api here
                getStatistic();

            }
        });
    }

    private void getStatistic() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String dateNeedFilter = dateFilter.getText().toString().trim();
        customProgressBar.show();
        db.collection(Transaction.COLLECTION_TRANSACTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int count = 0;
                        double revenue = 0;
                        int qtyFinished = 0, qtyInprogress = 0, qtyCanceled = 0;
                        for(DocumentSnapshot document : queryDocumentSnapshots) {
                            String orderDate = document.getString("orderDate");
                            if(!isPass(dateNeedFilter, orderDate))   {
                                continue;
                            }

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
                        customProgressBar.dismiss();
                    }
                });
    }

    private boolean isPass(String txtDate, String txtDateFirebase)    {
        String mode = txtChoose.getText().toString().trim();

        String YEAR = txtDateFirebase.substring(0,4);
        String MM = txtDateFirebase.substring(5,7);
        String DD = txtDateFirebase.substring(8,10);

        if(mode.equals("Date")) {

            String localYEAR = txtDate.substring(10,14);
            String localMM = txtDate.substring(5,7);
            String localDD = txtDate.substring(0,2);
            if(localYEAR.equals(YEAR)
            && localMM.equals(MM)
            && localDD.equals(DD)) return true;

            return false;

        }

        if(mode.equals("Month"))    {
            String localYEAR = txtDate.substring(5);
            String localMM = txtDate.substring(0,2);
            System.out.println("alo" +localMM);
            if(localYEAR.equals(YEAR) && localMM.equals(MM)) return true;
            return false;
        }

        if(mode.equals("Year"))    {
            String localYEAR = txtDate.substring(0,4);
            if(localYEAR.equals(YEAR)) return true;
            return false;
        }

        return false;
    }

    private ArrayList<String> getListOptionMenu()   {
        ArrayList<String> list = new ArrayList<>();
        list.add("Date");
        list.add("Month");
        list.add("Year");

        return list;
    }

    private void openDatetimePicker(String mode)    {

        RackMonthPicker rackMonthPicker = new RackMonthPicker(this)
                .setLocale(Locale.ENGLISH)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        if(mode.equals("Month"))    {
                            String monthYear = "";
                            if(month < 10)  {
                                monthYear = "0" + month + " / " + year;
                            }
                            else    {
                                monthYear = month + " / " + year;
                            }

                            txtTime.setText(monthYear);
                            dateFilter.setText(monthYear);
                        }
                        if(mode.equals("Year"))    {
                            txtTime.setText(String.valueOf(year));
                            dateFilter.setText(String.valueOf(year));
                        }
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

        rackMonthPicker.show();
    }

    public void openPicker(String mode)   {
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dateNow = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                if(mode.equals("Date")) {
                    txtTime.setText(format1.format(calendar.getTime()));
                    dateFilter.setText(format1.format(calendar.getTime()));
                }
                if(mode.equals("Month")) {
                    txtTime.setText(format2.format(calendar.getTime()));
                }
                if(mode.equals("Year")) {
                    txtTime.setText(format3.format(calendar.getTime()));
                }
            }
        }, yearNow, monthNow, dateNow);

        datePickerDialog.setTitle("Select date");
        datePickerDialog.show();
    }

}