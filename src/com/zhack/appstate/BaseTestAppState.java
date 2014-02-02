package com.zhack.appstate;

import java.util.Set;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.zhack.ZombieHack;
import com.zhack.control.CreepControl;
import com.zhack.gameobject.PlayerBase;

public class BaseTestAppState extends AbstractAppState implements CommonAppState {
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
		ambient.setColor( ColorRGBA.White );
		rootNode.addLight( ambient );
	}
	
	public PlayerBase getPlayer() {
		return null;
	}

	public Set<CreepControl> getCreeps() {
		return null;
	}
}
