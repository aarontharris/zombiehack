package com.zhack.gameobject;

import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.zhack.datas.Facing;
import com.zhack.util.TrueEvery;

public class Player extends Geometry {

	private Facing facing;
	private Vector3f trajectory;
	private TrueEvery te010;
	
	private Vector3f prevLocation;
	private Vector3f prevprevLocation;

	public Player() {
		super();
		
		prevLocation = new Vector3f();
		prevLocation.x = getLocalTranslation().x;
		prevLocation.y = getLocalTranslation().y;
		prevLocation.z = getLocalTranslation().z;
		
		prevprevLocation = new Vector3f();
		prevprevLocation.x = getLocalTranslation().x;
		prevprevLocation.y = getLocalTranslation().y;
		prevprevLocation.z = getLocalTranslation().z;
		
		te010 = new TrueEvery(0.1f);
	}

	public void update(float runTime) {
		te010.update(runTime);
		if ( te010.isRipe() ) {
			updateTrajectory();
		}
	}
	
	protected void updateTrajectory() {
		//FIXME trajectory
		
		prevprevLocation.x = prevLocation.x;
		prevprevLocation.y = prevLocation.y;
		prevprevLocation.z = prevLocation.z;
		prevLocation.x = getLocalTranslation().x;
		prevLocation.y = getLocalTranslation().y;
		prevLocation.z = getLocalTranslation().z;
	}

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
