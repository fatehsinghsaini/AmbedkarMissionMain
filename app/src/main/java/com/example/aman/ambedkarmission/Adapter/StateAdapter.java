package com.example.aman.ambedkarmission.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aman.ambedkarmission.R;

import java.util.ArrayList;

public class StateAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> arrayLists;
    LayoutInflater layoutInflater;
    TextView wwe;
    ListView listViews;
    public StateAdapter(Context applicationContext, ArrayList<String> arrayList, TextView state, ListView listView) {
        context=applicationContext;
        arrayLists=arrayList;
        layoutInflater=LayoutInflater.from(context);
        wwe=state;
        listViews=listView;

    }

    @Override
    public int getCount() {
        return arrayLists.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view=layoutInflater.inflate(R.layout.list_layout,null);
        TextView text;
        text=(TextView)view.findViewById(R.id.text);
        text.setText(arrayLists.get(i));
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wwe.setText(arrayLists.get(i));
                listViews.setVisibility(View.GONE);

            }
        });
        return view ;
    }
}
