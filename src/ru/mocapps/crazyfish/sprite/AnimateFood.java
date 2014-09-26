package ru.mocapps.crazyfish.sprite;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;
import ru.mocapps.crazyfish.logic.LogicController;

public class AnimateFood extends AnimatedSprite {
	private String foodName;
	private Engine mEngine;

	public AnimateFood(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager vertexBufferObjectManager, Engine mEngine, String foodName) {

		super(pX, pY, pTiledTextureRegion, vertexBufferObjectManager);

		this.mEngine = mEngine;
		this.foodName = foodName;
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		try {
			float[] position = LogicController.getFoodPosition(foodName);
			this.setX(position[0]);
			this.setY(position[1]);
			// здесь мен€ютс€ координаты еды
			super.onManagedUpdate(pSecondsElapsed);
		} catch (Exception e) {
			Log.d("logcatch", "ERRR");
		}
	}

	public void removeFood() {
		mEngine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				clearUpdateHandlers();
				detachSelf();
				dispose();
				;
			}
		});

	}

	public String getName() {
		return foodName;
	}
}
