package com.illica.mycoupon.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.illica.mycoupon.R;
import com.illica.mycoupon.adapter.MyAdapter;
import com.illica.mycoupon.fragment.HistoryFragment;
import com.illica.mycoupon.model.CouponDescriptor;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private String TAG = "Detail_Activity";
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Set title of actionBar
        actionBar.setTitle(R.string.coupon_detail);


        this.mContext = this;

        if (savedInstanceState == null) {
            //Get Bundle
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null){
            }
        }
        //RECEIVE OUR DATA
        Intent i=getIntent();
        CouponDescriptor cp = (CouponDescriptor) i.getExtras().getSerializable(ListActivity.CouponObject);
        Integer pos = i.getExtras().getInt(ListActivity.Position);
        Log.d(TAG,String.format(cp.toString() + " - posizione: " + pos));

    }
}