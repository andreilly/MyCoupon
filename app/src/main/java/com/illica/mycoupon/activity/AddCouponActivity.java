package com.illica.mycoupon.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;

import com.illica.mycoupon.Other.Alert;
import com.illica.mycoupon.Other.Utils;
import com.illica.mycoupon.R;
import com.illica.mycoupon.definition.CouponType;
import com.illica.mycoupon.fragment.CaptureAct;
import com.illica.mycoupon.model.CouponDescriptor;
import com.illica.mycoupon.persistence.CouponDescriptorManager;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import java.time.LocalDate;

public class AddCouponActivity extends AppCompatActivity {

    private CouponDescriptorManager couponDescriptorManager = null;
    private Context mContext = null;
    Animation scaleUp, scaleDown;

    private Context context;
    private ImageButton btnAddCoupon;
    private Button btnScan;
    private EditText txtDescription;
    private EditText txtName;
    private EditText txtCode;
    private CalendarView cwDate;
    private RadioButton rbQRCode;
    private RadioButton rbBarcode;
    private Switch switchDate;
    private CheckBox reusable;

    //region Variables used to save inserted data from user
    private String companyName = null;
    private String description = null;
    private CouponType type = null;
    private String code = null;
    private LocalDate insertedDate = null;
    private String expiredDate = null;
    private String formatCoupon = null;
    private Boolean isReusable = false;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        this.mContext = this.getApplicationContext();
        this.couponDescriptorManager = couponDescriptorManager.getInstance(mContext);


        scaleUp = AnimationUtils.loadAnimation(mContext,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(mContext,R.anim.scale_down);

        //Init Toolbar
        initToolbar();

        //Set Activity Context
        this.context = this.getApplicationContext();

        //Init UI Component
        initUI();
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.addCouponToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void initUI() {

        //region Initialize component of the view
        btnAddCoupon = this.findViewById(R.id.btnAddCoupon);
        btnScan = this.findViewById(R.id.btnScan);
        txtCode = this.findViewById(R.id.txtCode);
        txtDescription = this.findViewById(R.id.txtDescription);
        txtName = this.findViewById(R.id.txtName);
        cwDate = this.findViewById(R.id.txtDate);
        rbQRCode = this.findViewById(R.id.rbQRCode);
        rbBarcode = this.findViewById(R.id.rbBarcode);
        switchDate = this.findViewById(R.id.switchDate);
        reusable = this.findViewById(R.id.reusable);
        cwDate.setVisibility(View.INVISIBLE);
        //endregion

        // Add coupon button listener
        this.btnAddCoupon.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP){
                    btnAddCoupon.startAnimation(scaleUp);
                    // If invalid insertion -> show error message
                    if(invalidInsertion()) {
                        Alert.showInformation(AddCouponActivity.this, getString(R.string.title_error), getString(R.string.msg_errore_parametri));
                    }else
                    {
                        //Get value from view
                        getDataFromView();

                        // Create and Add new coupon
                        couponDescriptorManager.addCouponToHead(createNewCoupon());

                        //region Show message and return on MainActivity
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddCouponActivity.this);
                        builder.setTitle(R.string.information);
                        builder.setMessage(R.string.coupon_aggiunto);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(context, MainActivity.class));
                            }
                        });
                        builder.create().show();
                        //endregion
                    }
                }else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnAddCoupon.startAnimation(scaleDown);
                }
                return true;
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

        //Listener for date
        switchDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cwDate.setVisibility(View.VISIBLE);
                } else {
                    cwDate.setVisibility(View.INVISIBLE);
                }
            }
        });

        //Listener for calendar view
        cwDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month, int date) {
                insertedDate =  LocalDate.of(year,month+1,date);
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
    /*
    Return true if date is checked, false otherwise
     */
    private boolean enterExpiredDate(){
       return  switchDate.isChecked();
    }


    /*
     Return CouponType
     */
    private CouponType getTypeCoupon(){
        if(rbBarcode.isChecked())
            return CouponType.Barcode;
        else if(rbQRCode.isChecked())
            return CouponType.QRCode;
        else
            return null;
    }

    /*
     Check if any required parameters are missing
     */
    private boolean invalidInsertion(){
        return Utils.isEmptyEditText(txtName) || Utils.isEmptyEditText(txtCode) || getTypeCoupon() == null;
    }

    /*
     Get data from view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDataFromView(){

        this.companyName = txtName.getText().toString();
        this.description = txtDescription.getText().toString();
        this.type = getTypeCoupon();
        this.code = txtCode.getText().toString();
        if(reusable.isChecked())
            isReusable = true;

        if(!enterExpiredDate())
            this.expiredDate = "";
        else
            this.expiredDate = insertedDate.toString();
    }
    /*
    Method that allow to create a new coupon
     */
    private CouponDescriptor createNewCoupon(){
        return new CouponDescriptor(this.companyName,this.description,this.type,this.code,this.expiredDate,this.formatCoupon,isReusable);
    }

}