package com.example.aman.ambedkarmission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aman.ambedkarmission.Adapter.StateAdapter;

import java.util.ArrayList;

public class Registration extends AppCompatActivity {
ListView listView;
StateAdapter stateAdapter;
TextView state;
LinearLayout listliner;
ArrayList<String> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        listliner=(LinearLayout)findViewById(R.id.listliner);
        state=(TextView)findViewById(R.id.state);
        arrayList.add("Andhra Pradesh");
        arrayList.add("Arunachal Pradesh");
        arrayList.add("Assam");
        arrayList.add("Bihar");
        arrayList.add("Chattisgarh");
        arrayList.add("Goa");
        arrayList.add("Gujarat");
        arrayList.add("Haryana");
        arrayList.add("Himachal Pradesh");
        arrayList.add("Jammu and Kashmir");
        arrayList.add("Jharkhand");
        arrayList.add("Karnataka");
        arrayList.add("Kerala");
        arrayList.add("Madhya Pradesh");
        arrayList.add("Maharashtra");
        arrayList.add("Manipur");
        arrayList.add("Meghalaya");
        arrayList.add("Mizoram");
        arrayList.add("Nagaland");
        arrayList.add("Odisha");
        arrayList.add("Punjab");
        arrayList.add("Rajasthan");
        arrayList.add("Sikkim");
        arrayList.add("Tamil Nadu");
        arrayList.add("Telangana");
        arrayList.add("Tripura");
        arrayList.add("Uttar Pradesh");
        arrayList.add("Uttarakhand");
        arrayList.add("West Bengal");
        listliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.VISIBLE);
            }
        });
        listView=(ListView)findViewById(R.id.list);
        stateAdapter =new StateAdapter(getApplicationContext(),arrayList,state,listView);
        listView.setAdapter(stateAdapter);
    }
}
