package ru.mocapps.crazyfish.sprite;

import java.util.Random;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import ru.mocapps.crazyfish.exception.FishNotFoundException;
import ru.mocapps.crazyfish.logic.LogicController;

public class AnimateFish extends AnimatedSprite {
	
	private static float angle;
	private String fishName;
	
	public AnimateFish(float pX, float pY, 
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager vertexBufferObjectManager, String fishName) {
		super(pX, pY, pTiledTextureRegion,
				vertexBufferObjectManager);
		this.fishName = fishName;	
		
		
		
	}
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		try {
			this.setX(LogicController.getFish(fishName).getXPosition());
			this.setY(LogicController.getFish(fishName).getYPosition());
			this.setRotation((float) Math.toDegrees(LogicController.getFish(fishName).getRotation() + 90));
		} catch (FishNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}			
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	public String getName(){
		return fishName;
	}
	
}
