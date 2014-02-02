package com.zhack;

public class BaseObject {

	private Logger logger;

	protected Logger log() {
		if (logger == null) {
			logger = new Logger( getClass() );
		}
		return logger;
	}

}
