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
import ru.mocapps.crazyfish.sprite.FishParts;
import android.hardware.SensorManager;
import android.view.Display;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FishActivity extends SimpleBaseGameActivity implements
		IOnSceneTouchListener, IOnAreaTouchListener {

	public static Scene scene;
	public static Camera camera;

	public static int CAMERA_WIDTH;
	public static int CAMERA_HEIGHT;
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

	public static PhysicsWorld physicsWorld;

	// private SceneManager sceneManager;

	private LogicController controller;

	private static ArrayList<AnimateFood> listFood = new ArrayList<AnimateFood>();
	private static ArrayList<AnimateFish> listFish = new ArrayList<AnimateFish>();

	// private BitmapTextureAtlas fishBitmapHead;
	// private BitmapTextureAtlas fishBitmapBody;
	// private BitmapTextureAtlas fishBitmapBackNail;
	// private BitmapTextureAtlas fishBitmapMiddleNail;

	public static TiledTextureRegion body;
	public static TiledTextureRegion head;
	public static TiledTextureRegion backnail;
	public static TiledTextureRegion middlenail;
	public static TiledTextureRegion fishHammer2;

	public static int animationDelay = 145;

	@Override
	public EngineOptions onCreateEngineOptions() {

		// width/height
		Display display = getWindowManager().getDefaultDisplay();
		CAMERA_WIDTH = display.getWidth();
		CAMERA_HEIGHT = display.getHeight();

		// create camera
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), camera);

		return options;
	}

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

		/*
		 * fishBitmapHead = new BitmapTextureAtlas(getTextureManager(), 27, 26);
		 * 
		 * head = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
		 * fishBitmapHead, this, "parts/head.png", 0, 0, 1, 1);
		 * fishBitmapHead.load();
		 * 
		 * fishBitmapBody = new BitmapTextureAtlas(getTextureManager(), 29, 63);
		 * 
		 * body = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
		 * fishBitmapBody, this, "parts/body.png", 0, 0, 1, 1);
		 * fishBitmapBody.load();
		 * 
		 * fishBitmapMiddleNail = new BitmapTextureAtlas(getTextureManager(),
		 * 16, 19);
		 * 
		 * middlenail = BitmapTextureAtlasTextureRegionFactory
		 * .createTiledFromAsset(fishBitmapMiddleNail, this,
		 * "parts/middle nail.png", 0, 0, 1, 1); fishBitmapMiddleNail.load();
		 */
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

		// �����������
		// createWalls();
		// createFish();

		FishActivity.physicsWorld = new PhysicsWorld(new Vector2(0,
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
		PhysicsFactory.createBoxBody(FishActivity.physicsWorld, ground,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(FishActivity.physicsWorld, roof,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(FishActivity.physicsWorld, left,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(FishActivity.physicsWorld, right,
				BodyType.StaticBody, wallFixtureDef);

		scene.attachChild(ground);
		scene.attachChild(roof);
		scene.attachChild(left);
		scene.attachChild(right);

		scene.registerUpdateHandler(FishActivity.physicsWorld);
		scene.setOnAreaTouchListener(this);

		addFish(0, 50);
		addFish(150, 50);
		addFish(100, 250);
		addFish(50, 100);
		addFish(350, 200);

		return scene;
	}

	public void addFish(float pX, float pY) {
		AnimateFish animateFish;
		String s_name = LogicController.addFish(pX, pY);
		animateFish = new AnimateFish(pX, pY, fishHammer,
				getVertexBufferObjectManager(), s_name);

		animateFish.animate(animationDelay, true);

		scene.registerTouchArea(animateFish);
		scene.attachChild(animateFish);
		listFish.add(animateFish);
	}

	public void addFood(float xPosition, float yPosition) {
		AnimateFood foodSprite;
		String foodName = LogicController.addFood(xPosition, yPosition);
		foodSprite = new AnimateFood(xPosition, yPosition, foodFish,
				getVertexBufferObjectManager(), foodName);

		scene.registerTouchArea(foodSprite);
		scene.attachChild(foodSprite);
		listFood.add(foodSprite);
	}

	// �������� ���, ���������� � LogicController
	public static void removeFood(String s_name) {
		for (AnimateFood food : listFood) {
			if (food.getName() == s_name) {
				// FishActivity.scene.unregisterTouchArea(food);
				// FishActivity.scene.detachChild(food);
				food.removeFood();
				listFood.remove(food);
				break;
			}
		}
		System.gc();
	}

	// �������� ����, ���������� � LogicController
	public static void removeFish(String fishName) {
		for (AnimateFish fish : listFish) {
			if (fish.getName() == fishName) {
				// FishActivity.scene.unregisterTouchArea(food);
				// FishActivity.scene.detachChild(food);
				fish.removeFish();
				listFish.remove(fish);
				break;
			}
		}
		System.gc();
	}

}
