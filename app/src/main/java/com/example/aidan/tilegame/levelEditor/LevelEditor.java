package com.example.aidan.tilegame.levelEditor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.aidan.tilegame.LevelGenerator;
import com.example.aidan.tilegame.ProgressSaver;
import com.example.aidan.tilegame.Tile;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbDoubleCrate;
import java.util.ArrayList;

public class LevelEditor extends SurfaceView implements Runnable {

    volatile Boolean playing;
    private Thread gameThread = null;
    private static Context context;
    private static int height,width;
    private static int touchX,touchY;
    private static final int fps=30;
    private static long lastTime;
    private static int numberOfTilesInLevel = 12;
    private final static double sizeMultiplier = 0.97;
    private static ArrayList<Tile> tiles = new ArrayList<>();
    private static SelectionBar selectBar;
    private static Rect playingField;


    private android.graphics.Canvas canvas;
    private Paint paint;
    private SurfaceHolder surfaceHolder;

    public LevelEditor(Context context) {
        super(context);

        LevelEditor.context = context;

        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int buffer = 40;
        if (height > width) {
            playingField = new Rect(buffer, (height - width + 2 * buffer) / 2, width - buffer, (height + width - 2 * buffer) / 2);
        } else {
            playingField = new Rect((width - height + 2 * buffer) / 2, buffer, (width + height - 2 * buffer) / 2, height - buffer);
        }
        selectBar = new SelectionBar(context);
        if(tiles.isEmpty()){
            selectBar.generateBorder();
        }

        surfaceHolder = getHolder();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        for(Tile t:tiles){
            t.updateSize();
        }

    }

    public static int getFps() {
        return fps;
    }

    @Override
    public void run() {
        while (playing) {
            if(System.nanoTime()-lastTime>=1000000000/fps) {
                draw();
                for (Tile t : tiles) {
                    t.update();

                }
                lastTime = System.nanoTime();
            }
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            paint.setAlpha(80);
            canvas.drawBitmap(ProgressSaver.getBackground(context),-50,-30,paint);
            paint.setARGB(180,255,255,255);
            canvas.drawRect(playingField.left-10,playingField.top-10,playingField.right+10,playingField.bottom+10,paint);
            paint.reset();
            selectBar.paint(canvas,paint);
            paint.reset();
            canvas.save();
            canvas.translate((float)(playingField.width()/selectBar.getSize()*(1-sizeMultiplier))/2,(float)(playingField.width()/selectBar.getSize()*(1-sizeMultiplier))/2);
            try {
                for (Tile t : tiles) {
                    t.paint(canvas, paint);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            canvas.restore();
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (Exception e) {

        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public static boolean isTile(int x, int y) {
        for (Tile t : tiles) {
            if (t.getX() == x && t.getY() == y || (t instanceof DumbDoubleCrate && ((DumbDoubleCrate) t).getPosition() == 1 && t.getX() + 30 == x && t.getY() == y) || (t instanceof DumbDoubleCrate && ((DumbDoubleCrate) t).getPosition() == 2 && t.getX() == x && t.getY() + 30 == y)) {
                return true;
            }
        }
        return false;
    }

    public static int getScreenWidth() {
        return width;
    }
    public static int getScreenHeight() {
        return height;
    }

    public static double getSizeMultiplier() {
        return sizeMultiplier;
    }

    public static Rect getPlayingField() {
        return playingField;
    }

    public static int getTilesInLevel() {
        return numberOfTilesInLevel;
    }

    public static int getTouchX() {
        return touchX;
    }

    public static int getTouchY() {
        return touchY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                selectBar.released();
                touchX=-1;
                touchY=-1;
                break;
            case MotionEvent.ACTION_DOWN:
                touchX=(int)motionEvent.getRawX();
                touchY=(int)motionEvent.getRawY();
                selectBar.pressed();
                break;
            case MotionEvent.ACTION_MOVE:
                touchX=(int)motionEvent.getRawX();
                touchY=(int)motionEvent.getRawY();
        }
        return true;
    }

    public static void addTile(Tile t) {
        tiles.add(t);
    }

    public static Tile getTileAt(int x, int y) {
        for(Tile t:tiles){
            if(t.getX() ==x && t.getY()==y){
                return t;
            }
        }
        return null;
    }

    public static void removeTile(int x, int y) {
        for(int t=0;t<tiles.size();t++){
            if(tiles.get(t).getX()==x && tiles.get(t).getY()==y){
                tiles.remove(t);
                t--;
            }
        }
    }

    public static void save(){
        String levelString = LevelGenerator.encodeLevel(tiles,selectBar.getSize());
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        int levelNumber = getNextLevelId();
        editor.putString("customlevel"+levelNumber, levelString);
        editor.putInt("customLevel",levelNumber);
        editor.commit();
    }

    public static int getNextLevelId() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        int i=1;
        while(!settings.getString("customlevel"+i, "").equals("")){
            i++;
        }
        return i;
    }

    public static void changeSize(int i) {
        numberOfTilesInLevel=Math.max(1,numberOfTilesInLevel+i);
    }
}