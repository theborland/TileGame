package com.example.aidan.tilegame;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GameScreen extends AppCompatActivity {

    private Panel panel;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        panel = new Panel(this,b.getString("pack"));
        final GestureDetector gestureDetector = new GestureDetector(this, new Panel.SwipeGestureDetector());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        panel.setOnTouchListener(gestureListener);

        setContentView(panel);
    }

    @Override
    protected void onResume(){
        super.onResume();
        panel.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        panel.pause();
    }

}

