package ru.mocapps.crazyfish.logic;

import java.util.ArrayList;
import java.util.Random;

public abstract class Fish {
	private Random random;

	protected String fishName;
	// position vars
	protected float[] position;
	protected float[] direction;
	protected float rotation;

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
		this.random = new Random(System.currentTimeMillis());
		this.fishName = name;
		this.position = new float[2];
		this.direction = new float[2];
		this.position[0] = xPos;
		this.position[1] = yPos;
		this.direction[0] = random.nextFloat();
		this.direction[1] = random.nextFloat();
		this.level = 0;
		this.experience = 0;
		this.maxSpeed = 5;
		this.speed = maxSpeed;
		this.directionChange = 10;
		targetType = TargetType.None;

		maxHunger = 300;
		hunger = 250;
		maxLife = 300;
		life = 250;
	}

	protected Food getNearestFood() {
		ArrayList<Food> allFood = new ArrayList<Food>(LogicController.getAllFood());
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
			} else if (LogicController.isFoodAviable()) { // вообще есть Food на
															// поле?
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
			if (LogicController.isFoodAviable()) {
				Food food = getNearestFood();
				target[0] = food.getXPosition();
				target[1] = food.getYPosition();
				targetType = TargetType.Food;
				targetFoodName = food.getName();
			} else {
				target = targetRandomPosition;
			}
			break;
		default:
			if (LogicController.isFoodAviable()) {
				Food food = getNearestFood();
				target[0] = food.getXPosition();
				target[1] = food.getYPosition();
				targetType = TargetType.Food;
				targetFoodName = food.getName();
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

		// vector
		float xVector = target[0] - position[0];
		float yVector = target[1] - position[1];

		//расчет направления
		if (Math.abs(xVector) >= Math.abs(yVector)) {
			direction[0] = xVector / Math.abs(xVector);
			direction[1] = yVector / Math.abs(xVector);
		} else {
			direction[0] = xVector / Math.abs(yVector);
			direction[1] = yVector / Math.abs(yVector);
		}

		//расчет угла поворота и движение
		rotation = (float) Math.atan2(yVector, xVector);
		float[] newposition = new float[2];
		newposition[0] = position[0] + direction[0] * speed;
		newposition[1] = position[1] + direction[1] * speed;
		position = newposition;
		/*
		boolean isCollision = false;
		for (Fish fish : LogicController.getAllFishes()) {
			if (CMath.distance(newposition[0], newposition[1], fish.getXPosition(), fish.getYPosition()) < 15) {
				isCollision = true;
			}
		}
		
		if (!isCollision){
			position = newposition;
		}
		*/
		
		position[0] = Math.max(position[0], Math.min(position[0], LogicController.getFieldWidth()));
		position[0] = Math.max(position[0], Math.min(position[0], LogicController.getFieldHeight()));

		isTargetReached(target); // проверка достижения цели и поедание еды
		calculateLifeHunger();
	}

	//поедание еды, восстановления голода
	private void eatFood() {
		hunger += 100;
		if (hunger > maxHunger) {
			hunger = maxHunger;
		}
	}

	//расчет голода и здоровья
	private void calculateLifeHunger() {
		hunger -= 0.5; //
		if (hunger > 0){ //если голодаем
			life++; //выздоравливаем
			if (life > maxLife)
				life = maxLife;
		} else { //если не голодаем
			life -= 0.5; //теряем здоровье
			hunger = 0;
		}
		
		//если закончилось здоровье, умираем
		if (life < 0){
			LogicController.dieFish(this.fishName);
		}
		
		if (targetType == TargetType.Food){
			//ускорение движения рыбы, если голодает
			speed = maxSpeed;
			if (hunger < maxHunger * 0.4){ //если рыба голодает
				speed = maxSpeed * 2;
			} 
			if (hunger < maxHunger * 0.2){
				speed = maxSpeed * 3;
			}
		}
	}

	private boolean isTargetReached(float[] target) {
		if (CMath.distance(position[0], position[1], target[0], target[1]) < 10) {
			switch (targetType) {
			case Food:
				// if we enough near food
				LogicController.removeFood(targetFoodName); // eat food
				eatFood();
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

	public String getName() {
		return fishName;
	}

	public int getLevel() {
		return level;
	}

	public float getLife() {
		return life;
	}
	
	public float getHunger() {
		return hunger;
	}

	public float getMaxHunger() {
		return maxHunger;
	}

	public float getMaxLife() {
		return maxLife;
	}

	public float getRotation() {
		return rotation;
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