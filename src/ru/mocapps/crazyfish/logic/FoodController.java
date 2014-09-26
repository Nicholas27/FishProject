package ru.mocapps.crazyfish.logic;

import java.util.ArrayList;

import ru.mocapps.crazyfish.main.FishActivity;

public class FoodController extends Thread {

	private static ArrayList<Food> foods;

	public FoodController() {
		foods = new ArrayList<Food>();
	}

	public void makeStep() {
		synchronized (foods) {
			for (Food food : foods) {
				food.makeMove();
			}
		}
	}

	public void addFood(float xPos, float yPos, FoodTypes type, String name) {
		foods.add(new FoodCorn(xPos, yPos, name));
	}

	public void removeFood(String name) {
		//Log.d("logic", "foodController eat food: " + name);
		for (Food food : foods) {
			if (food.getName() == name) {
				FishActivity.removeFood(name);
				foods.remove(food);
				break;
			}
		}
		
	}

	public Food getFood(String name) {
		for (Food food : foods) {
			if (food.getName() == name)
				return food;
		}
		return null;
	}

	public ArrayList<Food> getAllFood() {
		synchronized (foods) {
			return foods;
		}
		
	}

	public boolean isFoodAviable(String name){
		synchronized (foods) {
			for (Food food : foods) {
				if (food.getName() == name)
					return true;
			}
			return false;
		}
	}
	
	public boolean isFoodAviable() {
		if (foods.size() > 0)
			return true;
		else
			return false;
	}
	
}
