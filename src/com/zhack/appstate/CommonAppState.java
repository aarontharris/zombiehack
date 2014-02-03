package com.zhack.appstate;

import com.zhack.gameobject.Player;

public interface CommonAppState {

	/** May return null - if this state is not player oriented */
	public Player getPlayer();

}
