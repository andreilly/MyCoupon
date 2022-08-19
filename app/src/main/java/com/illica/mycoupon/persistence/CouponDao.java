package com.illica.mycoupon.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;

import com.illica.mycoupon.model.CouponDescriptor;

import java.util.List;

@Dao
public interface CouponDao {


    @Query("SELECT * FROM coupons WHERE (expiryDate >= date('now') or expiryDate like '') and (reusable = 1 or (reusable = 0 and isUsed = 0)) ORDER BY expiryDate DESC")
    LiveData<List<CouponDescriptor>> getActiveLiveData();

    @Query("SELECT * FROM coupons WHERE (expiryDate < date('now') and expiryDate != '') and (isUsed = 0) ORDER BY expiryDate DESC")
    LiveData<List<CouponDescriptor>> getExpiringLiveData();

    @Query("SELECT * FROM coupons WHERE (reusable = 0 and isUsed = 1) ORDER BY expiryDate DESC")
    LiveData<List<CouponDescriptor>> getUsedLiveDate();

    @Insert
    void insertAll(CouponDescriptor... coupons);

    @Delete
    void delete(CouponDescriptor couponDescriptor);

    @Query("SELECT * FROM coupons ORDER BY id DESC")
    List<CouponDescriptor> getAll();

    @Query("UPDATE coupons SET isUsed = 1 WHERE id = :id")
    int updateUsed (long id);

}

