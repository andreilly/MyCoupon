package com.illica.mycoupon.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.illica.mycoupon.R;

public class MainActivity extends AppCompatActivity {


    public static final String Active_Coupon ="Coupon attivi";
    public static final String Expired_Coupon ="Coupon scaduti";
    public static final String Used_Coupon ="Coupon utilizzati";

    public static final String TypeList = "TypeList";

    //region Variable declaration
    private Button buttonActiveList;
    private Button buttonExpiredList;
    private Button buttonUsedList;

    private ImageButton addCouponButton = null;
    private Context context = null;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Activity Context

        this.context = this.getApplicationContext();

        //Init UI Component
        initUI();


    }
    private void initUI(){
        this.buttonActiveList = this.findViewById(R.id.activeCoupon);
        this.buttonExpiredList = this.findViewById(R.id.expiredCoupon);
        this.buttonUsedList = this.findViewById(R.id.couponUsed);
        this.addCouponButton = this.findViewById(R.id.addCouponButton);

        /*
         Handle listener for buttonActiveList
         */
        this.buttonActiveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Bundle bundle = new Bundle();
                 // put TypList in the bundle to indicate which coupon list to manage
                 bundle.putString(TypeList,Active_Coupon);
                 Intent newIntent = new Intent(new Intent(context,ListActivity.class));
                 newIntent.putExtras(bundle);
                 // Start new activity
                 startActivity(newIntent);
            }
        });
        /*
         Handle listener for buttonExpiredList
         */
        this.buttonExpiredList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                // put TypList in the bundle to indicate which coupon list to manage
                bundle.putString(TypeList,Expired_Coupon);
                Intent newIntent = new Intent(new Intent(context,ListActivity.class));
                newIntent.putExtras(bundle);
                // Start new activity
                startActivity(newIntent);
            }
        });
        /*
         Handle listener for button
         */
        this.buttonUsedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                // put TypList in the bundle to indicate which coupon list to manage
                bundle.putString(TypeList,Used_Coupon);
                Intent newIntent = new Intent(new Intent(context,ListActivity.class));
                newIntent.putExtras(bundle);
                // Start new activity
                startActivity(newIntent);
            }
        });
        /*
         Handle listener for addCouponButton
         */
        this.addCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddCouponActivity.class));
            }
        });
    }
}