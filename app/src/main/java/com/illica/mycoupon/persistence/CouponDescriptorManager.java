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
    /*
    `Method allow you to insert a new coupon
    `*/
    public void addCoupon(CouponDescriptor coupon) {
        this.couponDao.insertAll(coupon);
    }

    public void addCouponToHead(CouponDescriptor coupon){
        this.addCoupon(coupon);
    }
    /*
  ` Method that allow you to remove coupon
    */
    public void removeCoupon(CouponDescriptor coupon){
        this.couponDao.delete(coupon);
    }
    public List<CouponDescriptor> getCouponList(){ return this.couponDao.getAll(); }
    public LiveData<List<CouponDescriptor>> getActiveCouponLiveDataList(){
        return this.couponDao.getActiveLiveData();
    }
    public LiveData<List<CouponDescriptor>> getExpiringCouponLiveDataList(){
        return this.couponDao.getExpiringLiveData();
    }
    public LiveData<List<CouponDescriptor>> getUsedCouponLiveDataList(){
        return this.couponDao.getUsedLiveDate();
    }
    /*
    Method that allow you to update isUsed field of coupon
     */
    public int updateUsed (Integer idCoupon){
        return this.couponDao.updateUsed(idCoupon);
    }

}
