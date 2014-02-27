package com.iitgandhinagar.knot_onot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class TitleActivity extends Activity {
	public static List<Activity> ActivityList = new LinkedList<Activity>();
/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
		
	}*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityList.add(this);
		TitleView cview = new TitleView(this);
		cview.setKeepScreenOn(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(cview);
/*		String filename = "myfile";
		File file = new File(this.getExternalFilesDir(null), filename);
		String string = "Hello world!";
		FileOutputStream outputStream;
		try {
			  outputStream = openFileOutput(filename, Context.MODE_APPEND);
			  outputStream.write(string.getBytes("UTF-8"));
			  outputStream.close();
			  Log.v("File", "Created the file"+file.getPath());
			} catch (Exception e) {
			  e.printStackTrace();
			  Log.v("File", "Unable to create the file");
			}*/
		writeToExternalStoragePublic("myFile.txt", 143);
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_title, menu);
		return true;
	}*/
	public void writeToExternalStoragePublic(String filename, int content) {
	    String packageName = this.getPackageName();
	    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/" + packageName + "/files/";
//	    if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
	        try {
	            boolean exists = (new File(path)).exists();
	            if (!exists) {
	                new File(path).mkdirs();
	            }
	            // Open output stream
	            FileOutputStream fOut = new FileOutputStream(path + filename,true);
	            // write integers as separated ascii's
	            fOut.write((Integer.valueOf(content).toString() + " ").getBytes());
	            fOut.write((Integer.valueOf(content).toString() + " ").getBytes());
	            // Close output stream
	            fOut.flush();
	            fOut.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    //}
	}

}
