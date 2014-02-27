package com.iitgandhinagar.knot_onot;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;


public class Bubble {
	private float X, Y, VelocityX, VelocityY, Diameter;
	private int SegmentId,SegmentType;
	private Bitmap BallGraphic;
	private boolean Visible=true;
	private float TempX,TempY;
	private int ConnectedTo;
	private float BoundaryX, BoundaryY;
	private int ID;
	private boolean Connected=true, Cuttable=false;
	private float DistanceBetweenConnectedBubble;
	List<Bitmap> BallG = new LinkedList<Bitmap>();
	int BallRotationTracker=0;
	Random rand;
	int BubbleType;
	
	
	public boolean isCuttable() {
		return Cuttable;
	}

	public void setCuttable(boolean cuttable) {
		Cuttable = cuttable;
	}

	public float getDistanceBetweenConnectedBubble() {
		return DistanceBetweenConnectedBubble;
	}

	public void setDistanceBetweenConnectedBubble(
			float distanceBetweenConnectedBubble) {
		DistanceBetweenConnectedBubble = distanceBetweenConnectedBubble;
	}

	public boolean isConnected() {
		return Connected;
	}

	public void setConnected(boolean connected) {
		Connected = connected;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean CheckBoundary(float x, float y){
		if (x>X-BoundaryX && x<X+BoundaryX && y>Y-BoundaryY && y<Y+BoundaryY) {
			return true;
		}
		return false;
	}

	public int getConnectedTo() {
		return ConnectedTo;
	}

	public void setConnectedTo(int connectedTo) {
		ConnectedTo = connectedTo;
	}

	public float getTempX() {
		return TempX;
	}


	public void setTempX(float tempX) {
		TempX = tempX;
	}


	public float getTempY() {
		return TempY;
	}


	public void setTempY(float tempY) {
		TempY = tempY;
	}


	public boolean isVisible() {
		return Visible;
	}


	public void setVisible(boolean visible) {
		Visible = visible;
	}


	public Bubble(int Dia, Context myContext, int type) {
		Bitmap ballGraphic = Bitmap.createBitmap(Dia, Dia,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(ballGraphic);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(myContext.getResources().getColor(R.color.bubble));
		paint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(Dia/2, Dia/2, Dia/2, paint);
		BubbleType=type;
		
		Bitmap background;
		if (BubbleType==0) {
			background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.bubble20);
			background=Bitmap.createScaledBitmap(background,Level1View.PIPE_WIDTH, Level1View.PIPE_WIDTH, false);
			canvas.drawBitmap(background, 0,0, paint);
			//ballGraphic = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.sady);
			//ballGraphic = Bitmap.createScaledBitmap(ballGraphic, 30, 30, false);
			this.setBallGraphic(ballGraphic);
			BallG.add(background);
			background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.bubble21);
			background=Bitmap.createScaledBitmap(background,Level1View.PIPE_WIDTH, Level1View.PIPE_WIDTH, false);
			canvas.drawBitmap(background, 0,0, paint);
			BallG.add(background);
			background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.bubble22);
			background=Bitmap.createScaledBitmap(background,Level1View.PIPE_WIDTH, Level1View.PIPE_WIDTH, false);
			canvas.drawBitmap(background, 0,0, paint);
			BallG.add(background);
			background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.bubble23);
			background=Bitmap.createScaledBitmap(background,Level1View.PIPE_WIDTH, Level1View.PIPE_WIDTH, false);
			canvas.drawBitmap(background, 0,0, paint);
			BallG.add(background);
			background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.bubble24);
			background=Bitmap.createScaledBitmap(background,Level1View.PIPE_WIDTH, Level1View.PIPE_WIDTH, false);
			canvas.drawBitmap(background, 0,0, paint);
			BallG.add(background);
		}
		else if (BubbleType==1) {
			background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.bubble10);
			background=Bitmap.createScaledBitmap(background,Level1View.PIPE_WIDTH, Level1View.PIPE_WIDTH, false);
			canvas.drawBitmap(background, 0,0, paint);
			//ballGraphic = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.sady);
			//ballGraphic = Bitmap.createScaledBitmap(ballGraphic, 30, 30, false);
			this.setBallGraphic(ballGraphic);
			BallG.add(background);
			background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.bubble11);
			background=Bitmap.createScaledBitmap(background,Level1View.PIPE_WIDTH, Level1View.PIPE_WIDTH, false);
			canvas.drawBitmap(background, 0,0, paint);
			BallG.add(background);
			background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.bubble12);
			background=Bitmap.createScaledBitmap(background,Level1View.PIPE_WIDTH, Level1View.PIPE_WIDTH, false);
			canvas.drawBitmap(background, 0,0, paint);
			BallG.add(background);
			background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.bubble13);
			background=Bitmap.createScaledBitmap(background,Level1View.PIPE_WIDTH, Level1View.PIPE_WIDTH, false);
			canvas.drawBitmap(background, 0,0, paint);
			BallG.add(background);
			background = BitmapFactory.decodeResource(myContext.getResources(),R.drawable.bubble14);
			background=Bitmap.createScaledBitmap(background,Level1View.PIPE_WIDTH, Level1View.PIPE_WIDTH, false);
			canvas.drawBitmap(background, 0,0, paint);
			BallG.add(background);
		}
		else {
			
		}
	}


	public float getX() {
		return X;
	}

	public void setX(float x) {
		X = x;
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		Y = y;
	}

	public float getVelocityX() {
		return VelocityX;
	}

	public void setVelocityX(float velocityX) {
		VelocityX = velocityX;
	}

	public float getVelocityY() {
		return VelocityY;
	}

	public void setVelocityY(float velocityY) {
		VelocityY = velocityY;
	}

	public float getDiameter() {
		return Diameter;
	}

	public void setDiameter(float diameter) {
		Diameter = diameter;
	}

	public int getSegmentId() {
		return SegmentId;
	}

	public void setSegmentId(int segmentId) {
		SegmentId = segmentId;
	}

	public int getSegmentType() {
		return SegmentType;
	}


	public void setSegmentType(int segmentType) {
		SegmentType = segmentType;
	}


	public Bitmap getBallGraphic() {
		rand=new Random();
		if (BallRotationTracker%2==0) {
				switch (rand.nextInt(5)) {
				case 0:
					BallGraphic=BallG.get(0);
					break;
				case 1:
					BallGraphic=BallG.get(1);
					break;
				case 2:
					BallGraphic=BallG.get(2);
					break;
				case 3:
					BallGraphic=BallG.get(3);
					break;
				case 4:
					BallGraphic=BallG.get(4);
					break;

				default:
					break;
				}
			}
		
		BallRotationTracker++;
		return BallGraphic;
	}

	public void setBallGraphic(Bitmap ballGraphic) {
		BallGraphic = ballGraphic;
	}

	public int getType() {
		return BubbleType;
	}

	public void setType(int type) {
		BubbleType=type;
		
	}


}
