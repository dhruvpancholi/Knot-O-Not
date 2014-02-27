package com.iitgandhinagar.knot_onot;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

public class GoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		WebView webView = new WebView(this);
		webView.loadUrl("file:///android_asset/gameover.html");
	    webView.setKeepScreenOn(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(webView);
//		GoView gview = new GoView(this);
//		gview.setKeepScreenOn(true);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(gview);
		super.onCreate(savedInstanceState);
	}
}
