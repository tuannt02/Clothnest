package nhom7.clothnest.util.customizeComponent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import nhom7.clothnest.R;

public class CustomDialog extends Dialog {

    //interface
    public  interface Listener{
         void ListenerEntered();
    }
    public Context context;
    private Listener listener;

    public CustomDialog(Context context, Listener listener) {
        super(context);
        this.context=context;
        this.listener= listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlButton();
    }

    private void controlButton() {
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        builder.setTitle("Notification")
                .setIcon(R.mipmap.ic_launcher_foreground)
                .setMessage("Do you want selected cart? ")
                .setPositiveButton("YES", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "You selected the product", Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeButton("NO", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }
}
