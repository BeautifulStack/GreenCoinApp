package com.example.fairrepack.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fairrepack.R;

import java.util.Date;
import java.util.List;

public class TxAdapter extends BaseAdapter {
    private Context ctx;
    private List<Tx> txs;
    private String address;

    public TxAdapter(Context c, List<Tx> p, String a){
        this.ctx = c;
        this.txs = p;
        this.address = a;
    }

    @Override
    public int getCount() {
        return this.txs.size();
    }

    @Override
    public Tx getItem(int position) {
        return this.txs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean sender = true;
        Tx current = getItem(position);

        sender = current.getSender().equals(address);

        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(this.ctx);
            if (sender) {
                convertView = inflater.inflate(R.layout.tx_sender, null);
            } else {
                convertView = inflater.inflate(R.layout.tx_receiver, null);
            }
        }

        TextView tx_address = convertView.findViewById(R.id.tx_address);
        TextView tx_amount = convertView.findViewById(R.id.tx_amount);
        TextView tx_date = convertView.findViewById(R.id.tx_date);

        tx_address.setText((sender) ? current.getReceiver() : current.getSender());
        tx_amount.setText((sender) ? "- "+current.getAmount() : "+ "+current.getAmount());
        Date d = new Date(current.getTime());
        tx_date.setText(d.toString());

        return convertView;
    }
}
