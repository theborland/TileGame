package com.example.aidan.tilegame.tiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.aidan.tilegame.Panel;
import com.example.aidan.tilegame.Tile;

public class Wall extends Tile {
    private Bitmap scaledTexture;

    public Wall(int xPos, int yPos,Bitmap img) {
        super(xPos, yPos,img);
        scaledTexture = Bitmap.createScaledBitmap(super.getTexture(),(int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),(int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),false);

    }
    public void paint(Canvas canvas, Paint paint){
        canvas.drawBitmap(scaledTexture,super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left,super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,paint);
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
