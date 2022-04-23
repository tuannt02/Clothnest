package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import nhom7.clothnest.R;

public class TermsAndConditionsActivity extends AppCompatActivity {
    WebView wvTac;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        wvTac = findViewById(R.id.webview_termsAndConditions);
        wvTac.loadUrl("https://adidasapp.adidas.com/legal/confirmed/en-GB/terms.html");

        btnBack = findViewById(R.id.imageview_back_termsAndConditions);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}