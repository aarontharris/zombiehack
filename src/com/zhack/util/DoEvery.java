package com.zhack.util;

public class DoEvery {

	private float period;
	private float phase;
	private Runnable runnable;

	public DoEvery(float period, Runnable runnable) {
		this(period, period, runnable);
	}

	public DoEvery(float period, float phase, Runnable runnable) {
		this.period = period;
		this.phase = phase;
		this.runnable = runnable;
	}

	public void update(float tpf) {
		if (phase >= period) {
			phase = 0;
			runnable.run();
		} else {
			phase += tpf;
		}
	}
}
