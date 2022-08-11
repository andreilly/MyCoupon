package com.illica.mycoupon.persistence;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.illica.mycoupon.model.CouponDescriptor;

@Database(entities = {CouponDescriptor.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    public abstract CouponDao couponDao();

}
