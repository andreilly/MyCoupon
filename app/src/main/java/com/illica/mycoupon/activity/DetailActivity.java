package com.illica.mycoupon.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.illica.mycoupon.Other.Utils;
import com.illica.mycoupon.R;
import com.illica.mycoupon.adapter.MyAdapter;
import com.illica.mycoupon.definition.CouponType;
import com.illica.mycoupon.fragment.HistoryFragment;
import com.illica.mycoupon.model.CouponDescriptor;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.Util;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    boolean isImageFitToScreen;

    ImageView imgCode;
    TextView lbName;
    TextView lbCode;
    TextView lbDescription;
    TextView lbDate;


    CouponDescriptor cp;
    private String TAG = "Detail_Activity";
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.mContext = this;

        //Init toolbar
        initToolbar();

        //Receive data
        receiveData();

        // Initialize UI
        initUI();


        loadUI();


    }
    private void loadUI(){

        // Method that allows you to manage and view the encoding of the coupon
        handleEncodingCode();
        lbName.setText(cp.getCompanyName());
        lbDescription.setText(cp.getDescription());
        lbCode.setText(cp.getCode());
        if(cp.getExpiryDate() == ""){
            lbDate.setText("-");
        }else{
            lbDate.setText(cp.getExpiryDate());
        }

    }
    private void initToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Set title of actionBar
        actionBar.setTitle(R.string.coupon_detail);
    }
    private void receiveData(){
        Intent i=getIntent();
        cp = (CouponDescriptor) i.getExtras().getSerializable(ListActivity.CouponObject);
        Integer pos = i.getExtras().getInt(ListActivity.Position);

    }
    private void initUI(){

        imgCode = this.findViewById(R.id.couponImageList);
        lbCode = this.findViewById(R.id.lbCode);
        lbDescription = this.findViewById(R.id.lbDescription);
        lbName = this.findViewById(R.id.lbName);
        lbDate = this.findViewById(R.id.lbDate);

    }
    private void handleEncodingCode(){
        String format;
        String code;
        if(cp != null){
            code = cp.getCode();
            format = cp.getFormat();
            if(cp.getCouponType() == CouponType.QRCode.ordinal()) {
                if(cp.getFormat()==null){
                    imgCode.setImageResource(R.drawable.no_qrcode);

                }else{
                    encodingCoupon(cp.getCode(),format,400,400,false);
                }
            }else if(cp.getCouponType() == CouponType.Barcode.ordinal()) {
                if (cp.getFormat() == null) {
                    imgCode.setImageResource(R.drawable.no_barcode);
                }else{
                    encodingCoupon(cp.getCode(),format,380 ,280,true);
                }
            }
        }

    }
    private void encodingCoupon(String code, String format, Integer x, Integer y, Boolean border){
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        MultiFormatWriter writer = new MultiFormatWriter();
        try{
            //Initialize bit matrix
            BitMatrix matrix = writer.encode(code, BarcodeFormat.valueOf(format),x,y);
            //Initialize barcode encoder
            BarcodeEncoder encoder = new BarcodeEncoder();
            //Initialize Bitmap
            Bitmap bitmap = encoder.createBitmap(matrix);
            if(border){
                bitmap = Utils.addWhiteBorder(bitmap,20);
            }
            imgCode.setImageBitmap(bitmap);
            imgCode.setScaleType(ImageView.ScaleType.FIT_CENTER);



        }catch (WriterException e){
            e.printStackTrace();
        }

    }
}
