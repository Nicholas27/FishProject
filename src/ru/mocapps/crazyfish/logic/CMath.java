package ru.mocapps.crazyfish.logic;

public class CMath {
	public static float distance(float x1, float y1, float x2, float y2){
		return (float) Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}
	
	public static float[] angleToVector(float degree){
		float[] vector = new float[2];
		vector[0] = (float) Math.cos(degree);
		vector[1] = (float) Math.sin(degree);
		return vector;
	}
	
}
