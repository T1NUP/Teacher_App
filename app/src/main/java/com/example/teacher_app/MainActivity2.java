package com.example.teacher_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    Button logOut,activate,deactivate,show;
    EditText code;
    TextView title,token;
    ProgressBar progressBar;
    FirebaseAuth fa;
    GoogleSignInAccount account;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference write;
    String date,emailId,name,tok,inpDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        code= findViewById(R.id.code);
        activate= findViewById(R.id.on);
        deactivate= findViewById(R.id.off);
        title= findViewById(R.id.head);
        show= findViewById(R.id.attendence);
        logOut= findViewById(R.id.out);
        logOut.setVisibility(View.INVISIBLE);
        progressBar= findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        deactivate.setVisibility(View.INVISIBLE);

        fa= FirebaseAuth.getInstance();
        account = GoogleSignIn.getLastSignedInAccount(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //title.setText(account.getEmail().toString());

        inpDate = DateFormat.getDateTimeInstance().format(new Date());
        date =new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        emailId= account.getId();
        token= findViewById(R.id.tok);
        tok= account.getId();
        token.setText(tok);
        //int l= emailId.indexOf("@");
        //emailId= emailId.substring(0,l);
        name= account.getDisplayName();


        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((code.getText().toString()).equals(""))
                {
                    Toast.makeText(MainActivity2.this,"Please Create a code!!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    //date= date.substring(0,11);
                    write= FirebaseDatabase.getInstance().getReference(date);

                    //write.child("Flag").setValue("1");
                    //write.child("Id").setValue(emailId);
                    //write.child("Code").setValue(code.getText().toString());
                    //write.child("Students");
                    //write.child("Teacher").setValue(name);

                    Toast.makeText(MainActivity2.this,date,Toast.LENGTH_LONG).show();
                    write.child(tok);
                    write.child(tok).child(code.getText().toString());
                    write.child(tok).child(code.getText().toString()).child("Flag").setValue("1");
                    write.child(tok).child(code.getText().toString()).child("Name").setValue(name);
                    write.child(tok).child(code.getText().toString()).child("Time").setValue(inpDate);
                    write.child(tok).child(code.getText().toString()).child("Email ID").setValue(account.getEmail());
                    //write.child("ID").child("Code").child("Student");
                    //String upl = write.child("ID").child("Code").child("Student").push().getKey();
                    //write.child(emailId).child(code.getText().toString()).child(upl).setValue(account.getEmail());
                    Toast.makeText(MainActivity2.this,"Activated!!",Toast.LENGTH_LONG).show();
                    activate.setVisibility(View.INVISIBLE);
                    deactivate.setVisibility(View.VISIBLE);
                    logOut.setVisibility(View.INVISIBLE);
                    show.setVisibility(View.INVISIBLE);

                    code.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity2.this);
                    builder.setCancelable(true);
                    builder.setTitle("Alert!!");
                    builder.setMessage("SERVER ACTIVE!!");
                    builder.show();
                }
            }
        });

        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //date= date.substring(0,11);
                write= FirebaseDatabase.getInstance().getReference(date);

                write.child(emailId).child(code.getText().toString()).child("Flag").setValue("0");
                Toast.makeText(MainActivity2.this,"Deactivated!!",Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.INVISIBLE);
                deactivate.setVisibility(View.INVISIBLE);
                activate.setVisibility(View.VISIBLE);
                logOut.setVisibility(View.VISIBLE);
                show.setVisibility(View.VISIBLE);
                code.setEnabled(true);
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity2.this,Attendence.class);
                startActivity(intent);
            }
        });
    }
}