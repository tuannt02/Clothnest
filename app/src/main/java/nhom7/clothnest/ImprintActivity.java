package nhom7.clothnest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class ImprintActivity extends AppCompatActivity {
    WebView wvImprint;
    ImageView btnBack;

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
    }
}