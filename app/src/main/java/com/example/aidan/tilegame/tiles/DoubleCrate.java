package com.example.aidan.tilegame.tiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.aidan.tilegame.Panel;
import com.example.aidan.tilegame.ParticleManager;
import com.example.aidan.tilegame.Tile;
import com.example.aidan.tilegame.particles.dissolveParticle;
import com.example.aidan.tilegame.particles.hitParticle;


public class DoubleCrate extends Tile {
    private double oldX,oldY,moveSpeed;
    private boolean dead = false;
    private boolean inMotion=true;
    private Bitmap scaledTexture;

    private int position;
    //1 is sideways
    //2 is upright

    public DoubleCrate(int xPos, int yPos,int position,Bitmap img) {
        super(xPos, yPos, img);
        oldX = xPos;
        oldY = yPos;
        this.position = position;
        if (position == 1) {
            scaledTexture = Bitmap.createScaledBitmap(super.getTexture(), (int) (Panel.getPlayingField().width() / Panel.getTilesInLevel() * (2+(1-Panel.getSizeMultiplier())) * Panel.getSizeMultiplier()), (int) (Panel.getPlayingField().width() / Panel.getTilesInLevel() * Panel.getSizeMultiplier()), false);
        } else {
            scaledTexture = Bitmap.createScaledBitmap(super.getTexture(), (int) (Panel.getPlayingField().width() / Panel.getTilesInLevel() * Panel.getSizeMultiplier()), (int) (Panel.getPlayingField().width() / Panel.getTilesInLevel() * (2+(1-Panel.getSizeMultiplier())) * Panel.getSizeMultiplier()), false);

        }
    }

    public int getPosition() {
        return position;
    }

    public boolean isMoving(){
        return !((int) oldX == super.getX() && (int) oldY == super.getY());
    }

    @Override
    public void updateSize() {
        if (position == 1) {
            scaledTexture = Bitmap.createScaledBitmap(super.getTexture(), (int) (Panel.getPlayingField().width() / Panel.getTilesInLevel() * (2+(1-Panel.getSizeMultiplier())) * Panel.getSizeMultiplier()), (int) (Panel.getPlayingField().width() / Panel.getTilesInLevel() * Panel.getSizeMultiplier()), false);
        } else {
            scaledTexture = Bitmap.createScaledBitmap(super.getTexture(), (int) (Panel.getPlayingField().width() / Panel.getTilesInLevel() * Panel.getSizeMultiplier()), (int) (Panel.getPlayingField().width() / Panel.getTilesInLevel() * (2+(1-Panel.getSizeMultiplier())) * Panel.getSizeMultiplier()), false);

        }
    }

    @Override
    public void paint(Canvas canvas, Paint paint) {
        canvas.drawBitmap(scaledTexture,(int)oldX * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().left, (int)oldY * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().top, paint);
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
        if(position==1 && (Panel.isSpike((int)oldX, (int)oldY)) || Panel.isSpike((int)oldX+30, (int)oldY)){
            if(!dead){
                dissolveParticle p = new dissolveParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, (int)oldY*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,this);
                dissolveParticle p2 = new dissolveParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, (int)oldY*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,this);
            }
            dead=true;
        }
        if(position==2 && (Panel.isSpike((int)oldX, (int)oldY)) || Panel.isSpike((int)oldX, (int)oldY+30)){
            if(!dead){
                dissolveParticle p = new dissolveParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, (int)oldY*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,this);
                dissolveParticle p2 = new dissolveParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, (int)oldY*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,this);
            }
            dead=true;
        }
//        if(Math.abs(oldY-super.getY())<=2){
//            if(oldY!=super.getY() && inMotion && !dead){
//                if(position==2 && (Panel.isTile(super.getX(), super.getY()-30, Wall.class) || Panel.isTile(super.getX(), super.getY()+60, Wall.class))){
//                    //ParticleManager.newParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left+15, super.getY()+15, 3, "Hit");
//                    //ParticleManager.newParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left+15, super.getY()+45, 3, "Hit");
//                }else if(Panel.isTile(super.getX(), super.getY()-30, Wall.class) || Panel.isTile(super.getX(), super.getY()+30, Wall.class)){
//                    //ParticleManager.newParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left+15, super.getY()+15, 3, "Hit");
//                    //ParticleManager.newParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left+45, super.getY()+15, 3, "Hit");
//                }
//                inMotion=false;
//            }
//            oldY=super.getY();
//        }
//        if(Math.abs(oldX-super.getX())<=2){
//            if(oldX!=super.getX() && inMotion && !dead){
//                if(position==2 && (Panel.isTile((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left-30, super.getY(), Wall.class) || Panel.isTile(super.getX()+30, super.getY(), Wall.class))){
//                    //ParticleManager.newParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left+15, super.getY()+15, 3, "Hit");
//                    //ParticleManager.newParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left+15, super.getY()+45, 3, "Hit");
//                }else if(Panel.isTile(super.getX(), super.getY()-30, Wall.class) || Panel.isTile(super.getX(), super.getY()+60, Wall.class)){
//                    //ParticleManager.newParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left+15, super.getY()+15, 3, "Hit");
//                    //ParticleManager.newParticle((int)oldX*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left+45, super.getY()+15, 3, "Hit");
//                }
//                inMotion=false;
//            }
//            oldX=super.getX();
//        }
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
            if(position==2){
                if(Panel.isTile(super.getX(), super.getY()+60, Wall.class)){
                    hitParticle p = new hitParticle(super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, (super.getY()+30)*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,2);
                }
                if(Panel.isTile(super.getX(), super.getY()-30, Wall.class)){
                    hitParticle p = new hitParticle(super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,4);
                }
            }
            if(position==1){
                if(Panel.isTile(super.getX(), super.getY()+30, Wall.class)){
                    hitParticle p = new hitParticle(super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,2);
                }
                if(Panel.isTile(super.getX()+30, super.getY()+30, Wall.class)){
                    hitParticle p = new hitParticle((super.getX()+30)*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,2);
                }

                if(Panel.isTile(super.getX(), super.getY()-30, Wall.class)){
                    hitParticle p = new hitParticle(super.getX()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,4);
                }
                if(Panel.isTile(super.getX()+30, super.getY()-30, Wall.class)){
                    hitParticle p = new hitParticle((super.getX()+30)*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().left, super.getY()*Panel.getPlayingField().height()/Panel.getTilesInLevel()/30+Panel.getPlayingField().top,4);
                }
            }
        }
        if(Math.abs(oldX-super.getX())<=moveSpeed*1001.0/Panel.getFps() && oldX!=super.getX() && inMotion ){
            if(position==1) {
                if (Panel.isTile(super.getX() + 60, super.getY(), Wall.class)) {
                    hitParticle p = new hitParticle((super.getX()+30) * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().left, super.getY() * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().top, 1);
                }
                if (Panel.isTile(super.getX() - 30, super.getY(), Wall.class)) {
                    hitParticle p = new hitParticle(super.getX() * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().left, super.getY() * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().top, 3);
                }
            }
            if(position==2) {
                if (Panel.isTile(super.getX() + 30, super.getY(), Wall.class)) {
                    hitParticle p = new hitParticle(super.getX() * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().left, super.getY() * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().top, 1);
                }
                if (Panel.isTile(super.getX() + 30, super.getY()+30, Wall.class)) {
                    hitParticle p = new hitParticle(super.getX() * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().left, (super.getY()+30) * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().top, 1);
                }

                if (Panel.isTile(super.getX() - 30, super.getY(), Wall.class)) {
                    hitParticle p = new hitParticle(super.getX() * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().left, super.getY() * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().top, 3);
                }
                if (Panel.isTile(super.getX() - 30, super.getY()+30, Wall.class)) {
                    hitParticle p = new hitParticle(super.getX() * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().left, (super.getY()+30) * Panel.getPlayingField().height() / Panel.getTilesInLevel() / 30 + Panel.getPlayingField().top, 3);
                }
            }
        }
    }

    @Override
    public void pushLeft() {
        oldX=super.getX();
        oldY=super.getY();
        int i=1;
        if(position == 1){
            while(!Panel.isSolidTile(super.getX()-i*30,super.getY())){
                i++;
            }
        } else {
            while(!Panel.isSolidTile(super.getX()-i*30,super.getY()) && !Panel.isSolidTile(super.getX()-i*30,super.getY()+30)){
                i++;
            }
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
        if(position == 1){
            while(!Panel.isSolidTile(super.getX()+i*30+30,super.getY())){
                i++;
            }
        } else {
            while(!Panel.isSolidTile(super.getX()+i*30,super.getY()) && !Panel.isSolidTile(super.getX()+i*30,super.getY()+30)){
                i++;
            }
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
        if(position == 1){
            while(!Panel.isSolidTile(super.getX(),super.getY()-i*30) && !Panel.isSolidTile(super.getX()+30,super.getY()-i*30)){
                i++;
            }
        } else {
            while(!Panel.isSolidTile(super.getX(),super.getY()-i*30)){
                i++;
            }
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
        if(position == 1){
            while(!Panel.isSolidTile(super.getX(),super.getY()+i*30) && !Panel.isSolidTile(super.getX()+30,super.getY()+i*30)){
                i++;
            }
        } else {
            while(!Panel.isSolidTile(super.getX(),super.getY()+i*30+30)){
                i++;
            }
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
