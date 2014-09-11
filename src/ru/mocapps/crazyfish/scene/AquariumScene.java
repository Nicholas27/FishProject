package ru.mocapps.crazyfish.scene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;

import android.util.Log;

import ru.mocapps.crazyfish.main.FishActivity;

public class AquariumScene extends CameraScene
{
	
	
	private BitmapTextureAtlas splashTA;
	private ITextureRegion splashTR;

	public AquariumScene() {
		super(FishActivity.camera);
		setBackground(new Background(1,1,1));
		
		//loadResources();	
	
		//createScene();
	
	}
	
	
	public void loadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		splashTA = new BitmapTextureAtlas(SceneManager.activity.getTextureManager(),
				32, 32);
		splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTA, SceneManager.activity, "face_box.png", 0, 0);
		splashTA.load();
	}
	
	public void createScene()
	{
		
		
		Sprite icon = new Sprite(0, 0, splashTR, SceneManager.engine.getVertexBufferObjectManager());
		icon.setPosition((SceneManager.camera.getWidth()-icon.getWidth())/2, (SceneManager.camera.getHeight() - icon.getHeight())/2);
		attachChild(icon);
		
	}
	

	
	
	public static void onCreateResources(SimpleBaseGameActivity activity){
/*
		// upper pipe		
		BitmapTextureAtlas upperPipeTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 130, 60, TextureOptions.BILINEAR);
		TextureRegion mUpperPipeTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperPipeTextureAtlas, activity, "pipeupper.png", 0, 0);
		upperPipeTextureAtlas.load();

		// upper pipe section	
		BitmapTextureAtlas upperPipeSectionTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 120, 1, TextureOptions.BILINEAR);
		TextureRegion mUpperPipeSectionTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperPipeSectionTextureAtlas, activity, "pipesectionupper.png", 0, 0);
		upperPipeSectionTextureAtlas.load();


		// lower pipe		
		BitmapTextureAtlas lowerPipeTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 130, 60, TextureOptions.BILINEAR);
		TextureRegion mLowerPipeTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(lowerPipeTextureAtlas, activity, "pipelower.png", 0, 0);
		lowerPipeTextureAtlas.load();

		// lower pipe section	
		BitmapTextureAtlas lowerPipeSectionTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 120, 1, TextureOptions.BILINEAR);
		TextureRegion mLowerPipeSectionTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(lowerPipeSectionTextureAtlas, activity, "pipesectionlower.png", 0, 0);
		lowerPipeSectionTextureAtlas.load();*/
	}
	
	
	public void Show()
	{
		setVisible(true);
		setIgnoreUpdate(false);
	}
	
	public void Hide()
	{
		setVisible(false);
		setIgnoreUpdate(true);
	}
}
