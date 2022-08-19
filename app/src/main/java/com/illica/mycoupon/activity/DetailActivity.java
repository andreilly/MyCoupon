package com.illica.mycoupon.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.illica.mycoupon.Other.Utils;
import com.illica.mycoupon.R;
import com.illica.mycoupon.definition.CouponType;
import com.illica.mycoupon.model.CouponDescriptor;
import com.illica.mycoupon.persistence.CouponDescriptorManager;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.time.LocalDate;

public class DetailActivity extends AppCompatActivity {
    private CouponDescriptorManager couponDescriptorManager = null;
    private Context mContext = null;


    private String typeList;
    Button btnUsed;
    ImageView imgCode;
    TextView lbName;
    TextView lbCode;
    TextView lbDescription;
    TextView lbDate;
    ImageView imageReusable;
    CouponDescriptor cp;
    Integer pos = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.mContext = this;
        this.couponDescriptorManager = couponDescriptorManager.getInstance(mContext);

        //Receive data
        receiveData();

        //Init toolbar
        initToolbar();


        // Initialize UI
        initUI();


        loadUI();


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUI(){

        // Method that allows you to manage and view the encoding of the coupon
        handleEncodingCode();
        lbName.setText(cp.getCompanyName());
        if(cp.getDescription().equals(""))
            lbDescription.setText("-");
        else
            lbDescription.setText(cp.getDescription());

        lbCode.setText(cp.getCode());

        if(cp.getExpiryDate().equals(""))
            lbDate.setText("-");
        else{
            LocalDate dateLocal = LocalDate.parse(cp.getExpiryDate());
            lbDate.setText(Utils.convertInITAFormat(dateLocal));
        }

        if(cp.getReusable()==true)
            imageReusable.setImageResource(R.drawable.check_img);
        else
            imageReusable.setImageResource(R.drawable.x_img);

    }
    private void initToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.TypeList,typeList);
                bundle.putInt(ListActivity.Position,pos);
                Intent newIntent = new Intent(new Intent(mContext,ListActivity.class));
                newIntent.putExtras(bundle);
                startActivity(newIntent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);

            }
        });
        // Set title of actionBar
        actionBar.setTitle(R.string.coupon_detail);

    }

    private void receiveData(){
        Intent i=getIntent();
        cp = (CouponDescriptor) i.getExtras().getSerializable(ListActivity.CouponObject);
        pos = i.getExtras().getInt(ListActivity.Position);
        typeList = i.getExtras().getString(MainActivity.TypeList);

    }
    private void initUI(){

        imgCode = this.findViewById(R.id.couponImageList);
        lbCode = this.findViewById(R.id.lbCode);
        lbDescription = this.findViewById(R.id.lbDescription);
        lbName = this.findViewById(R.id.lbName);
        lbDate = this.findViewById(R.id.lbDate);
        imageReusable = this.findViewById(R.id.imageReusable);
        btnUsed = this.findViewById(R.id.btnUsed);

        if(!typeList.equals(MainActivity.Active_Coupon)){
            btnUsed.setVisibility(View.INVISIBLE);
        }

        btnUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponDescriptorManager.updateUsed(cp.getId());

                Toast.makeText(mContext, "Coupon contrassegnato con successo",
                        Toast.LENGTH_SHORT).show();
            }
        });
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
                    encodingCoupon(code,format,400,400,false);
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) imgCode.getLayoutParams();
                    layoutParams.bottomMargin = 50;
                    layoutParams.topMargin = 50;

                    imgCode.setLayoutParams(layoutParams);
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
