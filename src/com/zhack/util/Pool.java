package com.zhack.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * <pre>
 * Vector3f v1 = ObjectPool.obtainVector3f(0, 0, 0);
 * Vector3f v2 = ObjectPool.obtainVector3f(0, 1, 0);
 * Vector3f result = ObjectPool.obtainVector3f();
 * v1.subtract(v2, result); // result is
 * ObjectPool.release(result);
 * ObjectPool.release(v2);
 * ObjectPool.release(v1);
 * </pre>
 */
public class Pool {
	private static Pool self = new Pool();
	private Map<Class<?>, ConcurrentLinkedQueue<Object>> genericPool = new HashMap<Class<?>, ConcurrentLinkedQueue<Object>>();
	private ConcurrentLinkedQueue<Object> vector3fPool = new ConcurrentLinkedQueue<Object>();
	private ConcurrentLinkedQueue<Object> nodePool = new ConcurrentLinkedQueue<Object>();
	private ConcurrentLinkedQueue<Object> creepPool = new ConcurrentLinkedQueue<Object>();
	private int objectsCreated = 0;

	public static void init() {
		self.genericPool.put(Vector3f.class, self.vector3fPool);
		self.genericPool.put(Node.class, self.nodePool);
		self.genericPool.put(Node.class, self.creepPool);
	}

	public static Vector3f vector3f(float x, float y, float z) {
		if (!self.vector3fPool.isEmpty()) {
			Vector3f out = (Vector3f) self.vector3fPool.poll();
			out.x = x;
			out.y = y;
			out.z = z;
			return out;
		}
		self.objectsCreated++;
		return new Vector3f(x, y, z);
	}

	public static void release(Vector3f o) {
		try {
			self.vector3fPool.add(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Node node() {
		Node out = (Node) self.nodePool.poll();
		if (out == null) {
			self.objectsCreated++;
			out = new Node(); // children detached on release
		}
		return out;
	}

	public static void release(Node o) {
		try {
			o.detachAllChildren();
			self.nodePool.add(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param type
	 * @return
	 * @throws InstantiationException
	 *             when type does not have a public empty constructor
	 * @throws IllegalAccessException
	 *             private?
	 */
	public static <T> T obtain(Class<?> type) throws InstantiationException, IllegalAccessException {
		Object out = null;
		ConcurrentLinkedQueue<Object> queue = self.genericPool.get(type);
		if (queue == null) {
			queue = new ConcurrentLinkedQueue<Object>();
			self.genericPool.put(type, queue);
			self.objectsCreated++;
			out = type.newInstance();
		} else if (queue.isEmpty()) {
			self.objectsCreated++;
			out = type.newInstance();
		} else {
			out = queue.poll();
		}
		return (T) out;
	}

	public static void release(Object o) {
		try {
			ConcurrentLinkedQueue<Object> queue = self.genericPool.get(o.getClass());
			queue.add(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int size(Class<?> type) {
		ConcurrentLinkedQueue<Object> queue = self.genericPool.get(type);
		if (queue == null) {
			return 0;
		}
		return queue.size();
	}

	public static int size() {
		int total = 0;
		for (Class<?> type : self.genericPool.keySet()) {
			ConcurrentLinkedQueue<Object> queue = self.genericPool.get(type);
			if (queue != null) {
				total += queue.size();
			}
		}
		return total;
	}

}
