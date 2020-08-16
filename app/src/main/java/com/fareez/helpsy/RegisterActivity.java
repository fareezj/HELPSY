package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputEmail, InputPassword, InputPhone, InputAddress;
    private ProgressDialog loadingBar;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = findViewById(R.id.register_btn);
        InputName           = findViewById(R.id.register_username_input);
        InputEmail          = findViewById(R.id.register_email_input);
        InputPassword       = findViewById(R.id.register_password_input);
        InputPhone          = findViewById(R.id.register_phone_input);
        InputAddress        = findViewById(R.id.register_address_input);
        loadingBar          = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CreateAccount();

            }
        });
    }

    private void CreateAccount()
    {
        String username = InputName.getText().toString();
        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();
        String phone = InputPhone.getText().toString();
        String address = InputAddress.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your name !", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your email !", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password !", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone !", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address))
        {
            Toast.makeText(this, "Please write your address !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Checking Credential...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            // register the user in firebase

            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        String username = InputName.getText().toString();
                        String email = InputEmail.getText().toString();
                        String phone = InputPhone.getText().toString();
                        String address = InputAddress.getText().toString();
                        InsertUserDetails(username, email, phone, address);


                    }else {
                        Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void InsertUserDetails(final String username, final String email, final String phone, final String address)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(userID).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("email", email);
                    userdataMap.put("phone", phone);
                    userdataMap.put("address", address);

                    RootRef.child("Users").child(userID).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Account Created Successfully !", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error, Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
