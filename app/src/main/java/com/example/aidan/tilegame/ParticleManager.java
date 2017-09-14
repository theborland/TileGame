package com.example.aidan.tilegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.aidan.tilegame.particles.Particle;

import java.util.ArrayList;

public class ParticleManager {
    private static ArrayList<Particle> particles = new ArrayList<Particle>();
    public static void paint(Canvas canvas, Paint paint) {
        for(Particle p:particles){
            p.paint(canvas,paint);
            //remove PARTILES THAT R DED
        }
        for(int p=0;p<particles.size();p++){
            if(particles.get(p).isDone()){
                particles.remove(p);
                p--;
            }
        }
    }

    public static void addParticle(Particle particle) {
        particles.add(particle);
    }
}
