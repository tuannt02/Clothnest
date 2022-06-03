package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import nhom7.clothnest.R;
import nhom7.clothnest.notifications.NetworkChangeReceiver;

public class ImprintActivity extends AppCompatActivity {
    WebView wvImprint;
    ImageView btnBack;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprint);

        wvImprint = findViewById(R.id.webview_imprint);
        wvImprint.loadUrl("https://www.adidas-group.com/en/service/imprint/");

        btnBack = findViewById(R.id.imageview_back_imprint);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}