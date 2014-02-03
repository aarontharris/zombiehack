package com.zhack.datas;

import com.zhack.services.MaterialService.MaterialType;

public enum BlockType {

	/**
	 * <pre>
	 * RULES:
	 * 1. Once a blocktype is added, it cannot be changed or removed without proper migration of older data on the various servers. So just don't do it. 
	 * 2. BlockIds must be contiguous, no skips
	 * 3. The number of blocktypes must equal the number of sequential ids 0..(n-1).
	 */
	UNDEF((short) 0, false, null), //
	AIR((short) 1, false, null), //
	WATER((short) 2, true, null), //
	DIRT((short) 3, true, MaterialType.DIRT), //
	STONE((short) 4, true, MaterialType.STONE), //
	WHITE((short) 5, true, MaterialType.WHITE), //
	;

	private static BlockType[] typeMap;
	static {
		int len = BlockType.values().length;
		typeMap = new BlockType[len];
		for (BlockType t : BlockType.values()) {
			typeMap[t.blockId] = t;
		}
	}

	private short blockId;
	private MaterialType matType;
	private boolean visible;

	BlockType(short blockId, boolean visible, MaterialType matType) {
		this.blockId = blockId;
		this.visible = visible;
		this.matType = matType;
	}

	public short getId() {
		return blockId;
	}

	public MaterialType getMatType() {
		return matType;
	}

	public boolean isVisible() {
		return visible;
	}

	public static BlockType getById(short id) {
		return typeMap[id];
	}
}
