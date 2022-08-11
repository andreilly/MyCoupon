package com.illica.mycoupon.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.illica.mycoupon.Other.Alert;
import com.illica.mycoupon.Other.Utils;
import com.illica.mycoupon.R;
import com.illica.mycoupon.definition.CouponType;
import com.illica.mycoupon.fragment.CaptureAct;
import com.illica.mycoupon.model.CouponDescriptor;
import com.illica.mycoupon.persistence.CouponDescriptorManager;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class AddCouponActivity extends AppCompatActivity {

    private CouponDescriptorManager couponDescriptorManager = null;
    private Context mContext = null;


    private Context context = null;
    private ImageButton btnAddCoupon = null;
    private Button btnScan = null;
    private EditText txtDescription = null;
    private EditText txtName = null;
    private EditText txtCode = null;
    private CalendarView cwDate = null;
    private RadioButton rbQRCode = null;
    private RadioButton rbBarcode = null;
    private Switch switchDate = null;
    private String formatCoupon = null;

    // Variables used to save inserted data from user
    String companyName = null;
    private String description = null;
    private CouponType type = null;
    private String code = null;
    private LocalDate insertedDate = null;
    private String expiredDate = null;

    private Boolean isReusable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        this.mContext = this.getApplicationContext();
        this.couponDescriptorManager = couponDescriptorManager.getInstance(mContext);


        Toolbar toolbar = (Toolbar) findViewById(R.id.addCouponToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Set Activity Context
        this.context = this.getApplicationContext();
        //Init UI Component
        initUI();
    }

    private void initUI() {
        // Initialize component of the view
        btnAddCoupon = this.findViewById(R.id.btnAddCoupon);
        btnScan = this.findViewById(R.id.btnScan);
        txtCode = this.findViewById(R.id.txtCode);
        txtDescription = this.findViewById(R.id.txtDescription);
        txtName = this.findViewById(R.id.txtName);
        cwDate = this.findViewById(R.id.txtDate);
        rbQRCode = this.findViewById(R.id.rbQRCode);
        rbBarcode = this.findViewById(R.id.rbBarcode);
        switchDate = this.findViewById(R.id.switchDate);
        cwDate.setVisibility(View.INVISIBLE);

        // Add coupon button listener
        this.btnAddCoupon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(invalidInsertion()) {
                    Alert.showInformation(AddCouponActivity.this, getString(R.string.title_error), getString(R.string.msg_errore_parametri));
                }else
                {
                    getDataFromView();
                    couponDescriptorManager.addCouponToHead(createNewCoupon());
                    Alert.showInformation(AddCouponActivity.this,getString(R.string.information),getString(R.string.coupon_aggiunto) );

                }

            }

        });
        // Add scan button listener
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

        switchDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    cwDate.setVisibility(View.VISIBLE);
                } else {
                    cwDate.setVisibility(View.INVISIBLE);
                }
            }
        });
        cwDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month, int date) {
                insertedDate =  LocalDate.of(year,month,date);

            }
        });
    }

    /**
     * Method use to scan QRCODE or Barcode
     */
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            txtCode.setText(result.getContents());
            formatCoupon = result.getFormatName();

        }
    });

    /*
     Method that allow user to hide keyboard when click outside the TextField
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    private boolean enterExpiredDate(){
       return  switchDate.isChecked() == true ? true : false ;
    }

    private CouponType getTypeCoupon(){
        if(rbBarcode.isChecked() == true)
        {
            return CouponType.Barcode;
        }else if(rbQRCode.isChecked() == true){
            return CouponType.QRCode;
        }else {
            return null;
        }
    }
    private boolean invalidInsertion(){
       if (Utils.isEmptyEditText(txtName) || Utils.isEmptyEditText(txtCode) || getTypeCoupon() == null ){
            return true;
        }
       else {
           return false;
       }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDataFromView(){

        this.companyName = txtName.getText().toString();
        this.description = txtDescription.getText().toString();
        this.type = getTypeCoupon();
        this.code = txtCode.getText().toString();
        if(!enterExpiredDate()){
            this.expiredDate = "";
        }else
        {
            this.expiredDate = Utils.convertInITAFormat(insertedDate);
        }
    }
    private CouponDescriptor createNewCoupon(){
        return new CouponDescriptor(this.companyName,this.description,this.type,this.code,this.expiredDate,this.formatCoupon,false);
    }

}