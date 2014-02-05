package com.zhack.ui;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.zhack.BaseObject;
import com.zhack.ZombieHack;
import com.zhack.datas.BlockState;
import com.zhack.datas.World;
import com.zhack.services.MaterialService;
import com.zhack.services.MaterialService.MaterialType;
import com.zhack.services.MeshService;
import com.zhack.services.MeshService.MeshType;

public class CursorBox extends BaseObject {
	private static final float POLL_PERIOD = .1f; // 10 polls per second
	private static final float MAX_DIST = 4f;

	private ZombieHack app = ZombieHack.getInstance();
	private World world = World.getInstance();

	private Geometry geo;
	private Node parent;

	private float timeSinceLastPoll;
	private boolean enabled;
	private boolean visible;

	private Node guiNode;

	public CursorBox() {
		this(null);
	}

	public CursorBox(Node parent) {
		MaterialService matSvc = MaterialService.getInstance();
		MeshService meshSvc = MeshService.getInstance();

		timeSinceLastPoll = POLL_PERIOD;
		enabled = true;
		visible = true;

		geo = new Geometry();
		Mesh mesh = meshSvc.getMesh(MeshType.BOX);
		Material mat = matSvc.getMaterial(MaterialType.CURSORBOX);
		geo.setMesh(mesh);
		geo.setMaterial(mat);
		geo.setLocalTranslation(0, 0, 0);
		geo.setQueueBucket(Bucket.Transparent);

		attachToParent(parent);
	}

	/** rootNode is recommended */
	public void attachToParent(Node parent) {
		if (parent != null) {
			this.parent = parent;
			parent.attachChild(geo);
		}
	}

	public void setVisible() {
		visible = true;
		geo.setCullHint(CullHint.Never);
		// if ( geo.getParent() == null ) {
		// parent.attachChild(geo);
		// }
	}

	public void setInvisible() {
		visible = false;
		geo.setCullHint(CullHint.Always);
		// if ( geo.getParent() != null ) {
		// geo.removeFromParent();
		// }
	}

	public void setEnabled() {
		enabled = true;
	}

	public void setDisabled() {
		enabled = false;
	}

	public void update(float tpf, Camera cam) {
		if (enabled && timeSinceLastPoll >= POLL_PERIOD) {
			CollisionResult collision = pollCursorPosition(cam);
			doGuiUpdate(cam, collision);
			timeSinceLastPoll = 0;
		} else {
			timeSinceLastPoll += tpf;
		}
	}

	private boolean guiInit = false;
	private BitmapText hudText;

	public void doGuiUpdate(Camera cam, CollisionResult collision) {
		if (guiNode != null) {
			if (!guiInit) {
				BitmapFont guiFont = app.getDefaultFont();
				hudText = new BitmapText(guiFont, false);
				hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
				hudText.setColor(ColorRGBA.White); // font color
				guiNode.attachChild(hudText);
				guiInit = true;
			}

			String msg = "POS: " + cam.getLocation() + "\n"//
					+ "ROT: " + cam.getRotation();

			if (collision != null) {
				int targetX = (int) collision.getGeometry().getLocalTranslation().x;
				int targetY = (int) collision.getGeometry().getLocalTranslation().y;
				int targetZ = (int) collision.getGeometry().getLocalTranslation().z;
				short[] blockData = world.getBlockDataAtWorldPos(targetX, targetY, targetZ);
				msg = msg + "\nBLOCK: " + BlockState.readBlockType(blockData);
				msg = msg + " @ " + targetX + ", " + targetY + ", " + targetZ;
			}

			int width = app.getScreenWidth();
			int height = app.getScreenHeight();
			hudText.setText(msg); // the text

			int x = width - ((int) hudText.getLineWidth() + 1);
			int y = height - ((int) hudText.getLineHeight() + 1);
			hudText.setLocalTranslation(x, y, 0); // position
		}
	}

	private Ray camRay = new Ray();
	private CollisionResults camRayCollisionResults = new CollisionResults();

	private CollisionResult pollCursorPosition(Camera cam) {
		camRay.setOrigin(cam.getLocation());
		camRay.setDirection(cam.getDirection());// rather than new Vector3f
		camRay.setLimit(MAX_DIST);
		camRayCollisionResults.clear(); // clear before use
		parent.collideWith(camRay, camRayCollisionResults);

		if (camRayCollisionResults.size() > 0) {
			int cIdx = 0;

			CollisionResult collision = camRayCollisionResults.getCollision(cIdx++);
			Geometry target = collision.getGeometry();

			// move on until we get past the front and back side of the CursorBox
			while (cIdx < camRayCollisionResults.size() && target == geo) {
				collision = camRayCollisionResults.getCollision(cIdx++);
				target = collision.getGeometry();
			}

			if (target != null) {
				geo.setLocalTranslation(target.getLocalTranslation());
			}
			setVisible();

			return collision;
		} else {
			setInvisible();
		}

		return null;
	}

	public Geometry getGeometry() {
		return geo;
	}

	public void enableDebugHUD(Node guiNode) {
		this.guiNode = guiNode;
	}
}
