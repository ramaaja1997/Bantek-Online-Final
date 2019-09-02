package id.co.GMFAeroAsia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(3500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(SplashScreenActivity.this,TripAdminActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}
