package com.zhack.appstate.test;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;
import com.zhack.appstate.BaseTestAppState;
import com.zhack.datas.BlockType;
import com.zhack.datas.Chunk;
import com.zhack.datas.World;
import com.zhack.services.MaterialService;
import com.zhack.services.MeshService;
import com.zhack.services.MaterialService.MaterialType;
import com.zhack.services.MeshService.MeshType;
import com.zhack.ui.CursorBox;
import com.zhack.ui.UI;

public class GenBlocksTestState extends BaseTestAppState {
	private MaterialService matSvc = MaterialService.getInstance();
	private MeshService meshSvc = MeshService.getInstance();

	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean isPressed, float tpf) {
			if (name == ACTION_QUIT && !isPressed) { // on release
				app.stop();
			}
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

				if (camRayCollisionResults.size() > 0) {
					CollisionResult collision = camRayCollisionResults.getClosestCollision();
					Geometry target = collision.getGeometry();

					target.removeFromParent();
				}
			}
		}
	};

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);

		initInputs();
		UI.drawCrosshair(this.app);
		cursorBox = new CursorBox( rootNode );
		testDrawChunk();
	}

	private static final Trigger INPUT_QUIT = new KeyTrigger(KeyInput.KEY_ESCAPE);
	private static final Trigger INPUT_PRIMARY_ACTION = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);

	private static final String ACTION_QUIT = "ACTION_QUIT";
	private static final String ACTION_PRIMARY_ACTION = "ACTION_PRIMARY_ACTIO";
	private CursorBox cursorBox;

	private void initInputs() {
		// app.getInputManager().clearMappings();
		// app.getInputManager().clearRawInputListeners();

		app.getInputManager().addMapping(ACTION_QUIT, INPUT_QUIT);
		app.getInputManager().addListener(actionListener, ACTION_QUIT);

		app.getInputManager().addMapping(ACTION_PRIMARY_ACTION, INPUT_PRIMARY_ACTION);
		app.getInputManager().addListener(analogListener, ACTION_PRIMARY_ACTION);
	}

	private void testDrawChunk() {
		Chunk chunk = new Chunk(0, -5, 0);
		chunk.generate(World.WORLD_SEED);
		drawNearChunk(chunk, rootNode);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		cursorBox.update(tpf, cam);
	}

	/**
	 * Draw a chunk that is in close proximity to the player<br>
	 * Chunk will be drawn as many individual blocks rather than one large optimized block.
	 */
	private void drawNearChunk(Chunk chunk, Node parent) {
		// wcx == world-space center x aka, the world space coordinates for the center point of this chunk
		// cx == chunk-space x
		// wx == world-space x

		int wcx = chunk.getX() - chunk.getChunkWidth() / 2;
		int wcy = chunk.getY() - chunk.getChunkHeight() / 2;
		int wcz = chunk.getZ() - chunk.getChunkDepth() / 2;

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

						parent.attachChild(geo);
					}
				}
			}
		}
	}
}
