package com.illica.mycoupon.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.illica.mycoupon.definition.CouponType;

import java.util.Date;


@Entity(tableName = "coupons")
public class CouponDescriptor {


    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "companyName")
    private String companyName;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "code")
    private String code;

    @ColumnInfo(name = "couponType")
    private Integer couponType;

    @ColumnInfo(name = "expiryDate")
    private Date expiryDate;


    public CouponDescriptor(){
    }
    public CouponDescriptor(String companyName, String description, CouponType couponType, String code, Date expiryDate){
        super();
        this.companyName = companyName;
        this.code = code;
        this.couponType = couponType.ordinal();
        this.expiryDate = expiryDate;
        this.description = description;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    @Override
    public String toString() {
        return "Company name: "+companyName+" Description: "+ description+" Type:"+CouponType.values()[couponType]+" Code:"+code+" Data:"+expiryDate.toString();
    }
    @Override
    public boolean equals(Object o) {
        CouponDescriptor couponObj = (CouponDescriptor) o;
        if((couponObj.code == this.code) && (couponObj.expiryDate.compareTo(this.expiryDate))==0 && (couponObj.companyName == this.companyName ))
            return true;
        else
            return false;
    }
}
