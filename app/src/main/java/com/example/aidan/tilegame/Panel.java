package com.example.aidan.tilegame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.aidan.tilegame.tiles.Box;
import com.example.aidan.tilegame.tiles.Crate;
import com.example.aidan.tilegame.tiles.DoubleCrate;
import com.example.aidan.tilegame.tiles.EmptyCrate;
import com.example.aidan.tilegame.tiles.Spike;
import com.example.aidan.tilegame.tiles.Wall;

import java.util.ArrayList;

public class Panel extends SurfaceView implements Runnable{

    volatile Boolean playing;
    private static boolean firstPlay = true;
    private Thread gameThread = null;
    private static Context context;
    private static int height,width;
    private static int touchX,touchY;
    private static final int fps=100;
    private static long lastTime;
    private static int defaultLevel = 1,customLevel = 1,maxLevel=1;
    private static int numberOfTilesInLevel = 1;
    private final static double sizeMultiplier = 0.97;
    private static ArrayList<Tile> tiles = new ArrayList<>();
    private static String status = "loading",levelPack = "default";
    private static LevelGenerator levelGen;
    private static SideBar sideBar;
    private static Rect playingField;


    private android.graphics.Canvas canvas;
    private Paint paint;
    private SurfaceHolder surfaceHolder;

    public Panel(Context context,String levelPack) {
        super(context);

        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        Panel.context = context;
        Panel.levelPack =levelPack;

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        defaultLevel = settings.getInt("defaultLevel", 1);
        customLevel = settings.getInt("customLevel", 1);
        maxLevel = settings.getInt("maxLevel", 1);
        firstPlay = settings.getBoolean("firstPlay",true);

        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int buffer = 40;
        if (height > width) {
            playingField = new Rect(buffer, (height - width + 2 * buffer) / 2, width - buffer, (height + width - 2 * buffer) / 2);
        } else {
            playingField = new Rect((width - height + 2 * buffer) / 2, buffer, (width + height - 2 * buffer) / 2, height - buffer);
        }
        levelGen = new LevelGenerator(context);
        sideBar = new SideBar(playingField,width,height,context);

        surfaceHolder = getHolder();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(ProgressSaver.getDefaultData() != null && levelPack.equals("default")){
            tiles = ProgressSaver.getDefaultData();
            numberOfTilesInLevel = ProgressSaver.getDefaultTilesInLevel();

            for(Tile t:tiles){
                t.updateSize();
            }

        } else if(ProgressSaver.getCustomData() != null && levelPack.equals("custom")){
            tiles = ProgressSaver.getCustomData();
            numberOfTilesInLevel = ProgressSaver.getCustomTilesInLevel();

            for(Tile t:tiles){
                t.updateSize();
            }
        } else {
            playAgain();
        }

        status = "playing";

        //settings.edit().clear().commit();
    }

    public static int getFps() {
        return fps;
    }

    public static int getLevel() {
        if(levelPack.equals("default")){
            return defaultLevel;
        } else {
            return customLevel;
        }
    }

    public static String getLevelPack() {
        return levelPack;
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
            canvas.drawBitmap(ProgressSaver.getBackground(context),-30,-50,paint);
            paint.setARGB(180,255,255,255);
            canvas.drawRect(playingField.left-10,playingField.top-10,playingField.right+10,playingField.bottom+10,paint);
            paint.reset();
            canvas.save();
            canvas.translate((float)(playingField.width()/numberOfTilesInLevel*(1-sizeMultiplier))/2,(float)(playingField.width()/numberOfTilesInLevel*(1-sizeMultiplier))/2);
            for (int i = 0; i < tiles.size(); i++) {
                if (!(tiles.get(i) instanceof EmptyCrate) && !(tiles.get(i) instanceof Wall)) {
                    tiles.get(i).paint(canvas, paint);
                }
                if (!tilesMoving() && ((tiles.get(i) instanceof Box && ((Box) tiles.get(i)).isDead()) || (tiles.get(i) instanceof Crate && ((Crate) tiles.get(i)).isDead()) || (tiles.get(i) instanceof DoubleCrate && ((DoubleCrate) tiles.get(i)).isDead()) || (tiles.get(i) instanceof EmptyCrate && ((EmptyCrate) tiles.get(i)).isDead()))) {
                    tiles.remove(i);
                    i--;
                }
            }
            for (Tile t : tiles) {
                if (t instanceof EmptyCrate || t instanceof Wall) {
                    t.paint(canvas, paint);
                }
            }
            paint.reset();
            ParticleManager.paint(canvas, paint);
            canvas.restore();
            paint.reset();
            sideBar.paint(canvas, paint);
            paint.reset();
            if(!status.equals("playing")) {
                Overlay.paint(canvas, paint);
                paint.reset();
            }

            if(tiles.isEmpty()){
                if(levelPack.equals("default")){
                    canvas.drawBitmap(Bitmap.createScaledBitmap(ProgressSaver.getDefaultEnd(context),playingField.width(),playingField.height(),false),playingField.left,playingField.top,paint);
                } else {
                    canvas.drawBitmap(Bitmap.createScaledBitmap(ProgressSaver.getCustomEnd(context),playingField.width(),playingField.height(),false),playingField.left,playingField.top,paint);
                }
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playAgain();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("defaultLevel", defaultLevel);
        editor.putInt("customLevel", customLevel);
        editor.commit();
        if(levelPack.equals("default")) {
            ProgressSaver.setDefaultData((ArrayList<Tile>)tiles.clone());
            ProgressSaver.setDefaultTilesInLevel(numberOfTilesInLevel);
        } else {
            ProgressSaver.setCustomData((ArrayList<Tile>)tiles.clone());
            ProgressSaver.setCustomTilesInLevel(numberOfTilesInLevel);
        }
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
        //Log.e("test","resumed on thread"+ gameThread.getName());
    }


    public static boolean isSolidTile(int x, int y) {
        for (Tile t : tiles) {
            if (!(t instanceof Spike) && t.getX() == x && t.getY() == y || (t instanceof DoubleCrate && ((DoubleCrate) t).getPosition() == 1 && t.getX() + 30 == x && t.getY() == y) || (t instanceof DoubleCrate && ((DoubleCrate) t).getPosition() == 2 && t.getX() == x && t.getY() + 30 == y)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTile(int x, int y, Class tileType) {
        for (Tile t : tiles) {
            if (!(t instanceof Spike) && tileType.isInstance(t) && (t.getX() == x && t.getY() == y || (t instanceof DoubleCrate && ((DoubleCrate) t).getPosition() == 1 && t.getX() + 30 == x && t.getY() == y) || (t instanceof DoubleCrate && ((DoubleCrate) t).getPosition() == 2 && t.getX() == x && t.getY() + 30 == y))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTileBesides(int x, int y, Class tileType) {
        for (Tile t : tiles) {
            if (!(t instanceof Spike) && !tileType.isInstance(t) && (t.getX() == x && t.getY() == y || (t instanceof DoubleCrate && ((DoubleCrate) t).getPosition() == 1 && t.getX() + 30 == x && t.getY() == y) || (t instanceof DoubleCrate && ((DoubleCrate) t).getPosition() == 2 && t.getX() == x && t.getY() + 30 == y))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSpike(int x, int y) {
        for (Tile t : tiles) {
            if (t instanceof Spike && t.getX() == x && t.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public static boolean tilesMoving() {
        for (Tile t : tiles) {
            if (t.isMoving()) {
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

    public static void levelComplete(int x, int y, int size) {
        if (status.equals("playing")) {
            status = "over";
            if(levelPack.equals("default")){
                defaultLevel++;
            } else {
                customLevel++;
            }
            Overlay.setTarget(x, y,size);
        }

    }

    public static void playAgain() {
        tiles.clear();
        if(levelPack.equals("default")) {
            tiles.addAll(levelGen.getLevel(defaultLevel, context));
        } else {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            String levelString = settings.getString("customlevel"+customLevel, "");
            if(!levelString.equals("")){
                tiles.addAll(LevelGenerator.decodeLevel(levelString));
            } else {
                customLevel=Math.max(customLevel-1,0);
                if(customLevel>0){
                    playAgain();
                }
            }
        }

    }

    private static void tileSort(String sort) {
        if (sort.equals("Right")) {
            for (int i = 1; i < tiles.size(); i++) {
                for (int k = 0; k < i; k++) {
                    if (tiles.get(i).getX() > tiles.get(k).getX()) {
                        Tile saleShifted = tiles.get(i);
                        for (int j = i; j > k; j--) {
                            tiles.set(j, tiles.get(j - 1));
                        }
                        tiles.set(k, saleShifted);
                    }
                }
            }
        } else if (sort.equals("Left")) {
            for (int i = 1; i < tiles.size(); i++) {
                for (int k = 0; k < i; k++) {
                    if (tiles.get(i).getX() < tiles.get(k).getX()) {
                        Tile saleShifted = tiles.get(i);
                        for (int j = i; j > k; j--) {
                            tiles.set(j, tiles.get(j - 1));
                        }
                        tiles.set(k, saleShifted);
                    }
                }
            }
        } else if (sort.equals("Up")) {
            for (int i = 1; i < tiles.size(); i++) {
                for (int k = 0; k < i; k++) {
                    if (tiles.get(i).getY() < tiles.get(k).getY()) {
                        Tile saleShifted = tiles.get(i);
                        for (int j = i; j > k; j--) {
                            tiles.set(j, tiles.get(j - 1));
                        }
                        tiles.set(k, saleShifted);
                    }
                }
            }
        } else if (sort.equals("Down")) {
            for (int i = 1; i < tiles.size(); i++) {
                for (int k = 0; k < i; k++) {
                    if (tiles.get(i).getY() > tiles.get(k).getY()) {
                        Tile saleShifted = tiles.get(i);
                        for (int j = i; j > k; j--) {
                            tiles.set(j, tiles.get(j - 1));
                        }
                        tiles.set(k, saleShifted);
                    }
                }
            }
        } else {
            System.out.println("Unknown Sort");
        }
    }

    public static String getStatus() {
        return status;
    }

    public static void setNumberOfTilesInLevel(int levelSize) {
        numberOfTilesInLevel = levelSize;
    }

    public static void setStatus(String string) {
        status = string;
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

    public static void levelChange(int i) {
        if(levelPack.equals("default")) {
            if (defaultLevel + i <= maxLevel) {
                defaultLevel += i;
            }
        } else {
            customLevel += i;
        }
    }

    public static int getMaxLevel(){
        return maxLevel;
    }

    public static void setMaxLevel(int i){
        maxLevel = i;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("maxLevel", maxLevel);
        editor.commit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                sideBar.released();
                touchX=-1;
                touchY=-1;
                break;
            case MotionEvent.ACTION_DOWN:
                touchX=(int)motionEvent.getRawX();
                touchY=(int)motionEvent.getRawY();
                break;
        }
        return true;
    }

    public static boolean firstPlay() {
        return firstPlay;
    }

    public static Context context() {
        return context;
    }

    static class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {

            Double angle = Math.toDegrees(Math.atan2(e1.getY() - e2.getY(), e2.getX() - e1.getX()));

            if (angle > 45 && angle <= 135) {
                if (!tilesMoving()) {
                    tileSort("Up");
                    for (Tile t : tiles) {
                        t.pushUp();
                    }
                }
            } else if (angle >= 135 && angle < 180 || angle < -135 && angle > -180) {
                    tileSort("Left");
                    if (!tilesMoving()) {
                    for (Tile t : tiles) {
                        t.pushLeft();
                    }
                }
            } else if (angle < -45 && angle >= -135) {
                if (!tilesMoving()) {
                    tileSort("Down");
                    for (Tile t : tiles) {
                        t.pushDown();
                    }
                }
            } else if (angle > -45 && angle <= 45) {
                if (!tilesMoving()) {
                    if(firstPlay){
                        firstPlay=false;
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("firstPlay", false);
                        editor.commit();

                    }
                    tileSort("Right");
                    for (Tile t : tiles) {
                        t.pushRight();
                    }
                }
            }
            return false;
        }
    }
}

//name tipping crates ++

//check all grey buttons +

//new font for everything ++

//new arrow +++

//buttons are small on ipad homsecreen ++++