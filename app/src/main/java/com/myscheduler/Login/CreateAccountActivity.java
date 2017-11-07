package com.myscheduler.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myscheduler.Newsfeed.MainActivity;
import com.myscheduler.R;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "CreateAccount Page";

    private AutoCompleteTextView mEmailFld;
    private EditText mPassFld,mConfirmPass;
    private Button mSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        mAuth = FirebaseAuth.getInstance();

        mEmailFld = (AutoCompleteTextView) findViewById(R.id.email_txt);
        mPassFld = (EditText) findViewById(R.id.password_txt);
        mConfirmPass = (EditText) findViewById(R.id.confirmPassword_txt);

        findViewById(R.id.b_signUp).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if( i == R.id.b_signUp)
        {
            createAccount();
        }
    }

    private void createAccount()
    {
        String email, pass;
        email = mEmailFld.getText().toString();
        pass = mPassFld.getText().toString();

        Log.d(TAG, "Create Account: "+email);
        if(!validateForm(email,pass))
        {
            return;
        }
        else if(!pass.equals(mConfirmPass.getText().toString()))
        {
            mConfirmPass.setError("Password does not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.d(TAG,"CreateUserEmailPass: Success");
                    Toast.makeText(CreateAccountActivity.this,"New Account Created", Toast.LENGTH_SHORT).show();

                    final FirebaseUser user = mAuth.getCurrentUser();
                    /*
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(CreateAccountActivity.this,"Verification Email sent", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });
                    */
                    updateUI(user);
                }
                else
                {
                    Log.w(TAG,"CreateUserEmailPass: failed ", task.getException());
                    Toast.makeText(CreateAccountActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                    updateUI(null);
                }
            }
        });
    }

    private boolean validateForm(String email, String pass)
    {
        boolean valid = true;
        if(TextUtils.isEmpty(email))
        {
            mEmailFld.setError("Required");
            valid = false;
        }
        else mEmailFld.setError(null);
        if(TextUtils.isEmpty(pass))
        {
            mPassFld.setError("Required");
            valid = false;
        }
        else mPassFld.setError(null);

        return valid;
    }
    private void updateUI(FirebaseUser user)
    {
        if(user!=null)
        {
            // go to news feed with user profile information
            Intent intent = new Intent(CreateAccountActivity.this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        else
        {
            //
            return;
        }
    }
}