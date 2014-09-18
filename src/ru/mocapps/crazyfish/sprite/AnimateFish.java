package ru.mocapps.crazyfish.sprite;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.text.TickerText;
import org.andengine.entity.text.TickerText.TickerTextOptions;
import org.andengine.entity.text.exception.OutOfCharactersException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;
import org.andengine.util.modifier.IModifier.IModifierListener;

import android.graphics.Typeface;
import android.opengl.GLES20;
import android.util.Log;
import ru.mocapps.crazyfish.exception.FishNotFoundException;
import ru.mocapps.crazyfish.logic.LogicController;
import ru.mocapps.crazyfish.main.FishActivity;

public class AnimateFish extends AnimatedSprite {

	private static float angle;
	private String fishName;
	private int delay = 45;
	private Engine mEngine;
	private final int COUNT_SPRITE = 22;

	private Text indicatorLife;
	private Font mFont;

	private int remain = 0;

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
		
		indicatorLife = new TickerText(0, 0, this.mFont, "TEXT", new TickerTextOptions(10) , vertexBufferObjectManager);
		indicatorLife.registerEntityModifier(
                 new SequenceEntityModifier(
                                 new ParallelEntityModifier(
                                                 new AlphaModifier(10, 0.0f, 1.0f),
                                                 new ScaleModifier(10, 0.5f, 1.0f)
                                 ),
                                 new RotationModifier(5, 0, 360)
                                 
                 )
                 
                 
		 );
		indicatorLife.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.attachChild(indicatorLife);

		
	}

	private void loadRes() {

		this.mFont = FontFactory.create(mEngine.getFontManager(),
				mEngine.getTextureManager(), 473, 200,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		try {
			this.setX(LogicController.getFish(fishName).getXPosition());
			this.setY(LogicController.getFish(fishName).getYPosition());
			this.setRotation((float) Math.toDegrees(LogicController.getFish(
					fishName).getRotation() + 90));

		} catch (FishNotFoundException e1) {
			e1.printStackTrace();
		}
		super.onManagedUpdate(pSecondsElapsed);
	}

	public void removeFish() {
		mEngine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				FishActivity.fishAct.addFish(0, 0);
				FishActivity.fishAct.addFish(110, 0);

				clearUpdateHandlers();
				detachSelf();
				dispose();				
			}
		});
	}

	public String getName() {
		return fishName;
	}

}
