package com.example.aidan.tilegame.tiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.aidan.tilegame.Panel;
import com.example.aidan.tilegame.Tile;

public class Spike extends Tile {
    private int direction;
    //1: ->
    //2: |
    //3 <-
    //4  ^
    private Bitmap scaledTexture;

    public Spike(int xPos, int yPos,int direction,Bitmap img) {
        super(xPos, yPos,img);
        this.direction = direction;
        scaledTexture = Bitmap.createScaledBitmap(super.getTexture(),(int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),(int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),false);
    }
    public void paint(Canvas canvas, Paint paint){
        canvas.save();
        canvas.rotate((direction-1)*90,super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left+((int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()))/2,
                (super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top+((int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()))/2));
        canvas.drawBitmap(scaledTexture,super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left,super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,paint);
        canvas.restore();
    }

    @Override
    public void pushLeft() {
    }

    @Override
    public void pushRight() {
    }

    @Override
    public void pushUp() {
    }

    @Override
    public void pushDown() {
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public void updateSize() {
        scaledTexture = Bitmap.createScaledBitmap(super.getTexture(),(int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),(int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),false);
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }
    @Override
    public Bitmap getScaledTexture() {
        return scaledTexture;
    }
}
