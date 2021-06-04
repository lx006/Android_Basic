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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register,forgetpswd;
    private EditText edittextmail,edittextpassword;
    private Button signin;

    private FirebaseAuth mAuth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register=findViewById(R.id.register);
        register.setOnClickListener(this);

        signin = findViewById(R.id.login);
        signin.setOnClickListener(this);

        edittextmail = findViewById(R.id.emailadrdress);
        edittextpassword = findViewById(R.id.password);
        forgetpswd = findViewById(R.id.forgotpswd);
        forgetpswd.setOnClickListener(this);

        progressbar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

    }

    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.register:
                startActivity(new Intent(this,registration.class));
                break;
            case R.id.login:
                userLogin();
                break;
            case R.id.forgotpswd:
                startActivity(new Intent(this,ForgetPassword.class));
                break;
        }
    }

    private void userLogin()
    {
        String mail = edittextmail.getText().toString().trim();
        String pswd = edittextpassword.getText().toString().trim();

        if(mail.isEmpty())
        {
            edittextmail.setError("Mail is empty");
            edittextmail.requestFocus();
            return;
        }
        if(pswd.isEmpty())
        {
            edittextpassword.setError("Password is empty");
            edittextpassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            edittextmail.setError("Please provide valid mail");
            edittextmail.requestFocus();
            return;
        }
        if(pswd.length()<6)
        {
            edittextpassword.setError("Min length of password should be 6 character");
            edittextpassword.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(mail,pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Check your email to verify your account",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Failed to login!! Please check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
