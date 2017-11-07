package com.myscheduler.HomeScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myscheduler.Login.ProfilePageActivity;
import com.myscheduler.Login.SignInActivity;
import com.myscheduler.Newsfeed.MainActivity;
import com.myscheduler.R;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private static final String TAG = "NewsFeed";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_board_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);







        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mAuth = FirebaseAuth.getInstance();

        displaySelectedScreen(R.id.newsfeed);
    }



    @Override
    public void onBackPressed()
    {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);
        return true;


    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId)
        {
            case R.id.EventCreate:
                fragment = new HomeFragment();
                break;

            case R.id.newsfeed:
                fragment = new MainActivity();
                break;
            case R.id.nav_myprofile:
                startActivity(new Intent(getApplicationContext(), ProfilePageActivity.class));
                break;
            case R.id.Noti:
                startActivity(new Intent(getApplicationContext(), com.myscheduler.Notification.MainActivity.class));
                break;

            case R.id.signOut:
                mAuth.signOut();
                // end activity, go back to sign in page;
                finish();
                Intent intent = new Intent(Dashboard.this,SignInActivity.class);
                startActivity(intent);

        }

        if (fragment != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment,"MAP_Fragment");
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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



    @Override
    public void onPause()
    {

        super.onPause();
    }

    @Override
    public void onResume()
    {

        super.onResume();
    }
}