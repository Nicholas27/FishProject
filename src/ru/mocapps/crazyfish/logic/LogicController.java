package ru.mocapps.crazyfish.logic;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.util.debug.Debug;

import android.util.Log;

import ru.mocapps.crazyfish.exception.FishNotFoundException;
import ru.mocapps.crazyfish.exception.FoodNotFoundException;

public class LogicController extends Thread {
	private static int delay = 50;

	private static int fieldWidth;
	private static int fieldHeight;
	private static FishController fishController;
	private static FoodController foodController;
	private static ArrayList<String> fishToDie;
	private static Random random;
	private static String[] fishTypes = new String[] { "small", "mid", "hammer" };
	private static String[] foodTypes = new String[] { "corn" };

	public LogicController(int width, int height) {
		fieldWidth = width;
		fieldHeight = height;
		fishController = new FishController();
		foodController = new FoodController();
		random = new Random(System.currentTimeMillis());
		fishToDie = new ArrayList<String>();
	}

	private static String randomName() {
		int length = 10;
		String characters = "qwertyuiopasdfghjklzxcvbnm";
		Random rng = new Random();
		char[] text = new char[length];
		for (int i = 0; i < length; i++)
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		return new String(text);
	}

	public static void addFood() {
		synchronized (foodController) {
			String foodType = foodTypes[random.nextInt(foodTypes.length)];
			String foodName = foodType + "_" + randomName();
			float xPos = random.nextInt(fieldWidth);
			float yPos = random.nextInt(fieldHeight);
			foodController.addFood(xPos, yPos, foodType, foodName);
		}
	}

	public static String addFood(float x, float y) {
		synchronized (foodController) {
			String foodType = foodTypes[random.nextInt(foodTypes.length)];
			String foodName = foodType + "_" + randomName();
			foodController.addFood(x, y, foodType, foodName);
			return foodName;
		}
	}

	public static String addFish() {
		synchronized (fishController) {
			String fishType = fishTypes[random.nextInt(fishTypes.length)];
			String fishName = fishType + "_" + randomName();
			float xPos = random.nextInt(fieldWidth);
			float yPos = random.nextInt(fieldHeight);
			fishController.addFish(xPos, yPos, fishType, fishName);
			return fishName;
		}
	}

	public static String addFish(float x, float y) {
		synchronized (fishController) {
			String fishType = fishTypes[random.nextInt(fishTypes.length)];
			String fishName = fishType + "_" + randomName();
			fishController.addFish(x, y, fishType, fishName);
			return fishName;
		}
	}

	public static String addFish(float x, float y, String fishType) {
		synchronized (fishController) {
			String fishName = fishType + "_" + randomName();
			fishController.addFish(x, y, fishType, fishName);
			return fishName;
		}
	}

	public static void removeFood(String name) {
		synchronized (foodController) {
			foodController.removeFood(name);
		}

	}

	public static void removeFish(String name) {
		synchronized (fishController) {
			fishController.removeFish(name);
		}
	}

	public static void dieFish(String name) {
		synchronized (fishToDie) {
			fishToDie.add(name);
		}
	}

	public static ArrayList<Food> getAllFood() {
		synchronized (foodController) {
			return foodController.getAllFood();
		}
	}

	public static Food getFood(String name) {
		synchronized (foodController) {
			return foodController.getFood(name);
		}
	}

	public static Fish getFish(String name) throws FishNotFoundException {
		synchronized (fishController) {
			return fishController.getFish(name);
		}
	}

	public static boolean isFoodAviable() {
		synchronized (foodController) {
			return foodController.isFoodAviable();
		}

	}

	public static boolean isFoodAviable(String name) {
		synchronized (foodController) {
			return foodController.isFoodAviable(name);
		}
	}

	@Override
	public void run() {
		while (true) {
			long time1 = System.currentTimeMillis();
			foodController.makeStep();
			fishController.makeStep();

			// удаляем умерших от голода рыб
			for (String name : fishToDie) {
				fishController.removeFish(name);
			}

			
			/*
			for (Fish fish : fishController.getAllFish()) {
				Log.d("fish log",
						"fish: " + fish.getName() + "life = " + fish.getLife()
								+ "; hunger = " + fish.getHunger());
			}
			Log.d("fish log", "COUNT = " + fishController.getAllFish().size());
			Log.d("fish log", "================\n");

			
			for (Food food : foodController.getAllFood()) {
				Log.d("food log", "food: " + food.getName());
			}
			Log.d("food log", "COUNT = " + foodController.getAllFood().size());
			Log.d("food log", "================\n");

			*/
			
			
			long time2 = System.currentTimeMillis();
			try {
				Thread.sleep(Math.max(delay - (time2 - time1), 1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static int getFieldWidth() {
		return fieldWidth;
	}

	public static int getFieldHeight() {
		return fieldHeight;
	}

	public static float[] getFoodPosition(String foodName) {
		float[] vector = new float[2];
		vector[0] = foodController.getFood(foodName).getXPosition();
		vector[1] = foodController.getFood(foodName).getYPosition();
		return vector;
	}

}