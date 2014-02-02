package com.zhack.control;

import java.util.Set;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.zhack.ZombieHack;
import com.zhack.event.EventMgr;
import com.zhack.gameobject.CreepGeometry;
import com.zhack.gameobject.PlayerBase;
import com.zhack.util.Pool;
import com.zhack.util.Utl;

public class CreepControl extends AbstractControl {
	// FIXME: @TIP you can use a control on only 1 spatial at a time, but the control can be recycled via a pool.

	private int health;
	private PlayerBase player;

	public CreepControl() {
		player = ZombieHack.getInstance().getAppState().getPlayer(); // FIXME: may be null if not a player state
	}

	@Override
	public CreepGeometry getSpatial() {
		return (CreepGeometry) super.getSpatial();
	}

	@Override
	protected void controlUpdate(float tpf) {
		Vector3f destination = player.getLocalTranslation();
		float playerRadius = 1.5f; // FIXME: get player's radius - 1 for now

		int health = getHealth();
		if (health > 0) {
			try {
				Utl.moveTowards(getSpatial(), destination, tpf);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (getSpatial().getLocalTranslation().distance(destination) <= playerRadius) {
			EventMgr.getInstance().onCollisionWithPlayer(player, this);
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}

	/** Obtain Creep and Geometry from Pool */
	public static CreepControl obtain(String name, float x, float y, float z) {
		CreepControl control = new CreepControl(); // POOL
		Geometry geo = Pool.creepGeometry(name, x, y, z);
		geo.addControl(control);
		control.setHealth(80);
		return control;
	}

	/** Destroy Creep state and return Control and Geometry to Pool */
	public void destroy() {
		CreepGeometry creep = null;
		try {
			creep = getSpatial();
			// FIXME: parent can be null? how?
			// ANSWER: because the CreepControl was still in the GamePlayAppState.creeps after CreepControl.destroy had been called

			Set<CreepControl> creeps = ZombieHack.getInstance().getAppState().getCreeps();
			if (creeps != null) {
				creeps.remove(this); // FIXME: cant remove in the middle of a iteration
			}
			creep.getParent().detachChild(creep); // detach
			creep.destroy();
			Pool.release(creep);
		} catch (Exception e) {
			e.printStackTrace();

			creep = getSpatial();
			// FIXME: parent can be null? how?
			creep.getParent().detachChild(creep); // detach
			creep.destroy();
			Pool.release(creep);
		}
	}

	public static class CreepData {
		int index = 0;
		int health = 0;
	}

	public void setHealth(int health) {
		// FIXME: @TIP UserData can only be Integer, Float, Boolean, String, Long - for now we'll just store the data in the Control rather than the
		// Spatial?
		int prevHealth = this.health;
		this.health = health;
		EventMgr.getInstance().onCreepHealthChange(this, prevHealth, this.health);
	}

	public int getHealth() {
		return this.health;
	}

}
