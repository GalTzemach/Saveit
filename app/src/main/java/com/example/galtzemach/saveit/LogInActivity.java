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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {

    // create FireBase auth feature
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    private ProgressDialog mProgressDialog;

    private TextView mLogInEmailField;
    private TextView mLogInPasswordField;

    private Button mLogInBtn;
    private Button mGoToRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Users");

        mLogInEmailField = (TextView) findViewById(R.id.logInEmailField);
        mLogInPasswordField = (TextView) findViewById(R.id.LogInPasswordField);

        mGoToRegisterBtn = (Button) findViewById(R.id.btnNewUser);
        mGoToRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerIntent = new Intent(LogInActivity.this, RegisterActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);

            }
        });

        mLogInBtn = (Button) findViewById(R.id.btnLogIn);
        mLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog.setMessage("Signing in...");
                mProgressDialog.show();

                checkLogIn();

            }
        });

    }

    private void checkLogIn() {

        String email = mLogInEmailField.getText().toString().trim();
        String password = mLogInPasswordField.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            mProgressDialog.dismiss();
            Toast.makeText(LogInActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();

        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        //checkUserExist();

                        mProgressDialog.dismiss();

                        Intent mainIntent = new Intent(LogInActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                    } else {

                        mProgressDialog.dismiss();
                        Toast.makeText(LogInActivity.this, "Error Log In!", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }


    }

    @Override
    public void onBackPressed() {


    }
}
