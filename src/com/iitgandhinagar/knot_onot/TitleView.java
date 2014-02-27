package com.iitgandhinagar.knot_onot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;

public class TitleView extends View {

	Context myContext;
	public static int screenW=240, screenH=320;
	Bitmap bitmap;
	Intent gameIntent;
	int counter;
	private Bitmap playButtonUp;
	private Bitmap playButtonDown, background;
	boolean playButtonPressed;
	private Bitmap titleGraphic, new_bmp;
	static Paint textPaint;
	Bitmap Splash;
	private boolean visionButtonPressed = false;
	private static SoundPool sounds;
	private static int cutSound;

	public TitleView(Context context) {
		super(context);
		myContext = context;
		sounds = new SoundPool(5,
				AudioManager.STREAM_MUSIC, 0); 
				cutSound = sounds.load(myContext, R.raw.click, 1);
		bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.background1_bak);
		//bitmap=Bitmap.createScaledBitmap(bitmap, screenW, screenH, false);
		titleGraphic = BitmapFactory.decodeResource(getResources(),
				R.drawable.not);
		playButtonUp = BitmapFactory.decodeResource(getResources(),
				R.drawable.playup1);
		playButtonDown = BitmapFactory.decodeResource(getResources(),
				R.drawable.playdown1);
		background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.titlebackground);
		Splash=BitmapFactory.decodeResource(getResources(),R.drawable.splash1);
		background=Bitmap.createScaledBitmap(background, screenW, screenH, false);
		new_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.vision);
		//Rect bounds = new Rect();
		//Paint textPaint=new Paint();
		/*textPaint.setTextSize(20);
		
		textPaint.getTextBounds(Splash,0,Splash.length(),bounds);
		int height = bounds.height();
		int width = textPaint.measureText(text);*/
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (counter<4) {
			if (screenW>400 || screenH>600) {
				bitmap=Bitmap.createScaledBitmap(bitmap, screenW, screenH, false);
			}
			canvas.drawBitmap(bitmap, 0,0, null);
			canvas.drawBitmap(Splash,
					(screenW - Splash.getWidth()) / 2,
					(float) (screenH * 0.4), null);
		}
		try {
			if (counter==0) {
				Thread.sleep(1);
			}
			else if(counter<4) {
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		counter++;
		/*if (counter==4) {
			gameIntent = gameIntent = new Intent(myContext, TitleActivity.class);
			myContext.startActivity(gameIntent);
		}*/
		if (counter<=4) {
			invalidate();
		}
		if(counter>=4){
			background=Bitmap.createScaledBitmap(background, screenW, screenH, false);
			canvas.drawBitmap(background,0,0, null);
			canvas.drawBitmap(titleGraphic,
					(screenW - titleGraphic.getWidth()) / 2,
					(float) (screenH * 0.25), null);

			if (playButtonPressed) {
				canvas.drawBitmap(playButtonDown,
						(screenW - playButtonDown.getWidth()) / 2,
						(float) (screenH * 0.5), null);
			} else {
				canvas.drawBitmap(playButtonUp,
						(screenW - playButtonUp.getWidth()) / 2,
						(float) (screenH * 0.5), null);
			}
			canvas.drawBitmap(new_bmp, (screenW - new_bmp.getWidth()) / 2, (float) (screenH * 0.7), null);
		}
	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenW = w;
		screenH = h;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventAction = event.getAction();
		float X = event.getX();
		float Y = event.getY();

		switch (eventAction) {
		case MotionEvent.ACTION_DOWN:
			if ((X > (screenW - playButtonUp.getWidth()) / 2 && X < ((screenW - playButtonUp
					.getWidth()) / 2) + playButtonUp.getWidth())
					&& (Y > (int) (screenH * 0.5) && Y < (int) (screenH * 0.5)
							+ playButtonUp.getHeight())) {
				AudioManager audioManager = (AudioManager)myContext.getSystemService(Context.AUDIO_SERVICE);
	     		float volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	     		sounds.play(cutSound, volume, volume, 1, 0,1);
				playButtonPressed = true;
			}
			if ((X > (screenW -new_bmp.getWidth()) / 2 && X < ((screenW - new_bmp
					.getWidth()) / 2) + new_bmp.getWidth())
					&& (Y > (int) (screenH * 0.7) && Y < (int) (screenH * 0.7)
							+ new_bmp.getHeight())) {
				AudioManager audioManager = (AudioManager)myContext.getSystemService(Context.AUDIO_SERVICE);
	     		float volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	     		sounds.play(cutSound, volume, volume, 1, 0,1);
				visionButtonPressed  = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if (playButtonPressed) {
				Intent gameIntent = new Intent(myContext, Level1Activity.class);
				myContext.startActivity(gameIntent);
			}
			playButtonPressed = false;
			if (visionButtonPressed) {
				Intent gameIntent = new Intent(myContext, InfoActivity.class);
				myContext.startActivity(gameIntent);
			}
			visionButtonPressed = false;
			break;
		}
		invalidate();
		return true;

	}

}
