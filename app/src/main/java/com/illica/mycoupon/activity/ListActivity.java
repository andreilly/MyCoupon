package com.illica.mycoupon.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.illica.mycoupon.R;
import com.illica.mycoupon.fragment.HistoryFragment;

public class ListActivity extends AppCompatActivity {
    private Context mContext = null;

    SearchView searchView;
    public static final String CouponObject = "CouponObject";
    public static final String Position = "Position";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.mContext = this;
        if (savedInstanceState == null) {
            //Get Bundle
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null){
                actionBar.setTitle(bundle.getString(MainActivity.TypeList));
            }
            getSupportFragmentManager().beginTransaction().add(R.id.container, new HistoryFragment()).commit();
        }

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