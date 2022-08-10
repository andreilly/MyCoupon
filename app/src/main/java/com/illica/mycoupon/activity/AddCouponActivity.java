package com.illica.mycoupon.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.illica.mycoupon.R;
import com.illica.mycoupon.fragment.CaptureAct;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class AddCouponActivity extends AppCompatActivity {

    private Context context = null;

    private ImageButton btnAddCoupon = null;

    private Button btnScan = null;
    private EditText txtDescription = null;
    private EditText txtName = null;
    private EditText txtCode = null;
    private CalendarView txtDate = null;
    private RadioButton rbQRCode = null;
    private RadioButton rbBarcode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        Toolbar toolbar = (Toolbar)findViewById(R.id.addCouponToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Set Activity Context
        this.context = this.getApplicationContext();
        //Init UI Component
        initUI();
    }
    private void initUI(){
          Log.d("","Init UI");

          btnAddCoupon = this.findViewById(R.id.btnAddCoupon);

          btnScan = this.findViewById(R.id.btnScan);

          txtDescription = this.findViewById(R.id.txtDescription);
          txtName = this.findViewById(R.id.txtName);
          txtDate = this.findViewById(R.id.txtDate);
          rbQRCode = this.findViewById(R.id.rbQRCode);
          rbBarcode = this.findViewById(R.id.rbBarcode);
        this.btnAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("","Add button CLicked !");

            }
        });
        this.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanOptions options = new ScanOptions();
                options.setPrompt("Volume up to flash on");
                options.setBeepEnabled(true);
                options.setOrientationLocked(true);
                options.setCaptureActivity(CaptureAct.class);
                barLauncher.launch(options);
            }
        });

    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddCouponActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    });
}