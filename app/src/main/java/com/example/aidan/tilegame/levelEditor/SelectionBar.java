package com.example.aidan.tilegame.levelEditor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.aidan.tilegame.HomeScreen;
import com.example.aidan.tilegame.ProgressSaver;
import com.example.aidan.tilegame.R;
import com.example.aidan.tilegame.SideBar;
import com.example.aidan.tilegame.Tile;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbBox;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbCrate;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbDoubleCrate;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbEmptyCrate;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbSpike;
import com.example.aidan.tilegame.levelEditor.dumbTiles.DumbWall;
import com.example.aidan.tilegame.tiles.Box;

import java.util.logging.Level;

public class SelectionBar {
    private String selectedItem = "null";
    private int difX,difY;
    private Rect box;
    private Rect crate;
    private Rect doubleCrate;
    private Rect emptyCrate;
    private Rect spike;
    private Rect wall;
    private Rect spike2;
    private Rect spike3;
    private Rect spike4;
    private Rect doubleCrate2;
    private Bitmap boxImg,crateImg,doubleCrate1Img,doubleCrate2Img,emptyCrateImg,spikeImg,wallImg;
    private Bitmap boxImgRaw,crateImgRaw,doubleCrate1ImgRaw,doubleCrate2ImgRaw,emptyCrateImgRaw,spikeImgRaw,wallImgRaw;
    private Context context;
    private float hoverTopBack= 1;
    private Rect buttonSave;
    private float hoverSave= 1;
    private Rect buttonTopBack;
    private Rect buttonSizeUp;
    private float hoverSizeUp= 1;
    private Rect buttonSizeDown;
    private float hoverSizeDown= 1;
    private int boxSize;

    public SelectionBar(Context context){
        this.context = context;
        if(LevelEditor.getScreenWidth()>LevelEditor.getScreenHeight()){
            //select
            int buffer = 30;
            int width = LevelEditor.getPlayingField().left;
            int tileSize = Math.min((LevelEditor.getScreenHeight()-7*buffer)/6,(width-3*buffer)/2);
            //button
            int leftSpaceWidth = LevelEditor.getPlayingField().left;
            int leftRightBuffer = leftSpaceWidth / 8;
            int topBottomBuffer = LevelEditor.getScreenHeight() / 18;
            boxSize = leftSpaceWidth - leftRightBuffer * 2;
            buttonTopBack = new Rect(LevelEditor.getPlayingField().right+leftRightBuffer, LevelEditor.getScreenHeight()-topBottomBuffer-boxSize, LevelEditor.getPlayingField().right+leftRightBuffer + boxSize, LevelEditor.getScreenHeight()-topBottomBuffer);
            buttonSave = new Rect(LevelEditor.getPlayingField().right+leftRightBuffer, topBottomBuffer, LevelEditor.getPlayingField().right+leftRightBuffer + boxSize, topBottomBuffer+boxSize);
            int smallBoxSize = boxSize/2;
            buttonSizeUp = new Rect(LevelEditor.getPlayingField().right+leftRightBuffer+(boxSize-smallBoxSize)/2, LevelEditor.getPlayingField().centerY()-smallBoxSize, LevelEditor.getPlayingField().right+leftRightBuffer+(boxSize-smallBoxSize)/2+smallBoxSize, LevelEditor.getPlayingField().centerY());
            buttonSizeDown = new Rect(LevelEditor.getPlayingField().right+leftRightBuffer+(boxSize-smallBoxSize)/2, LevelEditor.getPlayingField().centerY(), LevelEditor.getPlayingField().right+leftRightBuffer+(boxSize-smallBoxSize)/2+smallBoxSize, LevelEditor.getPlayingField().centerY()+smallBoxSize);

            box = new Rect(buffer,buffer,tileSize+buffer,tileSize+buffer);
            crate = new Rect(buffer,2*buffer+tileSize,tileSize+buffer,tileSize+(2*buffer+tileSize));

            doubleCrate = new Rect(2*buffer+tileSize,(int)(1.5*buffer),tileSize+(2*buffer+tileSize),2*tileSize+(int)(1.5*buffer));
            doubleCrate2 = new Rect((int)(1.5*buffer),3*buffer+2*tileSize,2*tileSize+(int)(1.5*buffer),3*tileSize+3*buffer);

            emptyCrate = new Rect(buffer,4*buffer+3*tileSize,tileSize+buffer,4*(tileSize+buffer));
            wall = new Rect(2*buffer+tileSize,4*buffer+3*tileSize,2*(tileSize+buffer),4*(tileSize+buffer));

            spike = new Rect(buffer,5*buffer+4*tileSize,tileSize+buffer,5*(tileSize+buffer));
            spike2 = new Rect(2*buffer+tileSize,5*buffer+4*tileSize,2*(tileSize+buffer),5*(tileSize+buffer));

            spike3 = new Rect(buffer,6*buffer+5*tileSize,tileSize+buffer,6*(tileSize+buffer));
            spike4 = new Rect(2*buffer+tileSize,6*buffer+5*tileSize,2*(tileSize+buffer),6*(tileSize+buffer));
        } else {
            //select
            int buffer = 30;
            int height = LevelEditor.getPlayingField().top;
            int tileSize = (height-3*buffer)/2;
            //button
            int bottomSpaceHeight = LevelEditor.getScreenHeight() - LevelEditor.getPlayingField().bottom;
            int topBottomBuffer = bottomSpaceHeight / 8;
            int leftRightBuffer = LevelEditor.getScreenWidth() / 18;
            boxSize = bottomSpaceHeight - topBottomBuffer * 2;
            buttonTopBack = new Rect(leftRightBuffer, LevelEditor.getPlayingField().bottom + topBottomBuffer, leftRightBuffer + boxSize, LevelEditor.getPlayingField().bottom + topBottomBuffer + boxSize);
            buttonSave = new Rect(LevelEditor.getScreenWidth()-leftRightBuffer-boxSize, LevelEditor.getPlayingField().bottom + topBottomBuffer, LevelEditor.getScreenWidth()-leftRightBuffer, LevelEditor.getPlayingField().bottom + topBottomBuffer + boxSize);
            int smallBoxSize = boxSize/2;
            buttonSizeUp = new Rect(LevelEditor.getPlayingField().centerX()-(smallBoxSize)/2,LevelEditor.getPlayingField().bottom + leftRightBuffer, LevelEditor.getPlayingField().centerX()-(smallBoxSize)/2+smallBoxSize, LevelEditor.getPlayingField().bottom + leftRightBuffer+smallBoxSize);
            buttonSizeDown = new Rect(LevelEditor.getPlayingField().centerX()-(smallBoxSize)/2, LevelEditor.getPlayingField().bottom + leftRightBuffer+smallBoxSize, LevelEditor.getPlayingField().centerX()-(smallBoxSize)/2+smallBoxSize, LevelEditor.getPlayingField().bottom + leftRightBuffer+2*smallBoxSize);

            box = new Rect(buffer,buffer,tileSize+buffer,tileSize+buffer);
            crate = new Rect(2*buffer+tileSize,buffer,2*buffer+2*tileSize,buffer+tileSize);
            doubleCrate2 = new Rect((int)(1.5*buffer),2*buffer+tileSize,2*(tileSize+(int)(.7*buffer)),2*(tileSize+buffer));
            doubleCrate = new Rect(3*buffer+2*tileSize,(int)(1.5*buffer),3*tileSize+3*buffer,2*tileSize+2*(int)(.7*buffer));

            emptyCrate = new Rect(4*buffer+3*tileSize,buffer,4*(tileSize+buffer),tileSize+buffer);
            wall = new Rect(4*buffer+3*tileSize,2*buffer+tileSize,4*(tileSize+buffer),2*(tileSize+buffer));

            spike = new Rect(5*buffer+4*tileSize,buffer,5*(tileSize+buffer),tileSize+buffer);
            spike2 = new Rect(5*buffer+4*tileSize,2*buffer+tileSize,5*(tileSize+buffer),2*(tileSize+buffer));

            spike3 = new Rect(6*buffer+5*tileSize,buffer,6*(tileSize+buffer),tileSize+buffer);
            spike4 = new Rect(6*buffer+5*tileSize,2*buffer+tileSize,6*(tileSize+buffer),2*(tileSize+buffer));
        }
        boxImgRaw = BitmapFactory.decodeResource(context.getResources(), R.drawable.boxpixelated);
        crateImgRaw = BitmapFactory.decodeResource(context.getResources(),R.drawable.cratepixelated);
        doubleCrate1ImgRaw = BitmapFactory.decodeResource(context.getResources(),R.drawable.doublecrate2pixelated);
        doubleCrate2ImgRaw = BitmapFactory.decodeResource(context.getResources(),R.drawable.doublecrate1pixelated);
        emptyCrateImgRaw = BitmapFactory.decodeResource(context.getResources(),R.drawable.emptycratepixelated);
        spikeImgRaw = BitmapFactory.decodeResource(context.getResources(),R.drawable.spikespixelated);
        wallImgRaw = BitmapFactory.decodeResource(context.getResources(),R.drawable.wallpixelated);
        boxImg = Bitmap.createScaledBitmap(boxImgRaw,box.width(),box.height(),false);
        crateImg = Bitmap.createScaledBitmap(crateImgRaw,crate.width(),crate.height(),false);
        doubleCrate1Img = Bitmap.createScaledBitmap(doubleCrate1ImgRaw,doubleCrate.width(),doubleCrate.height(),false);
        doubleCrate2Img = Bitmap.createScaledBitmap(doubleCrate2ImgRaw,doubleCrate2.width(),doubleCrate2.height(),false);
        emptyCrateImg = Bitmap.createScaledBitmap(emptyCrateImgRaw,emptyCrate.width(),emptyCrate.height(),false);
        spikeImg = Bitmap.createScaledBitmap(spikeImgRaw,spike.width(),spike.height(),false);
        wallImg = Bitmap.createScaledBitmap(wallImgRaw,wall.width(),wall.height(),false);
    }

    public void paint(Canvas canvas, Paint paint){
//        for(int i=0;i<=LevelEditor.getTilesInLevel();i++){
//            paint.setColor(Color.LTGRAY);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(3);
//            canvas.drawLine(LevelEditor.getPlayingField().width()/LevelEditor.getTilesInLevel()*i+LevelEditor.getPlayingField().left,LevelEditor.getPlayingField().top,LevelEditor.getPlayingField().width()/LevelEditor.getTilesInLevel()*i+LevelEditor.getPlayingField().left,LevelEditor.getPlayingField().bottom,paint);
//            canvas.drawLine(LevelEditor.getPlayingField().left,LevelEditor.getPlayingField().height()/LevelEditor.getTilesInLevel()*i+LevelEditor.getPlayingField().top,LevelEditor.getPlayingField().right,LevelEditor.getPlayingField().height()/LevelEditor.getTilesInLevel()*i+LevelEditor.getPlayingField().top,paint);
//        }
        canvas.drawBitmap(boxImg,box.left,box.top,paint);
        canvas.drawBitmap(crateImg,crate.left,crate.top,paint);
        canvas.drawBitmap(doubleCrate1Img,doubleCrate.left,doubleCrate.top,paint);
        canvas.drawBitmap(doubleCrate2Img,doubleCrate2.left,doubleCrate2.top,paint);
        canvas.drawBitmap(emptyCrateImg,emptyCrate.left,emptyCrate.top,paint);
        canvas.drawBitmap(spikeImg,spike.left,spike.top,paint);
        canvas.drawBitmap(wallImg,wall.left,wall.top,paint);
        canvas.save();
        canvas.rotate(90,spike2.exactCenterX(),spike2.exactCenterY());
        canvas.drawBitmap(spikeImg,spike2.left,spike2.top,paint);
        canvas.restore();
        canvas.save();
        canvas.rotate(180,spike3.exactCenterX(),spike3.exactCenterY());
        canvas.drawBitmap(spikeImg,spike3.left,spike3.top,paint);
        canvas.restore();
        canvas.save();
        canvas.rotate(270,spike4.exactCenterX(),spike4.exactCenterY());
        canvas.drawBitmap(spikeImg,spike4.left,spike4.top,paint);
        canvas.restore();

        if(selectedItem.equals("box")){
            canvas.drawBitmap(boxImg,LevelEditor.getTouchX()+difX,LevelEditor.getTouchY()+difY,paint);
        }
        if(selectedItem.equals("crate")){
            canvas.drawBitmap(crateImg,LevelEditor.getTouchX()+difX,LevelEditor.getTouchY()+difY,paint);
        }
        if(selectedItem.equals("doubleCrate")){
            canvas.drawBitmap(doubleCrate1Img,LevelEditor.getTouchX()+difX,LevelEditor.getTouchY()+difY,paint);
        }
        if(selectedItem.equals("doubleCrate2")){
            canvas.drawBitmap(doubleCrate2Img,LevelEditor.getTouchX()+difX,LevelEditor.getTouchY()+difY,paint);
        }
        if(selectedItem.equals("emptyCrate")){
            canvas.drawBitmap(emptyCrateImg,LevelEditor.getTouchX()+difX,LevelEditor.getTouchY()+difY,paint);
        }
        if(selectedItem.equals("wall")){
            canvas.drawBitmap(wallImg,LevelEditor.getTouchX()+difX,LevelEditor.getTouchY()+difY,paint);
        }
        if(selectedItem.equals("spike")){
            canvas.drawBitmap(spikeImg,LevelEditor.getTouchX()+difX,LevelEditor.getTouchY()+difY,paint);
        }
        if(selectedItem.equals("spike2")){
            canvas.save();
            canvas.rotate(90,LevelEditor.getTouchX()+difX+spike2.width()/2,LevelEditor.getTouchY()+difY+spike2.height()/2);
            canvas.drawBitmap(spikeImg,LevelEditor.getTouchX()+difX,LevelEditor.getTouchY()+difY,paint);
            canvas.restore();
        }
        if(selectedItem.equals("spike3")){
            canvas.save();
            canvas.rotate(180,LevelEditor.getTouchX()+difX+spike3.width()/2,LevelEditor.getTouchY()+difY+spike3.height()/2);
            canvas.drawBitmap(spikeImg,LevelEditor.getTouchX()+difX,LevelEditor.getTouchY()+difY,paint);
            canvas.restore();
        }
        if(selectedItem.equals("spike4")){
            canvas.save();
            canvas.rotate(270,LevelEditor.getTouchX()+difX+spike4.width()/2,LevelEditor.getTouchY()+difY+spike4.height()/2);
            canvas.drawBitmap(spikeImg,LevelEditor.getTouchX()+difX,LevelEditor.getTouchY()+difY,paint);
            canvas.restore();
        }
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(boxSize/4.66f);
//        paint.setStrokeCap(Paint.Cap.ROUND);
//        paint.setStrokeJoin(Paint.Join.ROUND);

        canvas.save();
        canvas.scale(hoverTopBack/1.5f, hoverTopBack/1.5f, buttonTopBack.exactCenterX(), buttonTopBack.exactCenterY());
        paint.setColor(Color.argb(255,40,25,55));
        canvas.rotate(90,buttonTopBack.centerX(),buttonTopBack.centerY());
        canvas.drawPath(SideBar.triangle(buttonTopBack.centerX(),buttonTopBack.centerY(),boxSize/2.5f,true),paint);
        paint.setColor(Color.argb(200, 255, 204, 0));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(buttonTopBack,paint);
        canvas.restore();
        canvas.save();
        paint.setStrokeWidth(boxSize/9f);
        canvas.scale(hoverSave/1.5f, hoverSave/1.5f, buttonSave.exactCenterX(), buttonSave.exactCenterY());
        paint.setColor(Color.argb(255,40,25,55));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(savePath(buttonSave.centerX(),buttonSave.centerY(),boxSize/3.5f),paint);
        paint.setColor(Color.argb(200, 255, 204, 0));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(buttonSave,paint);
        canvas.restore();
        paint.setStrokeWidth(boxSize/4.66f);
        canvas.save();
        canvas.scale(hoverSizeUp/1.5f, hoverSizeUp/1.5f, buttonSizeUp.exactCenterX(), buttonSizeUp.exactCenterY());
        paint.setColor(Color.argb(255,40,25,55));
        paint.setStyle(Paint.Style.STROKE);
        canvas.rotate(90,buttonSizeUp.centerX(),buttonSizeUp.centerY());
        canvas.drawPath(SideBar.triangle(buttonSizeUp.centerX(),buttonSizeUp.centerY(),boxSize/50f,true),paint);
        paint.setColor(Color.argb(200, 255, 204, 0));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(buttonSizeUp,paint);
        canvas.restore();

        canvas.save();
        canvas.scale(hoverSizeDown/1.5f, hoverSizeDown/1.5f, buttonSizeDown.exactCenterX(), buttonSizeDown.exactCenterY());
        paint.setColor(Color.argb(255,40,25,55));
        paint.setStyle(Paint.Style.STROKE);
        canvas.rotate(-90,buttonSizeDown.centerX(),buttonSizeDown.centerY());
        canvas.drawPath(SideBar.triangle(buttonSizeDown.centerX(),buttonSizeDown.centerY(),boxSize/50f,true),paint);
        paint.setColor(Color.argb(200, 255, 204, 0));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(buttonSizeDown,paint);
        canvas.restore();
        paint.reset();

        if(buttonTopBack.contains(LevelEditor.getTouchX(), LevelEditor.getTouchY())){
            if(hoverTopBack<1.3){
                hoverTopBack+=(1.3-hoverTopBack)/100*1000/LevelEditor.getFps()+.0001;
            }
        } else if(hoverTopBack>1){
            hoverTopBack-=.001*1000/LevelEditor.getFps();
        }
        if(hoverTopBack<1){
            hoverTopBack=1;
        }
        if(hoverTopBack>1.3){
            hoverTopBack=1.3f;
        }
        if(buttonSave.contains(LevelEditor.getTouchX(), LevelEditor.getTouchY())){
            if(hoverSave<1.3){
                hoverSave+=(1.3-hoverSave)/100*1000/LevelEditor.getFps()+.0001;
            }
        } else if(hoverSave>1){
            hoverSave-=.001*1000/LevelEditor.getFps();
        }
        if(hoverSave<1){
            hoverSave=1;
        }
        if(hoverSave>1.3){
            hoverSave=1.3f;
        }

        if(buttonSizeUp.contains(LevelEditor.getTouchX(), LevelEditor.getTouchY())){
            if(hoverSizeUp<1.3){
                hoverSizeUp+=(1.3-hoverSizeUp)/100*1000/LevelEditor.getFps()+.0001;
            }
        } else if(hoverSizeUp>1){
            hoverSizeUp-=.001*1000/LevelEditor.getFps();
        }
        if(hoverSizeUp<1){
            hoverSizeUp=1;
        }
        if(hoverSizeUp>1.3){
            hoverSizeUp=1.3f;
        }
        if(buttonSizeDown.contains(LevelEditor.getTouchX(), LevelEditor.getTouchY())){
            if(hoverSizeDown<1.3){
                hoverSizeDown+=(1.3-hoverSizeDown)/100*1000/LevelEditor.getFps()+.0001;
            }
        } else if(hoverSizeDown>1){
            hoverSizeDown-=.001*1000/LevelEditor.getFps();
        }
        if(hoverSizeDown<1){
            hoverSizeDown=1;
        }
        if(hoverSizeDown>1.3){
            hoverSizeDown=1.3f;
        }
    }

    private Path savePath(int x, int y, float size) {
        Path p = new Path();
        p.moveTo(x-size,y-size);
        p.lineTo(x-size,y+size);
        p.lineTo(x+size,y+size);
        p.lineTo(x+size,y-size/2);
        p.lineTo(x+size/2,y-size);
        p.close();
        p.moveTo(x-size,y);
        p.lineTo(x+size,y);
        p.moveTo(x+size/3f,y-size/4);
        p.lineTo(x+size/3f,y-size/1.5f);

        return p;
    }

    public void released() {
        if(LevelEditor.getPlayingField().contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            int boxX = (int)((LevelEditor.getTouchX()-LevelEditor.getPlayingField().left)/(LevelEditor.getPlayingField().width()/(double)LevelEditor.getTilesInLevel()));
            int boxY = (int)((LevelEditor.getTouchY()-LevelEditor.getPlayingField().top)/(LevelEditor.getPlayingField().height()/(double)LevelEditor.getTilesInLevel()));
            if(!LevelEditor.isTile(boxX*30,boxY*30)){
                switch(selectedItem) {
                    case "box":
                        LevelEditor.addTile(new DumbBox(boxX*30,boxY*30,boxImgRaw));
                        break;
                    case "crate":
                        LevelEditor.addTile(new DumbCrate(boxX*30,boxY*30,crateImgRaw));
                        break;
                    case "doubleCrate":
                        if(!LevelEditor.isTile(boxX*30,boxY*30+30))
                        LevelEditor.addTile(new DumbDoubleCrate(boxX*30,boxY*30,2,doubleCrate1ImgRaw));
                        break;
                    case "doubleCrate2":
                        if(!LevelEditor.isTile(boxX*30+30,boxY*30))
                        LevelEditor.addTile(new DumbDoubleCrate(boxX*30,boxY*30,1,doubleCrate2ImgRaw));
                        break;
                    case "wall":
                        LevelEditor.addTile(new DumbWall(boxX*30,boxY*30,wallImgRaw));
                        break;
                    case "emptyCrate":
                        LevelEditor.addTile(new DumbEmptyCrate(boxX*30,boxY*30,emptyCrateImgRaw));
                        break;
                    case "spike":
                        LevelEditor.addTile(new DumbSpike(boxX*30,boxY*30,1,spikeImgRaw));
                        break;
                    case "spike2":
                        LevelEditor.addTile(new DumbSpike(boxX*30,boxY*30,2,spikeImgRaw));
                        break;
                    case "spike3":
                        LevelEditor.addTile(new DumbSpike(boxX*30,boxY*30,3,spikeImgRaw));
                        break;
                    case "spike4":
                        LevelEditor.addTile(new DumbSpike(boxX*30,boxY*30,4,spikeImgRaw));
                        break;
                }
            }
        }
        selectedItem = "none";

        if (buttonTopBack.contains(LevelEditor.getTouchX(), LevelEditor.getTouchY())) {
            Intent i = new Intent(context,HomeScreen.class);
            context.startActivity(i);
            ((AppCompatActivity)context).overridePendingTransition(R.anim.up_to_mid,R.anim.mid_to_down);
        }
        if (buttonSave.contains(LevelEditor.getTouchX(), LevelEditor.getTouchY())) {
            LevelEditor.save();
            ProgressSaver.setCustomData(null);
        }
        if (buttonSizeDown.contains(LevelEditor.getTouchX(), LevelEditor.getTouchY())) {
            sizeChange(false);
        }if (buttonSizeUp.contains(LevelEditor.getTouchX(), LevelEditor.getTouchY())) {
            sizeChange(true);
        }
    }

    public void pressed() {
        if(LevelEditor.getPlayingField().contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())) {
            int boxX = (int) ((LevelEditor.getTouchX() - LevelEditor.getPlayingField().left) / (LevelEditor.getPlayingField().width() / (double) LevelEditor.getTilesInLevel()));
            int boxY = (int) ((LevelEditor.getTouchY() - LevelEditor.getPlayingField().top) / (LevelEditor.getPlayingField().height() / (double) LevelEditor.getTilesInLevel()));
            if(boxX!=0 && boxX != LevelEditor.getTilesInLevel()-1 && boxY != 0 && boxY != LevelEditor.getTilesInLevel()-1) {
                if (LevelEditor.getTileAt(boxX * 30, boxY * 30) instanceof DumbBox) {
                    selectedItem = "box";
                }
                if (LevelEditor.getTileAt(boxX * 30, boxY * 30) instanceof DumbCrate) {
                    selectedItem = "crate";
                }
                if (LevelEditor.getTileAt(boxX * 30, boxY * 30) instanceof DumbDoubleCrate) {
                    if (((DumbDoubleCrate) LevelEditor.getTileAt(boxX * 30, boxY * 30)).getPosition() == 1) {
                        selectedItem = "doubleCrate2";
                    } else {
                        selectedItem = "doubleCrate";
                    }

                }
                if (LevelEditor.getTileAt(boxX * 30, boxY * 30) instanceof DumbEmptyCrate) {
                    selectedItem = "emptyCrate";
                }
                if (LevelEditor.getTileAt(boxX * 30, boxY * 30) instanceof DumbSpike) {
                    if (((DumbSpike) LevelEditor.getTileAt(boxX * 30, boxY * 30)).getPosition() == 1) {
                        selectedItem = "spike";
                    } else if (((DumbSpike) LevelEditor.getTileAt(boxX * 30, boxY * 30)).getPosition() == 2) {
                        selectedItem = "spike2";
                    } else if (((DumbSpike) LevelEditor.getTileAt(boxX * 30, boxY * 30)).getPosition() == 3) {
                        selectedItem = "spike3";
                    } else {
                        selectedItem = "spike4";
                    }
                }
                if (LevelEditor.getTileAt(boxX * 30, boxY * 30) instanceof DumbWall) {
                    selectedItem = "wall";
                }

                difX = -(LevelEditor.getPlayingField().width() / LevelEditor.getTilesInLevel()) / 2;
                difY = -(LevelEditor.getPlayingField().height() / LevelEditor.getTilesInLevel()) / 2;

                LevelEditor.removeTile(boxX * 30, boxY * 30);
            }
        }
        if(box.contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            selectedItem = "box";
            difX = box.left-LevelEditor.getTouchX();
            difY = box.top-LevelEditor.getTouchY();
        }
        if(crate.contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            selectedItem = "crate";
            difX = crate.left-LevelEditor.getTouchX();
            difY = crate.top-LevelEditor.getTouchY();
        }
        if(doubleCrate.contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            selectedItem = "doubleCrate";
            difX = doubleCrate.left-LevelEditor.getTouchX();
            difY = doubleCrate.top-LevelEditor.getTouchY();
        }
        if(doubleCrate2.contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            selectedItem = "doubleCrate2";
            difX = doubleCrate2.left-LevelEditor.getTouchX();
            difY = doubleCrate2.top-LevelEditor.getTouchY();
        }
        if(wall.contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            selectedItem = "wall";
            difX = wall.left-LevelEditor.getTouchX();
            difY = wall.top-LevelEditor.getTouchY();
        }
        if(emptyCrate.contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            selectedItem = "emptyCrate";
            difX = emptyCrate.left-LevelEditor.getTouchX();
            difY = emptyCrate.top-LevelEditor.getTouchY();
        }
        if(spike.contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            selectedItem = "spike";
            difX = spike.left-LevelEditor.getTouchX();
            difY = spike.top-LevelEditor.getTouchY();
        }
        if(spike2.contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            selectedItem = "spike2";
            difX = spike2.left-LevelEditor.getTouchX();
            difY = spike2.top-LevelEditor.getTouchY();
        }
        if(spike3.contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            selectedItem = "spike3";
            difX = spike3.left-LevelEditor.getTouchX();
            difY = spike3.top-LevelEditor.getTouchY();
        }
        if(spike4.contains(LevelEditor.getTouchX(),LevelEditor.getTouchY())){
            selectedItem = "spike4";
            difX = spike4.left-LevelEditor.getTouchX();
            difY = spike4.top-LevelEditor.getTouchY();
        }
    }

    public int getSize() {
        return LevelEditor.getTilesInLevel();
    }

    public void generateBorder(){
        for(int i=0;i<LevelEditor.getTilesInLevel();i++){
            LevelEditor.addTile(new DumbWall(30*i,0,wallImgRaw));
        }
        for(int i=0;i<LevelEditor.getTilesInLevel();i++){
            LevelEditor.addTile(new DumbWall(30*i,30*(LevelEditor.getTilesInLevel()-1),wallImgRaw));
        }
        for(int i=1;i<LevelEditor.getTilesInLevel()-1;i++){
            LevelEditor.addTile(new DumbWall(0,30*i,wallImgRaw));
        }
        for(int i=1;i<LevelEditor.getTilesInLevel()-1;i++){
            LevelEditor.addTile(new DumbWall(30*(LevelEditor.getTilesInLevel()-1),30*i,wallImgRaw));
        }
    }

    public void sizeChange(boolean big){
        int oldTiles = LevelEditor.getTilesInLevel();
        if(big) {
            LevelEditor.changeSize(1);
        } else {
            LevelEditor.changeSize(-1);
        }
        LevelEditor.removeTile(30*(oldTiles-1),30*(oldTiles-1));
        for(int i=0;i<LevelEditor.getTilesInLevel()+1;i++)
            LevelEditor.removeTile(30*i,0);
        for(int i=0;i<LevelEditor.getTilesInLevel()+1;i++)
            LevelEditor.removeTile(0,30*i);
        for(int i=1;i<LevelEditor.getTilesInLevel();i++)
            LevelEditor.removeTile(30*i,30*(oldTiles-1));
        for(int i=1;i<LevelEditor.getTilesInLevel();i++)
            LevelEditor.removeTile(30*(oldTiles-1),30*i);
        for(int i=1;i<LevelEditor.getTilesInLevel();i++){
            for(int r=1;r<LevelEditor.getTilesInLevel();r++){
                Tile t = LevelEditor.getTileAt(30*i,30*r);
                LevelEditor.removeTile(30*i,30*r);
                if(t instanceof DumbBox)
                LevelEditor.addTile(new DumbBox(t.getX(),t.getY(),boxImgRaw));
                if(t instanceof DumbCrate && t.getX()<(LevelEditor.getTilesInLevel()-1)*30 && t.getY()<(LevelEditor.getTilesInLevel()-1)*30)
                    LevelEditor.addTile(new DumbCrate(t.getX(),t.getY(),crateImgRaw));
                if(t instanceof DumbWall && t.getX()<(LevelEditor.getTilesInLevel()-1)*30 && t.getY()<(LevelEditor.getTilesInLevel()-1)*30)
                    LevelEditor.addTile(new DumbWall(t.getX(),t.getY(),wallImgRaw));
                if(t instanceof DumbEmptyCrate && t.getX()<(LevelEditor.getTilesInLevel()-1)*30 && t.getY()<(LevelEditor.getTilesInLevel()-1)*30)
                    LevelEditor.addTile(new DumbEmptyCrate(t.getX(),t.getY(),emptyCrateImgRaw));
                if(t instanceof DumbSpike && t.getX()<(LevelEditor.getTilesInLevel()-1)*30 && t.getY()<(LevelEditor.getTilesInLevel()-1)*30)
                    LevelEditor.addTile(new DumbSpike(t.getX(),t.getY(),((DumbSpike) t).getPosition(),spikeImgRaw));
                if(t instanceof DumbDoubleCrate && ((DumbDoubleCrate) t).getPosition()==1 && t.getX()<(LevelEditor.getTilesInLevel()-2)*30 && t.getY()<(LevelEditor.getTilesInLevel()-1)*30)
                    LevelEditor.addTile(new DumbDoubleCrate(t.getX(), t.getY(), 1, doubleCrate2Img));
                if(t instanceof DumbDoubleCrate && ((DumbDoubleCrate) t).getPosition()==2 && t.getY()<(LevelEditor.getTilesInLevel()-2)*30 && t.getX()<(LevelEditor.getTilesInLevel()-1)*30){
                    LevelEditor.addTile(new DumbDoubleCrate(t.getX(),t.getY(),2,doubleCrate1Img));
                    Log.e("g",String.valueOf(t.getX())+","+String.valueOf((LevelEditor.getTilesInLevel()-1)*30));
                }

            }
        }
        generateBorder();
    }
}
