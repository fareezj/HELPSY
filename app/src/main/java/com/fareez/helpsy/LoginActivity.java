package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fareez.helpsy.Model.Admins;
import com.fareez.helpsy.Model.Users;
import com.fareez.helpsy.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button loginButton;
    private FirebaseAuth fAuth;
    private TextView AdminLink, NotAdminLink, ForgotPassword;
    private String parentDbName = "Users";
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.login_email_input);
        mPassword = findViewById(R.id.login_password_input);
        loginButton = findViewById(R.id.login_btn);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);
        ForgotPassword = findViewById(R.id.forget_password_link);
        fAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginButton.setText("Login as Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void LoginUser()
    {
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            mEmail.setError("Email is Required.");
            return;
        }

        if(TextUtils.isEmpty(password)){
            mPassword.setError("Password is Required.");
            return;
        }

        if(password.length() < 6){
            mPassword.setError("Password must be more 6 characters");
            return;
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AuthenticateUser(email, password);
        }
    }


    private void AuthenticateUser(final String email, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        if (parentDbName.equals("Admins"))
        {
            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.child(parentDbName).child("00001").exists())
                    {
                        Admins adminsData = dataSnapshot.child(parentDbName).child("00001").getValue(Admins.class);

                        if(adminsData.getEmail().equals(email))
                        {
                            if(adminsData.getPassword().equals(password))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Error !.. Invalid Credentials ", Toast.LENGTH_SHORT).show();
                            loadingBar.cancel();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }
        else
        {
            // authenticate the user

            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        //Store User Cache
                        Paper.book().write(Prevalent.UserEmailKey, email);
                        Paper.book().write(Prevalent.UserPassword, password);

                        String userEmail = Paper.book().read(Prevalent.UserEmailKey);

                        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "User: " + userEmail, Toast.LENGTH_SHORT).show();
                        loadingBar.cancel();

                        final String userID = fAuth.getCurrentUser().getUid();

                        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(parentDbName).child(userID).exists())
                                {
                                    Users usersData = dataSnapshot.child(parentDbName).child(userID).getValue(Users.class);
                                    Prevalent.currentOnlineUser = usersData;
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else {
                        Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        loadingBar.cancel();

                    }

                }
            });
        }



    }




}
