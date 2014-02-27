package com.iitgandhinagar.knot_onot;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Level1Activity extends Activity {

	MediaPlayer splash;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TitleActivity.ActivityList.add(this);
		Level1View cview = new Level1View(this);
		cview.setKeepScreenOn(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(cview);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		splash.release();
	}
	@Override
	protected void onPause() {
		super.onPause();
		splash.pause();
		this.finish();
	}
	@Override
	protected void onResume() {
		super.onResume();
		splash=MediaPlayer.create(this,R.raw.bg);
		splash.start();
		splash.isLooping();
	}
	
}
