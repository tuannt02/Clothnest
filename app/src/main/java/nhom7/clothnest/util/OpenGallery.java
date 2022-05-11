package nhom7.clothnest.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenGallery {
    // Hàm này mở thư viện ảnh của device
    public static final int REQ_CODE_READ_EXTERNAL_STORAGE = 10;


    public static void onClickOpenGallery(Context context, ActivityResultLauncher<Intent> mActivityResultLauncher) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)   {
            openGallery(mActivityResultLauncher);
            return;
        }

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED)  {
            openGallery(mActivityResultLauncher);
        }
        else    {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions((Activity) context,permissions, REQ_CODE_READ_EXTERNAL_STORAGE);
        }
    }


    public static void openGallery(ActivityResultLauncher<Intent> mActivityResultLauncher)   {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

}
