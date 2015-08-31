package com.example.geek.porter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity {


    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();

        setContentView(R.layout.activity_splash);
        final ImageView myImage = (ImageView)findViewById(R.id.imgLogo);
        final Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        myImage.startAnimation(myRotation);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.down_from_top,R.anim.up_from_bottom);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}