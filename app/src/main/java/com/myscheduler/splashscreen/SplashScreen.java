package com.myscheduler.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.myscheduler.Login.SignInActivity;
import com.myscheduler.R;

import java.util.Random;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_splash);
        mProgress = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }
    //thread for splash screen running
//        Thread logoTimer = new Thread() {
//            public void run() {
//                try {
//                    sleep(2000);
//                } catch (InterruptedException e) {
//                    Log.d("Exception", "Exception" + e);
//                } finally {
//                    startActivity(new Intent(SplashScreen.this, SignInActivity.class));
//                }
//                finish();
//            }
//        };
//        logoTimer.start();


    private void doWork() {
        for (int progress = 0; progress < 200; progress += 1) {
            try {

                Random r = new Random();

                int a = r.nextInt((40-10)+1)+10;
                Thread.sleep(a);
                mProgress.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
                //Timber.e(e.getMessage());
            }
        }
    }

    private void startApp() {
        Intent intent = new Intent(SplashScreen.this, SignInActivity.class);
        startActivity(intent);
    }
}




