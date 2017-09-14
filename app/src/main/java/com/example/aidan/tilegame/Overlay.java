package com.example.aidan.tilegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;


public class Overlay {
    private static int scaling,targetX,targetY,targetSize;
    public static void paint(Canvas canvas, Paint paint){

        if(Panel.getStatus().equals("over")) {
            scaling-=1000/Panel.getFps();
            if (scaling <= targetSize) {
                if (scaling > targetSize-254) {
                    paint.setColor(Color.BLACK);
                    paint.setAlpha(Math.abs(scaling-targetSize));
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
                } else {
                    Panel.setStatus("overlayFadeout");
                    scaling = 255;
                    Panel.playAgain();
                }
            }
            paint.setColor(Color.BLACK);
            Path path = new Path();
            path.lineTo(0,0);
            path.lineTo(0,Panel.getScreenHeight());
            path.lineTo(Panel.getScreenWidth(),Panel.getScreenHeight());
            path.lineTo(Panel.getScreenWidth(),0);
            path.addCircle((float) (targetX), (float) (targetY),Math.max(scaling,targetSize), Path.Direction.CW);
            canvas.drawPath(path, paint);
        }

        if(Panel.getStatus().equals("reset")){
            scaling=Math.min(scaling,255);
            scaling+=1000/Panel.getFps();
            if(scaling>255){
                scaling=255;
                Panel.setStatus("overlayFadeout");
                Panel.playAgain();
            } else {
                paint.setColor(Color.BLACK);
                paint.setAlpha(Math.max(0,scaling));
                canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
            }
        }
        if(Panel.getStatus().equals("overlayFadeout")){
            scaling=Math.min(scaling,255);
            if(scaling<=1){
                Panel.setStatus("playing");
            }
            paint.setColor(Color.BLACK);
            paint.setAlpha(Math.max(0,scaling));
            canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
            scaling-=1000/Panel.getFps();
        }
    }


    public static void setTarget(int targetX, int targetY, int targetSize) {
        Overlay.targetX=targetX;
        Overlay.targetY=targetY;
        Overlay.targetSize=targetSize;
        scaling = (int)Math.hypot(Panel.getScreenHeight(),Panel.getScreenWidth())/2+1;

    }


    public static void scalingReset(int i) {
        scaling = i;

    }
}
