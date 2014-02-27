package com.iitgandhinagar.knot_onot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class GoView extends View {
	private Context myContext;
	private Bitmap gameover;
	private Bitmap play;
	private Bitmap stop;
	private int screenW = TitleView.screenW;
	private int screenH = TitleView.screenH;

	public GoView(Context context) {
		super(context);
		myContext = context;
		gameover = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.gameover);
		gameover = Bitmap.createScaledBitmap(gameover, screenW, screenH, false);
		play = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.play);
		play = Bitmap.createScaledBitmap(play, (int) 0.1 * screenW, (int) 0.1
				* screenW, false);
		stop = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.stop);
		stop = Bitmap.createScaledBitmap(stop, (int) 0.1 * screenW, (int) 0.1
				* screenW, false);
	}

	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(gameover, 0, 0, null);
		canvas.drawBitmap(play, (int) 0.7 * screenH, (int) 0.3 * screenW, null);
		canvas.drawBitmap(stop, (int) 0.7 * screenH, (int) 0.6 * screenW, null);

	}

	public boolean onTouchEvent(MotionEvent event) {
		int eventAction = event.getAction();
		float X = event.getX();
		float Y = event.getY();
		switch (eventAction) {
		case MotionEvent.ACTION_DOWN:
			if (Y > 0.7 * screenH && Y < 0.7 * screenH + 0.1 * screenW
					&& X > 0.3 * screenW && X < 0.4 * screenW) {
				Intent gameIntent = new Intent(myContext, Level1Activity.class);
				myContext.startActivity(gameIntent);
			}
			if (Y > 0.7 * screenH && Y < 0.7 * screenH + 0.1 * screenW
					&& X > 0.6 * screenW && X < 0.7 * screenW) {
				Intent gameIntent = new Intent(myContext, TitleActivity.class);
				myContext.startActivity(gameIntent);
			}

			break;
		}
		return true;
	}

}
