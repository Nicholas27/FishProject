package ru.mocapps.crazyfish.logic;

import java.util.ArrayList;
import java.util.Random;

import ru.mocapps.crazyfish.exception.FoodNotFoundException;

public abstract class Fish {
	private Random random;

	protected String fishName;
	// position vars
	protected float[] position;
	protected float[] direction;

	// target
	protected enum TargetType {
		Food, Random, None
	};

	protected TargetType targetType;
	protected String targetFoodName;
	protected float[] targetRandomPosition;

	// movement
	protected float directionChange;
	protected float speed;
	protected float maxSpeed;
	// life
	protected float hunger;
	protected float maxHunger;
	protected float life;
	protected float maxLife;
	// exp
	protected int level;
	protected int experience;

	public Fish(float xPos, float yPos, String name) {
		this.fishName = name;
		this.position = new float[2];
		this.direction = new float[2];
		this.position[0] = xPos;
		this.position[1] = yPos;
		this.direction[0] = 0.5f;
		this.direction[1] = 0.5f;
		this.level = 1;
		this.experience = 0;
		this.speed = 1;
		this.directionChange = 10;
		this.random = new Random(System.currentTimeMillis());
		targetType = TargetType.None;
	}

	protected Food getNearestFood() {
		ArrayList<Food> allFood = LogicController.getAllFood();
		Food nearest = null;
		float distance = Float.MAX_VALUE;
		for (Food food : allFood) {
			float distance2 = CMath.distance(position[0], position[1],
					food.getXPosition(), food.getYPosition());
			if (distance2 < distance) { // if current food nearer
				nearest = food; // remember more nearest
				distance = distance2; //
			}
		}
		return nearest;
	}

	protected float[] generateTargetPoint() {
		float[] target = new float[2];
		switch (targetType) {
		case Food:
			// food остался на поле?
			if (LogicController.isFoodAviable(targetFoodName)) { 
				Food food = LogicController.getFood(targetFoodName);
				target[0] = food.getXPosition();
				target[1] = food.getYPosition();
			} else if (LogicController.isFoodAviable()) { //вообще есть Food на поле?
				Food food = getNearestFood();
				target[0] = food.getXPosition();
				target[1] = food.getYPosition();
			} else {
				target[0] = random.nextInt(LogicController.getFieldWidth());
				target[1] = random.nextInt(LogicController.getFieldHeight());
				targetRandomPosition = target;
				targetType = TargetType.Random;
			}
			break;
		case Random:
			if (LogicController.isFoodAviable()){
				Food food = getNearestFood();
				target[0] = food.getXPosition();
				target[1] = food.getYPosition();
				targetType = TargetType.Food;
			} else {
				target = targetRandomPosition;
			}
			break;
		default:
			if (LogicController.isFoodAviable()){
				Food food = getNearestFood();
				target[0] = food.getXPosition();
				target[1] = food.getYPosition();
				targetType = TargetType.Food;
			} else {
				target[0] = random.nextInt(LogicController.getFieldWidth());
				target[1] = random.nextInt(LogicController.getFieldHeight());
				targetRandomPosition = target;
				targetType = TargetType.Random;
			}
			break;
		}
		return target;
	}

	public void makeStep() {
		float[] target = generateTargetPoint();
		
		//определение направления
		float[] fullVector = new float[2];
		fullVector[0] = target[0] - position[0];
		fullVector[1] = target[1] - position[1];
		
		//создание еденичного вектора направления
		float[] oneVector = new float[2];
		if (fullVector[0] > fullVector[1]){
			oneVector[0] = 1;
			oneVector[1] = fullVector[1] / fullVector[0];
		} else {
			oneVector[0] = fullVector[0] / fullVector[1];
			oneVector[1] = 1;
		}
		
		//вычисляем, куда будем отклоняться и на сколько
		float[] delta = new float[2];
		delta[0] = (oneVector[0] - direction[0]) / directionChange;
		delta[1] = (oneVector[1] - direction[1]) / directionChange;
		
		//изменяем направление
		direction[0] += delta[0];
		direction[1] += delta[1];
		
		//изменяем положение рыбки
		position[0] += direction[0] * speed;
		position[1] += direction[1] * speed;
		
		isTargetReached(target);
		fix();
	}
	
	/* немного крутого аскея
	      .--.   |V|
	     /    \ _| /
	     q .. p \ /
	      \--/  //
	     __||__//
	    /.    _/
	   // \  /
	  //   ||
	  \\  /  \
	   )\|    |
	  / || || |
	  |/\| || |
	     | || |
	     \ || /
	   __/ || \__
	  \____/\____/ 
	 */

	public boolean isTargetReached(float[] target) {
		if (CMath.distance(position[0], position[1], target[0], target[1]) < 5) {
			switch (targetType) {
			case Food:
				// if we enough near food
				LogicController.removeFood(targetFoodName); // eat food
				targetType = TargetType.None;
				break;
			case Random:
				targetType = TargetType.None;
				break;
			default:
				break;
			}
		}

		return false;
	}

	// fixes all fish location params
	public void fix() {
		position[0] = Math.min(Math.max(0, position[0]),
				LogicController.getFieldWidth());
		position[1] = Math.min(Math.max(0, position[1]),
				LogicController.getFieldHeight());
	}

	public String getName() {
		return fishName;
	}

	public int getLevel() {
		return level;
	}

	public float getLife() {
		return life;
	}

	public float getMaxHunger() {
		return maxHunger;
	}

	public float getMaxLife() {
		return maxLife;
	}

	public float getRotation() {
		return (float) Math.atan(direction[1] / direction[0]);
	}

	public float getSpeed() {
		return speed;
	}

	public float getXPosition() {
		return position[0];
	}

	public float getYPosition() {
		return position[1];
	}

}