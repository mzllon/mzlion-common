package com.mzlion.okhttpserver.entity;

import java.io.Serializable;

/**
 * Created by mzlion on 2016/1/9.
 */
public class UserAddress implements Serializable {

    private String userId;

    private String addressId;

    private String conName;

    private String conPhone;

    private String addressShow;

    private String provinceId;

    private String provinceName;

    private String cityId;

    private String cityName;

    private String zip;

    private String addressFull;

    /*private String addressStat;*/

    private String isDefault;

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddressFull() {
        return addressFull;
    }

    public void setAddressFull(String addressFull) {
        this.addressFull = addressFull;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressShow() {
        return addressShow;
    }

    public void setAddressShow(String addressShow) {
        this.addressShow = addressShow;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getConName() {
        return conName;
    }

    public void setConName(String conName) {
        this.conName = conName;
    }

    public String getConPhone() {
        return conPhone;
    }

    public void setConPhone(String conPhone) {
        this.conPhone = conPhone;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public UserAddress() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserAddress{");
        sb.append("addressFull='").append(addressFull).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", addressId='").append(addressId).append('\'');
        sb.append(", conName='").append(conName).append('\'');
        sb.append(", conPhone='").append(conPhone).append('\'');
        sb.append(", addressShow='").append(addressShow).append('\'');
        sb.append(", provinceId='").append(provinceId).append('\'');
        sb.append(", provinceName='").append(provinceName).append('\'');
        sb.append(", cityId='").append(cityId).append('\'');
        sb.append(", cityName='").append(cityName).append('\'');
        sb.append(", zip='").append(zip).append('\'');
        sb.append(", isDefault='").append(isDefault).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
