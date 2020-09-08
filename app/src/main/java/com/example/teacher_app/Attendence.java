package com.example.teacher_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Attendence extends AppCompatActivity {

    Spinner spinner;
    EditText date;
    Button show, submit;
    GoogleSignInAccount account;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase fa;
    DatabaseReference databaseReference,students;
    String email,aid;
    int l;
    List<String> codes= new ArrayList<String>();
    List<String> present= new ArrayList<String>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        account = GoogleSignIn.getLastSignedInAccount(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        email= account.getId();
        //l=email.indexOf("@");
        //email= email.substring(0,l);

        spinner= findViewById(R.id.spinner);
        date= findViewById(R.id.date);
        submit= findViewById(R.id.submit);
        show= findViewById(R.id.show);

        spinner.setVisibility(View.INVISIBLE);
        show.setVisibility(View.INVISIBLE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((date.getText().toString()).equals("")) {
                    Toast.makeText(Attendence.this, "Please Enter Date!!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    databaseReference= FirebaseDatabase.getInstance().getReference();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(date.getText().toString()))
                            {
                                if(dataSnapshot.child(date.getText().toString()).hasChild(email))
                                {
                                    submit.setVisibility(View.INVISIBLE);
                                    for(DataSnapshot p: dataSnapshot.child(date.getText().toString()).child(email).getChildren())
                                    {
                                        codes.add(p.getKey().toString());
                                       // Toast.makeText(Attendence.this,p.getValue().toString(),Toast.LENGTH_SHORT).show();
                                    }

                                    show.setVisibility(View.VISIBLE);
                                    spinner.setVisibility(View.VISIBLE);
                                    spinner.setAdapter(new ArrayAdapter<String>(Attendence.this, android.R.layout.simple_spinner_dropdown_item, codes));
                                    showOptions();
                                }
                                else
                                    Toast.makeText(Attendence.this,"No Data Available..",Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(Attendence.this,"No Data Available..",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Attendence.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }

    public void showOptions()
    {


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aid= spinner.getSelectedItem().toString();
                students= FirebaseDatabase.getInstance().getReference(date.getText().toString())
                        .child(email).child(aid).child("PRESENT");

                students.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       for(DataSnapshot ds: dataSnapshot.getChildren())
                       {
                           present.add(ds.getValue().toString());
                       }

                       listView= findViewById(R.id.lv);
                        listView.setAdapter(null);
                        ArrayAdapter adapter = new ArrayAdapter<String>(Attendence.this,R.layout.support_simple_spinner_dropdown_item,present);
                        listView.setAdapter(adapter);
                        Intent intent= new Intent(Attendence.this,Present.class);
                        intent.putExtra("key",aid);
                        intent.putExtra("date",date.getText().toString());
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Attendence.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}