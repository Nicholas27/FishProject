package ru.mocapps.crazyfish.logic;


public class FoodCorn extends Food {

	public FoodCorn(float xPos, float yPos, String name) {
		super(xPos, yPos, name);
	}

	@Override
	public void makeMove() {
		basicMove();
	}

}
