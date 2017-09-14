package com.example.aidan.tilegame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;


public class ProgressSaver {
    private static ArrayList<Tile> defaultTiles;
    private static ArrayList<Tile> customTiles;
    private static ArrayList<Bitmap> clouds;
    private static Bitmap backround, defaultEnd, customEnd, glowCenter;
    private static int defaultTilesInLevel, customTilesInLevel;

    public static ArrayList<Tile> getDefaultData() {
        return defaultTiles;
    }

    public static void setDefaultData(ArrayList<Tile> t) {
        defaultTiles = t;
    }

    public static ArrayList<Tile> getCustomData() {
        return customTiles;
    }

    public static void setCustomData(ArrayList<Tile> t) {
        customTiles = t;
    }

    public static Bitmap getBackground(Context context) {
            if(backround == null) {
                backround = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.backround5), Math.max(Resources.getSystem().getDisplayMetrics().heightPixels,Resources.getSystem().getDisplayMetrics().widthPixels) + 300, Math.max(Resources.getSystem().getDisplayMetrics().heightPixels,Resources.getSystem().getDisplayMetrics().widthPixels) + 300, false);
            }
            return backround;
    }

    public static Bitmap getCustomEnd(Context context) {
        if (customEnd == null) {
            customEnd = BitmapFactory.decodeResource(context.getResources(), R.drawable.customend);
        }
        return customEnd;
    }

    public static Bitmap getDefaultEnd(Context context) {
        if (defaultEnd == null) {
            defaultEnd = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultend);
        }
        return defaultEnd;
    }

    public static Bitmap getGlowCenter(Context context) {
        if (glowCenter == null) {
            glowCenter = BitmapFactory.decodeResource(context.getResources(), R.drawable.glowcenter);
        }
        return glowCenter;
    }

    public static int getCustomTilesInLevel() {
        return customTilesInLevel;
    }

    public static void setCustomTilesInLevel(int customTilesInLevel) {
        ProgressSaver.customTilesInLevel = customTilesInLevel;
    }

    public static int getDefaultTilesInLevel() {
        return defaultTilesInLevel;
    }

    public static void setDefaultTilesInLevel(int defaultTilesInLevel) {
        ProgressSaver.defaultTilesInLevel = defaultTilesInLevel;
    }

    public static Bitmap getCloud(int i) {
        if (clouds == null) {
            clouds = new ArrayList<>();
            for (int n = 1; n < 9; n++) {
                int resId = Panel.context().getResources().getIdentifier("cloud" + n, "drawable", Panel.context().getPackageName());
                clouds.add(BitmapFactory.decodeResource(Panel.context().getResources(), resId));
            }
        }
        return clouds.get(i - 1);
    }
}
