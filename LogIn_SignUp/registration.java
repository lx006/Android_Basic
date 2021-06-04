package com.example.loginemailverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class registration extends AppCompatActivity implements View.OnClickListener {

     TextView banner;
    private EditText fullname,age,email,editpswd;
    private ProgressBar progressbar;
    private Button registeruser;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        banner = findViewById(R.id.banner);
        fullname = findViewById(R.id.fullname);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        editpswd = findViewById(R.id.editpswd);
        progressbar = findViewById(R.id.progressBar2);
        registeruser = findViewById(R.id.submit);

        registeruser.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.banner:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.submit:
                registerUser();
                break;

        }
    }

    private void registerUser()
    {
        String mail = email.getText().toString().trim();
        String pswd = editpswd.getText().toString().trim();
        String name = fullname.getText().toString().trim();
        String userage = age.getText().toString().trim();

        if(name.isEmpty())
        {
            fullname.setError("Full name is empty");
            fullname.requestFocus();
            return;
        }
        if(mail.isEmpty())
        {
            email.setError("Mail is empty");
            email.requestFocus();
            return;
        }
        if(pswd.isEmpty())
        {
            editpswd.setError("Password is empty");
            editpswd.requestFocus();
            return;
        }
        if(userage.isEmpty())
        {
            age.setError("Full name is empty");
            age.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            email.setError("Please provide valid mail");
            email.requestFocus();
            return;
        }
        if(pswd.length()<6)
        {
            editpswd.setError("Min length of password should be 6 character");
            editpswd.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail,pswd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            User user = new User(name,userage,mail);

                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(registration.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                        progressbar.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        Toast.makeText(registration.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                        progressbar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(registration.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
