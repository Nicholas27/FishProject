package ru.mocapps.crazyfish.logic;

import java.util.Random;

public abstract class Food {
	private String name;
	
	//current food coordinates
	protected float xPosition;
	protected float yPosition;
	private Random random;
	
	public Food(float xPos, float yPos, String name) {
		random = new Random();
		this.xPosition = xPos;
		this.yPosition = yPos;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public float getXPosition() {
		return xPosition;
	}

	public float getYPosition() {
		return yPosition;
	}
	
	protected void basicMove(){
		xPosition += random.nextInt(3) - 1;
		yPosition += random.nextInt(3) - 1;
		xPosition = Math.min(Math.max(xPosition, 0), LogicController.getFieldWidth());
		yPosition = Math.min(Math.max(yPosition, 0), LogicController.getFieldHeight());
	}
	
	public abstract void makeMove();
}