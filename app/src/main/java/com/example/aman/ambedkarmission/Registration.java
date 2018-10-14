package com.example.aman.ambedkarmission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aman.ambedkarmission.Adapter.StateAdapter;
import com.example.aman.ambedkarmission.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Registration extends AppCompatActivity {
    private static final String TAG = "Ambedkarmission";
    ListView listView;
StateAdapter stateAdapter;
TextView state;
LinearLayout listliner;
ArrayList<String> arrayList=new ArrayList<>();
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private String userId;

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

        firebaseSetUp();

    }

    private void firebaseSetUp() {
        mFirebaseInstance = FirebaseDatabase.getInstance();


        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Missan");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });


    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String name, String email) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(name, email);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.getName() + ", " + user.getEmail());

                // Display newly updated name and email
              //  txtDetails.setText(user.getName() + ", " + user.getEmail());

                // clear edit text
               // inputEmail.setText("");
              //  inputName.setText("");


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String name, String email) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);

        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);
    }


}
