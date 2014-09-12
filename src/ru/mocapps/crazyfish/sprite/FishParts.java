package ru.mocapps.crazyfish.sprite;

import org.andengine.engine.Engine;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ru.mocapps.crazyfish.main.FishActivity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class FishParts {

	
	private float pX;
	private float pY;
	private  VertexBufferObjectManager pVertexBufferObjectManager;
	private Scene scene;
	
	public FishParts(float startX, float startY, VertexBufferObjectManager pVertexBufferObjectManager, Scene scene)
	{
		this.pX = startX;
		this.pY = startY;
		
		this.pVertexBufferObjectManager = pVertexBufferObjectManager;
		this.scene = scene;
		
		createFish();
	}
	
	float pos = 0;
	float iteral = 1;
	
	public void createFish() {

		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(10, 0.2f, 0.5f);

		final Sprite head = new Sprite(pX, pY, FishActivity.head, pVertexBufferObjectManager);	
		final Body anchorHead = PhysicsFactory.createBoxBody(FishActivity.physicsWorld, head, BodyType.StaticBody, objectFixtureDef);

		final Sprite body = new Sprite(pX, pY + head.getHeight() / 2, FishActivity.body, pVertexBufferObjectManager);
		final Body anchorBody = PhysicsFactory.createCircleBody(FishActivity.physicsWorld, body, BodyType.StaticBody, objectFixtureDef);
		

		final Sprite moddlenail = new Sprite(pX + body.getWidth() / 4, pY + body.getHeight(), FishActivity.middlenail, pVertexBufferObjectManager);
		final Body anchorMiddleNail = PhysicsFactory.createCircleBody(FishActivity.physicsWorld, moddlenail, BodyType.StaticBody, objectFixtureDef);
		
		
		
		scene.attachChild(head);
		scene.attachChild(body);
		scene.attachChild(moddlenail);


		final Line connectionLine = new Line(pX + 100, pY + 200, 100, 320,  pVertexBufferObjectManager);
		scene.attachChild(connectionLine);

		
		FishActivity.physicsWorld.registerPhysicsConnector(new PhysicsConnector(body, anchorBody, true, true)
		{
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				
				
				body.setPosition(120, pos);
				
				pos += iteral;
				
			/*	final Vector2 movingBodyWorldCenter = anchorBody.getWorldCenter();
				connectionLine.setPosition(connectionLine.getX1(), connectionLine.getY1(), movingBodyWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, movingBodyWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			*/
				
				
			}		
		});
		FishActivity.physicsWorld.registerPhysicsConnector(new PhysicsConnector(head, anchorHead, true, true));
		
		
		RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
		revoluteJointDef.initialize(anchorBody, anchorHead, anchorBody.getWorldCenter());
		/*revoluteJointDef.enableMotor = true;
		revoluteJointDef.motorSpeed = 10;
		revoluteJointDef.maxMotorTorque = 200;*/
		FishActivity.physicsWorld.createJoint(revoluteJointDef);
		
		/*revoluteJointDef = new RevoluteJointDef();
		revoluteJointDef.initialize(anchorBody, anchorMiddleNail, anchorBody.getWorldCenter());
		FishActivity.physicsWorld.createJoint(revoluteJointDef);*/
		

	}
	
}
