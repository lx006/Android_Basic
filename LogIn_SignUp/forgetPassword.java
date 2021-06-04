package com.example.loginemailverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText resetemail;
    private Button resetbutton;
    private ProgressBar progressbar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        resetemail = findViewById(R.id.resetemail);
        resetbutton = findViewById(R.id.resetbutton);
        progressbar = findViewById(R.id.progressBar3);

        auth = FirebaseAuth.getInstance();
        resetbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        resetPassword();
    }

    private void resetPassword()
    {
        String mail = resetemail.getText().toString().trim();
        if(mail.isEmpty())
        {
            resetemail.setError("Email is empty");
            resetemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            resetemail.setError("Please provide valid email");
            resetemail.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgetPassword.this,"Check your mail to reset password",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(ForgetPassword.this,"Try again, Something went wrong!!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
