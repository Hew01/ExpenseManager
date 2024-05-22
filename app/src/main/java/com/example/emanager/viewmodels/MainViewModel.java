package com.example.emanager.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.emanager.models.Account;
import com.example.emanager.models.Transaction;
import com.example.emanager.utils.Constants;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.Realm.Transaction.OnSuccess;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.log.LogLevel;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class MainViewModel extends AndroidViewModel {

    public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();
    public MutableLiveData<List<Account>> accounts = new MutableLiveData<>();
    public MutableLiveData<RealmResults<Transaction>> categoriesTransactions = new MutableLiveData<>();

    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();

    static RealmResults<Transaction> Transaction;

    public MutableLiveData<Double> networth = new MutableLiveData<>();

    Realm realm;
    Calendar calendar;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);

        //Xóa toàn bộ dữ liệu trong database
        /*Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        // delete all realm objects
        realm.deleteAll();
        //commit realm changes
        realm.commitTransaction();*/

        setupDatabase();
    }
    public void setupSync(String email, String password) {
        App app = new App(new AppConfiguration.Builder(Constants.AppId).build());
        Credentials credentials = Credentials.emailPassword(email, password);

        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if (result.isSuccess()) {
                    Log.v("User", "Login success");
                    Constants.UserId = app.currentUser().getId();
                    saveUserCredentialsToPreferences(Constants.UserId, email, password);

                    SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                                @Override
                                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                    subscriptions.addOrUpdate(Subscription.create("mysubscription",
                                            realm.where(Transaction.class).equalTo("owner_id",Constants.UserId)));
                                    subscriptions.addOrUpdate(Subscription.create("acsubscription",
                                            realm.where(Account.class).equalTo("owner_id",Constants.UserId)));

                                }
                            })
                            .build();

                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            setRealm(realm);
                            Log.v("EXAMPLE", "Successfully opened a realm.");
                            Subscription subscription = realm.getSubscriptions().find("mysubscription");
                            if (subscription != null) {
                                Log.v("Sync", "Subscription found: " + subscription.getName());
                            } else {
                                Log.v("Sync", "Subscription not found");
                            }
                        }

                        @Override
                        public void onError(Throwable exception) {
                            Log.e("EXAMPLE", "Failed to open realm: " + exception.getMessage());
                        }
                    });



                } else {
                    Log.v("User", "Failed to log in: " + result.getError().getErrorMessage());
                }
            }
        });
    }

    private void saveUserCredentialsToPreferences(String userId, String email, String password) {
        SharedPreferences sharedPref = getApplication().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("UserId", userId);
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.apply();
    }

    public void clearUserCredentials() {
        SharedPreferences sharedPref = getApplication().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("UserId");
        editor.remove("Email");
        editor.remove("Password");
        editor.apply();
    }

    public void getTransactions(Calendar calendar, String type) {
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        RealmResults<Transaction> newTransactions = null;
        if(Constants.SELECTED_TAB_STATS == Constants.DAILY) {
            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", type)
                    .findAll();

        } else if(Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH,0);

            Date startTime = calendar.getTime();


            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", type)
                    .equalTo("owner_id",Constants.UserId)
                    .findAll();
        }
       // newTransactions = realm.where(Transaction.class).equalTo("owner_id",Constants.UserId).findAll();

        categoriesTransactions.setValue(newTransactions);

    }


    public void getTransactions(Calendar calendar) {
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        double income = 0;
        double expense = 0;
        double total = 0;
        RealmResults<Transaction> newTransactions = null;
        if(Constants.SELECTED_TAB == Constants.DAILY) {
            // Select * from transactions
            // Select * from transactions where id = 5

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.INCOME)
                    .sum("amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount")
                    .doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .sum("amount")
                    .doubleValue();



        } else if(Constants.SELECTED_TAB == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH,0);

            Date startTime = calendar.getTime();


            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.INCOME)
                    .equalTo("owner_id",Constants.UserId)
                    .sum("amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.EXPENSE)
                    .equalTo("owner_id",Constants.UserId)
                    .sum("amount")
                    .doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .sum("amount")
                    .doubleValue();
        }

        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(total);
        transactions.setValue(newTransactions);
        Transaction = newTransactions;
//        RealmResults<Transaction> newTransactions = realm.where(Transaction.class)
//                .equalTo("date", calendar.getTime())
//                .findAll();

    }

    public void addTransaction(Transaction transaction) {
        transaction.setId(new ObjectId() );
        transaction.setOwner_id(Constants.UserId);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(transaction);

                Account ac = realm.where(Account.class).equalTo("accountName",transaction.getAccount()).findFirst();
                if (Constants.INCOME.equals(transaction.getType())) {
                    ac.setAccountAmount( ac.getAccountAmount() + transaction.getAmount());
                } else if (Constants.EXPENSE.equals(transaction.getType())) {
                    ac.setAccountAmount( ac.getAccountAmount() - transaction.getAmount());
                }
                realm.copyToRealmOrUpdate(ac);
            }
        });

    }

    public void editTransaction(Transaction transaction) {
        realm.executeTransaction(realm -> {
            realm.copyToRealmOrUpdate(transaction);
        });
        getTransactions(calendar);
    }

    public void deleteTransaction(Transaction transaction) {
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransactions(calendar);
    }

    public void addTransactions() {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Business", "Cash", "Some note here", new Date(), 500, new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE, "Investment", "Bank", "Some note here", new Date(), -900, new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Rent", "Other", "Some note here", new Date(), 500, new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Business", "Card", "Some note here", new Date(), 500, new Date().getTime()));
        // some code here
        realm.commitTransaction();
    }
    public double getNetWorth()
    {
        double netWorth = 0.0;

        RealmResults<Account> accounts = realm.where(Account.class).equalTo("owner_id",Constants.UserId).findAll();
        for (Account ac:accounts)
        {
            netWorth+=ac.getAccountAmount();
        }
        return netWorth;
    }
    public void addAccount(Account ac)
    {
        ac.set_id(new ObjectId() );
        ac.setOwner_id(Constants.UserId);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(ac);
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                accounts.setValue(getListAccount());
                networth.setValue(getNetWorth());
            }});

    }

    public List<Account> getListAccount()
    {
        List<Account> list= new ArrayList<>();
        RealmResults<Account> accounts= realm.where(Account.class).equalTo("owner_id",Constants.UserId).findAll();
        for (Account ac:accounts)
        {
            list.add(ac);
        }
        return list;
    }
    void setupDatabase() {
        Realm.deleteRealm(Realm.getDefaultConfiguration());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true) // Cho phép ghi trên luồng UI
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

    }

    public void setRealm(Realm realm)
    {
        this.realm=realm;
    }
    public void clearRealmConfiguration() {
        Realm.removeDefaultConfiguration();
        realm.close();
    }

    public static RealmResults<Transaction>  getFilterTransaction(){return Transaction;}

}
