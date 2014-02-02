package com.zhack.appstate;

import java.util.Set;

import com.zhack.control.CreepControl;
import com.zhack.gameobject.PlayerBase;

public interface CommonAppState {

	/** May return null - if this state is not player oriented */
	public PlayerBase getPlayer();

	/** May return null - if this state is not player oriented */
	public Set<CreepControl> getCreeps();

}
