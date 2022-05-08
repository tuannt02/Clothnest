package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import nhom7.clothnest.R;
import nhom7.clothnest.util.customizeComponent.CustomDialogAdminAdd;

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
                CustomDialogAdminAdd customDialogAdminAdd = new CustomDialogAdminAdd(Admin_VoucherActivity.this,
                        "Add voucher",
                        2,
                        "alo",
                        "blo",
                        new CustomDialogAdminAdd.IClickListenerOnSaveBtn() {
                            @Override
                            public void onSubmit(String txtInput1, String txtInput2) {
                                System.out.println(txtInput1 + "__" + txtInput2);
                                Toast.makeText(Admin_VoucherActivity.this, txtInput1 + "__" + txtInput2, Toast.LENGTH_SHORT).show();
                            }
                        });
                customDialogAdminAdd.show();
            }
        });
    }
}