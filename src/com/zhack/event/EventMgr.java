package com.zhack.event;

import com.zhack.ZombieHack;
import com.zhack.appstate.GamePlayAppState;
import com.zhack.control.CreepControl;
import com.zhack.control.TowerControl;
import com.zhack.gameobject.PlayerBase;

public class EventMgr {
	private static final EventMgr self = new EventMgr();

	public static EventMgr getInstance() {
		return self;
	}

	private EventMgr() {
	}

	public void onCollisionWithPlayer( PlayerBase player, CreepControl creepControl ) {
		System.out.println( "Creep collided with player" );
		player.setHealth( player.getHealth() - creepControl.getHealth() );
		creepControl.setHealth( 0 );
	}

	public void onPlayerHealthChange( int prevHealth, int postHealth ) {
		System.out.printf( "Player health change %s -> %s\n", prevHealth, postHealth );
		if ( postHealth <= 0 ) {
			System.out.println( "Game Over - You Died\n" );
		}
	}

	public void onTowerHitsCreep( TowerControl towerControl, CreepControl creepControl ) {
		System.out.printf( "%s hits %s", towerControl.getSpatial().getName(), creepControl.getSpatial().getName() );
		creepControl.setHealth( creepControl.getHealth() - 5 );
	}

	public void onCreepHealthChange( CreepControl creep, int prevHealth, int postHealth ) {
		System.out.printf( "Creep health change %s -> %s\n", prevHealth, postHealth );
		if ( postHealth <= 0 ) {
			creep.destroy(); // kill and release to pool
		}
	}
}
