package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import nhom7.clothnest.R;
import nhom7.clothnest.util.customizeComponent.CustomDialogAdminAdd;
import nhom7.clothnest.util.customizeComponent.CustomToast;

public class Admin_VoucherActivity extends AppCompatActivity {

    Button btnOnpenDiag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_voucher);

        btnOnpenDiag = findViewById(R.id.open_dig);

        btnOnpenDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomToast.DisplayToast(Admin_VoucherActivity.this, 3, "Them thanh cong Them thanh cong Them thanh cong Them thanh cong");
            }
        });
    }
}