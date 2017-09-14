package com.example.aidan.tilegame.tiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.aidan.tilegame.Overlay;
import com.example.aidan.tilegame.Panel;
import com.example.aidan.tilegame.ParticleManager;
import com.example.aidan.tilegame.Tile;
import com.example.aidan.tilegame.particles.dissolveParticle;
import com.example.aidan.tilegame.particles.hitParticle;


public class EmptyCrate extends Tile {
    private double oldX,oldY,moveSpeed;
    private boolean dead = false;
    private boolean inMotion=true;
    private Bitmap scaledTexture;
    public EmptyCrate(int xPos, int yPos,Bitmap img) {
        super(xPos, yPos,img);
        oldX=xPos;
        oldY=yPos;
        scaledTexture = Bitmap.createScaledBitmap(super.getTexture(),(int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),(int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),false);
    }
    public boolean isMoving(){
        return !((int) oldX == super.getX() && (int) oldY == super.getY());
    }

    @Override
    public void updateSize() {
        scaledTexture = Bitmap.createScaledBitmap(super.getTexture(),(int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),(int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),false);
    }

    public void paint(Canvas canvas, Paint paint){
        canvas.drawBitmap(scaledTexture,(int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left,(int)oldY*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,paint);
    }
    public void update(){
        if(oldX<super.getX()){
            oldX+=moveSpeed*1000/Panel.getFps();
        }
        if(oldX>super.getX()){
            oldX-=moveSpeed*1000/Panel.getFps();
        }
        if(oldY<super.getY()){
            oldY+=moveSpeed*1000/Panel.getFps();
        }
        if(oldY>super.getY()){
            oldY-=moveSpeed*1000/Panel.getFps();
        }
        if(Panel.isSpike((int)oldX,(int)oldY)){
            if(!dead){
                dissolveParticle p = new dissolveParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, (int)oldY*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,this);
            }
            dead=true;
            Panel.setStatus("reset");
            Overlay.scalingReset(1);
        }
        if(Math.abs(oldY-super.getY())<=2){
            if(oldY!=super.getY() && inMotion && !dead){
                inMotion=false;
            }
            oldY=super.getY();
        }
        if(Math.abs(oldX-super.getX())<=2){
            if(oldX!=super.getX() && inMotion && !dead){
                inMotion=false;
            }
            oldX=super.getX();
        }
        if(Math.abs(oldY-super.getY())<=moveSpeed*1001.0/Panel.getFps() && oldY!=super.getY() && inMotion ){
            if(Panel.isTile(super.getX(), super.getY()+30, Wall.class)){
                hitParticle p = new hitParticle(super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,2);
            }
            if(Panel.isTile(super.getX(), super.getY()-30, Wall.class)){
                hitParticle p = new hitParticle(super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,4);
            }
        }
        if(Math.abs(oldX-super.getX())<=moveSpeed*1001.0/Panel.getFps() && oldX!=super.getX() && inMotion ){
            if(Panel.isTile(super.getX()+30, super.getY(), Wall.class)){
                hitParticle p = new hitParticle(super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,1);
            }
            if(Panel.isTile(super.getX()-30, super.getY(), Wall.class)){
                hitParticle p = new hitParticle(super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,3);
            }
        }
    }

    @Override
    public void pushLeft() {
        oldX=super.getX();
        oldY=super.getY();
        int i=1;
        while(!Panel.isTileBesides(super.getX()-i*30,super.getY(),Box.class)){
            i++;
        }
        super.setX(super.getX()-(i-1)*30);
        moveSpeed = Math.abs((oldX-super.getX())/100.0);
        inMotion=true;
    }

    @Override
    public void pushRight() {
        oldX=super.getX();
        oldY=super.getY();
        int i=1;
        while(!Panel.isTileBesides(super.getX()+i*30,super.getY(),Box.class)){
            i++;
        }
        super.setX(super.getX()+(i-1)*30);
        moveSpeed = Math.abs((oldX-super.getX())/100.0);
        inMotion=true;
    }
    @Override
    public void pushUp() {
        oldX=super.getX();
        oldY=super.getY();
        int i=1;
        while(!Panel.isTileBesides(super.getX(),super.getY()-i*30,Box.class)){
            i++;
        }
        super.setY(super.getY()-(i-1)*30);
        moveSpeed = Math.abs((oldY-super.getY())/100.0);
        inMotion=true;
    }

    @Override
    public void pushDown() {
        oldX=super.getX();
        oldY=super.getY();
        int i=1;
        while(!Panel.isTileBesides(super.getX(),super.getY()+i*30,Box.class)){
            i++;
        }
        super.setY(super.getY()+(i-1)*30);
        moveSpeed = Math.abs((oldY-super.getY())/100.0);
        inMotion=true;
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public Bitmap getScaledTexture() {
        return scaledTexture;
    }
}
