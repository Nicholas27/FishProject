package ru.mocapps.crazyfish.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class SceneManager extends Scene {

	private AllScenes currentScene;
	public static BaseGameActivity activity;
	public static Engine engine;
	public static Camera camera;

	public AquariumScene aquriumScene;

	public enum AllScenes {
		AQUARIUM
	}

	public SceneManager(BaseGameActivity act, Engine eng, Camera cam) {
		this.activity = act;
		this.engine = eng;
		this.camera = cam;

	}

	private void init() {
		aquriumScene = new AquariumScene();

		setCurrentScene(AllScenes.AQUARIUM);
	}

	/*
	 * public void loadSplashResources() {
	 * BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	 * 
	 * splashTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 32,
	 * 32); splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
	 * splashTA, this.activity, "face_box.png", 0, 0); splashTA.load(); }
	 * 
	 * /* public void loadMenuResourses() {
	 * 
	 * }
	 * 
	 * public void loadGameResourses() {
	 * 
	 * }
	 * 
	 * public Scene createSplashScene() { splachScene = new Scene();
	 * splachScene.setBackground(new Background(0, 0, 0));
	 * 
	 * Sprite icon = new Sprite(0, 0, splashTR,
	 * engine.getVertexBufferObjectManager());
	 * icon.setPosition((camera.getWidth()-icon.getWidth())/2,
	 * (camera.getHeight() - icon.getHeight())/2);
	 * splachScene.attachChild(icon);
	 * 
	 * return splachScene; }
	 */
	/*
	 * public Scene createGameScene() { return null; }
	 * 
	 * public Scene createMenuScene() { menuScene = new Scene();
	 * menuScene.setBackground(new Background(1, 1, 1));
	 * 
	 * Sprite icon = new Sprite(0, 0, splashTR,
	 * engine.getVertexBufferObjectManager());
	 * icon.setPosition((camera.getWidth()-icon.getWidth())/2,
	 * (camera.getHeight() - icon.getHeight())/2); menuScene.attachChild(icon);
	 * 
	 * return menuScene; }
	 */
	public AllScenes getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(AllScenes currentScene) {
		this.currentScene = currentScene;
		switch (currentScene) {
		case AQUARIUM:
			engine.setScene(aquriumScene);
			break;
		/*
		 * case MENU: engine.setScene(menuScene); break; case GAME:
		 * engine.setScene(gameScene); break;
		 */
		}
	}

	public Scene createAquariumScene() {
		// TODO Auto-generated method stub
		aquriumScene = new AquariumScene();
		aquriumScene.createScene();

		return aquriumScene;
	}

}
