package com.zhack.util;

public class TrueEvery {
	
	private float period;
	private float phase;
	private float lastUpdateTime = 0;

	public TrueEvery( float period ) {
		this( period, period, 0 );
	}
	
	public TrueEvery( float period, float phase, float runTime ) {
		this.period = period;
		this.phase = phase;
		this.lastUpdateTime = runTime;
	}
	
	public void update( float runTime ) {
		float timePassed = runTime - lastUpdateTime;
		phase += timePassed;
	}
	
	public boolean isRipe() {
		if ( phase >= period ) {
			phase = 0;
			return true;
		}
		return false;
	}
}
