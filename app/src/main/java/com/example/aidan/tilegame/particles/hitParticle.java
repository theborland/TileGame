package com.example.aidan.tilegame.particles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.example.aidan.tilegame.Panel;
import com.example.aidan.tilegame.ProgressSaver;

import java.util.ArrayList;

public class hitParticle extends Particle {
    private ArrayList<Bitmap> clouds = new ArrayList<>();
    private int angle;
    public hitParticle(int x, int y,int angle) {
        super(x, y, 0);
        this.angle = angle;
        for(int i=1;i<9;i++) {
            clouds.add(Bitmap.createScaledBitmap(ProgressSaver.getCloud(i),
                    (int)(Panel.getPlayingField().height()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),
                    (int)(Panel.getPlayingField().width()/Panel.getTilesInLevel()*Panel.getSizeMultiplier()),
                    false));
        }
    }

    @Override
    public void paint(Canvas canvas, Paint paint) {
        if(super.getTime()>=1) {
            paint.reset();
            canvas.save();
            Bitmap cloud = clouds.get((int) super.getTime() - 1);
            canvas.rotate((angle - 1) * 90, cloud.getWidth() / 2 + super.getX(), cloud.getHeight() / 2 + super.getY());
            canvas.drawBitmap(cloud, super.getX()+cloud.getWidth()/10, super.getY(), paint);
//
//            switch(angle){
//                case 1:
//                    canvas.drawBitmap(cloud, super.getX()+cloud.getWidth()/2, super.getY(), paint);
//                    break;
//                case 2:
//                    canvas.drawBitmap(cloud, super.getX(), super.getY()+cloud.getHeight()/2, paint);
//                    break;
//                case 3:
//                    canvas.drawBitmap(cloud, super.getX()+cloud.getWidth()/2, super.getY(), paint);
//                    break;
//                case 4:
//                    canvas.drawBitmap(cloud, super.getX(), super.getY()-cloud.getHeight()/211`  , paint);
//                    break;
//            }

            canvas.restore();
        }
        super.setTime(super.getTime() + 32.0 / Panel.getFps());
    }

    @Override
    public boolean isDone() {
        return super.getTime()>8;
    }
}
