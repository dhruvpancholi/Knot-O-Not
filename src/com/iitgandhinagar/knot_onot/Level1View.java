// This class will be painted on the screen as soon as the player hits the play button

package com.iitgandhinagar.knot_onot;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint({ "DrawAllocation", "ResourceAsColor" })
public class Level1View extends View {

	/**
	 * @param myContext
	 *            The context of the activity
	 */
	private static Context myContext;
	int posx = 10, posy = 10;
	static int screenW = TitleView.screenW;
	static int screenH = TitleView.screenH;
	public static int PIPE_WIDTH = screenW / 12;
	public static int LOOP_DIAMETER = (int) (screenW / 2.4);
	public static final float VELOCITY_CIRCUIT_0 = (float) screenH / 320;
	public static final float VELOCITY_CIRCUIT_1 = (float) ((float) screenH / (320));
	public static final float RESISTANCE_PER_UNIT_LENGTH = 1;
	public static final float STARTING_POTENTIAL_0 = 500 * screenW / 320;
	public static final float STARTING_POTENTIAL_1 = 500 * screenW / 320;
	public static float BUBBLE_RESISTANCE = 10;
	public static final float RECONNECT_DISTANCE = 3 * LOOP_DIAMETER / 4;
	public static final float RESISTANCE_TO_ADD_ON_CLICK = BUBBLE_RESISTANCE;
	public static final int SCORE_INCREMENT = 5;
	private boolean show_mission = true;

	static int GeneratorX = (int) (0.65 * LOOP_DIAMETER);
	static int GeneratorX1 = (int) (1.75 * LOOP_DIAMETER);

	// Random Generator
	Random rand = new Random();

	// To create an array of array list ArrayList<ArrayList<String>>

	private static SoundPool sounds;
	private static int cutSound, click;

	// To track the time of the frames
	int time = 0;

	// This list will track all the bubbles there on the screen
	public List<Bubble> BubbleList = new LinkedList<Bubble>();
	public List<Bubble> BubbleList1 = new LinkedList<Bubble>();

	// List of segments
	public static List<Segment> SegmentList = new LinkedList<Segment>();
	public static List<Segment> SegmentList1 = new LinkedList<Segment>();

	// This variable will assign the id to every new segment created
	static int SegmentIdAssign = 0;
	static int SegmentIdAssign1 = 0;

	// This variable will be used to increase the value of the Y position of the
	// bubble
	float posY = 0;

	// This is the background graphic of the level 1
	Bitmap Level1;

	/**
	 * This variable is used to assign the offset so as to place the pipes and
	 * loops in such a way that they fit in each other
	 */
	float multiFactor = (float) Math.cos(Math.asin((PIPE_WIDTH / 2)
			/ (LOOP_DIAMETER / 2)));

	// Used to universally change the styling of the object
	private static Paint paint;
	private Paint paint1;
	/**
	 * @param mission_bmp
	 *            The image which is used to display the aim of the game, when
	 *            the game activity starts
	 */
	private Bitmap mission_bmp;
	/**
	 * @param bubble
	 *            Temporary bubble for the creation of the bubble list and also
	 *            dealing with the bubble on the screen
	 */
	Bubble bubble;
	/**
	 * @param score
	 *            To track the score of the user
	 * @param lives
	 *            To track the lives of the user
	 */
	int score = 0, lives = 3;
	/**
	 * @param BubbleId
	 *            To track whether the bubble is in first or second path
	 */
	int[] BubbleId = new int[2];
	/**
	 * @param TimeCuttable
	 *            The time up till which the given thread is cuttable
	 */
	int TimeCuttable = 0;

	// Constructor will be used for the creation and initiation
	public Level1View(Context context) {
		super(context);

		myContext = context;

		paint = new Paint();
		paint.setAntiAlias(true);
		paint1 = new Paint();
		paint1.setAntiAlias(true);
		paint1.setColor(Color.RED);
		mission_bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.mission5);
		mission_bmp = Bitmap.createScaledBitmap(mission_bmp, screenW, screenH,
				false);

		sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		cutSound = sounds.load(myContext, R.raw.cut1, 1);
		click = sounds.load(myContext, R.raw.click, 1);

		// Creating the level graphics once
		Level1 = Level1View.createLevel1();

		// Setting Up Bubbles
		for (int i = 0; i < 1; i++) {
			generateBubble();
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {

		/**
		 * Drawing the background image to avoid the creation of dynamic objects
		 * at the time of drawing
		 */
		canvas.drawBitmap(Level1, 0, 0, paint);
		if (show_mission) {
			canvas.drawBitmap(mission_bmp, 0, 0, null);
			invalidate();
		}
		/**
		 * Game over :(
		 */
		else if (lives < 0) {
			Intent gameIntent = new Intent(myContext, GoActivity.class);
			myContext.startActivity(gameIntent);
		}
		/**
		 * Let the game begin ;)
		 */
		else {

			/**
			 * @param addX
			 *            X coordinate which is added or subtracted in the loop
			 *            with segment id 3 or 4
			 * 
			 * @param offsetY
			 *            Y distance of the bubble from the center of the circle
			 * @param dist
			 *            Distance of the bubble from the center of the circle
			 */
			float addX = 0, offsetY = 0, dist = 0;
			/**
			 * Number of bubbles in both the circuits are same so iterating
			 * through each of the loops
			 */
			for (int i = 0; i < BubbleList.size(); i++) {
				bubble = BubbleList.get(i);
				if (bubble.isVisible()) {
					if (bubble.getSegmentType() == 3
							|| bubble.getSegmentType() == 4) {
						offsetY = SegmentList.get(bubble.getSegmentId())
								.getStartPointY();
					}
					dist = bubble.getY() - offsetY - LOOP_DIAMETER / 2
							+ PIPE_WIDTH;
					addX = Segment.getX(bubble.getSegmentType(), dist);
					bubble.setTempX(bubble.getX() + addX);

					float distance = (float) Math
							.sqrt((bubble.getTempX() - BubbleList1.get(i)
									.getTempX())
									* (bubble.getTempX() - BubbleList1.get(i)
											.getTempX())
									+ (bubble.getTempY() - BubbleList1.get(i)
											.getTempY())
									* (bubble.getTempY() - BubbleList1.get(i)
											.getTempY()));
					bubble.setDistanceBetweenConnectedBubble(distance);
					/**
					 * Code for previous version
					 */
					/*
					 * if (!bubble.isConnected()) { if (distance <
					 * RECONNECT_DISTANCE) { bubble.setConnected(true); } } else
					 * { if (distance > 1.25 * LOOP_DIAMETER && distance < 1.40
					 * * LOOP_DIAMETER) { paint.setColor(Color.RED);
					 * bubble.setCuttable(true); } else {
					 * bubble.setCuttable(false); } }
					 * 
					 * // To draw the line connecting the centers if
					 * (bubble.isConnected()) { paint.setStrokeWidth(3);
					 * canvas.drawLine(bubble.getTempX(), bubble.getY(),
					 * BubbleList1.get(i).getTempX(), BubbleList1.get(i)
					 * .getY(), paint); paint.setStrokeWidth(1); }
					 */

					// paint.setColor(myContext.getResources()
					// .getColor(R.color.bubble));

					paint.setColor(Color.BLACK);
					canvas.drawBitmap(bubble.getBallGraphic(), bubble.getX()
							+ addX - PIPE_WIDTH / 2, bubble.getY() - PIPE_WIDTH
							/ 2, paint);
					/**
					 * Next frame
					 */
					bubble.setY(bubble.getY()
							+ SegmentList
									.get(bubble.getSegmentId())
									.getVelocityY(bubble.getSegmentType(), dist));

					if (bubble.getX() > screenW + PIPE_WIDTH / 2
							|| bubble.getY() > screenH + PIPE_WIDTH / 2) {
						bubble.setVisible(false);
						/*
						 * if
						 * (bubble.isConnected()&&!BubbleList1.get(i).isVisible
						 * ()) { lives--; }
						 */
						// BubbleList.remove(i);
					}

				}

			}

			addX = 0;
			offsetY = 0;
			dist = 0;
			for (int i = 0; i < BubbleList1.size(); i++) {
				bubble = BubbleList1.get(i);
				if (bubble.isVisible()) {
					if (bubble.getSegmentType() == 3
							|| bubble.getSegmentType() == 4) {
						offsetY = SegmentList1.get(bubble.getSegmentId())
								.getStartPointY();
					}
					dist = bubble.getY() - offsetY - LOOP_DIAMETER / 2
							+ PIPE_WIDTH;
					addX = Segment.getX(bubble.getSegmentType(), dist);
					bubble.setTempX(bubble.getX() + addX);
					canvas.drawBitmap(bubble.getBallGraphic(), bubble.getX()
							+ addX - PIPE_WIDTH / 2, bubble.getY() - PIPE_WIDTH
							/ 2, paint);
					bubble.setY(bubble.getY()
							+ SegmentList1
									.get(bubble.getSegmentId())
									.getVelocityY(bubble.getSegmentType(), dist));

					if (bubble.getX() > screenW + PIPE_WIDTH / 2
							|| bubble.getY() > screenH + PIPE_WIDTH / 2) {
						bubble.setVisible(false);
						// BubbleList1.remove(i);
					}

				}

			}

			Bubble[] bubbles = new Bubble[2];
			for (int i = 0; i < BubbleList.size(); i++) {
				bubbles[0] = BubbleList.get(i);
				if (!bubbles[0].isConnected()) {
					for (int j = 0; j < BubbleList1.size(); j++) {
						bubbles[1] = BubbleList1.get(j);
						if (!bubbles[1].isConnected()) {
							if (bubbles[0].getType() == bubbles[1].getType()) {
								double distance = Math.sqrt(Math.pow(
										bubbles[0].getTempX()
												- bubbles[1].getTempX(), 2)
										+ Math.pow(bubbles[0].getY()
												- bubbles[1].getY(), 2));
								if (distance < 1.5f * Level1View.LOOP_DIAMETER) {
									bubbles[0].setConnected(true);
									bubbles[1].setConnected(true);
									bubbles[0].setConnectedTo(j);
									bubbles[1].setConnectedTo(i);
								}
							}
						}
					}
				}
			}

			for (int i = 0; i < BubbleList.size(); i++) {
				bubbles[0] = BubbleList.get(i);
				bubbles[1] = BubbleList.get(bubbles[0].getConnectedTo());

				if (bubbles[0].isConnected()) {
					double distance = Math
							.sqrt(Math.pow(
									bubbles[0].getTempX()
											- bubbles[1].getTempX(), 2)
									+ Math.pow(
											bubbles[0].getY()
													- bubbles[1].getY(), 2));
					if (distance > 1 * Level1View.LOOP_DIAMETER
							&& distance < 3 * Level1View.LOOP_DIAMETER) {
						bubbles[0].setCuttable(true);
						bubbles[1].setCuttable(true);
					} else {
						bubbles[0].setCuttable(false);
						bubbles[1].setCuttable(false);
					}
					int conTo = bubbles[0].getConnectedTo();
					bubbles[1] = BubbleList1.get(conTo);
					if (bubbles[0].isCuttable()) {
						paint.setColor(Color.RED);
						canvas.drawLine(bubbles[0].getTempX(),
								bubbles[0].getY(), bubbles[1].getTempX(),
								bubbles[1].getY(), paint);
					} else {
						paint.setColor(Color.BLACK);
						canvas.drawLine(bubbles[0].getTempX(),
								bubbles[0].getY(), bubbles[1].getTempX(),
								bubbles[1].getY(), paint);
					}
					/*
					 * if (bubble[1].isConnected()) { paint.setStrokeWidth(3);
					 * double
					 * distance=Math.sqrt(Math.pow(bubble[0].getX()-bubble
					 * [1].getX(),
					 * 2)+Math.pow(bubble[0].getY()-bubble[1].getY(), 2)); if
					 * (distance>1.5*Level1View.LOOP_DIAMETER &&
					 * distance<1.5*Level1View.LOOP_DIAMETER) {
					 * bubble[0].setCuttable(true); bubble[1].setCuttable(true);
					 * paint.setColor(Color.RED);
					 * canvas.drawLine(bubble[0].getTempX(), bubble[0].getY(),
					 * bubble[1].getTempX(), bubble[1].getY(), paint); } else {
					 * bubble[0].setCuttable(false);
					 * bubble[1].setCuttable(false); paint.setColor(Color.RED);
					 * canvas.drawLine(bubble[0].getTempX(), bubble[0].getY(),
					 * bubble[1].getTempX(), bubble[1].getY(), paint); }
					 * 
					 * paint.setStrokeWidth(1); }
					 */

				}
			}

			// Nullify each and every segment
			for (int i = 0; i < SegmentList.size(); i++) {
				SegmentList.get(i).setNoOfBubbles(0);
			}
			for (int i = 0; i < SegmentList1.size(); i++) {
				SegmentList1.get(i).setNoOfBubbles(0);
			}

			// To set the resistance of each and every segment
			for (int i = 0; i < BubbleList.size(); i++) {
				SegmentList.get(BubbleList.get(i).getSegmentId())
						.increaseBubbleCount();
			}
			for (int i = 0; i < BubbleList1.size(); i++) {
				SegmentList1.get(BubbleList1.get(i).getSegmentId())
						.increaseBubbleCount();
			}

			for (int i = 0; i < SegmentList.size(); i++) {
				SegmentList.get(i).setResistance(1);
			}
			for (int i = 0; i < SegmentList1.size(); i++) {
				SegmentList1.get(i).setResistance(1);
			}
			// commented out the potential part
			/*
			 * // To format the flow rate of the whole circuit float resistance
			 * = 0; for (int i = 0; i < SegmentList.size(); i++) { if
			 * (SegmentList.get(i).getSegmentType() == 4) { continue; } if
			 * (SegmentList.get(i).getSegmentType() == 3) { resistance +=
			 * (SegmentList.get(i).getResistance() * SegmentList .get(i +
			 * 1).getResistance()) / (SegmentList.get(i).getResistance() +
			 * SegmentList .get(i + 1).getResistance()); } else { resistance +=
			 * SegmentList.get(i).getResistance(); } } // To set the flow of
			 * each and every segment for (int i = 0; i < SegmentList.size();
			 * i++) { SegmentList.get(i).setVelocityY(STARTING_POTENTIAL_0 /
			 * resistance); }
			 * 
			 * float resistance1 = 0; for (int i = 0; i < SegmentList1.size();
			 * i++) { if (SegmentList1.get(i).getSegmentType() == 4) { continue;
			 * } if (SegmentList1.get(i).getSegmentType() == 3) { resistance1 +=
			 * (SegmentList1.get(i).getResistance() * SegmentList1 .get(i +
			 * 1).getResistance()) / (SegmentList1.get(i).getResistance() +
			 * SegmentList1 .get(i + 1).getResistance()); } else { resistance1
			 * += SegmentList1.get(i).getResistance(); } }
			 * 
			 * // To set the flow rate of each and every segment for (int i = 0;
			 * i < SegmentList1.size(); i++) {
			 * SegmentList1.get(i).setVelocityY(STARTING_POTENTIAL_1 /
			 * resistance); }
			 * 
			 * // To assign the potential at each and every point on the
			 * segment, the // setter // in the segment object will also assign
			 * the corresponding flow rate to // the segment float tempPotential
			 * = STARTING_POTENTIAL_0, tempPotential1 = STARTING_POTENTIAL_1;
			 * int no = 0;
			 * 
			 * for (int i = 0; i < SegmentList.size(); i++) { tempPotential =
			 * SegmentList.get(i).setStartPotential(tempPotential); } for (int i
			 * = 0; i < SegmentList1.size(); i++) { tempPotential =
			 * SegmentList1.get(i).setStartPotential( tempPotential1); }
			 */
			// paint.setColor(myContext.getResources().getColor(R.color.helicopter));
			// paint.setStyle(Paint.Style.FILL);
			// canvas.drawCircle(posx, posy, 20, paint);
			/**
			 * Drawing the info
			 */
			paint.setTextSize(20);
			paint.setColor(Color.WHITE);
			canvas.drawText(" Score: " + score, (float) (0.6 * screenW), 20,
					paint);
			canvas.drawText(" Lives: " + lives, (float) (0.6 * screenW), 40,
					paint);
			paint.setColor(Color.BLACK);

			try {
				Thread.sleep(17);
				// posY+=2;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (time == 4 * 510) {
				generateBubble();
				time = 0;
			}

			checkSegmentChange();
			time += 17;

			// To redraw the screen
			invalidate();

		}
	}

	private void generateBubble() {
		bubble = new Bubble(PIPE_WIDTH, myContext, rand.nextInt(2));
		bubble.setSegmentId(1);
		bubble.setSegmentType(2);
		bubble.setVelocityY(2);
		bubble.setX(GeneratorX);
		bubble.setY(0);
		bubble.setID(BubbleId[0]);
		// bubble.setConnectedTo(BubbleId[1]);
		bubble.setConnected(false);
		SegmentList.get(0).increaseBubbleCount();
		BubbleList.add(bubble);

		bubble = new Bubble(PIPE_WIDTH, myContext, rand.nextInt(2));
		bubble.setSegmentId(1);
		bubble.setSegmentType(1);
		bubble.setVelocityY(2);
		bubble.setX(GeneratorX1);
		bubble.setY(0);
		bubble.setID(BubbleId[1]);
		// bubble.setConnectedTo(BubbleId[0]);
		bubble.setConnected(false);
		SegmentList1.get(0).increaseBubbleCount();
		BubbleList1.add(bubble);
		BubbleId[0]++;
		BubbleId[1]++;
	}

	// To check for the segment change of each bubble
	private void checkSegmentChange() {
		Segment segment = new Segment(1);
		Random rand = new Random();
		int nextSegment = 3;
		for (int i = 0; i < BubbleList.size(); i++) {
			bubble = BubbleList.get(i);
			for (int j = 0; j < SegmentList.size(); j++) {
				segment = SegmentList.get(j);
				if (bubble.getY() >= segment.getStartPointY()
						&& bubble.getY() < segment.getEndPointY()) {
					if (bubble.getSegmentType() == 1
							|| bubble.getSegmentType() == 2) {
						if (segment.getSegmentType() == 3
								|| segment.getSegmentType() == 4) {
							//
							if (segment.getSegmentType() == 3) {
								if (segment.getResistance() < SegmentList.get(
										j + 1).getResistance()) {
									nextSegment = 3;
								} else if (segment.getResistance() > SegmentList
										.get(j + 1).getResistance()) {
									nextSegment = 4;
								} else {
									nextSegment = 3 + rand.nextInt(2);
								}
							} else {
								if (segment.getResistance() < SegmentList.get(
										j - 1).getResistance()) {
									nextSegment = 4;
								} else if (segment.getResistance() > SegmentList
										.get(j - 1).getResistance()) {
									nextSegment = 3;
								} else {
									nextSegment = 3 + rand.nextInt(2);
								}
							}
							//
							// nextSegment = 3 + rand.nextInt(2);
							if (nextSegment == 3) {
								bubble.setSegmentType(3);
								bubble.setSegmentId(segment.getSegmentId());
							} else {
								bubble.setSegmentType(4);
								bubble.setSegmentId(segment.getSegmentId() + 1);
							}
						} else {
							bubble.setSegmentType(segment.getSegmentType());
							bubble.setSegmentId(segment.getSegmentId());
						}
					} else {
						if (segment.getSegmentType() == 1
								|| segment.getSegmentType() == 2) {
							bubble.setSegmentType(segment.getSegmentType());
							bubble.setSegmentId(segment.getSegmentId());
						}
					}

					break;
				}
			}
		}
		nextSegment = 3;
		for (int i = 0; i < BubbleList1.size(); i++) {
			bubble = BubbleList1.get(i);
			for (int j = 0; j < SegmentList1.size(); j++) {
				segment = SegmentList1.get(j);
				if (bubble.getY() >= segment.getStartPointY()
						&& bubble.getY() < segment.getEndPointY()) {
					if (bubble.getSegmentType() == 1
							|| bubble.getSegmentType() == 2) {
						if (segment.getSegmentType() == 3
								|| segment.getSegmentType() == 4) {
							//
							if (segment.getSegmentType() == 3) {
								if (segment.getResistance() < SegmentList1.get(
										j + 1).getResistance()) {
									nextSegment = 3;
								} else if (segment.getResistance() > SegmentList1
										.get(j + 1).getResistance()) {
									nextSegment = 4;
								} else {
									nextSegment = 3 + rand.nextInt(2);
								}
							} else {
								if (segment.getResistance() < SegmentList1.get(
										j + 1).getResistance()) {
									nextSegment = 3;
								} else if (segment.getResistance() > SegmentList1
										.get(j + 1).getResistance()) {
									nextSegment = 4;
								} else {
									nextSegment = 3 + rand.nextInt(2);
								}
							}
							//
							// nextSegment = 3 + rand.nextInt(2);
							if (nextSegment == 3) {
								bubble.setSegmentType(3);
								bubble.setSegmentId(segment.getSegmentId());
							} else {
								bubble.setSegmentType(4);
								bubble.setSegmentId(segment.getSegmentId() + 1);
							}
						} else {
							bubble.setSegmentType(segment.getSegmentType());
							bubble.setSegmentId(segment.getSegmentId());
						}
					} else {
						if (segment.getSegmentType() == 1
								|| segment.getSegmentType() == 2) {
							bubble.setSegmentType(segment.getSegmentType());
							bubble.setSegmentId(segment.getSegmentId());
						}
					}

					break;
				}
			}

		}

	}

	private static Bitmap createLevel1() {

		Bitmap tempBitmap = Bitmap.createBitmap(screenW, screenH,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(tempBitmap);

		Bitmap background = BitmapFactory.decodeResource(
				myContext.getResources(), R.drawable.background);
		background = Bitmap.createScaledBitmap(background, screenW, screenH,
				false);
		canvas.drawBitmap(background, 0, 0, paint);
		/*
		 * paint.setStyle(Paint.Style.FILL);
		 * paint.setColor(myContext.getResources
		 * ().getColor(R.color.Background)); canvas.drawRect(0, 0, screenW,
		 * screenH, paint); paint.setStyle(Paint.Style.STROKE);
		 * paint.setColor(Color.BLACK);
		 */
		/*
		 * // Defining the x value of generator int GeneratorX = (int)
		 * (0.65*LOOP_DIAMETER);
		 */
		// Tracker of Y for placing segment points
		int TrackerY = 0;
		// Drawing the first loop
		Bitmap loop;
		loop = Level1View.createLoop(PIPE_WIDTH, LOOP_DIAMETER, paint);
		canvas.drawBitmap(loop, GeneratorX - loop.getWidth() / 2, TrackerY,
				paint);
		// This loop will contain four segments
		TrackerY = createLoopSegments(GeneratorX, TrackerY, 0);

		Bitmap pipe = Level1View.createPipe(PIPE_WIDTH,
				screenH - 2 * loop.getHeight(), paint);
		canvas.drawBitmap(pipe, GeneratorX - pipe.getWidth() / 2,
				loop.getHeight(), paint);
		// Creating the pipe
		createSegment(1, GeneratorX, TrackerY, GeneratorX, TrackerY + screenH
				- 2 * loop.getHeight(), 0);
		TrackerY = TrackerY + screenH - 2 * loop.getHeight();

		canvas.drawBitmap(loop, GeneratorX - loop.getWidth() / 2,
				loop.getHeight() + screenH - 2 * loop.getHeight(), paint);
		// This loop will contain four segments
		TrackerY = createLoopSegments(GeneratorX, TrackerY, 0);

		// Creating the second circuit
		TrackerY = 0;

		// Creating the bottom pipe of the 2nd circuit
		pipe = Level1View.createPipe(PIPE_WIDTH,
				(screenH - loop.getHeight()) / 2, paint);
		canvas.drawBitmap(pipe, GeneratorX1 - pipe.getWidth() / 2, TrackerY,
				paint);
		// Creating the pipe
		createSegment(1, GeneratorX1, TrackerY, GeneratorX1, TrackerY
				+ (screenH - loop.getHeight()) / 2, 1);
		TrackerY = TrackerY + (screenH - loop.getHeight()) / 2;

		// Creating the loop for the 2nd circuit
		loop = Level1View.createLoop(PIPE_WIDTH, LOOP_DIAMETER, paint);
		canvas.drawBitmap(loop, GeneratorX1 - loop.getWidth() / 2, TrackerY,
				paint);
		// This loop will contain four segments
		TrackerY = createLoopSegments(GeneratorX, TrackerY, 1);

		// Creating the bottom pipe of the 2nd circuit
		pipe = Level1View.createPipe(PIPE_WIDTH,
				(screenH - loop.getHeight()) / 2, paint);
		canvas.drawBitmap(pipe, GeneratorX1 - pipe.getWidth() / 2, TrackerY,
				paint);
		// Creating the pipe
		createSegment(1, GeneratorX1, TrackerY, GeneratorX1, TrackerY
				+ (screenH - loop.getHeight()) / 2, 1);
		TrackerY = TrackerY + (screenH - loop.getHeight()) / 2;

		return tempBitmap;
	}

	private static int createLoopSegments(int GeneratorX, int TrackerY, int num) {

		if (num == 0) {
			// Creating the pipe straight upper segment
			createSegment(2, GeneratorX, 0, GeneratorX, TrackerY + 3
					* PIPE_WIDTH / 2, num);
			TrackerY = TrackerY + 3 * PIPE_WIDTH / 2;

			// Creating the left loop part segment
			createSegment(3, GeneratorX, TrackerY, GeneratorX, TrackerY
					+ LOOP_DIAMETER - PIPE_WIDTH, num);

			// Creating the right loop part segment
			createSegment(4, GeneratorX, TrackerY, GeneratorX, TrackerY
					+ LOOP_DIAMETER - PIPE_WIDTH, num);
			TrackerY = TrackerY + LOOP_DIAMETER - PIPE_WIDTH;

			// Creating the pipe straight lower segment
			createSegment(2, GeneratorX, TrackerY, GeneratorX, TrackerY + 3
					* PIPE_WIDTH / 2, num);
			TrackerY = TrackerY + 3 * PIPE_WIDTH / 2;

		} else {
			// Creating the pipe straight upper segment
			createSegment(2, GeneratorX1, 0, GeneratorX1, TrackerY + 3
					* PIPE_WIDTH / 2, num);
			TrackerY = TrackerY + 3 * PIPE_WIDTH / 2;

			// Creating the left loop part segment
			createSegment(3, GeneratorX1, TrackerY, GeneratorX1, TrackerY
					+ LOOP_DIAMETER - PIPE_WIDTH, num);

			// Creating the right loop part segment
			createSegment(4, GeneratorX1, TrackerY, GeneratorX1, TrackerY
					+ LOOP_DIAMETER - PIPE_WIDTH, num);
			TrackerY = TrackerY + LOOP_DIAMETER - PIPE_WIDTH;

			// Creating the pipe straight lower segment
			createSegment(2, GeneratorX1, TrackerY, GeneratorX1, TrackerY + 3
					* PIPE_WIDTH / 2, num);
			TrackerY = TrackerY + 3 * PIPE_WIDTH / 2;
		}

		return TrackerY;
	}

	private static void createSegment(int type, int startX, int startY,
			int endX, int endY, int num) {
		Segment segment = new Segment(type);
		segment.setStartPointX(startX);
		segment.setStartPointY(startY);
		segment.setEndPointX(endX);
		segment.setEndPointY(endY);
		segment.setPipeWidth(PIPE_WIDTH);
		segment.setLoopDiameter(LOOP_DIAMETER);
		segment.setResistancePerUnitLength(RESISTANCE_PER_UNIT_LENGTH);
		segment.setResistance(0);
		segment.setBubbleResistance(BUBBLE_RESISTANCE);
		if (num == 0) {
			segment.setSegmentId(SegmentIdAssign);
			segment.setVelocityY(VELOCITY_CIRCUIT_0);
			SegmentIdAssign++;
			SegmentList.add(segment);
		} else {
			segment.setSegmentId(SegmentIdAssign1);
			segment.setVelocityY(VELOCITY_CIRCUIT_1);
			SegmentIdAssign1++;
			SegmentList1.add(segment);
		}
	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// screenW = w;
		// screenH = h;
		// PIPE_WIDTH=screenW/12;
		// LOOP_DIAMETER=(int) (screenW/2.4);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventAction = event.getAction();
		float X = event.getX();
		float Y = event.getY();
		int boundary = 30;

		if (eventAction == MotionEvent.ACTION_MOVE) {
			AudioManager audioManager = (AudioManager) myContext
					.getSystemService(Context.AUDIO_SERVICE);
			float volume = audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);

			sounds.play(cutSound, volume, volume, 1, 0, 1);
			for (int i = 0; i < BubbleList.size(); i++) {
				bubble = BubbleList.get(i);
				if (bubble.isCuttable()) {
					if (X > bubble.getX() && X < BubbleList1.get(i).getX()) {
						if (bubble.getY() > BubbleList1.get(i).getY()) {
							if (Y < bubble.getY() + PIPE_WIDTH
									&& Y > BubbleList1.get(i).getY()
											- PIPE_WIDTH) {
								if (bubble.isConnected()) {
									score += SCORE_INCREMENT;
								}
								bubble.setConnected(false);
							}
						} else {
							if (Y > bubble.getY() - PIPE_WIDTH
									&& Y < BubbleList1.get(i).getY()
											+ PIPE_WIDTH) {
								if (bubble.isConnected()) {
									score += SCORE_INCREMENT;
								}
								bubble.setConnected(false);
							}
						}
					}
				}

			}
		}
		if (eventAction == MotionEvent.ACTION_DOWN) {
			Segment segment;
			sounds.pause(cutSound);
			if (show_mission)
				show_mission = false;
			for (int i = 0; i < SegmentList.size(); i++) {
				segment = SegmentList.get(i);
				if (segment.getSegmentType() == 3) {
					if (Y > segment.getStartPointY()
							&& Y < segment.getStartPointY() + LOOP_DIAMETER / 2
									- PIPE_WIDTH / 2) {
						if (X < segment.getStartPointX()
								&& X > segment.getStartPointX() - LOOP_DIAMETER
										/ 2) {
							segment.addResistance(RESISTANCE_TO_ADD_ON_CLICK);
							AudioManager audioManager = (AudioManager) myContext
									.getSystemService(Context.AUDIO_SERVICE);
							float volume = audioManager
									.getStreamVolume(AudioManager.STREAM_MUSIC);
							sounds.play(click, volume, volume, 1, 0, 1);
							// sounds.pause(cutSound);
						}
					} else if (Y < segment.getEndPointY()
							&& Y > segment.getStartPointY() + LOOP_DIAMETER / 2
									- PIPE_WIDTH / 2) {
						if (X < segment.getStartPointX()
								&& X > segment.getStartPointX() - LOOP_DIAMETER
										/ 2) {
							segment.addResistance(-RESISTANCE_TO_ADD_ON_CLICK);
							AudioManager audioManager = (AudioManager) myContext
									.getSystemService(Context.AUDIO_SERVICE);
							float volume = audioManager
									.getStreamVolume(AudioManager.STREAM_MUSIC);
							sounds.play(click, volume, volume, 1, 0, 1);
							// sounds.pause(cutSound);
						}
					}

				} else if (segment.getSegmentType() == 4) {
					if (Y > segment.getStartPointY()
							&& Y < segment.getStartPointY() + LOOP_DIAMETER / 2
									- PIPE_WIDTH / 2) {
						if (X > segment.getStartPointX()
								&& X < segment.getStartPointX() + LOOP_DIAMETER
										/ 2) {
							segment.addResistance(RESISTANCE_TO_ADD_ON_CLICK);
							AudioManager audioManager = (AudioManager) myContext
									.getSystemService(Context.AUDIO_SERVICE);
							float volume = audioManager
									.getStreamVolume(AudioManager.STREAM_MUSIC);
							sounds.play(click, volume, volume, 1, 0, 1);
						}
					} else if (Y < segment.getEndPointY()
							&& Y > segment.getStartPointY() + LOOP_DIAMETER / 2
									- PIPE_WIDTH / 2) {
						if (X > segment.getStartPointX()
								&& X < SegmentList.get(i).getStartPointX()
										+ LOOP_DIAMETER / 2) {
							segment.addResistance(-RESISTANCE_TO_ADD_ON_CLICK);
							AudioManager audioManager = (AudioManager) myContext
									.getSystemService(Context.AUDIO_SERVICE);
							float volume = audioManager
									.getStreamVolume(AudioManager.STREAM_MUSIC);
							sounds.play(click, volume, volume, 1, 0, 1);
						}
					}
				}
			}

			for (int i = 0; i < SegmentList1.size(); i++) {
				segment = SegmentList1.get(i);
				if (segment.getSegmentType() == 3) {
					if (Y > segment.getStartPointY()
							&& Y < segment.getStartPointY() + LOOP_DIAMETER / 2
									- PIPE_WIDTH / 2) {
						if (X < segment.getStartPointX()
								&& X > segment.getStartPointX() - LOOP_DIAMETER
										/ 2) {
							segment.addResistance(RESISTANCE_TO_ADD_ON_CLICK);
							AudioManager audioManager = (AudioManager) myContext
									.getSystemService(Context.AUDIO_SERVICE);
							float volume = audioManager
									.getStreamVolume(AudioManager.STREAM_MUSIC);
							sounds.play(click, volume, volume, 1, 0, 1);
						}
					} else if (Y < segment.getEndPointY()
							&& Y > segment.getStartPointY() + LOOP_DIAMETER / 2
									- PIPE_WIDTH / 2) {
						if (X < segment.getStartPointX()
								&& X > segment.getStartPointX() - LOOP_DIAMETER
										/ 2) {
							segment.addResistance(-RESISTANCE_TO_ADD_ON_CLICK);
							AudioManager audioManager = (AudioManager) myContext
									.getSystemService(Context.AUDIO_SERVICE);
							float volume = audioManager
									.getStreamVolume(AudioManager.STREAM_MUSIC);
							sounds.play(click, volume, volume, 1, 0, 1);
						}
					}

				} else if (segment.getSegmentType() == 4) {
					if (Y > segment.getStartPointY()
							&& Y < segment.getStartPointY() + LOOP_DIAMETER / 2
									- PIPE_WIDTH / 2) {
						if (X > segment.getStartPointX()
								&& X < segment.getStartPointX() + LOOP_DIAMETER
										/ 2) {
							segment.addResistance(RESISTANCE_TO_ADD_ON_CLICK);
							AudioManager audioManager = (AudioManager) myContext
									.getSystemService(Context.AUDIO_SERVICE);
							float volume = audioManager
									.getStreamVolume(AudioManager.STREAM_MUSIC);
							sounds.play(click, volume, volume, 1, 0, 1);
						}
					} else if (Y < segment.getEndPointY()
							&& Y > segment.getStartPointY() + LOOP_DIAMETER / 2
									- PIPE_WIDTH / 2) {
						if (X > segment.getStartPointX()
								&& X < SegmentList.get(i).getStartPointX()
										+ LOOP_DIAMETER / 2) {
							segment.addResistance(-RESISTANCE_TO_ADD_ON_CLICK);
							AudioManager audioManager = (AudioManager) myContext
									.getSystemService(Context.AUDIO_SERVICE);
							float volume = audioManager
									.getStreamVolume(AudioManager.STREAM_MUSIC);
							sounds.play(click, volume, volume, 1, 0, 1);
						}
					}
				}
			}
		}

		/*
		 * switch (eventAction) { case MotionEvent.ACTION_DOWN: break; case
		 * MotionEvent.ACTION_MOVE:
		 * 
		 * break; case MotionEvent.ACTION_UP: break; }
		 */
		return true;

	}

	public static Bitmap createLoop(int pipeWidth, int loopDia, Paint paint) {
		paint.setStrokeWidth(2);
		Bitmap loop = Bitmap.createBitmap(loopDia, loopDia + 2 * pipeWidth,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(loop);
		float offsetY = 0;

		float angle = (float) ((360 * Math.asin(((float) PIPE_WIDTH / 2)
				/ ((float) LOOP_DIAMETER / 2))) / Math.PI);
		float multiFactor = (float) Math.cos(Math.asin((PIPE_WIDTH / 2)
				/ (LOOP_DIAMETER / 2)));

		float startAngle = (-90 + angle / 2);
		float sweepAngle = (180 - angle);
		float startAngleR = (90 + angle / 2);
		paint.setStyle(Paint.Style.STROKE);

		canvas.drawLine((loopDia - pipeWidth) / 2, 0,
				(loopDia - pipeWidth) / 2, pipeWidth, paint);
		canvas.drawLine((loopDia + pipeWidth) / 2, 0,
				(loopDia + pipeWidth) / 2, pipeWidth, paint);
		offsetY = pipeWidth;

		RectF outer = new RectF(0, offsetY, loopDia, offsetY
				+ (multiFactor * loopDia));
		// paint.setStyle(Paint.Style.FILL);
		// paint.setColor(myContext.getResources().getColor(R.color.water));
		// canvas.drawArc(outer, 0, 360, false, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		canvas.drawArc(outer, startAngle, sweepAngle, false, paint);
		canvas.drawArc(outer, startAngleR, sweepAngle, false, paint);

		// To draw the inner part of the loop .i.e
		canvas.drawCircle(loopDia / 2, offsetY + (multiFactor * loopDia / 2),
				loopDia / 2 - pipeWidth, paint);
		// To draw the buttons
		RectF inner = new RectF(pipeWidth, offsetY + pipeWidth, loopDia
				- pipeWidth, offsetY + (loopDia - pipeWidth));

		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(myContext.getResources().getColor(R.color.resis4));
		canvas.drawArc(inner, 0, 90, true, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		canvas.drawArc(inner, 0, 90, true, paint);

		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(myContext.getResources().getColor(R.color.resis4));
		canvas.drawArc(inner, 90, 90, true, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		canvas.drawArc(inner, 90, 90, true, paint);

		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(myContext.getResources().getColor(R.color.resis1));
		canvas.drawArc(inner, 180, 90, true, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		canvas.drawArc(inner, 180, 90, true, paint);

		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(myContext.getResources().getColor(R.color.resis1));
		canvas.drawArc(inner, 270, 90, true, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		canvas.drawArc(inner, 270, 90, true, paint);

		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		offsetY = offsetY + loopDia * multiFactor;

		canvas.drawLine((loopDia - pipeWidth) / 2, offsetY,
				(loopDia - pipeWidth) / 2, offsetY + pipeWidth, paint);
		canvas.drawLine((loopDia + pipeWidth) / 2, offsetY,
				(loopDia + pipeWidth) / 2, offsetY + pipeWidth, paint);
		offsetY = offsetY + pipeWidth;

		paint.setStyle(Paint.Style.FILL_AND_STROKE);

		return loop;
	}

	public static Bitmap createPipe(int pipeWidth, int pipeHeight, Paint paint) {
		Bitmap pipe = Bitmap.createBitmap(pipeWidth + 2, pipeHeight,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(pipe);

		canvas.drawLine(1, 0, 1, pipeHeight, paint);
		canvas.drawLine(pipeWidth + 1, 0, pipeWidth + 1, pipeHeight, paint);

		return pipe;
	}

	public float Distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

}
