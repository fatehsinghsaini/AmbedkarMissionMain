package com.info.work.ambedkarmission.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Registration {

    public String name;
    public String fathersName;
    public String castName;
    public String mailId;
    public String mobileNo;
    public String stateName;
    public String districtName;
    public String villageName;
    public String postalCode;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Registration.class)
    public Registration() {
    }

    public Registration(String name, String fathersName, String castName, String mailId, String mobileNo, String stateName, String districtName, String villageName, String postalCode) {
        this.name = name;
        this.fathersName = fathersName;
        this.castName = castName;
        this.mailId = mailId;
        this.mobileNo = mobileNo;
        this.stateName = stateName;
        this.districtName = districtName;
        this.villageName = villageName;
        this.postalCode = postalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getCastName() {
        return castName;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


}