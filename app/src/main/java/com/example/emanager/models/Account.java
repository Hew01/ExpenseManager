package com.example.emanager.models;

import com.example.emanager.utils.Constants;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Account extends RealmObject {

    @PrimaryKey
    @Required
    private ObjectId _id;
    private double accountAmount;
    private String accountName;
    private String owner_id;

    public Account() {

    }

    public Account(double accountAmount, String accountName) {
        this.accountAmount = accountAmount;
        this.accountName = accountName;
        this._id=new ObjectId();
        this.owner_id= Constants.UserId;
    }

    public double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(double accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
}
