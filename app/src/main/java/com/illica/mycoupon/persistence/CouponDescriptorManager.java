package com.illica.mycoupon.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.illica.mycoupon.model.CouponDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CouponDescriptorManager {

    private Context context = null;
    private AppDatabase db = null;
    private CouponDao couponDao = null;


    private static CouponDescriptorManager instance = null;

    private CouponDescriptorManager (Context context) {
        this.context = context;
        // Recupero il riferimento
        this.db = Room.databaseBuilder(context, AppDatabase.class, "coupons-database").allowMainThreadQueries().build();
        this.couponDao = this.db.couponDao();
    }

    /**
     * Singleton that allow you to get a instance of CouponDescriptorManagers
     * @param context context
     * @return an instance of CouponDescriptorManager
     */
    public static CouponDescriptorManager getInstance(Context context){
        if(instance == null)
            instance = new CouponDescriptorManager(context);
        return instance;
    }
    public void addCoupon(CouponDescriptor coupon) {
        this.couponDao.insertAll(coupon);
    }
    public void addCouponToHead(CouponDescriptor coupon){
        this.addCoupon(coupon);
    }
    public void removeCoupon(CouponDescriptor coupon){
        this.couponDao.delete(coupon);
    }
    public List<CouponDescriptor> getCouponList(){ return this.couponDao.getAll(); }
    public LiveData<List<CouponDescriptor>> getCouponLiveDataList(){
        return this.couponDao.getActiveLiveData();
    }


}
