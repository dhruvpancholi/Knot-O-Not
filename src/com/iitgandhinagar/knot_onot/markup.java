package com.iitgandhinagar.knot_onot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.webkit.WebView;

public class markup extends WebView{
	
	Bitmap background;

	public markup(Context context) {
		super(context);
		background = BitmapFactory.decodeResource(context.getResources(),R.drawable.titlebackground);
		background=Bitmap.createScaledBitmap(background, TitleView.screenW, TitleView.screenH, false);
	}
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(background, 0, 0, null);loadUrl("file:///android_asset/test.html");
	}

}
