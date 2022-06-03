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

public class PrivacyPolicyActivity extends AppCompatActivity {
    WebView wvPrivacy;
    ImageView btnBack;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        wvPrivacy = findViewById(R.id.webview_privacyPolicy);
        wvPrivacy.loadUrl("https://www.uniqlo.com/uk/en/info/privacy-policy.html");

        btnBack = findViewById(R.id.imageview_back_privacyPolicy);
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