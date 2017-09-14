package com.example.aidan.tilegame;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.aidan.tilegame.levelEditor.LevelEditor;
import com.example.aidan.tilegame.levelEditor.SelectionBar;


public class SideBar {
    private float hoverForward = 1;
    private float hoverBack= 1;
    private float hoverMiddle= 1;
    private float hoverTopBack= 1;
    private float hoverTrash= 1;
    private Rect buttonTrash;
    private Rect buttonTopBack;
	private Rect buttonBack;
	private Rect buttonForward;
	private Rect buttonMiddle;
    private int boxSize;
    private Bitmap arrow;
    private Context context;

    public SideBar(Rect playingField,int width, int height, Context context){
        this.context=context;
        if(Panel.firstPlay()){
            int imageHeight = Panel.getPlayingField().height()/5;
            arrow  = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.arrow),imageHeight,(int)(imageHeight*.7),false);
        }
        if(height>width) {
            int bottomSpaceHeight = height - playingField.bottom;
            int topBottomBuffer = (height - playingField.bottom) / 8;
            int leftRightBuffer = width / 18;
            boxSize = bottomSpaceHeight - topBottomBuffer * 2;

            buttonMiddle = new Rect((width - boxSize) / 2, playingField.bottom + topBottomBuffer, (width - boxSize) / 2 + boxSize, playingField.bottom + topBottomBuffer + boxSize);
            buttonForward = new Rect(width - leftRightBuffer - boxSize, playingField.bottom + topBottomBuffer, width - leftRightBuffer, playingField.bottom + topBottomBuffer + boxSize);
            buttonBack = new Rect(leftRightBuffer, playingField.bottom + topBottomBuffer, leftRightBuffer + boxSize, playingField.bottom + topBottomBuffer + boxSize);
            buttonTopBack = new Rect(leftRightBuffer, topBottomBuffer, leftRightBuffer + boxSize, topBottomBuffer + boxSize);
            buttonTrash = new Rect(playingField.right-leftRightBuffer-boxSize, topBottomBuffer, playingField.right-leftRightBuffer, topBottomBuffer + boxSize);

        } else {
            int leftSpaceWidth = playingField.left;
            int leftRightBuffer = playingField.left / 8;
            int topBottomBuffer = height / 18;
            boxSize = leftSpaceWidth - leftRightBuffer * 2;

            buttonMiddle = new Rect(leftRightBuffer, (height - boxSize) / 2, leftRightBuffer + boxSize, (height - boxSize) / 2 + boxSize);
            buttonForward = new Rect(leftRightBuffer, topBottomBuffer,leftRightBuffer + boxSize, topBottomBuffer + boxSize);
            buttonBack = new Rect(leftRightBuffer, height-topBottomBuffer-boxSize, leftRightBuffer + boxSize, height-topBottomBuffer);
            buttonTopBack = new Rect(playingField.right+leftRightBuffer, height-topBottomBuffer-boxSize, playingField.right+leftRightBuffer + boxSize, height-topBottomBuffer);
            buttonTrash = new Rect(playingField.right+leftRightBuffer, topBottomBuffer, playingField.right+leftRightBuffer + boxSize, topBottomBuffer + boxSize);
        }
    }

    private int swipeDisplayValue=0;
    public void paint(Canvas canvas, Paint paint) {
        if(Panel.firstPlay() && Panel.getLevelPack().equals("default")){
            swipeDisplayValue+=300/Panel.getFps();
            paint.setAlpha((int)(Math.cos(Math.toRadians(swipeDisplayValue))*100)+105);
            canvas.drawBitmap(arrow,Panel.getPlayingField().centerX()-Panel.getPlayingField().height()/10-90+swipeDisplayValue,Panel.getPlayingField().centerY()-arrow.getHeight()/2,paint);
            paint.reset();
            if(swipeDisplayValue>180){
                swipeDisplayValue=0;
            }
        }

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(boxSize/4.66f);
//        paint.setStrokeCap(Paint.Cap.ROUND);
//        paint.setStrokeJoin(Paint.Join.ROUND);
        canvas.save();

        //triangle1
		canvas.scale(hoverForward/1.5f, hoverForward/1.5f, buttonForward.exactCenterX(), buttonForward.exactCenterY());
		paint.setColor(Color.argb(255,40,25,55));
		canvas.drawPath(triangle(buttonForward.centerX(),buttonForward.centerY(),boxSize/2.5f,false),paint);
		canvas.rotate((float)Math.toDegrees(hoverForward*11+1.5f), buttonForward.exactCenterX(), buttonForward.exactCenterY());
        if((Panel.getLevel()!=Panel.getMaxLevel() && Panel.getLevelPack().equals("default")) || ((Panel.getLevel()<getNextLevelId()-1) && Panel.getLevelPack().equals("custom"))) {
            paint.setColor(Color.argb(200, 255, 204, 0));
        } else {
            paint.setColor(Color.argb(200, 192, 192, 192));
        }
        paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(buttonForward,paint);
        canvas.restore();
        canvas.save();


        //triangle2
		canvas.scale(hoverBack/1.5f, hoverBack/1.5f, buttonBack.exactCenterX(), buttonBack.exactCenterY());
        paint.setColor(Color.argb(255,40,25,55));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawPath(triangle(buttonBack.centerX(),buttonBack.centerY(),boxSize/2.5f,true),paint);
		canvas.rotate((float)Math.toDegrees(hoverBack*-11-1.5f), buttonBack.exactCenterX(), buttonBack.exactCenterY());
        if(Panel.getLevel()>1) {
            paint.setColor(Color.argb(200, 255, 204, 0));
        } else {
            paint.setColor(Color.argb(200, 192, 192, 192));
        }
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(buttonBack,paint);
        canvas.restore();
        canvas.save();

        //home

        canvas.scale(hoverTopBack/1.5f, hoverTopBack/1.5f, buttonTopBack.exactCenterX(), buttonTopBack.exactCenterY());
        paint.setColor(Color.argb(255,40,25,55));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.save();
        canvas.rotate(90,buttonTopBack.centerX(),buttonTopBack.centerY());
        canvas.drawPath(triangle(buttonTopBack.centerX(),buttonTopBack.centerY(),boxSize/2.5f,true),paint);
        canvas.restore();
        paint.setColor(Color.argb(200, 255, 204, 0));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(buttonTopBack,paint);
        canvas.restore();
        canvas.save();

        //trash
        canvas.save();
        canvas.scale(hoverTrash/1.5f, hoverTrash/1.5f, buttonTrash.exactCenterX(), buttonTrash.exactCenterY());
        paint.setColor(Color.argb(255,40,25,55));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(trashPath(buttonTrash.centerX(),buttonTrash.centerY(),boxSize/3.5f),paint);
        if(Panel.getLevelPack().equals("custom") && getNextLevelId()>1) {
            paint.setColor(Color.argb(200, 255, 204, 0));
        } else {
            paint.setColor(Color.argb(200, 192, 192, 192));
        }
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(buttonTrash,paint);
        canvas.restore();


        //reset
        canvas.scale(hoverMiddle/1.5f, hoverMiddle/1.5f, buttonMiddle.exactCenterX(), buttonMiddle.exactCenterY());
        paint.setColor(Color.argb(255,40,25,55));
        paint.setStrokeWidth(boxSize/6.4f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(resetPath(buttonMiddle.centerX(),buttonMiddle.centerY(),boxSize/3.5f),paint);
        paint.setStrokeWidth(boxSize/8f);
        canvas.drawPath(triangle(buttonMiddle.centerX(),buttonMiddle.centerY()-boxSize/3.5f,boxSize/5,true),paint);
        paint.setColor(Color.argb(200,255,204,0));
        paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(buttonMiddle,paint);
        canvas.restore();
		update();
	}
	public void update() {
		if(buttonForward.contains(Panel.getTouchX(), Panel.getTouchY())){
			if(hoverForward<1.3){
				hoverForward+=(1.3-hoverForward)/100*1000/Panel.getFps()+.0001;
			}
		}
		else if(hoverForward>1){
			hoverForward-=.001*1000/Panel.getFps();
		}
		if(hoverForward<1){
			hoverForward=1;
		}
		if(hoverForward>1.3){
			hoverForward=1.3f;
		}
		if(buttonBack.contains(Panel.getTouchX(), Panel.getTouchY())){
			if(hoverBack<1.3){
				hoverBack+=(1.3-hoverBack)/100*1000/Panel.getFps()+.0001;
			}
		}
		else if(hoverBack>1){
			hoverBack-=.001*1000/Panel.getFps();
		}
		if(hoverBack<1){
			hoverBack=1;
		}
		if(hoverBack>1.3){
			hoverBack=1.3f;
		}
        if(buttonTopBack.contains(Panel.getTouchX(), Panel.getTouchY())){
			if(hoverTopBack<1.3){
                hoverTopBack+=(1.3-hoverTopBack)/100*1000/Panel.getFps()+.0001;
			}
		} else if(hoverTopBack>1){
            hoverTopBack-=.001*1000/Panel.getFps();
        }
        if(hoverTopBack<1){
            hoverTopBack=1;
        }
        if(hoverTopBack>1.3){
            hoverTopBack=1.3f;
        }
        if(buttonTrash.contains(Panel.getTouchX(), Panel.getTouchY())){
            if(hoverTrash<1.3){
                hoverTrash+=(1.3-hoverTrash)/100*1000/Panel.getFps()+.0001;
            }
        } else if(hoverTrash>1){
            hoverTrash-=.001*1000/Panel.getFps();
        }
        if(hoverTrash<1){
            hoverTrash=1;
        }
        if(hoverTrash>1.3){
            hoverTrash=1.3f;
        }
        if(buttonMiddle.contains(Panel.getTouchX(), Panel.getTouchY())){
            if(hoverMiddle<1.3){
                hoverMiddle+=(1.3-hoverMiddle)/100*1000/Panel.getFps()+.0001;
            }
        }
		else if(hoverMiddle>1){
			hoverMiddle-=.001*1000/Panel.getFps();
		}
		if(hoverMiddle<1){
			hoverMiddle=1;
		}
		if(hoverMiddle>1.3){
			hoverMiddle=1.3f;
		}
	}

    public void released() {
        if(Panel.getStatus().equals("playing")) {
            if (buttonMiddle.top<Panel.getTouchY() && buttonMiddle.bottom>Panel.getTouchY() && buttonMiddle.left<Panel.getTouchX() && buttonMiddle.right>Panel.getTouchX()) {
                Panel.setStatus("reset");
                Overlay.scalingReset(1);
            }
            if (buttonBack.contains(Panel.getTouchX(), Panel.getTouchY()) && Panel.getLevel()!=1) {
                Panel.setStatus("reset");
                Panel.levelChange(-1);
                Overlay.scalingReset(1);
            }
            if (buttonForward.contains(Panel.getTouchX(), Panel.getTouchY()) && !(Panel.getLevelPack().equals("default") && Panel.getLevel()==Panel.getMaxLevel()) && !(Panel.getLevelPack().equals("custom") && Panel.getLevel()>=getNextLevelId()-1)) {
                Panel.setStatus("reset");
                Panel.levelChange(1);
                Overlay.scalingReset(1);
            }
            if (buttonTopBack.contains(Panel.getTouchX(), Panel.getTouchY())) {
                Intent i = new Intent(context,HomeScreen.class);
                context.startActivity(i);
                ((AppCompatActivity)context).overridePendingTransition(R.anim.up_to_mid,R.anim.mid_to_down);
            }
            if (buttonTrash.contains(Panel.getTouchX(), Panel.getTouchY()) && !Panel.getLevelPack().equals("default")  && getNextLevelId()>1) {
                int lastLevel = getNextLevelId()-1;
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();
                for(int i=Panel.getLevel();i<lastLevel;i++){

                    editor.putString("customlevel"+i,settings.getString("customlevel"+(i+1),""));
                }
                editor.remove("customlevel"+lastLevel);

                editor.commit();
                Panel.setStatus("reset");
            }
        }
    }



	public static Path triangle(float x, float y, float length, boolean dir){
		Path triangle = new Path();
		if(dir){
            triangle.moveTo(x-length/2f, y);
			triangle.lineTo(x+length/1.8f, y+length/1.8f);
			triangle.lineTo(x+length/1.8f, y-length/1.8f);
            triangle.close();
        } else {
            triangle.moveTo(x+length/2f, y);
			triangle.lineTo(x-length/1.8f, y+length/1.8f);
			triangle.lineTo(x-length/1.8f, y-length/1.8f);
            triangle.close();
		}

		return triangle;
	}
	public Path resetPath(float x, float y,float length){
		Path curve = new Path();
		curve.moveTo(x-length, y-length);
		curve.lineTo(x-length, y+length);
		curve.lineTo(x+length, y+length);
		curve.lineTo(x+length, y-length);
		curve.lineTo(x+length/2, y-length);
		return curve;
	}

    public Path trashPath(float x, float y,float length){
        Path curve = new Path();
        curve.moveTo(x-length, y-length);
        curve.lineTo(x+length, y+length);
        curve.moveTo(x-length, y+length);
        curve.lineTo(x+length, y-length);
        return curve;
    }
    public int getNextLevelId() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        int i=1;
        while(!settings.getString("customlevel"+i, "").equals("")){
            i++;
        }
        return i;
    }
}
