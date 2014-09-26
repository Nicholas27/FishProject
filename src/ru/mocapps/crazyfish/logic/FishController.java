package ru.mocapps.crazyfish.logic;

import java.util.ArrayList;

import ru.mocapps.crazyfish.exception.FishNotFoundException;
import ru.mocapps.crazyfish.main.FishActivity;

public class FishController {
	private ArrayList<Fish> fishes;

	public FishController() {
		fishes = new ArrayList<Fish>();
	}

	// add fish by type and position
	public void addFish(float xPos, float yPos, FishTypes fishType, String name) {
		switch (fishType) {
		case Hammer:
			fishes.add(new HammerFish(xPos, yPos, name));
			break;
		case Mid:
			fishes.add(new MidFish(xPos, yPos, name));
			break;
		case Small:
			fishes.add(new SmallFish(xPos, yPos, name));
			break;
		default:
			break;
		}
	}

	// delete fish, search by name
	public void removeFish(String name) {
		for (Fish fish : fishes) {
			if (fish.getName() == name) {
				fishes.remove(fish);
				FishActivity.removeFish(name);
				break;
			}
		}
	}

	// make steps for all fishes
	public void makeStep() {
		for (Fish fish : fishes) {
			fish.makeStep();
		}
	}
	
	public Fish getFish(String name) throws FishNotFoundException {
		for (Fish fish : fishes) {
			if (fish.getName() == name)
				return fish;
		}
		throw new FishNotFoundException();
	}

	public int getFishCount() {
		return fishes.size();
	}

	public ArrayList<Fish> getAllFish() {
		return fishes;
	}

}
