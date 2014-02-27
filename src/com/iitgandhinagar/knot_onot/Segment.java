package com.iitgandhinagar.knot_onot;

//This class based on the segment id will perform several actions

public class Segment {

	// This varriable will be used to store the segment type
	// 1: For the straight pipe
	// 2: For the pipe straight
	// 3: For the right side of the loop
	// 4: For the left hand side of the loop
	private int SegmentType;

	public void setNoOfBubbles(int noOfBubbles) {
		NoOfBubbles = noOfBubbles;
	}

	// This variable will give the segment id, which will be unique for each and
	// every object
	private int SegmentId;
	/**
	 * @param XCoordinate
	 *            function to return a specific value based on the current Y
	 *            position
	 */
	private float XCoordinate;
	// This two varibles will show the start point of the segment
	private float StartPointX, StartPointY;
	// This two variables will show the end point of the segment
	private float EndPointX, EndPointY;
	// Creating own copies of pipewidth and loop diameter
	private static float PipeWidth, LoopDiameter;
	// Y velocity in the given segment
	private float VelocityY;
	// Indicates the number of bubbles
	int NoOfBubbles = 0;
	// Defines the resistance per unit length
	private float ResistancePerUnitLength;
	// Defines the path length of the segment
	private float PathLength;
	// Resistance of the the segment
	private float Resistance;
	// Potential difference across the segment
	private float PotentialDifference;
	// This variable defines the resistance of each bubble
	private float BubbleResistance = 0;
	// Defines the flow rate in the segment
	float FlowRate = 0;
	// Defines the start and the end potential of the segment
	float StartPotential, EndPotential;
	// Return the temp Velocity
	float TempVelocity;
	// The resistence that is entered by the user
	float UserResistance;

	public float getTempVelocity() {
		TempVelocity = (float) ((StartPotential - EndPotential) / (0.5 * Resistance));
		return TempVelocity;
	}

	public void setTempVelocity(float tempVelocity) {
		TempVelocity = tempVelocity;
	}

	public float getBubbleResistance() {
		return BubbleResistance;
	}

	public void setBubbleResistance(float bubbleResistance) {
		BubbleResistance = bubbleResistance;
	}

	public void increaseBubbleCount() {
		NoOfBubbles++;
	}

	public void decreaseBubblCount() {
		NoOfBubbles--;
	}

	public int getNoOfBubbles() {
		return NoOfBubbles;
	}

	public void setVelocityY(float velocityY) {
		VelocityY = velocityY;
	}

	public float getVelocityY(int type, float dist) {
		// VelocityY=10*(StartPotential-EndPotential)/Resistance;
		if (type == 3 || type == 4) {

			return (float) (VelocityY / 2 * Math.sin(Math.acos(dist
					/ Level1View.LOOP_DIAMETER)));
		}
		return VelocityY;
	}

	public void setPipeWidth(float pipeWidth) {
		PipeWidth = pipeWidth;
	}

	public void setLoopDiameter(float loopDiameter) {
		LoopDiameter = loopDiameter;
	}

	public Segment(int type) {
		SegmentType = type;

	}

	public float getPathLength() {
		return PathLength;
	}

	public void setPathLength(float pathLength) {
		PathLength = pathLength;
	}

	public float getResistancePerUnitLength() {
		return ResistancePerUnitLength;
	}

	public void setResistancePerUnitLength(float resistancePerUnitLength) {
		if (SegmentType == 1 || SegmentType == 2) {
			PathLength = EndPointY - StartPointY;
		}
		if (SegmentType == 3 || SegmentType == 4) {
			PathLength = EndPointY - StartPointY;
			PathLength = (float) (PathLength / 2 * Math.PI);
		}
		ResistancePerUnitLength = resistancePerUnitLength;
	}

	public float getResistance() {
		return Resistance;
	}

	// This method is not same as default method
	//
	public void setResistance(float resistance) {
		if (resistance == 0) {
			Resistance = resistance;
		} else {
			Resistance = ResistancePerUnitLength * PathLength
					+ BubbleResistance * NoOfBubbles + UserResistance;
		}

	}

	public float getPotentialDifference() {
		return PotentialDifference;
	}

	public void setPotentialDifference(float potentialDifference) {
		PotentialDifference = potentialDifference;

	}

	public float getStartPotential() {
		return StartPotential;
	}

	// @noOfBubbles denotes the number of bubbles in the next segment if the
	// current segment type is 3 then it will be processed
	public float setStartPotential(float startPotential) {
		setResistance(1);
		StartPotential = startPotential;
		float resistance = 0;
		if (SegmentType == 3) {
			resistance = (Resistance * Level1View.SegmentList
					.get(SegmentId + 1).Resistance)
					/ (Resistance + Level1View.SegmentList.get(SegmentId + 1).Resistance);
		} else if (SegmentType == 4) {
			resistance = (Resistance * Level1View.SegmentList
					.get(SegmentId - 1).Resistance)
					/ (Resistance + Level1View.SegmentList.get(SegmentId - 1).Resistance);
		} else {
			resistance = Resistance;
		}
		EndPotential = startPotential - VelocityY * resistance;
		if (SegmentType == 3) {
			return startPotential;
		} else {
			return EndPotential;
		}
	}

	public float getEndPotential() {
		return EndPotential;
	}

	public void setEndPotential(float endPotential) {
		EndPotential = endPotential;
	}

	public int getSegmentType() {
		return SegmentType;
	}

	public void setSegmentType(int segmentType) {
		SegmentType = segmentType;
	}

	public int getSegmentId() {
		return SegmentId;
	}

	public void setSegmentId(int segmentId) {
		SegmentId = segmentId;
	}

	public float getXCoordinate() {
		return XCoordinate;
	}

	public void setXCoordinate(float xCoordinate) {
		XCoordinate = xCoordinate;
	}

	public float getStartPointX() {
		return StartPointX;
	}

	public void setStartPointX(float startPointX) {
		StartPointX = startPointX;
	}

	public float getStartPointY() {
		return StartPointY;
	}

	public void setStartPointY(float startPointY) {
		StartPointY = startPointY;
	}

	public float getEndPointX() {
		return EndPointX;
	}

	public void setEndPointX(float endPointX) {
		EndPointX = endPointX;
	}

	public float getEndPointY() {
		return EndPointY;
	}

	public void setEndPointY(float endPointY) {
		EndPointY = endPointY;
	}

	public static float getX(int type, float y) {
		y = y - PipeWidth / 2;
		if (type == 1 || type == 2) {
			return 0;
		} else if (type == 3) {
			return (float) -Math.sqrt(((LoopDiameter - PipeWidth) / 2)
					* ((LoopDiameter - PipeWidth) / 2) - y * y);
		} else if (type == 4) {
			return (float) Math.sqrt(((LoopDiameter - PipeWidth) / 2)
					* ((LoopDiameter - PipeWidth) / 2) - y * y);
		} else {
			return 0;
		}
	}

	public float getLoopDiameter() {
		return LoopDiameter;
	}

	public void addResistance(float res) {
		UserResistance += res;
	}

	public void decreaseResistance(float res) {
		UserResistance -= res;
	}
}
