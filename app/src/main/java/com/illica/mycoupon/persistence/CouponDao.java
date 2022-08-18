package com.illica.mycoupon.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.illica.mycoupon.model.CouponDescriptor;

import java.util.List;

@Dao
public interface CouponDao {
    @Query("SELECT * FROM coupons WHERE expiryDate >= date('now') or expiryDate like '' ORDER BY expiryDate DESC")
    LiveData<List<CouponDescriptor>> getActiveLiveData();

    @Query("SELECT * FROM coupons WHERE expiryDate < date('now') and expiryDate != '' ORDER BY expiryDate DESC")
    LiveData<List<CouponDescriptor>> getExpiringLiveData();

    @Insert
    void insertAll(CouponDescriptor... coupons);

    @Delete
    void delete(CouponDescriptor couponDescriptor);

    @Query("SELECT * FROM coupons ORDER BY id DESC")
    List<CouponDescriptor> getAll();
}

