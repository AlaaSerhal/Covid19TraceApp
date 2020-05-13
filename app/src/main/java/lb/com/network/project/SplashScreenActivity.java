package lb.com.network.project;

import androidx.appcompat.app.AppCompatActivity;
import lb.com.network.project.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class SplashScreenActivity extends AppCompatActivity {
    private static final String PREF_NAME = "MyAppPreferences";
    private static int PRIVATE_MODE = 0;
    public static final String IsFirsTime = "isFirstTimeKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences prefs = this.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        final Boolean isFirstTime = prefs.getBoolean(IsFirsTime, true);
        final String name = prefs.getString("nameKey", null);
        Thread timerThread = new Thread() {
            public void run() {


                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (isFirstTime) {
                        Intent intent = new Intent(SplashScreenActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timerThread.start();
    }


}
