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

    static final String Active_Coupon ="Coupon attivi";
    static final String Expired_Coupon ="Coupon utilizzati";
    static final String TypeList = "TypeList";
    private Button buttonActiveList = null;
    private Button buttonExpiredList = null;
    private ImageButton addCouponButton = null;
    private Context context = null;
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
        this.addCouponButton = this.findViewById(R.id.addCouponButton);
        this.buttonActiveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Bundle bundle = new Bundle();
                 bundle.putString(TypeList,Active_Coupon);
                 Intent newIntent = new Intent(new Intent(context,ListActivity.class));
                 newIntent.putExtras(bundle);
                 startActivity(newIntent);
            }
        });
        this.buttonExpiredList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(TypeList,Active_Coupon);
                Intent newIntent = new Intent(new Intent(context,ListActivity.class));
                newIntent.putExtras(bundle);
                startActivity(newIntent);
            }
        });
        this.addCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddCouponActivity.class));
            }
        });
    }
}