package com.example.aidan.tilegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;


public class Particle {
    private int x,y,time;
    private int color;

    public Particle(int x,int y,int time,int color){
        this.x=x;
        this.y=y;
        this.time=time;
        this.color=color;
    }

    public void paint(Canvas canvas, Paint paint){
        paint.reset();
        paint.setColor(color);
        canvas.drawCircle(x,y,time,paint);
        if(Math.random()<.1){
            time-=1000/Panel.getFps();
        }
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getTime(){
        return time;
    }


    public int getColor(){
        return color;
    }
}
