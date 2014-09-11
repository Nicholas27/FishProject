package ru.mocapps.crazyfish.logic;

import java.util.ArrayList;

import android.util.Log;

import ru.mocapps.crazyfish.exception.FishNotFoundException;

public class FishController {
	private ArrayList<Fish> fishes;

	public FishController() {
		fishes = new ArrayList<Fish>();
	}

	// add fish by type and position
	public void addFish(float xPos, float yPos, String type, String name) {

		if (type == "small") {
			fishes.add(new SmallFish(xPos, yPos, name));
		}

		if (type == "mid") {
			fishes.add(new MidFish(xPos, yPos, name));
		}

		if (type == "hammer") {
			fishes.add(new HammerFish(xPos, yPos, name));
		}

	}

	// delete fish, search by name
	public void removeFish(String name) {
		for (Fish fish : fishes) {
			if (fish.getName() == name) {
				fishes.remove(fish);
				break;
			}
		}
	}

	// make steps for all fishes
	public void makeStep() {
		for (int i = 0; i < fishes.size(); i++) {
			Fish fish = fishes.get(i);
			fish.makeStep();
			Log.d("fish", fish.getName() + ": X = " + fish.getXPosition() + "; Y = " + fish.getYPosition());
			fishes.set(i, fish);
		}
		Log.d("fish", "    ---------        ");
	}

	public void printTrace() {
		if (fishes.size() > 0) {
			System.out.println("FISH: count = " + fishes.size());
			for (Fish fish : fishes) {
				System.out.println(fish.getName() + "x=" + fish.getXPosition()
						+ "y=" + fish.getYPosition());
			}
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

}
