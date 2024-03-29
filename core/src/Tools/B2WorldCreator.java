		package Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;

public class B2WorldCreator {
	
	public B2WorldCreator(World world, TiledMap map) {
		BodyDef bdef = new BodyDef(); 					/* For creating		*/
		PolygonShape shape = new PolygonShape();		/* Platform Physics	*/
		FixtureDef fdef = new FixtureDef();				/* For Mario        */
		Body body;										/* In World       	*/
		
		
		//For ground, copy and do for other object layers
		for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			 
			
			
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth()/2f) /MyGdxGame.PPM, (rect.getY() + rect.getHeight()/2f )  / 100f);
			
			body = world.createBody(bdef);
			
			shape.setAsBox((rect.getWidth() ) /2f /MyGdxGame.PPM , (rect.getHeight()) /2f / MyGdxGame.PPM);// reference starts in the center, and expands both sides outwrds;
			fdef.shape = shape;
			body.createFixture(fdef);
		}
	}

}
