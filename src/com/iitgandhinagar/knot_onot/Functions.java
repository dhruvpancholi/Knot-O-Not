package com.iitgandhinagar.knot_onot;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Functions {
	static Paint paint;
	
	public Functions(){
		//paint.setStrokeWidth(3);
		//paint.setColor(Color.BLACK);
	}
	
	public static void resolveConnections(Canvas canvas, List<Bubble> list1, List<Bubble> list2){
		Bubble[] bubble=new Bubble[2];
		for (int i = 0; i < list1.size(); i++) {
			bubble[0]=list1.get(i);
			if (!bubble[0].isConnected()) {
				for (int j = 0; j < list2.size(); j++) {
					bubble[1]=list2.get(j);
					if (bubble[0].getType()==bubble[1].getType()) {
						if (Math.sqrt(Math.pow(bubble[0].getX()-bubble[1].getX(), 2)+Math.pow(bubble[0].getY()-bubble[1].getY(), 2))<Level1View.LOOP_DIAMETER) {
							bubble[0].setConnected(true);bubble[1].setConnected(true);
							bubble[0].setConnectedTo(j);bubble[1].setConnectedTo(i);
						}
					}
				}
			}
		}
	}
	public static void drawConnections(Canvas canvas, List<Bubble> list1, List<Bubble> list2){
		Bubble[] bubble=new Bubble[2];
		for (int i = 0; i < list1.size(); i++) {
			bubble[0]=list1.get(i);
			if (bubble[0].isConnected()) {
				bubble[1]=list2.get(bubble[0].getConnectedTo());
				if (bubble[1].isConnected()) {
					canvas.drawLine(bubble[0].getX(), bubble[0].getY(), bubble[1].getX(), bubble[1].getY(), paint);
				}
				
			}
		}
	}
}
