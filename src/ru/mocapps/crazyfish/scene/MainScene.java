package ru.mocapps.crazyfish.scene;

import org.andengine.entity.scene.Scene;

public class MainScene extends Scene {

	private AquariumScene aquriumScene = new AquariumScene();

	public static int curScene;

	private final int AQURIUMSCENE_ = 0;

	public MainScene() {
		attachChild(aquriumScene);
		ShowAquriumScene();
	}

	public void ShowAquriumScene() {
		aquriumScene.Show();
		curScene = AQURIUMSCENE_;
	}

}
