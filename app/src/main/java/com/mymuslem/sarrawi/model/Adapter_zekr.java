package com.mymuslem.sarrawi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Adapter_zekr implements Parcelable {
    private int ID_zekr;
    private int Name_ID;
    private String Description;
    private String hint;
    private String counter;
    private String Qat;

    public Adapter_zekr(int ID_zekr, int name_ID, String description, String hint, String counter) {
        this.ID_zekr = ID_zekr;
        Name_ID = name_ID;
        Description = description;
        this.hint = hint;
        this.counter = counter;
    }

    public Adapter_zekr(int name_ID) {
        Name_ID = name_ID;
    }

    public Adapter_zekr() {
    }

    protected Adapter_zekr(Parcel in) {
        ID_zekr = in.readInt();
        Name_ID = in.readInt();
        Description = in.readString();
        hint = in.readString();
        counter = in.readString();
        Qat = in.readString();
    }

    public static final Creator<Adapter_zekr> CREATOR = new Creator<Adapter_zekr>() {
        @Override
        public Adapter_zekr createFromParcel(Parcel in) {
            return new Adapter_zekr(in);
        }

        @Override
        public Adapter_zekr[] newArray(int size) {
            return new Adapter_zekr[size];
        }
    };

    public int getID_zekr() {
        return ID_zekr;
    }

    public void setID_zekr(int ID_zekr) {
        this.ID_zekr = ID_zekr;
    }

    public int getName_ID() {
        return Name_ID;
    }

    public void setName_ID(int name_ID) {
        Name_ID = name_ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getQat() {
        return Qat;
    }

    public void setQat(String qat) {
        Qat = qat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID_zekr);
        dest.writeInt(Name_ID);
        dest.writeString(Description);
        dest.writeString(hint);
        dest.writeString(counter);
        dest.writeString(Qat);
    }
}
