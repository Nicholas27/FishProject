package ru.mocapps.crazyfish.sprite;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ru.mocapps.crazyfish.logic.LogicController;

public class AnimateFood extends AnimatedSprite {
	private String foodName;
	
	public AnimateFood(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager vertexBufferObjectManager, String foodName) {
		
		super(pX, pY, pTiledTextureRegion, vertexBufferObjectManager);
		this.foodName = foodName;
	}
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		this.setX(LogicController.getFood(foodName).getXPosition());
		this.setY(LogicController.getFood(foodName).getYPosition());
		// здесь мен€ютс€ координаты еды
		super.onManagedUpdate(pSecondsElapsed);
	}

	public String getName(){
		return foodName;
	}
}
