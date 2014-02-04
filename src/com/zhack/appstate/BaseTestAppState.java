package com.zhack.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.zhack.Logger;
import com.zhack.ZombieHack;
import com.zhack.gameobject.Player;

public class BaseTestAppState extends AbstractAppState implements CommonAppState {

	private Logger logger;

	protected Logger log() {
		if (logger == null) {
			logger = new Logger(getClass());
		}
		return logger;
	}

	protected ZombieHack app;
	protected Camera cam;
	protected Node rootNode;
	protected AssetManager assetManager;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = (ZombieHack) app;
		this.cam = this.app.getCamera();
		this.rootNode = this.app.getRootNode();
		this.assetManager = this.app.getAssetManager();

		this.app.setDisplayFps(true);
		this.app.setDisplayStatView(true);
		cam.setLocation(Vector3f.ZERO);

		AmbientLight ambient = new AmbientLight();
		ambient.setColor(ColorRGBA.White);
		rootNode.addLight(ambient);
	}

	public Player getPlayer() {
		return null;
	}

}
