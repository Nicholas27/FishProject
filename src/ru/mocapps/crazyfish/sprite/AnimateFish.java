package ru.mocapps.crazyfish.sprite;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.text.exception.OutOfCharactersException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import android.graphics.Typeface;
import android.util.Log;
import ru.mocapps.crazyfish.exception.FishNotFoundException;
import ru.mocapps.crazyfish.logic.LogicController;

public class AnimateFish extends AnimatedSprite {

	private static float angle;
	private String fishName;
	private int delay = 45;
	private Engine mEngine;
	private final int COUNT_SPRITE = 22;
	
	private Text indicatorLife;
	private Font  mFont; 

	
	public AnimateFish(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager vertexBufferObjectManager, Engine mEngine, String fishName) {
		super(pX, pY, pTiledTextureRegion, vertexBufferObjectManager);
		
		
		this.fishName = fishName;
		this.mEngine = mEngine;
			
		long[] arrDelay = new long[COUNT_SPRITE];
		
		for(int i = 0; i < arrDelay.length; i++)
		{
			arrDelay[i] = delay;
		}
		
		int[] arrFrame = new int[COUNT_SPRITE];
		Random rand = new Random();
		int curFrame = rand.nextInt(COUNT_SPRITE);
		Log.d("curFrame ANIMATEFISH", curFrame + "");
		for(int i = 0; i < arrFrame.length; i++)
		{
			arrFrame[i] = curFrame;
			curFrame++;
			if(curFrame ==  COUNT_SPRITE)
			{
				curFrame = 0;
			}
		}
		
		for(int i = 0; i < arrFrame.length; i++)
		{
			Log.d("curFrame ANIMATEFISH", fishName + " " +  arrFrame[i]);

		}
		
		this.animate(arrDelay, arrFrame);
		
		loadRes();
		
		indicatorLife = new Text(pX, pY, this.mFont, "111", new TextOptions(HorizontalAlign.CENTER), vertexBufferObjectManager);
		this.attachChild(indicatorLife);

		
	}
	
	private void loadRes()
	{
		
		this.mFont = FontFactory.create(mEngine.getFontManager(), mEngine.getTextureManager(), 473, 200, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		try {
			this.setX(LogicController.getFish(fishName).getXPosition());
			this.setY(LogicController.getFish(fishName).getYPosition());
			this.setRotation((float) Math.toDegrees(LogicController.getFish(
					fishName).getRotation() + 90));
			
			
			/*mEngine.runOnUpdateThread(new Runnable() {

				@Override
				public void run() {
					try {
						indicatorLife.setText(Float.toString(LogicController.getFish(fishName).getLife()));
					} catch (OutOfCharactersException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FishNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});*/
			
			
		} catch (FishNotFoundException e1) {
			e1.printStackTrace();
		}
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	public void removeFish(){
		mEngine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				clearUpdateHandlers();
				detachSelf();
				dispose();;
			}
		});
	}
	
	public String getName() {
		return fishName;
	}

}
