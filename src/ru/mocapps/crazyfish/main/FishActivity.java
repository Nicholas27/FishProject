package ru.mocapps.crazyfish.main;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;

import org.andengine.entity.util.FPSLogger;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import ru.mocapps.crazyfish.logic.LogicController;

import ru.mocapps.crazyfish.sprite.AnimateFish;
import ru.mocapps.crazyfish.sprite.AnimateFood;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FishActivity extends SimpleBaseGameActivity implements
		IOnSceneTouchListener, IOnAreaTouchListener {

	public static Scene scene;
	public static Camera camera;

	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
	public static final float DEMO_VELOCITY = 100.0f;

	private BitmapTextureAtlas mainTexture;
	// private ITextureRegion playerTextureRegion;
	// private ITextureRegion smileR;

	// private TiledTextureRegion smileRTiled;

	public static TiledTextureRegion fishHammer;

	private BitmapTextureAtlas foodTexture;

	public static TiledTextureRegion foodFish;

	// private BitmapTextureAtlas fishAtlas;
	public static ITextureRegion fishRegion;

	public PhysicsWorld physicsWorld;

	// private SceneManager sceneManager;

	private LogicController controller;

	private static ArrayList<AnimateFood> listFood = new ArrayList<AnimateFood>();
	private static ArrayList<AnimateFish> listFish = new ArrayList<AnimateFish>();

	@Override
	public EngineOptions onCreateEngineOptions() {

		// create camera
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), camera);

		return options;
	}

	/*
	 * private BitmapTextureAtlas fishBitmapHead; private BitmapTextureAtlas
	 * fishBitmapBody; private BitmapTextureAtlas fishBitmapBackNail; private
	 * BitmapTextureAtlas fishBitmapMiddleNail;
	 */
	public static TiledTextureRegion body;
	public static TiledTextureRegion head;
	public static TiledTextureRegion backnail;
	public static TiledTextureRegion middlenail;
	public static TiledTextureRegion fishHammer2;

	public static int animationDelay = 45;

	private void loadGfx() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mainTexture = new BitmapTextureAtlas(getTextureManager(), 473, 200);

		fishHammer = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mainTexture, this, "hammer-sprite.png",
						0, 0, 11, 2);
		mainTexture.load();

		foodTexture = new BitmapTextureAtlas(getTextureManager(), 30, 31);

		foodFish = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				foodTexture, this, "food/cornnic.png", 0, 0, 1, 1);
		foodTexture.load();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {
			// this.addFish(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			this.addFood(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			return true;
		}
		return false;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (pSceneTouchEvent.isActionDown()) {
			// this.removeFish((AnimateFish)pTouchArea);
			// removeFood((AnimateFood) pTouchArea);
			return true;
		}
		return false;
	}

	public void addFish(float pX, float pY) {
		AnimateFish animateFish;
		String s_name = LogicController.addFish(pX, pY);
		animateFish = new AnimateFish(pX, pY, fishHammer,
				getVertexBufferObjectManager(), s_name);
		animateFish.animate(animationDelay, true);
		listFish.add(animateFish);
		scene.registerTouchArea(animateFish);
		scene.attachChild(animateFish);
	}

	public void addFood(float pX, float pY) {
		AnimateFood foodSprite;
		String s_name = LogicController.addFood(pX, pY);
		foodSprite = new AnimateFood(pX, pY, foodFish,
				getVertexBufferObjectManager(), s_name);

		listFood.add(foodSprite);
		// foodSprite.animate(animationDelay, true);
		scene.registerTouchArea(foodSprite);
		scene.attachChild(foodSprite);

	}

	// удаление по имени
	public static void removeFood(String s_name) {
		for (AnimateFood food : listFood) {
			if (food.getName() == s_name) {
				FishActivity.scene.unregisterTouchArea(food);
				FishActivity.scene.detachChild(food);
				listFood.remove(food);
			}
		}
		System.gc();
	}

	public void removeFish(AnimateFish animFish) {
		LogicController.removeFish(animFish.getName());
		scene.unregisterTouchArea(animFish);
		scene.detachChild(animFish);
		animFish.dispose();
		System.gc();
	}

	@Override
	protected void onCreateResources() {
		loadGfx();
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		controller = new LogicController(CAMERA_WIDTH, CAMERA_HEIGHT);
		controller.start();

		float pRed = 0.49f;
		float pGreen = 0.5f;
		float pBlue = 0.84f;

		scene = new Scene();
		scene.setBackground(new Background(pRed, pGreen, pBlue));
		scene.setOnSceneTouchListener(this);

		// ограничения
		// createWalls();
		// createFish();

		this.physicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), true);

		final VertexBufferObjectManager vertexBufferObjectManager = this
				.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2,
				CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2,
				vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT,
				vertexBufferObjectManager);
		final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2,
				CAMERA_HEIGHT, vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0,
				0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.physicsWorld, ground,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.physicsWorld, roof,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.physicsWorld, left,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.physicsWorld, right,
				BodyType.StaticBody, wallFixtureDef);

		scene.attachChild(ground);
		scene.attachChild(roof);
		scene.attachChild(left);
		scene.attachChild(right);

		scene.registerUpdateHandler(this.physicsWorld);
		scene.setOnAreaTouchListener(this);

		addFish(0, 50);
		addFish(50, 50);
		addFish(100, 50);
		addFish(50, 100);
		addFish(200, 200);

		return scene;
	}

}
