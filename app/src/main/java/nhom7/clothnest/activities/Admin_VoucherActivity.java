package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.VoucherAdapter;
import nhom7.clothnest.models.Voucher;
import nhom7.clothnest.util.customizeComponent.CustomDialog;
import nhom7.clothnest.util.customizeComponent.CustomDialogAdminAdd;
import nhom7.clothnest.util.customizeComponent.CustomToast;

public class Admin_VoucherActivity extends AppCompatActivity {

    TextView btnAdd;
    TextView btnClose;
    GridView gridViewVoucher;
    ArrayList<Voucher> voucherArrayList;
    VoucherAdapter voucherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_voucher);

        // Init ui
        initUi();


        // Set on click listener
        setOnClickListener();

        voucherArrayList = getVoucher();
        voucherAdapter = new VoucherAdapter(Admin_VoucherActivity.this, R.layout.voucher_item, voucherArrayList);
        gridViewVoucher.setAdapter(voucherAdapter);

    }

    private void initUi()   {
        btnAdd = findViewById(R.id.admin_voucher_btn_add);
        btnClose = findViewById(R.id.admin_voucher_btn_close);
        gridViewVoucher = findViewById(R.id.admin_voucher_gridview);
        voucherArrayList = new ArrayList<>();
    }

    private void setOnClickListener()   {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogAdminAdd customDialogAdminAdd = new CustomDialogAdminAdd(Admin_VoucherActivity.this,
                        "",
                        2,
                        "Code",
                        "Discount",
                        new CustomDialogAdminAdd.IClickListenerOnSaveBtn() {
                            @Override
                            public void onSubmit(String txtInput1, String txtInput2) {
                                CustomToast.DisplayToast(Admin_VoucherActivity.this,3, "This is Toast warning");
                            }
                        });

                customDialogAdminAdd.show();
            }
        });
    }

    private ArrayList<Voucher> getVoucher() {
        ArrayList<Voucher> arrayList = new ArrayList<>();

        arrayList.add(new Voucher("NAMMOI2022", 15));
        arrayList.add(new Voucher("TUDOI", 20));
        arrayList.add(new Voucher("QUOCTETHIEUNHI", 25));
        arrayList.add(new Voucher("LETINHNHAN", 10));
        arrayList.add(new Voucher("QUOCKHANH2/9", 20));

        return  arrayList;

    }
}