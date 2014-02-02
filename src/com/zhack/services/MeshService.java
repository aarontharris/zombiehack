package com.zhack.services;

import java.util.HashMap;
import java.util.Map;

import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.zhack.BaseObject;

public class MeshService extends BaseObject {

	public static enum MeshType {
		BOX
	}

	private static final MeshService self = new MeshService();

	public static final MeshService getInstance() {
		return self;
	}

	private Map<MeshType, Mesh> meshMap = new HashMap<MeshType, Mesh>();

	public void init() {
		meshMap.put(MeshType.BOX, new Box(0.5f, 0.5f, 0.5f));

		// sanity check @ runtime :( // maybe disable in release mode?
		if (meshMap.size() != MeshType.values().length) {
			for (MeshType type : MeshType.values()) {
				if (meshMap.get(type) == null) {
					log().error("Missing Mesh Definition for '%s'", type);
				}
			}
			throw new IllegalStateException("Not all MeshTypes are defined");
		}
	}

	public Mesh getMesh(MeshType type) {
		return meshMap.get(type);
	}
}
