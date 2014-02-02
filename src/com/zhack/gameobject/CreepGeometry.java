package com.zhack.gameobject;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture.WrapMode;
import com.zhack.ZombieHack;
import com.zhack.util.Pool;
import com.zhack.util.Utl;

public class CreepGeometry extends Geometry {
	// FIXME: @TIP i just realized why these properties should be in the control.
	// by placing properties of the control in the control, that control can now be
	// applied to any object. Just by adding a CreepControl to a random Geometry,
	// that geometry will now become a creep - so it makes sense that the dependencies of that behavior (control)
	// are solely tied to the behavior
	// A compromise may be - generic controls that can be applied to many, should be self contained
	// but controls that are specific to a singular geometry can be pulled apart?
	//
	// With this thinking, now it makes sense that there should be an abstraction between controls and geometry.
	// As well, events should dealt with by the control when specific to the control and less by the EventMgr
	// private int health;
	// private int index;

	private CreepGeometry() {
		super();
		setMesh( Utl.box );
		scale( .25f, .25f, .25f );

		Material mat = null;
		boolean shaded = false;
		if ( shaded ) {
			mat = new Material( ZombieHack.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md" );
			// mat.setBoolean( "UseMaterialColors", true );
			mat.setColor( "Diffuse", ColorRGBA.Gray );
			mat.setColor( "Ambient", ColorRGBA.DarkGray );
			mat.setTexture( "DiffuseMap", ZombieHack.getInstance().getAssetManager().loadTexture( "Textures/floor.jpg" ) );
			mat.getTextureParam( "DiffuseMap" ).getTextureValue().setWrap( WrapMode.Repeat );
			// mat.setColor( "Specular", ColorRGBA.White );
			// mat.setFloat( "Shininess", 8f );
		} else {
			mat = new Material( ZombieHack.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md" );
			mat.setTexture( "ColorMap", ZombieHack.getInstance().getAssetManager().loadTexture( "Textures/floor.jpg" ) );
		}
		setMaterial( mat );
	}

	/**
	 * best to not create directly, instead obtain from Pool: {@link Pool#creep(String, float, float, float)}
	 */
	public static CreepGeometry obtainNewCreep( String name, float x, float y, float z ) {
		CreepGeometry geom = new CreepGeometry();
		geom.setName( name );
		geom.setLocalTranslation( x, y, z );
		return geom;
	}

	/** prepare for reuse prior to being placed back into the pool */
	public void destroy() {
		// What about the random controls added at runtime? how do we clear them? do we have to keep track of them? :(
		// I figured it out: Spatial.getNumControls() and Spatial.getControl( index ), these allow you to iterate
	}

}
