package com.illica.mycoupon.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.illica.mycoupon.definition.CouponType;

import java.io.Serializable;


@Entity(tableName = "coupons")
public class CouponDescriptor implements Serializable {


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
    private String expiryDate;

    @ColumnInfo(name = "format")
    private String format;

    @ColumnInfo(name = "isUsed")
    private Boolean isUsed;

    @ColumnInfo (name = "reusable")
    private Boolean reusable;


    /**
     * Empty constructor
     */
    public CouponDescriptor(){
    }

    /**
     * Constructor for CouponDescriptor
     * @param companyName companyName
     * @param description description
     * @param couponType  Type of coupon
     * @param code        Code
     * @param expiryDate  Expiration date
     * @param format      Format of code
     * @param reusable    Reusable
     */
    public CouponDescriptor(String companyName, String description, CouponType couponType, String code, String expiryDate, String format, Boolean reusable){
        super();
        this.companyName = companyName;
        this.code = code;
        this.couponType = couponType.ordinal();
        this.expiryDate = expiryDate;
        this.description = description;
        this.reusable = reusable;
        this.format = format;
        this.isUsed = false;

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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getReusable() {
        return reusable;
    }

    public void setReusable(Boolean reusable) {
        this.reusable = reusable;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "Company name: "+companyName+" Description: "+ description+" Type:"+CouponType.values()[couponType]+" Code:"+code+" Date:"+expiryDate.toString() + " Format: " + format + " isReusable: " + reusable.toString();
    }
    @Override
    public boolean equals(Object o) {
        CouponDescriptor couponObj = (CouponDescriptor) o;
        return (couponObj.code.equals(this.code)) && (couponObj.expiryDate.compareTo(this.expiryDate)) == 0 && (couponObj.companyName.equals(this.companyName));
    }
}
