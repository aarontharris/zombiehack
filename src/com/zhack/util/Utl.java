package com.zhack.util;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.zhack.ZombieHack;

/** Call Utl.init() once when your app starts before it is used */
public final class Utl {
	private static Utl self;
	private static SimpleApplication app;

	private Utl() {
	}

	public static void init( SimpleApplication app ) {
		Utl.self = new Utl();
		self.app = app;
	}

	public static final Box box = new Box( 1, 1, 1 ); // Optimization to reuse the 1x1x1 and just scale the geom. Wont work well with textures. In that case you may cache one of
														// each
														// mesh.

	public static Geometry createBoxGeom( final Vector3f location, float w, float h, float d, ColorRGBA color, String name ) {
		Geometry geom = new Geometry( name, box );
		geom.scale( w, h, d );
		Material mat = new Material( app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md" );
		mat.setBoolean( "UseMaterialColors", true );
		mat.setColor( "Diffuse", ColorRGBA.Gray );
		mat.setColor( "Ambient", ColorRGBA.DarkGray );
		mat.setColor( "Specular", ColorRGBA.White );
		mat.setFloat( "Shininess", 8f );
		geom.setMaterial( mat );
		geom.setLocalTranslation( location );
		return geom;
	}

	/**
	 * <pre>
	 * Let A be the position of towardsMe
	 * Let B be the position of moveMe
	 * A - B gives us a vector ( not a position ) consisting of a direction and magnitude in the form of the amount of x, y and z to move B to A.
	 * We want the direction, but we want to change the magnitude to dist provide by the caller.
	 * To do this we can normalize the vector so that the amount of x, y and z are relative to 1 unit, not the distance between B to A.
	 * Now that the vector is normalized to 1, we can multiply 1 by the dist param so that the vector represents 1 * dist = dist.
	 * 
	 * Note: if dist is unchanged frame after frame, the velocity will be constant, not smooth and attenuated. 
	 * Note: if you want attenuated speed, you'll needto adjust the value of the dist param.
	 * </pre>
	 * 
	 * @param moveMe
	 * @param towardsMe
	 * @param dist distance for this frame. 5 will move 5 units. 5/tpf will move 5 units per second.
	 */
	public static void moveTowards( Spatial moveMe, Vector3f towardsMe, float dist ) {
		Vector3f direction = null;
		try {
			direction = Pool.vector3f( 0, 0, 0 );
			direction = towardsMe.subtract( moveMe.getLocalTranslation(), direction ); // A - B = B's direction towards A with a magnitude of their distance
			direction = direction.normalizeLocal(); // scale direction's magnitude to be relative to 1WU so we can mult 1 x dist = dist.
			moveMe.move( direction.mult( dist ) ); // move A dist units in the direction of A-B.
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			Pool.release( direction );
		}
	}

	public static void moveTowards( Spatial moveMe, Spatial towardsMe, float dist ) {
		moveTowards( moveMe, towardsMe.getLocalTranslation(), dist );
	}
}
