package com.illica.mycoupon.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.illica.mycoupon.R;
import com.illica.mycoupon.fragment.ListCouponsFragment;

public class ListActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Context mContext = null;
    private String typeView;
    private Integer pos;
    SearchView searchView;
    public static final String CouponObject = "CouponObject";
    public static final String Position = "Position";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        this.mContext = this;

        //Init toolbar
        initToolbar();


        if (savedInstanceState == null) {
            //Get Bundle
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null){
                typeView = bundle.getString(MainActivity.TypeList);
                pos = bundle.getInt(ListActivity.Position);
                //Set title of the actionBar
                actionBar.setTitle(typeView);
            }
            getSupportFragmentManager().beginTransaction().add(R.id.container, new ListCouponsFragment()).commit();
        }

    }
    /*
     Init toolbar
     */
    private void initToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
         actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(new Intent(mContext,MainActivity.class));
                startActivity(newIntent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);

            }
        });
    }

    /**
     * Getter for TypeView
     * @return string
     */
    public String getTypeView(){
        return typeView;
    }

    /**
     * Getter for Pos
     * @return integer
     */
    public Integer getPos(){
        return pos;
    }



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

}