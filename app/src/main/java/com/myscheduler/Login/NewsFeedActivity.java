package com.myscheduler.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myscheduler.R;
public class NewsFeedActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NewsFeed";

    private FirebaseAuth mAuth;
    UserProfile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_activity_main);

        findViewById(R.id.b_signOut).setOnClickListener(this);
        findViewById(R.id.b_profilePage).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        //Bundle data = getIntent().getExtras();
        //profile = (UserProfile) data.getParcelable("Profile");
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //Bundle data = getIntent().getExtras();
        //profile = (UserProfile) data.getParcelable("Profile");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG,"CurrentUser email: "+currentUser.getEmail().toString());
    }

    private void signOut()
    {
        mAuth.signOut();
        // end activity, go back to sign in page;
        this.finish();
    }

    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if(i== R.id.b_signOut)
        {
            signOut();
        }
        else if(i== R.id.b_profilePage)
        {
            //Intent intent = new Intent(NewsFeedActivity.this,databaseTestActivity.class);
            Intent intent = new Intent(NewsFeedActivity.this,ProfilePageActivity.class);
            startActivity(intent);
            //this.finish();
        }
    }
}
