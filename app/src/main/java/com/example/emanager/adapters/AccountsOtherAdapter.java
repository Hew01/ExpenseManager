package com.example.emanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emanager.R;
import com.example.emanager.models.Account;

import java.util.List;

public class AccountsOtherAdapter extends RecyclerView.Adapter<AccountsOtherAdapter.AccountsOtherViewHolder> {

    private Context context;
    private List<Account> listAC;

    public AccountsOtherAdapter(Context context) {
        this.context = context;
    }
    public void setDataAC(List<Account> list)
    {
        this.listAC=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccountsOtherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account,parent,false);
        return new AccountsOtherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsOtherViewHolder holder, int position) {
        Account ac= listAC.get(position);
        if(ac==null) return;
        holder.name.setText(ac.getAccountName());
        holder.money.setText(String.valueOf(ac.getAccountAmount()));
    }

    @Override
    public int getItemCount() {
        if(listAC!=null)
            return listAC.size();
        return 0;
    }

    public class AccountsOtherViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView money;
        public AccountsOtherViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.acName);
            money=itemView.findViewById(R.id.acMoney);
        }
    }
}
