package com.example.teacher_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Present extends AppCompatActivity {

    ListView listView;
    String Email,Aid,Date,showDate;
    List<String> present= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present);

        listView= findViewById(R.id.listView);
        Intent e= getIntent();
        Email= e.getStringExtra("email");
        Aid= e.getStringExtra("key");
        Date= e.getStringExtra("date");

        DatabaseReference dt= FirebaseDatabase.getInstance().getReference(Date).child(Email).child(Aid);
        dt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("Time"))
                {
                    showDate = dataSnapshot.child("Time").getValue().toString();
                    //present.add(showDate);
                }
                else
                    showDate= "Sorry!! TimeStamp not Found..";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference db= FirebaseDatabase.getInstance().getReference(Date).child(Email).child(Aid).child("PRESENT");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    present.add(ds.getKey()+"\n"+ds.getValue().toString());
                }

                Collections.sort(present);
                present.add(0,showDate);
                ArrayAdapter adapter = new ArrayAdapter<String>(Present.this,R.layout.support_simple_spinner_dropdown_item,present);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Present.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}