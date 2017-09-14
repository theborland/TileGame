package com.example.aidan.tilegame;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbBox;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbCrate;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbDoubleCrate;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbEmptyCrate;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbSpike;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbWall;
import com.example.aidan.tilegame.tiles.Box;
import com.example.aidan.tilegame.tiles.Crate;
import com.example.aidan.tilegame.tiles.DoubleCrate;
import com.example.aidan.tilegame.tiles.EmptyCrate;
import com.example.aidan.tilegame.tiles.Spike;
import com.example.aidan.tilegame.tiles.Wall;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;


public class LevelGenerator {

    private static Context context;

    public LevelGenerator(Context context){
        LevelGenerator.context = context;
    }

    public ArrayList<Tile> getLevel(int waveId, Context context) {
        ArrayList<Tile> level = new ArrayList<Tile>();

        Resources res = context.getResources();
        XmlResourceParser parser = res.getXml(R.xml.levels);

        int eventType = 0;
        try {
            eventType = parser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        boolean correctLevel = false;
        String text=null,type=null,posX=null,posY=null,position=null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase("Level"+waveId)) {
                        correctLevel = true;
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("Level"+waveId)) {
                        return level;
                    } else if(tagname.equalsIgnoreCase("type")){
                        type = text.trim();
                    } else if(tagname.equalsIgnoreCase("posX")){
                        posX = text.trim();
                    } else if(tagname.equalsIgnoreCase("posY")){
                        posY = text.trim();
                    } else if(tagname.equalsIgnoreCase("position")){
                        position = text;
                    } else if(tagname.equalsIgnoreCase("size")){
                        Panel.setNumberOfTilesInLevel(Integer.valueOf(text.trim()));
                    } else if(tagname.equalsIgnoreCase("border") && correctLevel){
                        int x = Integer.valueOf(text.split(",")[0].trim());
                        int y = Integer.valueOf(text.split(",")[1].trim());
                        Bitmap img = BitmapFactory.decodeResource(context.getResources(),R.drawable.wallpixelated);
                        for(int i=0;i<x;i++){
                            level.add(new Wall(30*i,0,img));
                        }
                        for(int i=0;i<x;i++){
                            level.add(new Wall(30*i,30*(y-1),img));
                        }
                        for(int i=1;i<y-1;i++){
                            level.add(new Wall(0,30*i,img));
                        }
                        for(int i=1;i<y-1;i++){
                            level.add(new Wall(30*(x-1),30*i,img));
                        }

                    } else if(tagname.equalsIgnoreCase("tile") && correctLevel){
                        if(type.equals("Wall")) {
                            Bitmap img = BitmapFactory.decodeResource(context.getResources(),R.drawable.wallpixelated);
                            level.add(new Wall(Integer.valueOf(posX)*30,Integer.valueOf(posY)*30,img));
                        } else if(type.equals("Crate")){
                            Bitmap img = BitmapFactory.decodeResource(context.getResources(),R.drawable.cratepixelated);
                            level.add(new Crate(Integer.valueOf(posX)*30,Integer.valueOf(posY)*30,img));
                        } else if(type.equals("EmptyCrate")){
                            Bitmap img = BitmapFactory.decodeResource(context.getResources(),R.drawable.emptycratepixelated);
                            level.add(new EmptyCrate(Integer.valueOf(posX)*30,Integer.valueOf(posY)*30,img));
                        } else if(type.equals("Box")){
                            Bitmap img = BitmapFactory.decodeResource(context.getResources(),R.drawable.boxpixelated);
                            level.add(new Box(Integer.valueOf(posX)*30,Integer.valueOf(posY)*30,img));

                        } else if(type.equals("DoubleCrate")){
                            Bitmap img;
                            if(Integer.valueOf(position.trim())==1) {
                                img = BitmapFactory.decodeResource(context.getResources(), R.drawable.doublecrate1pixelated);
                            } else {
                                img = BitmapFactory.decodeResource(context.getResources(), R.drawable.doublecrate2pixelated);
                            }
                            level.add(new DoubleCrate(Integer.valueOf(posX)*30,Integer.valueOf(posY)*30,Integer.valueOf(position.trim()),img));
                        } else if(type.equals("Spike")){
                            Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.spikespixelated);
                            level.add(new Spike(Integer.valueOf(posX)*30,Integer.valueOf(posY)*30,Integer.valueOf(position.trim()),img));
                        }
                    }
                    break;

                default:
                    break;
            }
            try {
                eventType = parser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("Test","Cannot find level");
        return level;
    }

    public static String encodeLevel(ArrayList<Tile> tiles, int size) {
        String levelString;
        levelString = size+":";
        for(Tile t:tiles) {
            if(t instanceof DumbBox)
                levelString += "box,"+t.getX()+","+t.getY()+":";
            if(t instanceof DumbCrate)
                levelString += "crate,"+t.getX()+","+t.getY()+":";
            if(t instanceof DumbEmptyCrate)
                levelString += "emptyCrate,"+t.getX()+","+t.getY()+":";
            if(t instanceof DumbWall)
                levelString += "wall,"+t.getX()+","+t.getY()+":";
            if(t instanceof DumbDoubleCrate)
                levelString += "doubleCrate,"+t.getX()+","+t.getY()+","+((DumbDoubleCrate) t).getPosition()+":";
            if(t instanceof DumbSpike)
                levelString += "spike,"+t.getX()+","+t.getY()+","+((DumbSpike) t).getPosition()+":";
        }
        return levelString;
    }

    public static ArrayList<Tile> decodeLevel(String s){
        ArrayList<Tile> level = new ArrayList<Tile>();
        Panel.setNumberOfTilesInLevel(Integer.valueOf(s.split(":")[0]));
            for(int i=1;i<s.split(":").length;i++){
                if(s.split(":")[2]!=null) {
                    if (s.split(":")[i].split(",")[0].equals("box"))
                        level.add(new Box(Integer.valueOf(s.split(":")[i].split(",")[1]), Integer.valueOf(s.split(":")[i].split(",")[2]), BitmapFactory.decodeResource(context.getResources(), R.drawable.boxpixelated)));
                    if (s.split(":")[i].split(",")[0].equals("crate"))
                        level.add(new Crate(Integer.valueOf(s.split(":")[i].split(",")[1]), Integer.valueOf(s.split(":")[i].split(",")[2]), BitmapFactory.decodeResource(context.getResources(), R.drawable.cratepixelated)));
                    if (s.split(":")[i].split(",")[0].equals("emptyCrate"))
                        level.add(new EmptyCrate(Integer.valueOf(s.split(":")[i].split(",")[1]), Integer.valueOf(s.split(":")[i].split(",")[2]), BitmapFactory.decodeResource(context.getResources(), R.drawable.emptycratepixelated)));
                    if (s.split(":")[i].split(",")[0].equals("wall"))
                        level.add(new Wall(Integer.valueOf(s.split(":")[i].split(",")[1]), Integer.valueOf(s.split(":")[i].split(",")[2]), BitmapFactory.decodeResource(context.getResources(), R.drawable.wallpixelated)));
                    if (s.split(":")[i].split(",")[0].equals("doubleCrate") && Integer.valueOf(s.split(":")[i].split(",")[3]) == 1)
                        level.add(new DoubleCrate(Integer.valueOf(s.split(":")[i].split(",")[1]), Integer.valueOf(s.split(":")[i].split(",")[2]), Integer.valueOf(s.split(":")[i].split(",")[3]), BitmapFactory.decodeResource(context.getResources(), R.drawable.doublecrate1pixelated)));
                    if (s.split(":")[i].split(",")[0].equals("doubleCrate") && Integer.valueOf(s.split(":")[i].split(",")[3]) == 2)
                        level.add(new DoubleCrate(Integer.valueOf(s.split(":")[i].split(",")[1]), Integer.valueOf(s.split(":")[i].split(",")[2]), Integer.valueOf(s.split(":")[i].split(",")[3]), BitmapFactory.decodeResource(context.getResources(), R.drawable.doublecrate2pixelated)));
                    if (s.split(":")[i].split(",")[0].equals("spike"))
                        level.add(new Spike(Integer.valueOf(s.split(":")[i].split(",")[1]), Integer.valueOf(s.split(":")[i].split(",")[2]), Integer.valueOf(s.split(":")[i].split(",")[3]), BitmapFactory.decodeResource(context.getResources(), R.drawable.spikespixelated)));
                }
            }
        return level;
    }
}
