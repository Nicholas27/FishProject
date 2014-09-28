package ru.mocapps.crazyfish.main;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import ru.mocapps.crazyfish.logic.FishTypes;
import ru.mocapps.crazyfish.logic.FoodTypes;
import ru.mocapps.crazyfish.logic.LogicController;
import ru.mocapps.crazyfish.sprite.AnimateFish;
import ru.mocapps.crazyfish.sprite.AnimateFood;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class FishLiveActivity extends BaseLiveWallpaperService {

	public static int CAMERA_WIDTH;
	public static int CAMERA_HEIGHT;
	public static int animationDelay = 145;
	private static Random random = new Random();
	private LogicController controller;

	public static Scene scene;
	public static Camera camera;
	public static ITextureRegion fishRegion;
	public static PhysicsWorld physicsWorld;

	// private BitmapTextureAtlas fishHammerTexture;
	// private BitmapTextureAtlas foodCornTexture;
	// public static TiledTextureRegion fishHammerTiledTexture;
	// public static TiledTextureRegion foodCornTiledTexture;

	private ArrayList<BitmapTextureAtlas> foodCornTextures = new ArrayList<BitmapTextureAtlas>();
	private ArrayList<TiledTextureRegion> foodCornTiledTextures = new ArrayList<TiledTextureRegion>();
	private ArrayList<BitmapTextureAtlas> fishTextures = new ArrayList<BitmapTextureAtlas>();
	private ArrayList<TiledTextureRegion> fishTiledTextures = new ArrayList<TiledTextureRegion>();

	private static ArrayList<AnimateFood> listFood = new ArrayList<AnimateFood>();
	private static ArrayList<AnimateFish> listFish = new ArrayList<AnimateFish>();

	public static TiledTextureRegion body;
	public static TiledTextureRegion head;
	public static TiledTextureRegion backnail;
	public static TiledTextureRegion middlenail;
	public static TiledTextureRegion fishHammer2;
	
	public final static String PREFERENCES = "gc986.template.livewallpaper"; // Адрес хранилища настроек 

	@Override
	public void onSurfaceChanged(final GLState pGLState, final int pWidth, final int pHeight) {
		super.onSurfaceChanged(pGLState, pWidth, pHeight);
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		wm.getDefaultDisplay().getRotation();
		// весь предыдущий код нужен для того что бы узнать новые размеры окна
		// так как наша картинка масштабируется по размерам окна
		this.mEngine.getCamera().set(0, 0, pWidth, pHeight); // это и есть та
																// самая строчка
	}

	/** Вычисление пропорции для изображений */
	public float GetTrueProportion(int width, int height, float imgWidth, float imgHeight) {
		// Вычисление пропорций для правильного отображения картинок
		float _Ratio = 0;
		if (imgWidth < imgHeight) {
			_Ratio = (float) width / imgWidth;
		} else {
			_Ratio = (float) height / imgHeight;
		}
		return _Ratio;
	}

	/** Создание движка */
	@Override
	public EngineOptions onCreateEngineOptions() {
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		CAMERA_WIDTH = displayMetrics.widthPixels;
		CAMERA_HEIGHT = displayMetrics.heightPixels;

		// create camera
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);

		// fishAct = this;
		return options;
	}

	private void loadGfx() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		ArrayList<String> foodNames = new ArrayList<String>();
		foodNames.add("corn1.png");
		foodNames.add("corn2.png");
		foodNames.add("corn3.png");
		for (int i = 0; i < foodNames.size(); i++) {
			foodCornTextures.add(new BitmapTextureAtlas(getTextureManager(), 25, 25));
			foodCornTiledTextures.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(foodCornTextures.get(i), this, "food/" + foodNames.get(i), 0, 0, 1, 1));
			foodCornTextures.get(i).load();
		}

		ArrayList<String> fishNames = new ArrayList<String>();
		fishNames.add("hammer-sprite.png");
		fishNames.add("mid-sprite.png");
		for (int i = 0; i < fishNames.size(); i++) {
			fishTextures.add(new BitmapTextureAtlas(getTextureManager(), 440, 200));
			fishTiledTextures.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(fishTextures.get(i), this, "fish/" + fishNames.get(i), 0, 0, 11, 2));
			fishTextures.get(i).load();
		}
	}
	
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		loadGfx();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	
	
	private int getFoodTypeID(FoodTypes type) {
		switch (type) {
		case Corn1:
			return 0;
		case Corn2:
			return 1;
		case Corn3:
			return 2;
		default:
			return 0;
		}
	}

	private int getFishTypeID(FishTypes type) {
		switch (type) {
		case Hammer:
			return 0;
		case Mid:
			return 1;
		case Small:
			return 1;
		default:
			return 0;
		}
	}

	private FoodTypes getRandromFoodType() {
		return FoodTypes.values()[random.nextInt(FoodTypes.values().length)];
	}

	private FishTypes getRandomFishType() {
		return FishTypes.values()[random.nextInt(FishTypes.values().length)];
	}

	public void addFish(float pX, float pY, FishTypes fishType) {
		AnimateFish animateFish;
		String fish_name = LogicController.addFish(pX, pY, fishType);
		animateFish = new AnimateFish(pX, pY, fishTiledTextures.get(getFishTypeID(fishType)), getVertexBufferObjectManager(), mEngine, fish_name);
		Log.d("sys", "fish added");
		// animateFish.animate(animationDelay, true);

		scene.registerTouchArea(animateFish);
		scene.attachChild(animateFish);
		listFish.add(animateFish);
	}

	public void addFood(float xPosition, float yPosition, FoodTypes foodType) {
		AnimateFood foodSprite = null;
		String foodName;
		foodName = LogicController.addFood(xPosition, yPosition);
		Log.d("food", foodName + " added");
		foodSprite = new AnimateFood(xPosition, yPosition, foodCornTiledTextures.get(getFoodTypeID(foodType)), getVertexBufferObjectManager(), mEngine, foodName);
		scene.registerTouchArea(foodSprite);
		scene.attachChild(foodSprite);
		listFood.add(foodSprite);
	}

	
	// удаление еды, вызывается в LogicController
	public static void removeFood(String s_name) {
		for (AnimateFood food : listFood) {
			if (food.getName() == s_name) {
				// FishActivity.scene.unregisterTouchArea(food);
				// FishActivity.scene.detachChild(food);
				Log.d("food", food.getName() + " deleted");
				food.removeFood();
				listFood.remove(food);

				break;
			}
		}
		System.gc();
	}

	// удаление рыбы, вызывается в LogicController
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

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		controller = new LogicController(CAMERA_WIDTH, CAMERA_HEIGHT);
		controller.start();

		float pRed = 0.49f;
		float pGreen = 0.5f;
		float pBlue = 0.84f;

		scene = new Scene();
		scene.setBackground(new Background(pRed, pGreen, pBlue));
		//scene.setOnSceneTouchListener(this);

		this.physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), true);

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.physicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.physicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.physicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.physicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		scene.attachChild(ground);
		scene.attachChild(roof);
		scene.attachChild(left);
		scene.attachChild(right);
		scene.registerUpdateHandler(this.physicsWorld);
		//scene.setOnAreaTouchListener(this);

		addFish(0, 50, FishTypes.Hammer);
		addFish(150, 50, FishTypes.Mid);
		addFish(100, 250, FishTypes.Mid);
		addFish(50, 100, FishTypes.Small);
		addFish(350, 200, FishTypes.Small);
		addFish(0, 50, FishTypes.Hammer);
		addFish(150, 50, FishTypes.Mid);
		addFish(100, 250, FishTypes.Mid);
		addFish(50, 100, FishTypes.Small);
		addFish(350, 200, FishTypes.Small);
		/*
		 * for (int i = 0; i < 100; i++) {
		 * addFish(random.nextInt(CAMERA_WIDTH-1),
		 * random.nextInt(CAMERA_HEIGHT-1), getRandomFishType()); }
		 */
		// Отправляем сообщение что закончили формировать сцену
		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}