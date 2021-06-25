package com.example.fairrepack.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class TxAdapter extends BaseAdapter {
    private Context ctx;
    private List<Tx> txs;

    public TxAdapter(Context c, List<Tx> p){
        this.ctx = c;
        this.txs = p;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
