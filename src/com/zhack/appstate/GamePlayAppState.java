package com.zhack.appstate;

import java.util.HashSet;
import java.util.Set;

import jme3tools.optimize.GeometryBatchFactory;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.WrapMode;
import com.zhack.ZombieHack;
import com.zhack.control.CreepControl;
import com.zhack.gameobject.PlayerBase;
import com.zhack.gameobject.Tower;

public class GamePlayAppState extends AbstractAppState implements CommonAppState {
	private SimpleApplication app;
	private Camera cam;
	private Node rootNode;
	private AssetManager assetManager;
	private Set<Spatial> spatialMess = new HashSet<Spatial>();
	private Set<CreepControl> creeps = new HashSet<CreepControl>();
	private PlayerBase player;

	private int level;
	private int score;
	private boolean lastGameWon;

	// private PointLight camLight;

	@Override
	public void initialize( AppStateManager _stateManager, Application _app ) {
		super.initialize( _stateManager, _app );
		this.app = (SimpleApplication) _app;
		this.cam = this.app.getCamera();
		this.rootNode = this.app.getRootNode();
		this.assetManager = this.app.getAssetManager();

		app.setDisplayFps( true );
		app.setDisplayStatView( true );

		cam.setLocation( Vector3f.ZERO );

		// DirectionalLight sun = new DirectionalLight();
		// PointLight sun = new PointLight();
		// sun.setPosition( new Vector3f( 0, 10, 5 ) );
		// sun.setRadius( 100f );
		// sun.setDirection( new Vector3f( 1, 0, -2 ) );
		// sun.setColor( ColorRGBA.White );
		// rootNode.addLight( sun );
		AmbientLight ambient = new AmbientLight();
		ambient.setColor( ColorRGBA.White );
		rootNode.addLight( ambient );

		// camLight = new PointLight();
		// camLight.setRadius( 10f );
		// camLight.setColor( ColorRGBA.White );
		// rootNode.addLight( camLight );

		// DirectionalLightShadowRenderer dslr = new DirectionalLightShadowRenderer( assetManager, 1024, 2 );
		// PointLightShadowRenderer pslr = new PointLightShadowRenderer( assetManager, 1024 );
		// pslr.setLight( sun );
		// app.getViewPort().addProcessor( pslr );
		rootNode.setShadowMode( ShadowMode.Off );

		if ( false ) { // Floor
			Geometry floor = new Geometry(); // Utl.createBoxGeom( Vector3f.ZERO, 16.5f, 0.5f, 16.5f, ColorRGBA.Orange, "PlayerBase" );
			floor.setShadowMode( ShadowMode.Off );
			floor.setMesh( new Box( 1, 1, 1 ) );
			floor.scale( 16.5f, 1f, 16.5f );
			floor.move( 0, -2.0f, 10 );
			Material mat = new Material( ZombieHack.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md" );
			// mat.setBoolean( "UseMaterialColors", true );
			mat.setColor( "Diffuse", ColorRGBA.Orange );
			mat.setColor( "Ambient", ColorRGBA.DarkGray );
			mat.setTexture( "DiffuseMap", ZombieHack.getInstance().getAssetManager().loadTexture( "Textures/floor.jpg" ) );
			mat.getTextureParam( "DiffuseMap" ).getTextureValue().setWrap( WrapMode.Repeat );
			floor.getMesh().scaleTextureCoordinates( new Vector2f( 8, 8 ) );
			// mat.setColor( "Specular", ColorRGBA.White );
			// mat.setFloat( "Shininess", 8f );
			floor.setMaterial( mat );
			rootNode.attachChild( addToMess( floor ) );
		} else {
			generateFloor();
		}

		player = PlayerBase.obtainPlayerBase( "Player", 0, 0, 0 );
		if ( false ) { // player
			Node playerNode = new Node();
			playerNode.attachChild( player );
			rootNode.attachChild( addToMess( playerNode ) );
		}

		if ( false ) { // towers
			Node towerNode = new Node();
			towerNode.attachChild( Tower.obtainTower( "tower", -5, 2f, 3 ) );
			towerNode.attachChild( Tower.obtainTower( "tower", 5, 2, 3 ) );
			towerNode.attachChild( Tower.obtainTower( "tower", -5, 2, 6 ) );
			towerNode.attachChild( Tower.obtainTower( "tower", 5, 2, 6 ) );
			towerNode.attachChild( Tower.obtainTower( "tower", -5, 2, 9 ) );
			towerNode.attachChild( Tower.obtainTower( "tower", 5, 2, 9 ) );
			rootNode.attachChild( addToMess( towerNode ) );
		}

		if ( false ) { // Creeps
			Node creepNode = null;
			CreepControl creepControl = null;

			for ( int x : new int[] { -3, -2, -1, 0, 1, 2, 3 } ) {
				for ( int y : new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 } ) {
					for ( int z = 20; z <= 20; z++ ) {
						String name = "Creep_" + x + "_" + y + "_" + z;
						creepNode = new Node(); // FIXME: POOL
						creepNode.attachChild( CreepControl.obtain( name, x, y, z ).getSpatial() );
						rootNode.attachChild( addToMess( creepNode ) );
						creeps.add( creepNode.getChild( 0 ).getControl( CreepControl.class ) );
					}
				}
			}
		}
	}

	private boolean isObjectAtLocation( float x, float y, float z ) {
		// hack to hide all cubes that are not at the top
		if ( y == 0 ) {
			return false;
		}
		return true;
	}

	public static final int MAPSIZE_X = 1000;
	public static final int MAPSIZE_Y = 1000;
	public static final int MAPSIZE_Z = 1000;

	public static final int STD_CHUNK_X = 10;
	public static final int STD_CHUNK_Y = 10;
	public static final int STD_CHUNK_Z = 10;
	public static final int STD_CHUNK_XOFF = -STD_CHUNK_X / 2;
	public static final int STD_CHUNK_YOFF = -STD_CHUNK_Y / 2;
	public static final int STD_CHUNK_ZOFF = -STD_CHUNK_Z / 2;

	/** STD_CHUNK_X x STD_CHUNK_Y x STD_CHUNK_Z centered */
	private void drawChunk( float x, float y, float z ) {
		Material mat = new Material( ZombieHack.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md" );
		Texture texture = ZombieHack.getInstance().getAssetManager().loadTexture( "Textures/floor.jpg" );
		// Texture texture = TowerDefDemo.getInstance().getAssetManager().loadTexture( "Textures/placeholder_block.png" );
		// Texture texture = TowerDefDemo.getInstance().getAssetManager().loadTexture( "Textures/assets/minecraft/Textures/blocks/brick.png" );

		 texture.setMagFilter( MagFilter.Nearest );
		mat.setTexture( "ColorMap", texture );
		// mat.setColor( "Color", ColorRGBA.Green );
		Box mesh = new Box( .5f, .5f, .5f );
		Geometry geom = new Geometry();
		geom.setCullHint( CullHint.Dynamic );
		geom.setShadowMode( ShadowMode.Off );
		drawChunk( STD_CHUNK_X, STD_CHUNK_Y, STD_CHUNK_Z, STD_CHUNK_XOFF, STD_CHUNK_YOFF, STD_CHUNK_ZOFF, x, y, z, mat, mesh, geom );
	}

	/**
	 * <pre>
	 * 
	 * 0123456789  <--- cube index
	 * oooooooooo  <--- one row on the X axis, 10 cubes wide
	 * |    |   |
	 * |    |   Right Aligned: xOff == -9 (zero-based, 10 items)
	 * |    |
	 * |    Centered: xOff == -5 (-width/2)
	 * |
	 * Left Aligned: xOff == 0
	 * 
	 * Summary: You are specifying the x-starting-position (xOff) relative to the x-origin (x).
	 * 
	 * </pre>
	 * 
	 * @param w - width of the chunk
	 * @param h - height of the chunk
	 * @param d - depth of the chunk
	 * @param xOff - Starting position of the chunk-x, relative to param x. Let w==10 then x-align-left==0, x-centered==-5, x-align-right==-10.
	 * @param yOff - follows same pattern as cenX but regards h.
	 * @param zOff - follows same pattern as cenX but regards d.
	 * @param x - the x position based on cenX
	 * @param y the y position based on cenY
	 * @param z the z position based on cenZ
	 */
	private void drawChunk( int w, int h, int d, float xOff, float yOff, float zOff, float x, float y, float z, Material mat, Mesh mesh, Geometry geom ) {
		// draw one chunk at a time in order of z, y, x
		for ( int zIdx = 0; zIdx < d; zIdx++ ) {
			Node chunkNode = new Node(); // GBF.optimize works better in sheets of Z planes
			for ( int yIdx = 0; yIdx < h; yIdx++ ) {
				for ( int xIdx = 0; xIdx < w; xIdx++ ) {
//					Geometry copy = geom.clone(); // cloning seems to help
					Geometry copy = new Geometry();
					copy.setMesh( mesh ); // i set mesh here rather than instantiation of the Geometry because I'm trying to promote reuse of the single mesh not copy
					copy.setMaterial( mat );
					copy.setLocalTranslation( x + xOff + xIdx, y + yOff + yIdx, z + zOff + zIdx );

					// Cull any objects not on the surface
					// if ( isObjectAtLocation( copy.getLocalTranslation().x, copy.getLocalTranslation().y + 1, copy.getLocalTranslation().z ) ) {
					// copy.setCullHint( CullHint.Always );
					// }
					chunkNode.attachChild( copy );
				}
			}
//			Node optimizedChunkNode = (Node) GeometryBatchFactory.optimize( chunkNode ); // GBF.optimize works better in sheets of Z planes. EDIT: except when u look down :(
//			rootNode.attachChild( optimizedChunkNode );
			rootNode.attachChild( chunkNode );
		}
	}

	private void generateFloor() {
		USING_CHUNKS_generateFloor();
	}

	private void USING_CHUNKS_generateFloor() {
		int wChunk = 1;
		int hChunk = 1;
		int dChunk = 1;

		int x = 0;
		int y = -hChunk * STD_CHUNK_Y / 2; // move the block down below my feet
		int z = 0;

		int xOff = -STD_CHUNK_X * ( wChunk / 2 );
		int yOff = -STD_CHUNK_Y * ( hChunk / 2 );
		int zOff = -STD_CHUNK_Z * ( dChunk / 2 );

		for ( int zIdx = 0; zIdx < dChunk; zIdx++ ) {
			for ( int yIdx = 0; yIdx < hChunk; yIdx++ ) {
				for ( int xIdx = 0; xIdx < wChunk; xIdx++ ) {
					drawChunk( x + xOff + STD_CHUNK_X * xIdx, y + yOff + STD_CHUNK_Y * yIdx, z + zOff + STD_CHUNK_Z * zIdx );
				}
			}
		}
	}

	// what made this blazing fast?
	// 1. I started using the GeometryBatchFactory but that didn't do a lot at first.
	// 2. Then I switched from foreach x, y, z to foreach z, y, x and that helped A LOT A LOT.
	// 3. Then as I upped the number of blocks I discovered very slow loading and imagined i was running into memory issues overdoing the optimization
	// -- so I decided to optimize each Z plane and now it loads fast and renders fast...
	private void OLD_BUT_WORKS_WELL_generateFloor() {
		int w = 100;
		int h = 25;
		int d = 100;
		Box box = new Box( .5f, .5f, .5f );
		// Material mat = new Material( TowerDefDemo.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md" );
		// mat.setColor( "Diffuse", ColorRGBA.Gray );
		// mat.setColor( "Ambient", ColorRGBA.DarkGray );
		// mat.setTexture( "DiffuseMap", TowerDefDemo.getInstance().getAssetManager().loadTexture( "Textures/floor.jpg" ) );
		// mat.getTextureParam( "DiffuseMap" ).getTextureValue().setWrap( WrapMode.EdgeClamp );

		Material mat = new Material( ZombieHack.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md" );
		mat.setTexture( "ColorMap", ZombieHack.getInstance().getAssetManager().loadTexture( "Textures/floor.jpg" ) );

		Geometry tile = new Geometry();
		tile.setCullHint( CullHint.Dynamic );
		tile.setShadowMode( ShadowMode.Off );

		for ( int z = -d / 2; z < d / 2; z++ ) {
			Node tmp = new Node();

			for ( int x = -w / 2; x < w / 2; x++ ) {
				for ( int y = 0; y <= h; y++ ) {
					Geometry copy = tile.clone(); // cloning seems to help
					copy.setMesh( box );
					copy.setMaterial( mat );
					copy.setLocalTranslation( x, -1 - y, z );
					tmp.attachChild( copy );
				}
			}

			Node optimized = (Node) GeometryBatchFactory.optimize( tmp );
			rootNode.attachChild( optimized );
		}

	}

	private void TINKERING_generateFloor() {
		int w = 37;
		int h = 37;
		int d = 37;
		Box box = new Box( .5f, .5f, .5f );
		// Material mat = new Material( TowerDefDemo.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md" );
		// mat.setColor( "Diffuse", ColorRGBA.Gray );
		// mat.setColor( "Ambient", ColorRGBA.DarkGray );
		// mat.setTexture( "DiffuseMap", TowerDefDemo.getInstance().getAssetManager().loadTexture( "Textures/floor.jpg" ) );
		// mat.getTextureParam( "DiffuseMap" ).getTextureValue().setWrap( WrapMode.EdgeClamp );

		Material mat = new Material( ZombieHack.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md" );
		mat.setTexture( "ColorMap", ZombieHack.getInstance().getAssetManager().loadTexture( "Textures/floor.jpg" ) );

		Geometry tile = new Geometry();
		tile.setCullHint( CullHint.Dynamic );
		tile.setShadowMode( ShadowMode.Off );

		for ( int z = 0; z < d; z++ ) {
			Node tmp = new Node();

			for ( int y = 0; y < h; y++ ) {
				for ( int x = 0; x < w; x++ ) {
					System.out.printf( "%s,%s,%s\n", x, y, z );
					Geometry copy = tile.clone(); // cloning seems to help
					copy.setMesh( box );
					copy.setMaterial( mat );
					copy.setLocalTranslation( x - w / 2, -1 - y, z - d / 2 ); // center
					tmp.attachChild( copy );
				}
			}

			Node optimized = (Node) GeometryBatchFactory.optimize( tmp );
			rootNode.attachChild( optimized );
		}

	}

	@Override
	public void update( float tpf ) {
		super.update( tpf );
		// camLight.setPosition( cam.getLocation() );
	}

	@Override
	public void cleanup() {
		super.cleanup();
		creeps.clear();

		// clean up mess
		for ( Spatial s : spatialMess ) {
			rootNode.detachChild( s ); // FIXME leak - creepNodes aren't cleared until game over -- they should get cleared on death
		}
	}

	private Spatial addToMess( Spatial spatial ) {
		spatialMess.add( spatial );
		return spatial;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel( int level ) {
		this.level = level;
	}

	public int getScore() {
		return score;
	}

	public void setScore( int score ) {
		this.score = score;
	}

	public boolean isLastGameWon() {
		return lastGameWon;
	}

	public void setLastGameWon( boolean lastGameWon ) {
		this.lastGameWon = lastGameWon;
	}

	@Override
	public PlayerBase getPlayer() {
		return player;
	}

	@Override
	public Set<CreepControl> getCreeps() {
		return creeps;
	}
}
