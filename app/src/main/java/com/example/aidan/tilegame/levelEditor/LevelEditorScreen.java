package com.example.aidan.tilegame.levelEditor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LevelEditorScreen extends AppCompatActivity {

    private LevelEditor levelEditor;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        levelEditor = new LevelEditor(this);

        setContentView(levelEditor);


    }

    @Override
    protected void onResume(){
        super.onResume();
        levelEditor.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        levelEditor.pause();
    }
}
