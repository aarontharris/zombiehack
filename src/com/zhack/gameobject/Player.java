package com.zhack.gameobject;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture.WrapMode;
import com.zhack.ZombieHack;
import com.zhack.event.EventMgr;

public class Player extends Geometry {

	// public static Mesh mesh = new Box( 1, 1, 1 );
	// static {
	// mesh.scaleTextureCoordinates( new Vector2f( 2, 1 ) );
	// }
	//
	// private int index = 0;
	// private int health = 0;
	// private int chargeNum = 0;
	//
	// private Player() {
	// setMesh( mesh );
	// scale( 2f, 1, 1f );
	// Material mat = new Material( ZombieHack.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md" );
	// mat.setBoolean( "UseMaterialColors", true );
	// mat.setColor( "Diffuse", ColorRGBA.Yellow );
	// mat.setColor( "Ambient", ColorRGBA.DarkGray );
	// mat.setTexture( "DiffuseMap", ZombieHack.getInstance().getAssetManager().loadTexture( "Textures/floor.jpg" ) );
	// mat.getTextureParam( "DiffuseMap" ).getTextureValue().setWrap( WrapMode.Repeat );
	// setMaterial( mat );
	// // addControl( new TowerControl() );
	// }
	//
	// public static Player obtainPlayerBase( String name, float x, float y, float z ) {
	// Player geom = new Player();
	// geom.setName( name );
	// geom.setLocalTranslation( x, y, z );
	// geom.setHealth( 100 );
	// return geom;
	// }
	//
	// public int getHealth() {
	// return health;
	// }
	//
	// public void setHealth( int health ) {
	// int prevHealth = this.health;
	// this.health = health;
	// EventMgr.getInstance().onPlayerHealthChange( prevHealth, this.health );
	// }
	//
	// public int getIndex() {
	// return index;
	// }
	//
	// public void setIndex( int index ) {
	// this.index = index;
	// }
	//
	// public int getChargeNum() {
	// return chargeNum;
	// }
	//
	// public void setChargeNum( int chargeNum ) {
	// this.chargeNum = chargeNum;
	// }
}
