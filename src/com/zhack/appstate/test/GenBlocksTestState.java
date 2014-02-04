package com.zhack.appstate.test;

import jme3tools.optimize.GeometryBatchFactory;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Ray;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.zhack.appstate.BaseTestAppState;
import com.zhack.datas.BlockType;
import com.zhack.datas.Chunk;
import com.zhack.datas.Sector;
import com.zhack.datas.World;
import com.zhack.gameobject.Player;
import com.zhack.services.MaterialService;
import com.zhack.services.MeshService;
import com.zhack.services.MeshService.MeshType;
import com.zhack.ui.CursorBox;
import com.zhack.ui.UI;
import com.zhack.util.DoEvery;
import com.zhack.util.Utl;

public class GenBlocksTestState extends BaseTestAppState {

	private World world = World.getInstance();
	private Player player;

	private MaterialService matSvc = MaterialService.getInstance();
	private MeshService meshSvc = MeshService.getInstance();

	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean isPressed, float tpf) {
			if (name == ACTION_QUIT && !isPressed) { // on release
				app.stop();
			}
			// else if ( name == ACTION_FORWARD && isPressed ) {
			// player.move(0, 0, tpf);
			// } else if ( name == ACTION_BACKWARD && isPressed ) {
			// player.move(0, 0, -tpf);
			// } else if ( name == ACTION_LEFTWARD && isPressed ) {
			// player.move(tpf, 0, 0);
			// } else if ( name == ACTION_RIGHTWARD && isPressed ) {
			// player.move(-tpf, 0, 0);
			// }
		}
	};

	private AnalogListener analogListener = new AnalogListener() {
		private Ray camRay = new Ray();
		private CollisionResults camRayCollisionResults = new CollisionResults();

		public void onAnalog(String name, float intensity, float tpf) {
			if (name == ACTION_PRIMARY_ACTION) {
				camRay.setOrigin(cam.getLocation());
				camRay.setDirection(cam.getDirection());// rather than new Vector3f
				camRayCollisionResults.clear(); // clear before use
				rootNode.collideWith(camRay, camRayCollisionResults);

				CollisionResult collision = Utl.getFirstCollision(camRayCollisionResults, cursorBox.getGeometry());
				if (collision != null) {
					Geometry target = collision.getGeometry();
					target.removeFromParent();
				}
			}
		}
	};

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);

		player = new Player();
		
		initInputs();
		UI.drawCrosshair(this.app);
		cursorBox = new CursorBox(rootNode);
		cursorBox.enableDebugHUD(this.app.getGuiNode()); // DEBUG
		testDrawChunk();
	}

	private static final Trigger INPUT_QUIT = new KeyTrigger(KeyInput.KEY_ESCAPE);
	private static final Trigger INPUT_FORWARD = new KeyTrigger(KeyInput.KEY_W);
	private static final Trigger INPUT_BACKWARD = new KeyTrigger(KeyInput.KEY_S);
	private static final Trigger INPUT_LEFTWARD = new KeyTrigger(KeyInput.KEY_A);
	private static final Trigger INPUT_RIGHTWARD = new KeyTrigger(KeyInput.KEY_D);
	private static final Trigger INPUT_PRIMARY_ACTION = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);

	private static final String ACTION_QUIT = "ACTION_QUIT";
	private static final String ACTION_FORWARD = "ACTION_FORWARD";
	private static final String ACTION_BACKWARD = "ACTION_BACKWARD";
	private static final String ACTION_LEFTWARD = "ACTION_LEFTWARD";
	private static final String ACTION_RIGHTWARD = "ACTION_RIGHTWARD";
	private static final String ACTION_PRIMARY_ACTION = "ACTION_PRIMARY_ACTIO";
	private CursorBox cursorBox;

	private void initInputs() {
		// app.getInputManager().clearMappings();
		// app.getInputManager().clearRawInputListeners();

		app.getInputManager().addMapping(ACTION_QUIT, INPUT_QUIT);
		// app.getInputManager().addMapping(ACTION_FORWARD, INPUT_FORWARD);
		// app.getInputManager().addMapping(ACTION_BACKWARD, INPUT_BACKWARD);
		// app.getInputManager().addMapping(ACTION_LEFTWARD, INPUT_LEFTWARD);
		// app.getInputManager().addMapping(ACTION_RIGHTWARD, INPUT_RIGHTWARD);
		app.getInputManager().addListener(actionListener, ACTION_QUIT); // , ACTION_FORWARD, ACTION_BACKWARD, ACTION_LEFTWARD, ACTION_RIGHTWARD);

		app.getInputManager().addMapping(ACTION_PRIMARY_ACTION, INPUT_PRIMARY_ACTION);
		app.getInputManager().addListener(analogListener, ACTION_PRIMARY_ACTION);
	}

	private void testDrawChunk() {
		Chunk chunk = null;

		int x = 0;
		int y = 0;
		int z = 0;

		 chunk = new Chunk(x, y, z);
		 chunk.generate(World.WORLD_SEED);
		drawNearChunk(chunk, rootNode);
//		drawNearChunk(playersChunk, rootNode);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		world.update();

		// every frame updates
		cursorBox.update(tpf, cam);

		bindCameraToPlayer();

		// nth updates
		every10nth.update(tpf);
	}

	private DoEvery every10nth = new DoEvery(0.1f, new Runnable() {
		public void run() {
			doPlayerBlockChangedDetection();
		}
	});

	private Chunk playersChunk = null;
	private Sector playerSector = null;
	private String playerPosDesc;

	protected void doPlayerBlockChangedDetection() {
		int x = (int) player.getLocalTranslation().x;
		int y = (int) player.getLocalTranslation().y;
		int z = (int) player.getLocalTranslation().z;
//		log().debug("doPlayerBlockChangedDetection %s,%s,%s", x, y, z);
		Sector sector = world.getSectorAtWorldPos(x, y, z);
		if ( sector != playerSector ) {
//			log().debug("doPlayerBlockChangedDetection - sector changed %s,%s,%s", sector.getX(), sector.getY(), sector.getZ());
			playerSector = sector;
		}
		
		Chunk chunk = world.getChunkAtWorldPos(x, y, z);
		if (chunk != playersChunk) {
//			log().debug("doPlayerBlockChangedDetection - chunk changed %s,%s,%s", chunk.getX(), chunk.getY(), chunk.getZ());
			playersChunk = chunk;
			drawNearChunk(playersChunk, rootNode);
		}
		
		
		String tmp = world.describePos(x, y, z);
		if ( !tmp.equals(playerPosDesc) ) {
			playerPosDesc = tmp;
			log().debug(playerPosDesc);
		}
	}

	/**
	 * Draw a chunk that is not in close proximity to the player<br>
	 * Chunk will be drawn as a singular optimized object rather than many individual blocks.<br>
	 * Don't let the player reach an optimized chunk or results will be not fun.
	 */
	private void drawFarChunk(Chunk chunk, Node parent) {
		drawChunk(chunk, parent, true);
	}

	/**
	 * Draw a chunk that is in close proximity to the player<br>
	 * Chunk will be drawn as many individual blocks rather than one large optimized block.
	 */
	private void drawNearChunk(Chunk chunk, Node parent) {
		drawChunk(chunk, parent, false);
	}

	private void drawChunk(Chunk chunk, Node parent, boolean optimized) {
		// wcx == world-space chunk origin x aka, the world space coordinates for the 0x0x0 point of this chunk
		// cx == chunk-space x
		// wx == world-space x

		if (chunk == null) {
			return;
		}

		int wcx = chunk.getX(); // - chunk.getChunkWidth() / 2;
		int wcy = chunk.getY(); // - chunk.getChunkHeight() / 2;
		int wcz = chunk.getZ(); // - chunk.getChunkDepth() / 2;

		Node chunkNode = parent;
		if (optimized) {
			chunkNode = new Node();
		}

		for (int cz = 0; cz < chunk.getChunkDepth(); cz++) {
			int wz = wcz + cz;

			for (int cy = 0; cy < chunk.getChunkHeight(); cy++) {
				int wy = wcy + cy;

				for (int cx = 0; cx < chunk.getChunkWidth(); cx++) {
					int wx = wcx + cx;

					BlockType blockType = chunk.getBlockType(cx, cy, cz);
					if (blockType.isVisible()) {

						Geometry geo = new Geometry();
						geo.setMesh(meshSvc.getMesh(MeshType.BOX));
						geo.setMaterial(matSvc.getMaterial(blockType.getMatType()));
						geo.setLocalTranslation(wx, wy, wz);

						chunkNode.attachChild(geo);
					}
				}
			}
		}

		if (optimized) {
			chunkNode = (Node) GeometryBatchFactory.optimize(chunkNode);
			parent.attachChild(chunkNode);
		}
	}

	@Override
	public Player getPlayer() {
		return super.getPlayer();
	}

	public void bindCameraToPlayer() {
		// FIXME: for now we're actually binding player to camera;
		player.setLocalTranslation(cam.getLocation());
		player.setLocalRotation(cam.getRotation());
		// cam.setLocation(player.getLocalTranslation());
		// cam.setRotation(player.getLocalRotation());

	}
}
