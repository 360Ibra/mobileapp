package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private DatabaseReference databaseReference;


    private FirebaseAuth mAuth;
    private Button registerUser;
    private EditText editTextName, editTextBroker, editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        registerUser = (Button) findViewById(R.id.button2);
        registerUser.setOnClickListener(this);

        editTextBroker = (EditText)findViewById(R.id.Broker);
        editTextName = (EditText)findViewById(R.id.personName);
        editTextEmail = (EditText)findViewById(R.id.editTextTextEmailAddress2);
        editTextPassword = (EditText)findViewById(R.id.editTextTextPassword);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button2:
                registerUser();
                break;
        }

    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String broker = editTextBroker.getText().toString().trim();

        if (name.isEmpty()){
            editTextName.setError("Name is Required");
            editTextName.requestFocus();
            return;

        }

        if (broker.isEmpty()){
            editTextBroker.setError("Broker is Required");
            editTextBroker.requestFocus();
            return;

        }



        if (email.isEmpty()){
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;

        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(("Please provide valid email"));
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError(("Password is required!"));
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6 ){
            editTextPassword.setError(("Min password length should be 6 characters"));
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user    = new User(name,broker,email);



                            FirebaseDatabase.getInstance().getReference("investors")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                                FirebaseDatabase.getInstance().getReference("users").child("fmNKSTflBTfaghFzhvo5pDQdTbM2").child("clients").push().setValue(email);



//                                                HashMap<String, String> emails = new HashMap<String, String>();
//                                                Map<String, Object> map = new HashMap<String, Object>();
//                                                map.put(email, "");
//                                                FirebaseDatabase.getInstance().getReference("users").child("4xSpJ601emUug4i7D6c79QrpGlB3").updateChildren(map);



                                            }else {
                                                Toast.makeText(RegisterActivity.this, "Failed to Register Try Again!", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });

                        }else {
                            Toast.makeText(RegisterActivity.this, "Failed to Register Try Again!", Toast.LENGTH_LONG).show();
                        }


                    }
                });




    }
}