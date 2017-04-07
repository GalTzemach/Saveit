package com.example.galtzemach.saveit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    private TextView mNameField;
    private TextView mEmailField;
    private TextView mPasswordField;
    private Button mRegisterBtn;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mDataBase = FirebaseDatabase.getInstance().getReference().child("Users");

        mNameField = (TextView) findViewById(R.id.nameField);
        mEmailField = (TextView) findViewById(R.id.logInEmailField);
        mPasswordField = (TextView) findViewById(R.id.LogInPasswordField);

        mRegisterBtn = (Button) findViewById(R.id.btnRegister);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();

            }
        });




    }

    private void startRegister() {

        final String name = mNameField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            Toast.makeText(RegisterActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();

        } else {

            mProgress.setMessage("Create new user...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDataBase.child(user_id);
                        current_user_db.child("Name").setValue(name);

                        mProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);


                    } else {

                        //checkUserExist();

                        mProgress.dismiss();
                        Toast.makeText(RegisterActivity.this, "Failed / email already exist, try again!", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }


    }

        private void checkUserExist() {

        final String user_id = mAuth.getCurrentUser().getUid();



        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (! dataSnapshot.hasChild(user_id)) {

                    mProgress.dismiss();
                    Toast.makeText(RegisterActivity.this, "You need create account first!", Toast.LENGTH_LONG).show();

                } else {

                    mProgress.dismiss();
                    Toast.makeText(RegisterActivity.this, "Failed register!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
