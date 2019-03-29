package com.optisoft.emauser.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.widget.ImageView;

import com.optisoft.emauser.R;
import com.github.florent37.viewanimator.ViewAnimator;

public class FirstScreen extends AppCompatActivity {

    private ImageView logo;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }

        logo = (ImageView)findViewById(R.id.logo);
        ViewAnimator
                .animate(logo)
                .waitForHeight() //wait until a ViewTreeObserver notification
                .dp().width(0,200)
                .dp().height(0,100)
                .start();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent( getApplication(), LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }


}
