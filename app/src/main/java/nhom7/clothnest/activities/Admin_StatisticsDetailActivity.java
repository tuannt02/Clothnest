package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import nhom7.clothnest.R;

public class Admin_StatisticsDetailActivity extends AppCompatActivity {

    ImageView btnClose;


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
    }

    private void setOnClick()   {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}