package com.myscheduler.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myscheduler.HomeScreen.Dashboard;
import com.myscheduler.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SignIn Page";
    private AutoCompleteTextView mEmailFld;
    private static CheckBox show_hide_password;
    private EditText mPassFld;
    private static Animation shakeAnimation;
    private static View view;
    private Button mLoginBtn, mGoogleLoginBtn, mFbLoginBtn, mSignUpBtn, mForgotPassBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_root));

        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake);

        mEmailFld = (AutoCompleteTextView) findViewById(R.id.email_txt);
        mPassFld = (EditText) findViewById(R.id.password_txt);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        findViewById(R.id.b_login).setOnClickListener(this);
        findViewById(R.id.b_googleSignUp).setOnClickListener(this);
        findViewById(R.id.b_createAcc).setOnClickListener(this);
        findViewById(R.id.b_forgotPass).setOnClickListener(this);


        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            mPassFld.setInputType(InputType.TYPE_CLASS_TEXT);
                            mPassFld.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            mPassFld.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            mPassFld.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });

        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void onClick(View v)
    {

        int i = v.getId();
        if(i== R.id.b_login)
        {
            startSignIn();
        }
        else if(i==R.id.b_forgotPass)
        {

            Log.d(TAG,"Forgot Password");
            String emailAddress = mEmailFld.getText().toString();
            if(emailAddress.equals(""))
            {
                mEmailFld.setError("Required");
                return;
            }

            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                // go to profile page or home page
                                //working on it
                            }
                            else if(!task.isSuccessful())
                            {
                                Toast.makeText(SignInActivity.this,"Password reset Email send failed",Toast.LENGTH_LONG ).show();
                            }
                        }
                    });
        }
        else if(i==R.id.b_createAcc)
        {
            startActivity(new Intent(SignInActivity.this, CreateAccountActivity.class));
            this.finish();
        }
        else if(i==R.id.b_googleSignUp)
        {
            Intent intent = new Intent(SignInActivity.this, GoogleSignInActivity.class);
            startActivity(intent);
            //this.finish();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user)
    {
        if(user!=null)
        {
            //go to news feed with user profile information
            Intent intent = new Intent(SignInActivity.this,Dashboard.class);
            startActivity(intent);
            this.finish();
        }
        else
        {
            //
            return;
        }
    }

    private void startSignIn() {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        String email = mEmailFld.getText().toString();
        String pass = mPassFld.getText().toString();
        if(!validateForm(email,pass))
        {
            return;
        }

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
        {
            new CustomToast().Show_Toast(SignInActivity.this, view,
                    "All fields are required.");
            Toast.makeText(SignInActivity.this,"Field Empty!",Toast.LENGTH_LONG).show();
        }
        else {

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {
                       mLoginBtn.startAnimation(shakeAnimation);
                        new CustomToast().Show_Toast(SignInActivity.this, view,
                                "Enter both credentials.");
                        //Toast.makeText(SignInActivity.this,"Sign in Problem",Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(SignInActivity.this,"Sign in successfull",Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(SignInActivity.this, NewsFeedActivity.class));
                        updateUI(mAuth.getCurrentUser());
                        //progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }


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

    private void sendEmailVerification()
    {
        //findViewById(R.id.btn_sendEmailVerication).setEnabled(false);

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //findViewById(R.id.btn_sendEmailVerication).setEnabled(true);

                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignInActivity.this, "Verification Email sent to: "+user.getEmail(),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.e(TAG, "Send email verification ", task.getException());
                            Toast.makeText(SignInActivity.this,"Failed to send email verification",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
