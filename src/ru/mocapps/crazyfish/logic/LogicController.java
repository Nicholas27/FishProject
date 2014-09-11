package ru.mocapps.crazyfish.logic;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.util.debug.Debug;

import ru.mocapps.crazyfish.exception.FishNotFoundException;
import ru.mocapps.crazyfish.exception.FoodNotFoundException;

public class LogicController extends Thread {
	private static int delay = 200;
	
	private static int fieldWidth;
	private static int fieldHeight;
	private static FishController fishController;
	private static FoodController foodController;
	private static Random random;
	private static String[] fishTypes = new String[] {"small", "mid", "hammer"};
	private static String[] foodTypes = new String[] {"corn"};
	
	public LogicController(int width, int height){
		fieldWidth = width;
		fieldHeight = height;
		fishController = new FishController();
		foodController = new FoodController();
		random = new Random(System.currentTimeMillis());
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
	
	public static void addFood(){
		String foodType = foodTypes[random.nextInt(foodTypes.length)];
		String foodName = foodType + "_" + randomName();
		float xPos = random.nextInt(fieldWidth);
		float yPos = random.nextInt(fieldHeight);
		foodController.addFood(xPos, yPos, foodType, foodName);
	}
	
	public static String addFood(float x, float y){
		String foodType = foodTypes[random.nextInt(foodTypes.length)];
		String foodName = foodType + "_" + randomName();
		foodController.addFood(x, y, foodType, foodName);
		return foodName;
	}
	
	public static String addFish(){
		String fishType = fishTypes[random.nextInt(fishTypes.length)];
		String fishName = fishType + "_" + randomName();
		float xPos = random.nextInt(fieldWidth);
		float yPos = random.nextInt(fieldHeight);
		fishController.addFish(xPos, yPos, fishType, fishName);
		return fishName;
	}
	
	public static String addFish(float x, float y){
		String fishType = fishTypes[random.nextInt(fishTypes.length)];
		String fishName = fishType + "_" + randomName();
		fishController.addFish(x, y, fishType, fishName);
		return fishName;
	}
	
	public static String addFish(float x, float y, String fishType){
		String fishName = fishType + "_" + randomName();
		fishController.addFish(x, y, fishType, fishName);
		return fishName;
	}
	
	public static void removeFood(String name){
		foodController.removeFood(name);
	}
	
	public static void removeFish(String name){
		fishController.removeFish(name);
	}
	
	public static ArrayList<Food> getAllFood() {
		return foodController.getAllFood();
	}
	
	public static Food getFood(String name) {
		return foodController.getFood(name);
	}
	

	public static Fish getFish(String name) throws FishNotFoundException{
		return fishController.getFish(name);
	}
	
	
	public static boolean isFoodAviable(){
		return foodController.isFoodAviable();
	}
	
	public static boolean isFoodAviable(String name){
		return foodController.isFoodAviable(name);
	}
	
	
	@Override
	public void run(){
		while (true){
			long time1 = System.currentTimeMillis();
			foodController.makeStep();
			fishController.makeStep();
			Debug.d("logic", "fish = " + fishController.getFishCount());
			
			long time2 = System.currentTimeMillis();
			try {
				Thread.sleep(Math.max(delay - (time2 - time1), 1)); //sleep delay time - time needed for calculations
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
	
}