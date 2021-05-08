package com.lakshmimanivannan.jobhuntappserviceproviders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_Screen extends AppCompatActivity {

    Animation top,bottom;
    ImageView logo;
    TextView name,slogan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash__screen);

        top = (Animation) AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = (Animation) AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        logo = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.textView);
        slogan = (TextView) findViewById(R.id.textView2);


        logo.setAnimation(top);
        name.setAnimation(bottom);
        slogan.setAnimation(bottom);

        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();
    }

    private class LogoLauncher extends Thread{
        public void run(){
            try {
                sleep(5000);
            }catch (Exception e){
                e.printStackTrace();
            }

            SessionManager sessionManager = new SessionManager(Splash_Screen.this);
            if(sessionManager.checkLogin())
            {
                Intent intent = new Intent(Splash_Screen.this, ServiceView.class);
                startActivity(intent);
            }
            else
                startActivity(new Intent(Splash_Screen.this,Login.class));
            Splash_Screen.this.finish();
        }
    }
}