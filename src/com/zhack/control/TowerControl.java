package com.zhack.control;

import java.util.Set;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.zhack.ZombieHack;
import com.zhack.event.EventMgr;
import com.zhack.gameobject.Tower;

public class TowerControl extends AbstractControl {
	private static final float TOWER_RANGE = 5.0f;
	private long lastFireMs = 0;
	private long rechargeTimeMs = 1000; // 1 second

	@Override
	protected void controlUpdate(float tpf) {
		// FIXME: @TIP For now we'll do a radius check on all creeps
		// but I'm thinking it might be better to have a spherical spatial with the center located at the tower's weapon
		// when an object collide-enters the sphere we start shooting at it, when it collide-exits, we stop.
		// Concerns: fast moving objects may not trigger collisions as accurately? maybe miss an available shot?

		Set<CreepControl> creeps = ZombieHack.getInstance().getAppState().getCreeps();
		if (creeps != null) {
			for (CreepControl cc : creeps) {
				Spatial cspatial = cc.getSpatial();
				Vector3f clocaltrans = cspatial.getLocalTranslation();
				Spatial spatial = getSpatial();
				Vector3f localtrans = spatial.getLocalTranslation();
				float dist = clocaltrans.distance(localtrans);
				if (dist <= TOWER_RANGE) {
					long timeSinceLastFireMs = System.currentTimeMillis() - lastFireMs;
					if (timeSinceLastFireMs >= rechargeTimeMs) {
						EventMgr.getInstance().onTowerHitsCreep(this, cc);
						lastFireMs = System.currentTimeMillis();
						break; // only one shot
					}
				}
			}
		}
		// FIXME: @TIP Cool optimization - rather than doing a distance check every update, skip checks when the tower is recharging since it cant
		// fire anyway.
	}

	@Override
	public Tower getSpatial() {
		return (Tower) super.getSpatial();
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}

	public Integer getIndex() {
		return getSpatial().getIndex();
	}

	public void setIndex(Integer index) {
		getSpatial().setIndex(index);
	}

	public Integer getChargeNum() {
		return getSpatial().getChargeNum();
	}

	public void setChargeNum(Integer chargeNum) {
		getSpatial().setChargeNum(chargeNum);
	}
}
