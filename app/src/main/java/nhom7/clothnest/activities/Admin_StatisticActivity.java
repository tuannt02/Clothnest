package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import nhom7.clothnest.R;

public class Admin_StatisticActivity extends AppCompatActivity {

    ImageView btnClose;
    Button navViewDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_statistic);

        //Init UI
        initUi();

        // Set on click
        setOnClick();
    }

    private void initUi()   {
        btnClose = findViewById(R.id.admin_statistic_btn_close);
        navViewDetail = findViewById(R.id.admin_statistic_btn_view_detail);
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
}