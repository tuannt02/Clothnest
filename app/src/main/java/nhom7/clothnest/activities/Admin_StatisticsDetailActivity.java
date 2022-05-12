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

import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import nhom7.clothnest.R;
import nhom7.clothnest.util.customizeComponent.CustomDialog;
import nhom7.clothnest.util.customizeComponent.CustomDialogAdminAdd;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;

public class Admin_StatisticsDetailActivity extends AppCompatActivity {

    ImageView btnClose;
    RelativeLayout orderByMonth;
    TextView txtChoose;
    RelativeLayout pickTime;
    TextView txtTime;
    TextView txtError;
    Button btnSearch;

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


            }
        });
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
                            txtTime.setText(month + " / " + year);
                        }
                        if(mode.equals("Year"))    {
                            txtTime.setText(String.valueOf(year));
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

        SimpleDateFormat format1 = new SimpleDateFormat("dd / MM / yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("MM / yyyy");
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                if(mode.equals("Date")) {
                    txtTime.setText(format1.format(calendar.getTime()));
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