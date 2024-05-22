package com.example.emanager.models;

import androidx.annotation.NonNull;

import com.example.emanager.utils.Constants;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;
import io.realm.annotations.Required;

public class Transaction extends RealmObject {
    private String type, category, account, note;
    private Date date;
    private double amount;

    @PrimaryKey
    @Required
    private ObjectId _id;

    private String owner_id;

    public Transaction() {

    }

    public Transaction(String type, String category, String account, String note, Date date, double amount) {
        this.type = type;
        this.category = category;
        this.account = account;
        this.note = note;
        this.date = date;
        this.amount = amount;
        this._id = new ObjectId();
        this.owner_id= Constants.UserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId id) {
        this._id = id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
}
