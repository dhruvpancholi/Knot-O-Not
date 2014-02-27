package com.iitgandhinagar.knot_onot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class InfoView extends View {

	private Context myContext;
	private Bitmap info_bmp;
	private float screenW = TitleView.screenW;
	private float screenH = TitleView.screenH;

	public InfoView(Context context) {
		super(context);
		myContext = context;
		info_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.vision_info);
		info_bmp = Bitmap.createScaledBitmap(info_bmp, (int)screenW, (int)screenH, false);
	}
	
	public void onDraw(Canvas canvas){
		canvas.drawBitmap(info_bmp, 0, 0, null);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		int eventAction = event.getAction();
		switch(eventAction){
		case MotionEvent.ACTION_DOWN:
			Intent gameIntent = new Intent(myContext, TitleActivity.class);
			myContext.startActivity(gameIntent);
			break;
		}
		return true;
	}

}
