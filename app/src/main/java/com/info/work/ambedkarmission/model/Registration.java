package com.info.work.ambedkarmission.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Registration implements Parcelable {

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

    protected Registration(Parcel in) {
        name = in.readString();
        fathersName = in.readString();
        castName = in.readString();
        mailId = in.readString();
        mobileNo = in.readString();
        stateName = in.readString();
        districtName = in.readString();
        villageName = in.readString();
        postalCode = in.readString();
    }

    public static final Creator<Registration> CREATOR = new Creator<Registration>() {
        @Override
        public Registration createFromParcel(Parcel in) {
            return new Registration(in);
        }

        @Override
        public Registration[] newArray(int size) {
            return new Registration[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(fathersName);
        dest.writeString(castName);
        dest.writeString(mailId);
        dest.writeString(mobileNo);
        dest.writeString(stateName);
        dest.writeString(districtName);
        dest.writeString(villageName);
        dest.writeString(postalCode);
    }
}