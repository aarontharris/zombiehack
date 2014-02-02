package com.zhack.ui;

import com.jme3.scene.Geometry;
import com.zhack.ZombieHack;
import com.zhack.services.MaterialService;
import com.zhack.services.MaterialService.MaterialType;
import com.zhack.services.MeshService;
import com.zhack.services.MeshService.MeshType;

public class UI {
	private static MaterialService matSvc = MaterialService.getInstance();
	private static MeshService meshSvc = MeshService.getInstance();

	public static void drawCrosshair( ZombieHack app ) {
		Geometry crosshair = new Geometry();
		crosshair.setMesh(meshSvc.getMesh(MeshType.BOX));
		crosshair.scale(10);
		crosshair.setMaterial(matSvc.getMaterial(MaterialType.CROSSHAIR));
		crosshair.setLocalTranslation(app.getScreenWidth() / 2, app.getScreenHeight() / 2, 0);
		app.getGuiNode().attachChild(crosshair);
	}
	
}
