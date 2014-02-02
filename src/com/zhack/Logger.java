package com.zhack;

import java.util.HashMap;
import java.util.Map;

public class Logger {
	private static final Map<Class<?>, Logger> loggerMap = new HashMap<Class<?>, Logger>();

	public static final Logger inst(Class<?> clazz) {
		Logger out = loggerMap.get(clazz);
		if (out == null) {
			out = new Logger(clazz);
			loggerMap.put(clazz, out);
		}
		return out;
	}

	private Class<?> clazz;

	public Logger(Class<?> clazz) {
		this.clazz = clazz;
	}

	public void debug(String format, Object... objects) {
		System.out.println(clazz.getSimpleName() + ": " + String.format(format, objects));
	}

	public void error(String format, Object... objects) {
		System.err.println(clazz.getSimpleName() + ": " + String.format(format, objects));
	}

	public void error(Throwable t) {
		System.err.print(clazz.getSimpleName() + ": ");
		System.err.println(t.getMessage());
		t.printStackTrace();
	}
}
