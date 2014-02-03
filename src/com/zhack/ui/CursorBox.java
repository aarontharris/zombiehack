package com.zhack.ui;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.zhack.BaseObject;
import com.zhack.services.MaterialService;
import com.zhack.services.MaterialService.MaterialType;
import com.zhack.services.MeshService;
import com.zhack.services.MeshService.MeshType;

public class CursorBox extends BaseObject {
	private static final float POLL_PERIOD = .1f; // 10 polls per second
	private static final float MAX_DIST = 4f;

	private Geometry geo;
	private Node parent;

	private float timeSinceLastPoll;
	private boolean enabled;
	private boolean visible;

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
			pollCursorPosition(cam);
			timeSinceLastPoll = 0;
		} else {
			timeSinceLastPoll += tpf;
		}
	}

	private Ray camRay = new Ray();
	private CollisionResults camRayCollisionResults = new CollisionResults();

	private void pollCursorPosition(Camera cam) {
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
		} else {
			setInvisible();
		}
	}

	public Geometry getGeometry() {
		return geo;
	}
}
