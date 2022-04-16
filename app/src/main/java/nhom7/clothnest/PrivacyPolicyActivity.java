package nhom7.clothnest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class PrivacyPolicyActivity extends AppCompatActivity {
    WebView wvPrivacy;
    ImageView btnBack;

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
    }
}