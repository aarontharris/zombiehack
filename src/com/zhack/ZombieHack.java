package com.zhack;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.font.BitmapFont;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.zhack.appstate.test.GenBlocksTestState;
import com.zhack.services.MaterialService;
import com.zhack.services.MeshService;
import com.zhack.util.Pool;
import com.zhack.util.Utl;

public class ZombieHack extends SimpleApplication {

	public static void main(String[] args) {
		try {

			ZombieHack.app = new ZombieHack();
			// Logger.getLogger(TowerDefDemo.class.getName()).log(Level.INFO, "Capabilities: {0}", caps.toString());

			AppSettings settings = new AppSettings(true);
			settings.setTitle("My Tower Defense Demo");
			settings.setSettingsDialogImage("Interface/splash.png");
			settings.setFullscreen(true);
			settings.put("Width", 1440);
			settings.put("Height", 900);
			settings.put("VSync", false); // turn this off for huge framerate boost
			settings.put("Samples", 4); // anti aliasing 16x
			// settings.setUseInput( true ); // default=true. false disabled all user input until set to true again
			// app.setShowSettings( false );
			// settings.setRenderer(AppSettings.LWJGL_OPENGL2);
			app.setSettings(settings);

			Pool.init();
			Utl.init(app);

			// app.start( JmeContext.Type.Headless );
			app.start();
		} catch (Exception e) {
			Logger.inst(ZombieHack.class).error(e);
		}
	}

	private static ZombieHack app;

	public static ZombieHack getInstance() {
		return app;
	}

	private AbstractAppState appState;

	public void initViewport(float fov, float farClip) {
		float aspectRatio = settings.getWidth() / (float) settings.getHeight();
		cam.setFrustumPerspective(fov, aspectRatio, 0.01f, farClip);
	}

	@Override
	public void simpleInitApp() {
		// Collection<Caps> caps = app.renderer.getCaps();
		// for ( Caps cap : caps ) {
		// System.out.println( cap );
		// }

		MeshService.getInstance().init();
		MaterialService.getInstance().init(assetManager);

		initViewport(100f, 100f);

		// appState = new GamePlayAppState();
		appState = new GenBlocksTestState();
		getStateManager().attach(appState);

	}

	@Override
	public void simpleUpdate(float tpf) {
	}

	@Override
	public void simpleRender(RenderManager rm) {
	}

	// FIXME: instead, each object related to an appstate should get a weakref to its appstate
	// then when that object calls its own getAppState() and that method learns that the weakref is empty
	// it can redirect to a kill self.
	// public CommonAppState getAppState() {
	// return getStateManager().getState(GamePlayAppState.class);
	// }

	public BitmapFont getDefaultFont() {
		return guiFont;
	}

	public int getScreenWidth() {
		return settings.getWidth();
	}

	public int getScreenHeight() {
		return settings.getHeight();
	}
}
